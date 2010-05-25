package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import common.PeerInfo;
import common.PeerLoginInfo;

/**
 * @author czarek
 * TODO ta klasa powinna byc singletonem
 * TODO zaimplementowa
 */

public class Server {

	private TreeSet<PeerLoginInfo > loginInfo = new TreeSet<PeerLoginInfo>(); ; 
	TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>(); ;
	private FileInputStream is ; 
	private KeyStore keystore;
	private KeyManagerFactory kmf;
	private SSLContext sc;
	private SSLServerSocketFactory ssf;
	private SSLServerSocket ss;
	private STATE state;
	
	public enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}

	public Server(String keystoreLoc,  char[] kestorePass, int listeningPort)
	{
		try {
			is = new FileInputStream(keystoreLoc) ;
			this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, "123456".toCharArray());
			kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
			kmf.init(keystore, ("123456").toCharArray());
			sc = SSLContext.getInstance("SSL");
			sc.init(kmf.getKeyManagers(), null, null);
			ssf = sc.getServerSocketFactory();
			readLoginInfo("./res/server/peerLoginInfo.dat");
			ss = (SSLServerSocket)ssf.createServerSocket(listeningPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static void main(String[] args) {

		Server server = new Server ("./res/common/key/serverKeys" , "123456".toCharArray(), 9995 );

		/**
		 *
		 * Zrob z tego w¹tek
		 */
		System.out.println(server.ss.getLocalSocketAddress() + "  "+ server.ss.getLocalPort());
		
		while (true) {
			//new Server(ss.accept()).start();
			try {
				System.out.println("serwer czeka");
				new S2PConnection(server.ss.accept(), server);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

	/**
	 * @TODO dodac sol do hasel
	 */
	private void readLoginInfo(String peerLoginInfo)
	{
		try {
			System.out.println("readLoginInfo");
			FileReader fr = new FileReader(peerLoginInfo);

			BufferedReader br = new BufferedReader(fr);
			StringTokenizer st;// = new StringTokenizer();
			String line;
			while((line = br.readLine()) != null)
			{	

				st = new StringTokenizer(line);
				if((st.countTokens() )!= 2)
					throw new Exception("Ivalid peerLoginInfo.dat file");
				String login = st.nextToken();
				String hash =  st.nextToken();
				byte [] realHash = new byte [hash.length()/2]; 
				System.out.println("[Server.readLoginInfo()] " + hash.length());
				
				for(int i = 0 ; i < hash.length();)
				{
					realHash[i/2] = (byte)(( Character.digit(hash.charAt(i), 16) << 4 )+ Character.digit(hash.charAt(i +1), 16));
				//	Byte b = new Byte(hash.substring(i, i+1)));
					//realHash[i] = b.byteValue();
					i +=2;
					
				}
				loginInfo.add(new PeerLoginInfo(login, realHash));
				System.out.println("[Server.readLoginInfo()] hash" + hash + " " + realHash);
			}
			System.out.println("[Server.readLoginInfo()] " + loginInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean verifyPeer(PeerLoginInfo pli)
	{
		PeerLoginInfo pliAtServer= (loginInfo.subSet(pli, true, pli, true)).first();
		if (pliAtServer != null && Arrays.equals(pliAtServer.getPasswdHash(),  pli.getPasswdHash()))
			return true;
		else
			return false;			
	}	
}

