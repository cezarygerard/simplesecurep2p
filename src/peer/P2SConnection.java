package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.PeerServerProtocol;

public class P2SConnection implements Runnable {

	private Peer peer;
	String hostname;
	int port;
	BufferedReader input;
	PrintWriter output;
	SSLSocket s ;
	ObjectOutput objOutput;
	ObjectInput objInput;
	STATE state;
	public enum STATE {
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

		s = (SSLSocket) peer.sf.createSocket(hostname, port);
		s.addHandshakeCompletedListener(new HandshakeCompletedListener ()
		{
			public void handshakeCompleted(HandshakeCompletedEvent arg0) {
				System.out.println("handshakeCompleted " + arg0.toString() + arg0);						
			}			
		});
		s.startHandshake();
		input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		output = new PrintWriter(s.getOutputStream());	
		objOutput = new ObjectOutputStream(s.getOutputStream());
		objInput = new ObjectInputStream (s.getInputStream());
		
		send(PeerServerProtocol.LOGIN);
		objOutput.writeObject(peer.peerLogin);
		Thread newThrd = new Thread(this);
		newThrd.start();
		state = STATE.LOGGING;
		
	}

	public void run() {
		while (true)
		{
			try {
				String command = input.readLine();
				HandleCommand(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	  private void HandleCommand(String command) {
		System.out.println(command);
		
	}

	void send(String command) {
	        if (output != null)
	            output.println(command);
	    }

}

