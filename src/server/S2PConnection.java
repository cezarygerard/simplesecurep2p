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

import common.Connection;
import common.P2SProtocol;
import common.PeerInfo;
import common.PeerLoginInfo;

/**
 * @author czarek
 * TODO wylaczyc czesc wspolna klas S2PConnection i P2SConnection i zrobic wspolna nadklase
 */
public class S2PConnection extends Connection implements Runnable{

	Server server;
	private STATE state;
	private boolean loggedin;
	private enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}

	S2PConnection(Socket accept, Server serv) {
		super();
		this.socket = (SSLSocket) accept;
		server =serv;
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			close = false;
			loggedin = false;
			objOutput = new ObjectOutputStream(socket.getOutputStream());
			objInput = new ObjectInputStream (socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		thread = new  Thread(this);
		thread.start();
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
					terminateConnection();
				}
			}
		}

	}

	protected void HandleCommand(String command) {
		try{
			System.out.println("[S2PConnection.HandleCommand] command: " + command);

			if(command.equals(P2SProtocol.LOGIN))
			{
				System.out.println("[S2PConnection.HandleCommand]");

				PeerLoginInfo pli = (PeerLoginInfo)objInput.readObject();

				if(this.server.verifyPeer(pli))
				{
					System.out.println("[S2PConnection.HandleCommand] LOGINACK");
					send(P2SProtocol.LOGINACK);
					loggedin = true;
				}
				else
				{
					System.out.println("[S2PConnection.HandleCommand] LOGINFAILED");
					send(P2SProtocol.LOGINFAILED);
					terminateConnection();
				}
			} 
			else if (loggedin != true)
			{
				System.out.println("[S2PConnection.HandleCommand] wrong msg");
				terminateConnection();
			}
			else if (command.equals(P2SProtocol.MYINFO))
			{
				PeerInfo pi = (PeerInfo) objInput.readObject();
				server.peersInfo.add(pi);
			}
		}catch(Exception e){
			e.printStackTrace();
			terminateConnection();
		}

	}


}
