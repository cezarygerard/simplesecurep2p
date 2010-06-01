package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

import peer.Peer;

import common.PeerInfo;
import common.PeerLoginInfo;

/**
 * @author czarek
 * @TODO ta klasa powinna byc singletonem
 * @TODO Wybrac waska grupe akceptowanych szyfrowan w polaczeniach
 */

public class Server {

	SortedSet<PeerLoginInfo > loginInfo = Collections.synchronizedSortedSet(new TreeSet<PeerLoginInfo>()) ; 
	SortedMap<String, PeerInfo> peersInfo = Collections.synchronizedSortedMap(new TreeMap<String, PeerInfo>());

	private FileInputStream is ; 
	private KeyStore keystore;
	private KeyManagerFactory kmf;
	private SSLContext sc;
	private SSLServerSocketFactory ssf;
	private SSLServerSocket ss;
	PrivateKey caPrivKey;
	int listeningPort;
	public enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}
	public Server(String keystoreLoc,  char[] kestorePass)
	{
		try {
			is = new FileInputStream(keystoreLoc) ;
			this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, "123456".toCharArray());
			kmf =   KeyManagerFactory.getInstance("SunX509", "SunJSSE");
			kmf.init(keystore, ("123456").toCharArray());
			sc = SSLContext.getInstance("SSL");
			sc.init(kmf.getKeyManagers(), null, null);
			ssf = sc.getServerSocketFactory();
			readServerInfo("./res/server/ServerInfo.dat");
			ss = (SSLServerSocket)ssf.createServerSocket(listeningPort);
			caPrivKey = (PrivateKey) keystore.getKey("serverTrustedCert", "123456".toCharArray());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}	
	}


	public static void main(String[] args) throws Exception {


		Server server = new Server ("./res/server/key/serverKeys" , "123456".toCharArray());
	//	System.out.println(server.sc.getProvider());
	//	System.out.println(server.ss.getEnabledCipherSuites());
		for (int i = 0; i < server.ss.getEnabledCipherSuites().length; i++) {
			System.out.println(server.ss.getEnabledCipherSuites()[i]);
		}
	
		/**
		 * Zrob z tego w¹tek
		 */
		while (true) {
			//new Server(ss.accept()).start();
			try {
				System.out.println("serwer czeka");
				new S2PConnection(server.ss.accept(), server);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Metoda zaczytujaca z pliku loginy i skroty hasel
	 * @TODO dodac sol do hasel
	 */
	private void readServerInfo(String serverInfo)
	{
		try {
			System.out.println("readLoginInfo");
			FileReader fr = new FileReader(serverInfo);

			BufferedReader br = new BufferedReader(fr);
			StringTokenizer st;// = new StringTokenizer();
			String line;			
			boolean timeTobreak = false;
			while(!timeTobreak)
			{
				line =br.readLine();
				if(!(line.startsWith("#")))
				{	
					this.listeningPort = Integer.valueOf(line);
					timeTobreak = true;
				}
			}
			
			while((line = br.readLine()) != null )
			{		
				
				if(!(line.startsWith("#")))
				{
					st = new StringTokenizer(line);
					if((st.countTokens() )!= 2)
						throw new Exception("Ivalid peerLoginInfo.dat file");
	
					loginInfo.add(new PeerLoginInfo(st.nextToken(), st.nextToken(), true));
				}
			}
			System.out.println("[Server.readLoginInfo()] " + loginInfo);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Sprawdza, czy dane od peera zgadzaja sie z tymi z pliku
	 * @param PeerLoginInfo pli
	 * @return wynik weryfikacji
	 */
	boolean verifyPeer(PeerLoginInfo pli)
	{
		PeerLoginInfo pliAtServer= loginInfo.tailSet(pli).first();
		if (pliAtServer != null && (pliAtServer.getPasswdHash()).equals(pli.getPasswdHash()))
			return true;
		else
			return false;			
	}	

	/**
	 * Generowanie certyfikatu x509
	 * @param certInfo informacje ktore maja znalezc sie w certyfikacie
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 * @throws CertificateEncodingException
	 * @throws IllegalStateException
	 * @throws NoSuchAlgorithmException
	 */
	public common.Pair<X509Certificate, KeyPair> generateV3Certificate(X500Principal certInfo) throws InvalidKeyException,
	NoSuchProviderException, SignatureException, CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException,KeyStoreException {
		KeyPairGenerator keyGen;
		keyGen = KeyPairGenerator.getInstance("RSA");	
		keyGen.initialize(1024);
		KeyPair keyPair = keyGen.generateKeyPair(); 
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());		
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(((X509Certificate)this.keystore.getCertificate("servertrustedcert")).getSubjectX500Principal());	
		certGen.setNotBefore(new Date(System.currentTimeMillis() - 7*24*3600*1000));
		certGen.setNotAfter(new Date(System.currentTimeMillis() +  7*24*3600*1000));
		certGen.setSubjectDN(certInfo);
		certGen.setPublicKey(keyPair.getPublic());
		certGen.setSignatureAlgorithm("SHA1withDSA");
		
		//return new Pair(certGen.generate(this.caPrivKey), keyPair);
		return new common.Pair<X509Certificate, KeyPair>(certGen.generate(this.caPrivKey), keyPair);
	}
}

