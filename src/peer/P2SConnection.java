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
import java.util.Timer;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.Connection;
import common.P2SProtocol;

public class P2SConnection extends Connection implements Runnable {

	private Peer peer;
	private String hostname;
	private int port;
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
		socket = (SSLSocket) peer.sf.createSocket(InetAddress.getByName("192.168.1.4"), port);
		
		socket.addHandshakeCompletedListener(new HandshakeCompletedListener ()
		{
			public void handshakeCompleted(HandshakeCompletedEvent arg0) {
				System.out.println("handshakeCompleted " + arg0.toString() + arg0);						
			}			
		});
		//s.startHandshake();

		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);	

		objOutput = new ObjectOutputStream(socket.getOutputStream());
		objInput = new ObjectInputStream (socket.getInputStream());

		send(P2SProtocol.LOGIN);

		//	send(peer.peerLogin);

		thread = new Thread(this);
		thread.start();
		state = STATE.LOGGING;
		System.out.println("[P2SConnection.Connect()] ");

	}

	public void run() {
		while (!close)
		{
			try {
				System.out.println("P2SConnection");
				String command = input.readLine();
				HandleCommand(command);
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close = true;
			}
		}
	}

	protected void HandleCommand(String command) {
		try
		{
			System.out.println("[P2SConnection.HandleCommand] command: " + command);

			if (state == STATE.LOGGING && command.equals(P2SProtocol.LOGINACK))
			{
				state=STATE.LOGGEDIN;
				send(P2SProtocol.MYINFO);
				send(peer.myInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
			terminateConnection();
		}
	}
}
