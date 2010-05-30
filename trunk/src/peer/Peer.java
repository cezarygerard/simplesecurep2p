package peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

import common.FileInfo;
import common.PeerInfo;
import common.PeerLoginInfo;
import common.ServerInfo;
import common.utils;


/**
 * @author czarek
 * @TODO ta klasa powinna byc singletonem
 * @TODO Wybrac waska grupe akceptowanych szyfrowan w polaczeniach
 */
public class Peer implements Runnable {

	SortedMap <String, PeerInfo > peersInfo = Collections.synchronizedSortedMap(new TreeMap<String, PeerInfo>());
	//	SortedSet<List<FileInfo>> sharedFiles = Collections.synchronizedSortedSet(new TreeSet< List<FileInfo> > ());
	//	SortedSet<List<FileInfo>> someoneFiles = Collections.synchronizedSortedSet(new TreeSet< List<FileInfo> > ());
	//	SortedSet<List<FileInfo>> backUpFiles = Collections.synchronizedSortedSet(new TreeSet< List<FileInfo> > ());
	SortedSet<FileInfo> sharedFiles = Collections.synchronizedSortedSet(new TreeSet< FileInfo > ());
	SortedSet<FileInfo> someoneFiles = Collections.synchronizedSortedSet(new TreeSet< FileInfo > ());
	SortedSet<FileInfo> backUpFiles = Collections.synchronizedSortedSet(new TreeSet< FileInfo > ());

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
	ServerInfo serverInfo;
	X500Principal certInfo;
	Timer neighbourRecognitionTimer = new Timer("neighbourRecognitionThread");

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
			trustedKeystoreInputS.close();
			this.tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
			this.tmf.init(this.trustedKeystore);
			FileInputStream myKeystoreInputS = new FileInputStream("./res/peer/key/myKeys"); 
			this.myKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.myKeystore.load(myKeystoreInputS, "123456".toCharArray());
			myKeystoreInputS.close();
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
		p.getFiles(null);
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
		//	this.sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			this.sc.init(kmf.getKeyManagers(), null, null);
			this.ssf = sc.getServerSocketFactory();
			this.ss = (SSLServerSocket) ssf.createServerSocket(this.listeningPort);
			//ss.setWantClientAuth(true);
			ss.setNeedClientAuth(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		sendOutMyFilesInfo();
		neighbourRecognitionTimer.schedule(rocognizeNeighbour(), utils.NEIGHBOUR_RECOGNITION_PERIOD, utils.NEIGHBOUR_RECOGNITION_PERIOD);
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

	private TimerTask rocognizeNeighbour() {
		final Peer p = this;
		final PeerInfo neighbour;
		String neighbourKey = null;
		SortedMap<String, PeerInfo > sm = this.peersInfo.headMap(this.myInfo.addrMd);
		if(sm != null && sm.size() > 0)
		{
			neighbourKey = (String) sm.lastKey();
		}

		if(neighbourKey == null)
			neighbourKey = this.peersInfo.lastKey();

		neighbour = this.peersInfo.get(neighbourKey);
		if(!(neighbour.equals(this.myInfo)))
		{
			return new TimerTask() {

				@Override
				public void run() {				
					try {
						(new P2PConnection(p, neighbour.addr, neighbour.listeningPort)).handleNeighbour();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						p.peerDeathHandler(neighbour);
					}
				}
			};		
		}
		else
			return new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		};
	}

	protected void peerDeathHandler(final PeerInfo neighbour) {
		System.out.println("[Peer.peerDeathNotify] " + neighbour);
		this.peersInfo.remove(neighbour.addrMd);
		neighbourRecognitionTimer.schedule(rocognizeNeighbour(), utils.NEIGHBOUR_RECOGNITION_PERIOD, utils.NEIGHBOUR_RECOGNITION_PERIOD);
		final Peer p = this;
		Thread t = new Thread(new Runnable() {
		
			@Override
			public void run() {
				P2SConnection p2s = new P2SConnection(p,p.serverInfo.addr, p.serverInfo.listeningPort);
				p2s.peerDeathNotification(neighbour);
			}
		}, " peerDeathHandler " + neighbour);
		t.start();
	}

	void storeX509cert(X509Certificate cert, KeyPair keyPair) throws KeyStoreException {
		try {
			Certificate [] chain =  {cert};
			this.myKeystore.setKeyEntry("peerPrivKey", keyPair.getPrivate(), "123456".toCharArray(),chain);
			X509Certificate c = (X509Certificate) myKeystore.getCertificate("peerPrivKey");
			hasValidCert = (c.getNotAfter().getTime() > System.currentTimeMillis() + 24 * 3600 * 1000);
			FileOutputStream fos = new FileOutputStream("./res/peer/key/myKeys");
			myKeystore.store(fos, "123456".toCharArray());
			fos.close();		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	void StatrListening() {
		if (this.hasValidCert)
		{
			new Thread(this, "Listening Thread").start();
		}		
	}

	void getFiles(String path)
	{
		if(path == null)
			path = "./SharedFiles";

		File dir = new File(path);
		File[] files = dir.listFiles();
		if(files !=null)
		{
			for (int i = 0; i < files.length; i++) {
				try{

					System.out.println(files[i].getName() + " " + files[i].getAbsolutePath());
					FileInfo fi = new FileInfo(files[i]);
					fi.ownersInfo.add(this.myInfo);
					sharedFiles.add(fi);
				}catch (Exception e) {
					e.printStackTrace();
				}						
			}
		}
	}

	/**
	 * @return 
	 * @Todo optymalizacji poprzez grupowania plikow do poszczegolnych peerow i przeslanie tego jednym polaczeniem
	 */
	synchronized  void sendOutMyFilesInfo(){

		for (Iterator<FileInfo> iterator = sharedFiles.iterator(); iterator.hasNext();) 
		{
			synchronized (this) {
				final FileInfo fi = iterator.next();

				//final PeerInfo pi = this.peersInfo.get(this.peersInfo.lowerKey(fi.nameMD));
				PeerInfo infoOwner;// = new PeerInfo(null, 0);
				PeerInfo buckupOwner;
				String infoOwnerKey = null;// = this.peersInfo.lowerKey(fi.nameMD);
				String buckupOwnerKey = null;// = 

				SortedMap<String, PeerInfo > sm = this.peersInfo.headMap(fi.nameMD);
				if(sm != null && sm.size() > 0)
				{
					infoOwnerKey = (String) sm.lastKey();
				}

				if(infoOwnerKey == null)
					infoOwnerKey = this.peersInfo.lastKey();

				SortedMap<String, PeerInfo > sm1 = this.peersInfo.headMap(infoOwnerKey);
				if(sm1 != null && sm1.size() > 0)
				{
					buckupOwnerKey = (String) sm1.lastKey();
				}

				if(buckupOwnerKey == null)
					buckupOwnerKey = this.peersInfo.lastKey();


				infoOwner = this.peersInfo.get(infoOwnerKey);
				buckupOwner = this.peersInfo.get(buckupOwnerKey);

				final Peer p = this;

				System.out.println("[Peer.sendOutMyFilesInfo] srkoty peerow: infoOwnerKey: " + infoOwnerKey + " buckupOwnerKey "  + buckupOwnerKey + " myinfo: " + this.myInfo.addrMd);
				System.out.println("[Peer.sendOutMyFilesInfo] srkoty peerow: infoOwner.equals(this.myInfo): " + infoOwner.equals(this.myInfo) + " (buckupOwner.equals(this.myInfo)) "  + (buckupOwner.equals(this.myInfo)));

				if((infoOwner.equals(this.myInfo)))
				{
					infoOwner = null;
					if(!this.someoneFiles.add(fi))
					{//wpis juz byl!
						this.someoneFiles.tailSet(fi).first().ownersInfo.add(this.myInfo);
					}
				}

				if((buckupOwner.equals(this.myInfo)))
				{
					buckupOwner = null;
					if(!this.backUpFiles.add(fi))
					{//wpis juz byl!
						this.backUpFiles.tailSet(fi).first().ownersInfo.add(this.myInfo);
					}
				}
				final PeerInfo finalInfoOwner = infoOwner;
				final PeerInfo finalbuckupOwner = buckupOwner; 
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(finalInfoOwner != null)
							try {
								new P2PConnection(p, finalInfoOwner.addr, finalInfoOwner.listeningPort).sendFileInfo(fi);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
						if(finalbuckupOwner != null)
							try {
								new P2PConnection(p, finalbuckupOwner.addr, finalbuckupOwner.listeningPort).sendBackUpFileInfo(fi);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
				}, " send file infos  finalInfoOwner: " + finalInfoOwner +  " finalbuckupOwner: " + finalbuckupOwner);
			
				t.start();
			}
		}		
	}
}

