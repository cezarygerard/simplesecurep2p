package peer;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.TreeSet;

import javax.net.ssl.SSLContext;
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
	SSLSocketFactory sf;
	private int listeningPort;
	PeerLoginInfo peerLogin;
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
		//	this.myInfo = new PeerInfo(InetAddress.getByName("192.168.1.4"), 9998);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) throws UnknownHostException {

		Peer p = new Peer("./res/common/key/serverKeys" , "123456".toCharArray(), 9795);
		P2SConnection p2s = new P2SConnection(p, InetAddress.getByName("192.168.1.3"), 9995);
		p.peerLogin = new PeerLoginInfo("czarek", "12345", false);
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
