package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class SSLServer extends Thread {

	public static void main(String[] args) throws Exception {

		FileInputStream is = new FileInputStream("./key/serverKeys");
		
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "123456".toCharArray());
	
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
	//	tmf.init(keystore);
		
		KeyManagerFactory kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
		kmf.init(keystore, ("123456").toCharArray());
	//	KeyManager [] km = kmf.getKeyManagers();
		SSLContext sc = SSLContext.getInstance("SSL");
		
		sc.init(kmf.getKeyManagers(), null, null);
		
		
		ServerSocketFactory ssf = sc.getServerSocketFactory();
		ServerSocket ss = ssf.createServerSocket(9097);

		int soks = 0;
		System.out.println("Ready...");
		while (true) {
			new SSLServer(ss.accept()).start();
			soks++;
		}
	}

	private Socket sock;

	public SSLServer(Socket s) {
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
}