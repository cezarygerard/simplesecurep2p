package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.Connection;
import common.FileInfo;
import common.P2PProtocol;
import common.PeerInfo;


/**
 * @author czarek
 *	szkic klasy obslugujacejn polaczenie P2P
 */
public class P2PConnection extends Connection {

	private Peer peer;

	public P2PConnection(Socket accept, Peer thisPeer) 
	{
		super();
		System.out.println("[P2PConnection]");
		this.socket = (SSLSocket) accept;
		
		//	System.out.println(socket.getRemoteSocketAddress());
		//	System.out.println(socket.getLocalSocketAddress());
		peer =thisPeer;
		Init();
	}

	public P2PConnection(Peer thisPeer,InetAddress addr, int port) throws Exception
	{
		super();
		peer =thisPeer;
		try {
			socket = (SSLSocket) peer.sf.createSocket(addr, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
/*		socket.addHandshakeCompletedListener(new HandshakeCompletedListener ()
		{
			public void handshakeCompleted(HandshakeCompletedEvent arg0) {
		//		System.out.println("handshakeCompleted " + arg0.toString() + arg0);						
			}			
		});
*/
		//s.startHandshake();
		//to moznaby inicjalizowac po wstepnym postawieniu nasluchiwania - wiemy jakie gniazdko dostaniemy od systemu
		peer.myInfo = new PeerInfo(socket.getInetAddress(), peer.listeningPort);	
		Init();
	}

	void Init()
	{
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
		PeerInfo pi = new PeerInfo(socket.getInetAddress(), socket.getPort());
		peer.peersInfo.put(pi.addrMd, pi);
		thread = new  Thread(this);
		thread.start();
	}

	@Override
	public void run() {

		while (!close)
		{
			try {
				String command = input.readLine();
				System.out.println("[P2PConnection.run] command: " + command);
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

	protected void HandleCommand(String command)
	{
		try {
			if(command.equals(P2PProtocol.EXIT))
			{
					terminateConnectionGently();			
			}
			else if(command.equals(P2PProtocol.MYFILEINFO))
			{			
				FileInfo fi = (FileInfo) objInput.readObject();
				if(!peer.someoneFiles.add(fi))
				{//wpis juz byl!
					peer.someoneFiles.tailSet(fi).first().ownersInfo.add(peer.myInfo);
				}
				terminateConnectionGently();
			}
			else if(command.equals(P2PProtocol.MYFILEINFOBUCKUP))
			{
				FileInfo fi = (FileInfo) objInput.readObject();
				if(!peer.backUpFiles.add(fi))
				{//wpis juz byl!
					peer.backUpFiles.tailSet(fi).first().ownersInfo.add(peer.myInfo);
				}
				terminateConnectionGently();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
		}
	}

	public void sendFileInfo(FileInfo fi) {
		System.out.println("[P2PConnection.sendFileInfo] " + this.socket.getInetAddress());
		send(P2PProtocol.MYFILEINFO);
		send(fi);
		//terminateConnectionGently();
	}

	public void sendBackUpFileInfo(FileInfo fi) {
		// TODO Auto-generated method stub
		System.out.println("[P2PConnection.sendBackUpFileInfo] " + this.socket.getInetAddress());
		send(P2PProtocol.MYFILEINFOBUCKUP);
		send(fi);	
	}

	public void handleNeighbour() {
		// TODO Auto-generated method stub
		
	}
}
