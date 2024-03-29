package peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
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

import common.PeerActionObserver;
import common.FileInfo;
import common.PeerInfo;
import common.PeerLoginInfo;
import common.ServerInfo;
import common.TerminationListener;
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
	SortedSet<PeerActionObserver> observers = Collections.synchronizedSortedSet(new TreeSet<PeerActionObserver>());
	///elementy potrzebne do nasluchowania
	private SSLServerSocketFactory ssf;
	private SSLServerSocket ss;
	String sharedFilesDirectory;
	boolean loginSuccesfull;
	
	
	//public Peer(String keystore,  char[] kestorePass, int listeningPort)
	public Peer(int listeningPort) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchProviderException, KeyManagementException, UnrecoverableKeyException
	{
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
		loginSuccesfull = false;
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

	}

	public static Peer createPeer(String serverAddress, int serverListeningPort, int myListeningPort, String login, String password, String sharedFilesDirectory) throws IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, Exception
	{
		final Peer p = new Peer(myListeningPort);
		P2SConnection p2s = new P2SConnection(p, InetAddress.getByName(serverAddress), serverListeningPort);
		p.sharedFilesDirectory = sharedFilesDirectory;
		p.peerLogin = new PeerLoginInfo(login, password, false);
		p2s.addTerminationListener(new TerminationListener() {
			
			public void connectionTerminated() throws Exception{
				try{
				p.init();
				}catch (Exception e)
				{
					throw e;
				}				
			}
		});
		
		p2s.Connect();
		
		return p;
	}

	public static void main(String[] args) throws Exception {

		Peer p =  createPeer("192.168.1.3", 9995, 9975, "cza2rek", "123425", null);
		
/*		p.getFiles(null);
		P2SConnection p2s = new P2SConnection(p, InetAddress.getByName("192.168.1.3"), 9995);
		p.peerLogin = new PeerLoginInfo("czarek", "12345", false);
		try {
			p2s.Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		//	(new Thread(p)).start();
		while(true)
		{
			try {
				Thread.sleep(100 + utils.NEIGHBOUR_RECOGNITION_PERIOD );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			///@Debug
			//p.searchForFile("gfd");
		}
	}

	public void init() throws Exception {
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
		getFiles(this.sharedFilesDirectory);
		sendOutMyFilesInfo();
		addMyselfIntoNetwork();
	}

	public void run() {
		
		System.out.println("[Peer.run]");
		for (Iterator<PeerActionObserver> iterator = observers.iterator(); iterator.hasNext();) {
			(iterator.next()).peerActionPerformed(PeerActionObserver.CONNECTION_ESTABLISHED );			
		}

		
		neighbourRecognitionTimer.schedule(new RocognizeNeighbour(this), utils.NEIGHBOUR_RECOGNITION_PERIOD, utils.NEIGHBOUR_RECOGNITION_PERIOD);
		
		while (true) {
			//new Server(ss.accept()).start();
			try {
				//	for (int i = 0; i < this.ss.getEnabledCipherSuites().length; i++) {
				//		System.out.println(this.ss.getEnabledCipherSuites()[i]);
				//	}
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

	private void addMyselfIntoNetwork() throws Exception 
	{
		try{
		System.out.println("[Peer.addMyselfIntoNetwork]");
		PeerInfo prevPeer = getPrevPeerInfo(this.myInfo.addrMd);
		if(!prevPeer.equals(this.myInfo))
		{
			P2PConnection p2p = null ;
			try {
				p2p= new P2PConnection(this, prevPeer.addr,prevPeer.listeningPort);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Nieudane zalogowanie do sieci");
			}

			final Peer p = this;
			p2p.addTerminationListener(new TerminationListener() {

				public void connectionTerminated() throws Exception {
					PeerInfo nextPeer = getNextPeerInfo((p.myInfo.addrMd));
					try {
						P2PConnection p2p= new P2PConnection(p, nextPeer.addr,nextPeer.listeningPort);
						p2p.addTerminationListener(new TerminationListener() {

							@Override
							public void connectionTerminated() {
								p.StartListening();

							}
						});
						p2p.obtainBackUp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new Exception("Nieudane zalogowanie do sieci");
					}

				}
			});
			p2p.obtainFilesInfo();		
		}
		else 
			this.StartListening();
		}
		catch (Exception e) {
			this.StartListening();
		}
	}

	private class RocognizeNeighbour extends TimerTask
	{
		Peer thisPeer;
		public RocognizeNeighbour(Peer thisPeer) {
			super();
			this.thisPeer = thisPeer;
		}
		public void run() {
			PeerInfo neighbour;
			neighbour = getNextPeerInfo(thisPeer.myInfo.addrMd);
			if(!(neighbour.equals(thisPeer.myInfo)) && neighbour != null )
			{
				try {
					(new P2PConnection(thisPeer, neighbour.addr, neighbour.listeningPort)).handleNeighbour();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					thisPeer.handlePeerDeath(neighbour);
				}
			}
		}

	}
	/*	
	private TimerTask rocognizeNeighbour() {
		final Peer p = this;
		final PeerInfo neighbour;

		neighbour = getNextPeerInfo(this.myInfo.addrMd);

		if(!(neighbour.equals(this.myInfo)) && neighbour != null )
		{
			return new TimerTask() {

				@Override
				public void run() {				
					try {
						(new P2PConnection(p, neighbour.addr, neighbour.listeningPort)).handleNeighbour();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						p.handlePeerDeath(neighbour);
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
	 */
	private  PeerInfo getNextPeerInfo(String key)
	{
		String validKey = null;
		//SortedMap<String, PeerInfo > sm = this.peersInfo.tailMap(key);
		TreeMap<String, PeerInfo> tm = new TreeMap<String, PeerInfo>(this.peersInfo.tailMap(key));
		if(tm != null && tm.size() > 0)
		{
			validKey = (String) tm.higherKey(key);
		}

		if(validKey == null)
			validKey = this.peersInfo.firstKey();

		return  this.peersInfo.get(validKey);
	}

	private PeerInfo getPrevPeerInfo(String key)
	{
		String validKey = null;
		SortedMap<String, PeerInfo > sm = this.peersInfo.headMap(key);
		if(sm != null && sm.size() > 0)
		{
			validKey = (String) sm.lastKey();
		}

		if(validKey == null)
			validKey = this.peersInfo.lastKey();

		return  this.peersInfo.get(validKey);
	}

	protected void handlePeerDeath(final PeerInfo neighbour) {
		System.out.println("[Peer.peerDeathNotify] " + neighbour);
		this.peersInfo.remove(neighbour.addrMd);
		//	neighbourRecognitionTimer.cancel();
		//	neighbourRecognitionTimer = new Timer();
		//	neighbourRecognitionTimer.schedule(rocognizeNeighbour(), utils.NEIGHBOUR_RECOGNITION_PERIOD, utils.NEIGHBOUR_RECOGNITION_PERIOD);
		final Peer p = this;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try{
				P2SConnection p2s = new P2SConnection(p,p.serverInfo.addr, p.serverInfo.listeningPort);
				p2s.peerDeathNotification(neighbour);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "handlerPeerDeath, peerDeathNotification " + neighbour);
		t.start();
		this.someoneFiles.addAll(this.backUpFiles);
		this.backUpFiles.clear();


		final PeerInfo nexyPeer = getNextPeerInfo(this.myInfo.addrMd);
		final PeerInfo prevPeer = getPrevPeerInfo(this.myInfo.addrMd);
		//	if(!(nexyPeer.equals(this.myInfo)) && nexyPeer != null )
		//	{
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
					if(!(nexyPeer.equals(p.myInfo)) && nexyPeer != null )
					{
						try {
							P2PConnection p2pNext = new P2PConnection(p, nexyPeer.addr, nexyPeer.listeningPort);
							p2pNext.getBackUpFromNext(neighbour);
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(!(prevPeer.equals(p.myInfo)) && prevPeer != null)
					{
						try {
							P2PConnection p2pPrev = new P2PConnection(p, prevPeer.addr, prevPeer.listeningPort);
							p2pPrev.sendBackUpToPrev(neighbour);
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, "handlerPeerDeath getNewBackUp " + neighbour);
		t1.start();
		//	}
		/*
		final PeerInfo prevPeer = getPrevPeerInfo(this.myInfo.addrMd);	
		if(!(prevPeer.equals(this.myInfo)) && prevPeer != null )
		{
			Thread t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					P2PConnection p2p;
					try {
						p2p = new P2PConnection(p, prevPeer.addr, prevPeer.listeningPort);
						p2p.sendBackUpToPrev(neighbour);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, "handlerPeerDeath setNewBackUp " + neighbour);
			t2.start();
		}
		 */
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

	void StartListening() {
		if (this.hasValidCert && loginSuccesfull)
		{
			new Thread(this, "Listening Thread").start();
		}
		else
			this.handleLoginFailed();
	}

	void getFiles(String path)
	{
		if(path == null)
			path = "./SharedFiles";
		this.sharedFilesDirectory = path;
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
		try{
		for (Iterator<FileInfo> iterator = sharedFiles.iterator(); iterator.hasNext();) 
		{
			synchronized (this) {
				final FileInfo fi = iterator.next();
				System.out.println("[Peer.sendOutMyFilesInfo] " + fi.ownersInfo); 
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
		catch (Exception e) {
		//	this.handleLoginFailed();
		}
	}

	public void searchForFile(String soughtFileName){
		
		String fileNameMD = null;
		FileInfo soughtFileInfo = null;
		try {
			fileNameMD = utils.toHexString(utils.MDigest(null, soughtFileName.getBytes()));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		P2PConnection p2p = null;
		PeerInfo pi = null;
		try {
			pi = getPrevPeerInfo(fileNameMD);
			p2p = new P2PConnection(this, pi.addr, pi.listeningPort);
			p2p.searchForFile(fileNameMD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try{
				pi = getPrevPeerInfo(pi.addrMd);
				p2p = new P2PConnection(this, pi.addr, pi.listeningPort);
				p2p.searchForFile(fileNameMD);
			}catch (Exception e1) {
				e1.printStackTrace();
				fileFound(null);
			}
		}	
	}


	public void downloadFile(FileInfo soughtFileInfo)
	{
		//Random random = new Random();
		//int i = random.nextInt() % soughtFileInfo.ownersInfo.size();
		PeerInfo owner = soughtFileInfo.ownersInfo.get(0);
		P2PConnection p2p = null;
		try {
			p2p = new P2PConnection(this, owner.addr, owner.listeningPort);
		} catch (Exception e) {
			fileDownloaded(null);
			return;
		}
		p2p.downloadFile(soughtFileInfo);
	}	
	
	public void addPeerActionObserver(PeerActionObserver observer)
	{
		observers.add(observer);
		
	}
	
	void fileFound(FileInfo fi) {
		
		System.out.println(fi);
		for (Iterator<PeerActionObserver> iterator = observers.iterator(); iterator.hasNext();) {
			(iterator.next()).fileActionPerformed(fi, PeerActionObserver.FILE_FOUND);			
		}
		if(fi == null)
			synchroniezeWithServer();
		
	//	downloadFile(fi);
	}
	
	void fileDownloaded(FileInfo fi) {
		
		System.out.println(fi);
		for (Iterator<PeerActionObserver> iterator = observers.iterator(); iterator.hasNext();) {
			(iterator.next()).fileActionPerformed(fi, PeerActionObserver.FILE_DOWNLOADED);			
		}
		
		if(fi == null)
			synchroniezeWithServer();
	}
	
	void synchroniezeWithServer()
	{
		try
		{
		P2SConnection p2s = new P2SConnection(this, serverInfo.addr, serverInfo.listeningPort);
		p2s.synchronize();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleLoginFailed() {
		System.out.println("handleLoginFailed");
		for (Iterator<PeerActionObserver> iterator = observers.iterator(); iterator.hasNext();) {
			(iterator.next()).peerActionPerformed(PeerActionObserver.LOGIN_FAILED);			
		}
		
	}
}

