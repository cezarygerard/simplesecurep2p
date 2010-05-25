package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import common.P2SProtocol;
import common.PeerLoginInfo;

/**
 * @author czarek
 * TODO wylaczyc czesc wspolna klas S2PConnection i P2SConnection i zrobic wspolna nadklase
 */
public class S2PConnection implements Runnable{
	
	Server server;
	private BufferedReader input;
	private PrintWriter output;
	private SSLSocket socket ;
	private ObjectOutput objOutput;
	private ObjectInput objInput;
	private STATE state;
	private boolean close;
	
	private enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}
	
	S2PConnection(Socket accept, Server serv) {
		this.socket = (SSLSocket) accept;
		server =serv;
		try {
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		close = false;

		objOutput = new ObjectOutputStream(socket.getOutputStream());
		objInput = new ObjectInputStream (socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		Thread t = new  Thread(this);
		t.start();
	}

	@Override
	public void run() {
		
		while (!close)
		{
			try {
				System.out.println("S2PConnection");
				String command = input.readLine();
				HandleCommand(command);
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					close  = true;
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}

	private void HandleCommand(String command) {
		// TODO Auto-generated method stub
		System.out.println("[S2PConnection.HandleCommand] command: " + command);
		
		if(command.equals(P2SProtocol.LOGIN))
		{
			PeerLoginInfo pli;
			System.out.println("[S2PConnection.HandleCommand]");
			try {
				
				pli = (PeerLoginInfo)objInput.readObject();
				System.out.println(pli);
				if(this.server.verifyPeer(pli))
				{
					System.out.println("[S2PConnection.HandleCommand] LOGINACK");
					send(P2SProtocol.LOGINACK);
				}
				else
				{
					System.out.println("[S2PConnection.HandleCommand] LOGINFAILED");
					send(P2SProtocol.LOGINFAILED);
					close = true;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
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
