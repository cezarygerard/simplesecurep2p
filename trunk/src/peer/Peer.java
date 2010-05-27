package peer;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.TreeSet;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import common.CertInfo;
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
	int listeningPort;
	PeerLoginInfo peerLogin;
	PeerInfo myInfo;
	CertInfo certInfo;
	
	///elementy potrzebne do nasluchowania
	private SSLServerSocketFactory ssf;
	private SSLServerSocket ss;
	
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
			this.ssf = sc.getServerSocketFactory();
			this.ss = (SSLServerSocket) ssf.createServerSocket(this.listeningPort);
			this.certInfo = new CertInfo();
		//	this.myInfo = new PeerInfo(this.ss.getInetAddress(), this.listeningPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) throws UnknownHostException {

		Peer p = new Peer("./res/peer/key/peerTrustedKeys" , "123456".toCharArray(), 9795);
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
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		while (true) {
			//new Server(ss.accept()).start();
			try {
				System.out.println("serwer czeka");
				new P2PConnection(this.ss.accept(), this);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	


}
