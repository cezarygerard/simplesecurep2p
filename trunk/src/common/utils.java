package common;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

/**
 * @author czarek
 * Klasa z funkcjami ktora sa wykorzystywane w wielu miejscach
 */
public class utils {

	public static final int NEIGHBOUR_RECOGNITION_PERIOD = 300 * 1000;//ms

	/**
	 * Metoda do wygodnego obliczania skrotu
	 * @param mdFun funkcja skrotu jaka chcemy uzyc
	 * @param input tablica byte'ow do zahaszowania
	 * @return tablica byte'ow
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] MDigest(String mdFun,byte[] input) throws NoSuchAlgorithmException  
	{
		MessageDigest md;
		if(mdFun != null)
		{	
			md = MessageDigest.getInstance(mdFun);
		}
		else
			md = MessageDigest.getInstance("SHA1");

		return md.digest(input);
	}


	/**
	 * Gedy mamy skrot w postaci stringu hexadecymalnego mozna uzyc tej metody do policzenie skrotu w postaci byte[] 
	 * @param s string w postaci heksadecymalnej
	 * @return tablica byte'ow odpowadajaca argumentowi funkcji
	 */
	public static byte[] toByteArray(String s)
	{
		byte [] retVal = new byte [s.length()/2];
		for(int i = 0 ; i < s.length();)
		{
			retVal[i/2] = (byte)(( Character.digit(s.charAt(i), 16) << 4 )+ Character.digit(s.charAt(i +1), 16));
			i +=2;

		}
		return retVal;	
	}

	/**
	 * Zamiana byte[] na string heksadecymalny 
	 * @param input
	 * @return string heksadecymalny
	 */
	public static String toHexString(byte[] input) 
	{		
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<input.length;i++) {
			String hex = Integer.toHexString(0xFF & input[i]);
			if (hex.length() == 1) {
				// could use a for loop, but we're only dealing with a single byte
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}


	/**
	 * Metoda wydrukuje informacje o dostepnych interface'ach sieciowych 
	 * @throws SocketException 
	 */
	public static void printInterfaces() 
	{
		Enumeration<NetworkInterface> e = null;

		try {
			e = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		NetworkInterface ni;

		while(e.hasMoreElements())
		{
			ni = e.nextElement();

			try {
				if(ni.isUp() && !ni.isLoopback())
				{	
					System.out.println("NetworkInterface: " + ni);
					System.out.println("ni.isUp() " + ni.isUp());
					System.out.println("ni.getInetAddresses(): " + ni.getInetAddresses()); 
					System.out.println("ni.getInterfaceAddresses() " + ni.getInterfaceAddresses());
					System.out.println("ni.getName(): " + ni.getName());
					System.out.println("ni.getParent(): " + ni.getParent());
					System.out.println("ni.isVirtual(): "+ ni.isVirtual());
					System.out.println("ni.isPointToPoint() "+ ni.isPointToPoint());
					System.out.println(" ni.isLoopback() " + ni.isLoopback());
				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	/*	
	public static NetworkInterface getValuableINterface() throws SocketException
	{
		Enumeration<NetworkInterface> e = null;

		e = NetworkInterface.getNetworkInterfaces();

		NetworkInterface ni;
		while((ni = e.nextElement() )!= null)
		{
		}


	}
	 */

}


