package peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

import common.PeerInfo;
import common.PeerLoginInfo;


/**
 * @author czarek
 * @TODO ta klasa powinna byc singletonem
 * @TODO Wybrac waska grupe akceptowanych szyfrowan w polaczeniach
 */
public class Peer implements Runnable {

	TreeSet<PeerInfo > peersInfo = new TreeSet<PeerInfo>();
	private KeyStore trustedKeystore;
	private KeyStore myKeystore;
	private TrustManagerFactory tmf;
	private KeyManagerFactory kmf;
	private SSLContext sc;
	boolean hasValidCert;
	SSLSocketFactory sf;
	int listeningPort;
	PeerLoginInfo peerLogin;
	PeerInfo myInfo;
	X500Principal certInfo;
	
	///elementy potrzebne do nasluchowania
	private SSLServerSocketFactory ssf;
	private SSLServerSocket ss;
	
	//public Peer(String keystore,  char[] kestorePass, int listeningPort)
	public Peer(int listeningPort)
	{
		try {
			FileInputStream trustedKeystoreInputS = new FileInputStream("./res/peer/key/peerTrustedKeys");
			this.trustedKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.trustedKeystore.load(trustedKeystoreInputS,"123456".toCharArray());			
			this.tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
			this.tmf.init(this.trustedKeystore);
			FileInputStream myKeystoreInputS = new FileInputStream("./res/peer/key/myKeys"); 
			this.myKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.myKeystore.load(myKeystoreInputS, "123456".toCharArray());
			if(myKeystore.containsAlias("peerPrivKey"))
			{
				 try {
					X509Certificate cert = (X509Certificate) myKeystore
							.getCertificate("peerPrivKey");
					hasValidCert = (cert.getNotAfter().getTime() > System
							.currentTimeMillis() + 24 * 3600 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
					hasValidCert = false;
				}
			}
			else
			{
				hasValidCert = false;
			}
			this.kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
			this.kmf.init(myKeystore, "123456".toCharArray());
			this.sc = SSLContext.getInstance("TLS");
			this.sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			this.sf = sc.getSocketFactory();
			this.listeningPort = listeningPort;
	//		this.ssf = sc.getServerSocketFactory();
	//		this.ss = (SSLServerSocket) ssf.createServerSocket(this.listeningPort);
			
//			ss.setNeedClientAuth(true);			
//			ss.setWantClientAuth(true);
			
			String s = (new BufferedReader(new FileReader(new File("./res/peer/key/principals"))).readLine());
			this.certInfo = new X500Principal(s);
		//	this.myInfo = new PeerInfo(this.ss.getInetAddress(), this.listeningPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) throws UnknownHostException {

		Peer p = new Peer(9795);
		P2SConnection p2s = new P2SConnection(p, InetAddress.getByName("192.168.1.3"), 9995);
		p.peerLogin = new PeerLoginInfo("czarek", "12345", false);
		try {
			p2s.Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	(new Thread(p)).start();
		
		
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
		try {
			this.kmf.init(myKeystore, "123456".toCharArray());
			this.sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			this.ssf = sc.getServerSocketFactory();
			this.ss = (SSLServerSocket) ssf.createServerSocket(this.listeningPort);
			//ss.setWantClientAuth(true);
			ss.setNeedClientAuth(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		while (true) {
			//new Server(ss.accept()).start();
			try {
				for (int i = 0; i < this.ss.getEnabledCipherSuites().length; i++) {
					System.out.println(this.ss.getEnabledCipherSuites()[i]);
				}
				new P2PConnection(this.ss.accept(), this);
				
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void storeX509cert(X509Certificate cert, KeyPair keyPair) throws KeyStoreException {
		try {
			Certificate [] chain =  {cert};
			this.myKeystore.setKeyEntry("peerPrivKey", keyPair.getPrivate(), "123456".toCharArray(),chain);
			X509Certificate c = (X509Certificate) myKeystore.getCertificate("peerPrivKey");
			hasValidCert = (c.getNotAfter().getTime() > System.currentTimeMillis() + 24 * 3600 * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
