package peer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Random;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Peer {

	public static void main(String[] args) throws Exception {

		FileInputStream is = new FileInputStream("./key/serverKeys");

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "123456".toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
		tmf.init(keystore);

		SSLContext sc = SSLContext.getInstance("SSL");

		sc.init(null, tmf.getTrustManagers(), null);



		SocketFactory sf = sc.getSocketFactory();
		Socket s = sf.createSocket("localhost", 9097);

		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		for (int i = 0; i < 999; i++) {
			pw.println("from java2s" + i );
			Thread.sleep(Math.abs((new Random()).nextLong()%1000));
			System.out.println("wysylanie");
		}
		pw.println("from java2s.");
		pw.flush();
		System.out.println(br.readLine());
		s.close();
	}
}