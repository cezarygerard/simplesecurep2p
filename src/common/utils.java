package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class utils {

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

}
