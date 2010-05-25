package peer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.TreeSet;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import common.PeerInfo;
import common.PeerLoginInfo;


/**
 * @author czarek
 * TODO ta klasa powinna byc singletonem
 */
public class Peer implements Runnable {

	TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>();
	private KeyStore keystore;
	private TrustManagerFactory tmf;
	private SSLContext sc;
	private SSLSocketFactory sf;
	private int listeningPort;
	private PeerLoginInfo peerLogin;
	PeerInfo myInfo;
	
	public Peer(String keystore,  char[] kestorePass, int listeningPort)
	{
		try {
			FileInputStream is = new FileInputStream(keystore);
			this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.keystore.load(is,kestorePass);			
			this.tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
			this.tmf.init(this.keystore);
			this.sc = SSLContext.getInstance("TLS");
			this.sc.init(null, tmf.getTrustManagers(), null);
			this.sf = sc.getSocketFactory();
			this.listeningPort = listeningPort;
			this.myInfo = new PeerInfo(InetAddress.getByName("192.168.1.4"), 9998);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) {

		Peer p = new Peer("./res/common/key/serverKeys" , "123456".toCharArray(), 9795);
		P2SConnection p2s = new P2SConnection(p, "localhost", 9995);
		p.peerLogin = new PeerLoginInfo("czarek", "12345");
		try {
			p2s.Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true)
		{
			System.out.println("[Peer.main()] koniec");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {

	}
	


}
