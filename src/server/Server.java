package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import common.PeerInfo;
import common.PeerLoginInfo;
import common.utils;

/**
 * @author czarek
 * TODO ta klasa powinna byc singletonem
 * TODO zaimplementowa
 */

public class Server {

	private TreeSet<PeerLoginInfo > loginInfo = new TreeSet<PeerLoginInfo>(); ; 
	TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>();
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
			Enumeration<String> en = keystore.aliases();

			   for (; en.hasMoreElements(); ) {
			        String alias = (String)en.nextElement();

			        // Does alias refer to a private key?
			        boolean b = keystore.isKeyEntry(alias);

			        // Does alias refer to a trusted certificate?
			        b = keystore.isCertificateEntry(alias);
			       
			        b = false;
			    }

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static void main(String[] args) {

		Server server = new Server ("./res/common/key/serverKeys" , "123456".toCharArray(), 9995 );
		//Server server = new Server ("./res/server/key/serverKeys" , "123456".toCharArray(), 9995 );
	//	Server server = new Server ("./res/server/key/serverKeys" , "123456".toCharArray(), 9995 );
	/*	utils.printInterfaces();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
		/**
		 * Zrob z tego w¹tek
		 */
			while (true) {
			//new Server(ss.accept()).start();
			try {
				System.out.println("serwer czeka");
				new S2PConnection(server.ss.accept(), server);
				Thread.sleep(10000);
			} catch (Exception e1) {
				e1.printStackTrace();
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
	
				loginInfo.add(new PeerLoginInfo(st.nextToken(), st.nextToken(), true));
			}
			System.out.println("[Server.readLoginInfo()] " + loginInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean verifyPeer(PeerLoginInfo pli)
	{
		PeerLoginInfo pliAtServer= (loginInfo.subSet(pli, true, pli, true)).first();
		if (pliAtServer != null && (pliAtServer.getPasswdHash()).equals(pli.getPasswdHash()))
			return true;
		else
			return false;			
	}	
}

