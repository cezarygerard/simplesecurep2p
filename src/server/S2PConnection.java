package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSocket;
import javax.security.auth.x500.X500Principal;

import common.Connection;
import common.P2SProtocol;
import common.Pair;
import common.PeerInfo;
import common.PeerLoginInfo;

/**
 * @author czarek
 * TODO wylaczyc czesc wspolna klas S2PConnection i P2SConnection i zrobic wspolna nadklase
 */
public class S2PConnection extends Connection implements Runnable{

	Server server;
	private boolean loggedin;
	
	S2PConnection(Socket accept, Server serv) {
		super();
		this.socket = (SSLSocket) accept;
	//	System.out.println(socket.getRemoteSocketAddress());
	//	System.out.println(socket.getLocalSocketAddress());
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
				System.out.println("[S2PConnection.run] waiting for msg");
				String command = input.readLine();
				HandleCommand(command);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					close  = true;
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					terminateConnectionWithFailure();
				}
			}
		}

	}

	protected void HandleCommand(String command) {
		try{
			System.out.println("[S2PConnection.HandleCommand] command: " + command);
			//timeoutTask.cancel();
			if(command.equals(P2SProtocol.LOGIN))
			{
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
					terminateConnectionWithFailure();
				}
			} 
			else if (loggedin != true)
			{
				
				System.out.println("[S2PConnection.HandleCommand] wrong msg");
				terminateConnectionWithFailure();
			}
			else if (command.equals(P2SProtocol.MYINFO))
			{
				System.out.println("[S2PConnection.HandleCommand] MYINFO");
				PeerInfo pi = (PeerInfo) objInput.readObject();
				System.out.println("[S2PConnection.HandleCommand] PeerInfo: " + pi);
				server.peersInfo.put(pi.addrMd, pi);			
			}
			else if (command.equals(P2SProtocol.GETPEERSINFO))
			{
				send(P2SProtocol.PEERSINFO);
				send(server.peersInfo);
			}
			else if (command.equals(P2SProtocol.GETCERT))
			{
				X500Principal ci = (X500Principal ) objInput.readObject();
				send(P2SProtocol.CERT);
				Pair<X509Certificate, KeyPair> p = server.generateV3Certificate(ci);
				send(p.getFirst());
				send(p.getSecond());
				terminateConnectionGently();
			}
			else if (command.equals(P2SProtocol.EXIT))
			{
				terminateConnectionGently();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			terminateConnectionWithFailure();
		}

	}


}
