package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
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

	private static TreeSet<PeerLoginInfo > loginInfo = new TreeSet<PeerLoginInfo>(); ; 
	private static TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>(); ;
	FileInputStream is ; 
	KeyStore keystore;
	KeyManagerFactory kmf;
	SSLContext sc;
	SSLServerSocketFactory ssf;
	SSLServerSocket ss;

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
			readLoginInfo();
			ss = (SSLServerSocket)ssf.createServerSocket(9097);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		
		Server server = new Server ("./res/common/key/serverKeys" , "123456".toCharArray(), 9995 );
		
		//is = new FileInputStream("./res/common/key/serverKeys");
		//keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		//KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		//keystore.load(is, "123456".toCharArray());
		//KeyManagerFactory kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
		//kmf.init(keystore, ("123456").toCharArray());
		//SSLContext sc = SSLContext.getInstance("SSL");
		//sc.init(kmf.getKeyManagers(), null, null);
		//SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		//SSLServerSocket ss = (SSLServerSocket)ssf.createServerSocket(9097);
		
		//System.out.println("Ready...");

		/**
		*
		* Zrób z tego w¹tek
		*/
		while (true) {
			//new Server(ss.accept()).start();
			try {
				new S2PConnection(server.ss.accept(), server);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	loginInfo = new TreeSet<PeerLoginInfo>(); 
//	peersInfo = new TreeSet<PeerInfo>(); 
//	private static TreeMap<String , PeerLoginInfo > loginInfo = new TreeMap<String , PeerLoginInfo>(); 
//	private static TreeMap<String, PeerInfo > peersInfo = new TreeMap<String, PeerInfo >(); 
/*
	private Socket sock;

	private Server(Socket s) {
		sock = s;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter pw = new PrintWriter(sock.getOutputStream());

			String data = br.readLine();
			pw.println(data);
			pw.close();
			sock.close();

		} catch (IOException ioe) {

		}
	}
*/	

	/**
	 * @TODO dodac sol do hasel
	 */
	private static void readLoginInfo()
	{
		try {
			System.out.println("readLoginInfo");
			FileReader fr = new FileReader("./res/server/peerLoginInfo.dat");

			BufferedReader br = new BufferedReader(fr);
			StringTokenizer st;// = new StringTokenizer();
			String line;
			while((line = br.readLine()) != null)
			{	
				
				st = new StringTokenizer(line);
				if((st.countTokens() )!= 2)
					throw new Exception("Ivalid peerLoginInfo.dat file");

				loginInfo.add(new PeerLoginInfo(st.nextToken(), st.nextToken().getBytes()));

				System.out.println("po");
			}
			System.out.println(loginInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

