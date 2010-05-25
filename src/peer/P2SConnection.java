package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.Connection;
import common.PeerServerProtocol;

public class P2SConnection implements Runnable {

	private Peer peer;
	private String hostname;
	private int port;
	private BufferedReader input;
	private PrintWriter output;
	private SSLSocket s ;
	private ObjectOutput objOutput;
	private ObjectInput objInput;
	private STATE state;
	
	private enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}

	P2SConnection(Peer p,String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
		this.peer = p;
	}

	void Connect() throws Exception 
	{

		//s = (SSLSocket) peer.sf.createSocket(hostname, port);
		s = (SSLSocket) peer.sf.createSocket(InetAddress.getByName("192.168.1.4"), port);
		s.addHandshakeCompletedListener(new HandshakeCompletedListener ()
		{
			public void handshakeCompleted(HandshakeCompletedEvent arg0) {
				System.out.println("handshakeCompleted " + arg0.toString() + arg0);						
			}			
		});
		//s.startHandshake();

		input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		output = new PrintWriter(s.getOutputStream());	

		objOutput = new ObjectOutputStream(s.getOutputStream());
		objInput = new ObjectInputStream (s.getInputStream());

		send(PeerServerProtocol.LOGIN);

	//	send(peer.peerLogin);

		Thread newThrd = new Thread(this);
		newThrd.start();
		state = STATE.LOGGING;
		System.out.println("[P2SConnection.Connect()] ");

	}

	public void run() {
		while (true)
		{
			try {
				System.out.println("P2SConnection");
				String command = input.readLine();
				HandleCommand(command);
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void HandleCommand(String command) {
		System.out.println(command);
		send("HELLO");

	}

	private void send(String command) {
		if (output != null)
			output.println(command);
	}

	private void send(Object obj) {
		if (objOutput != null)
			try {
				objOutput.writeObject(obj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}

