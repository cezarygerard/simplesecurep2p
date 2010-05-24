package server;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Set;
import java.util.TreeSet;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.bouncycastle.x509.X509V1CertificateGenerator;
/**
 * @author czarek
 *
 */
public class Server extends Thread {

	public static void main(String[] args) throws Exception {

		
		FileInputStream is = new FileInputStream("./key/serverKeys");

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "123456".toCharArray());


		KeyManagerFactory kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
		kmf.init(keystore, ("123456").toCharArray());
		//	KeyManager [] km = kmf.getKeyManagers();
		SSLContext sc = SSLContext.getInstance("SSL");

		sc.init(kmf.getKeyManagers(), null, null);


		SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		//sc.c
		SSLServerSocket ss = (SSLServerSocket)ssf.createServerSocket(9097);
		
		readLoginInfo();

		System.out.println("Ready...");
		
		while (true) {
			new Server(ss.accept()).start();

		}
	}

	private TreeSet<PeerLoginInfo > loginInfo; 

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
			FileInputStream fis = new FileInputStream("./server/serverKeys");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

