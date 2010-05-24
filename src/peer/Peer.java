package peer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Random;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import common.PeerLoginInfo;


/**
 * @author czarek
 * TODO ta klasa powinna byc singletonem
 */
public class Peer implements Runnable {

	KeyStore keystore;
	TrustManagerFactory tmf;
	SSLContext sc;
	SSLSocketFactory sf;
	int listeningPort;
	PeerLoginInfo peerLogin;
	
	public Peer(String keystore,  char[] kestorePass, int listeningPort)
	{
		try {
			FileInputStream is = new FileInputStream(keystore);
			this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			this.keystore.load(is,kestorePass);			
			this.tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
			tmf.init(this.keystore);
			this.sc = SSLContext.getInstance("TLS");
			sc.init(null, tmf.getTrustManagers(), null);
			this.sf = sc.getSocketFactory();
			this.listeningPort = listeningPort;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {

		Peer p = new Peer("./res/common/key/serverKeys" , "123456".toCharArray(), 9795);
		P2SConnection p2s = new P2SConnection(p, "localhost", 9097);
		p.peerLogin = new PeerLoginInfo("czarek", "12345");
		try {
			p2s.Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
	//	while (true)
     //       try {
   //             String command = bufferReader.readLine();
     //           if (!handleCommand(command)) {
      //          }
              	
	//	catch (IOException e) {
	//		e.printStackTrace();
   //     }
	}
	


}
