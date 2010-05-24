package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import common.PeerInfo;
/**
 * @author czarek
 *
 */
public class Server extends Thread {

	public static void main(String[] args) throws Exception {


		FileInputStream is = new FileInputStream("./res/common/key/serverKeys");

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "123456".toCharArray());
		KeyManagerFactory kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
		kmf.init(keystore, ("123456").toCharArray());
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		SSLServerSocket ss = (SSLServerSocket)ssf.createServerSocket(9097);
		readLoginInfo();
		System.out.println("Ready...");

		while (true) {
			new Server(ss.accept()).start();

		}
	}

	private static TreeSet<PeerLoginInfo > loginInfo = new TreeSet<PeerLoginInfo>(); 
	private static TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>(); 
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

