package peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.Connection;
import common.FileInfo;
import common.P2PProtocol;
import common.PeerInfo;
import common.utils;


/**
 * @author czarek
 *	szkic klasy obslugujacejn polaczenie P2P
 */
public class P2PConnection extends Connection {

	private Peer peer;
	OutputStream oStream; 
	InputStream iStream;
	public P2PConnection(Socket accept, Peer thisPeer) 
	{
		super();
		System.out.println("[P2PConnection] " + accept.getInetAddress());
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
		System.out.println("[P2PConnection()] "+ addr + "port: " + port);
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
			oStream = socket.getOutputStream();
			iStream = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		thread = new  Thread(this, "P2PCONNECTION " + socket.getInetAddress());
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
				} catch (Exception e1) {
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
			else if(command.equals(P2PProtocol.MY_FILE_INFO))
			{			
				FileInfo fi = (FileInfo) objInput.readObject();
				System.out.println("P2PConnection] MY_FILE_INFO " + fi);
				if(!peer.someoneFiles.add(fi))
				{//wpis juz byl!
					peer.someoneFiles.tailSet(fi).first().ownersInfo.add(peer.myInfo);
				}
				terminateConnectionGently();
			}
			else if(command.equals(P2PProtocol.MY_FILE_INFO_BUCKUP))
			{
				FileInfo fi = (FileInfo) objInput.readObject();
				System.out.println("P2PConnection] MY_FILE_INFO_BUCKUP " + fi);
				if(!peer.backUpFiles.add(fi))
				{//wpis juz byl!
					peer.backUpFiles.tailSet(fi).first().ownersInfo.add(peer.myInfo);
				}
				terminateConnectionGently();
			}
			else if(command.equals(P2PProtocol.NEIGHBOUR_RECOGNITION_INIT))
			{
				send(P2PProtocol.NEIGHBOUR_RECOGNITION_ACK);
				send(new TreeSet<FileInfo>(peer.someoneFiles));
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.NEIGHBOUR_RECOGNITION_ACK))
			{
				TreeSet<FileInfo> files_info = (TreeSet<FileInfo>) objInput.readObject();
				if(!peer.backUpFiles.containsAll(files_info))
				{
					peer.backUpFiles.addAll(files_info);
				}
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.PEER_DEATH_NOTIFICATION))
			{
				PeerInfo pi = (PeerInfo) objInput.readObject();
				peer.peersInfo.remove(pi.addrMd);
			}
			else if (command.equals(P2PProtocol.GET_NEW_BACK_UP_FROM_NEXT))
			{
				send(P2PProtocol.GET_NEW_BACK_UP_FROM_NEXT_ACK);
				send(new TreeSet<FileInfo>(peer.someoneFiles));
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.SEND_BACK_UP_TO_PREV))
			{
				TreeSet<FileInfo> files_info = (TreeSet<FileInfo>) objInput.readObject();
				peer.backUpFiles.clear();
				peer.backUpFiles.addAll(files_info);
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.GET_NEW_BACK_UP_FROM_NEXT_ACK))
			{
				TreeSet<FileInfo> files_info = (TreeSet<FileInfo>) objInput.readObject();
				if(!peer.backUpFiles.containsAll(files_info))
				{
					peer.backUpFiles.addAll(files_info);
				}
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.GET_FILES_INFO))
			{
				PeerInfo pi = (PeerInfo) objInput.readObject();
				this.peer.peersInfo.put(pi.addrMd, pi);
				send(P2PProtocol.GET_FILES_INFO_ACK);			
				send(new TreeSet<FileInfo>(this.peer.someoneFiles.tailSet(new FileInfo(pi.addrMd))));
			}
			else if (command.equals(P2PProtocol.GET_FILES_INFO_ACK))
			{
				TreeSet<FileInfo> files_info = (TreeSet<FileInfo>) objInput.readObject();
				if(!peer.someoneFiles.containsAll(files_info))
				{
					peer.someoneFiles.addAll(files_info);
				}
				sendBackUpToPrev(null);			
			}
			else if (command.equals(P2PProtocol.GET_INITIAL_BACK_UP_FROM_NEXT))
			{
				PeerInfo pi = (PeerInfo) objInput.readObject();
				this.peer.peersInfo.put(pi.addrMd, pi);
			}
			else if (command.equals(P2PProtocol.GET_FILE_OWNER))
			{
				String fileMD = input.readLine();
				send(P2PProtocol.GET_FILE_OWNER_ACK);
				FileInfo tmp = new FileInfo(fileMD);
				try{
					FileInfo fi = ((new TreeSet<FileInfo>(peer.someoneFiles)).subSet(tmp, true, tmp, true)).first();
					send(P2PProtocol.FILE_FOUND);
					send(fi);
				}
				catch (Exception e)
				{
					send(P2PProtocol.FILE_FIND_FAILED);
					terminateConnectionWithFailure();
					return;
				}
				//send(peer.someoneFiles.)
				terminateConnectionGently();
			}
			else if (command.equals(P2PProtocol.GET_FILE_OWNER_ACK))
			{
				if(input.readLine().equals(P2PProtocol.FILE_FOUND))
				{	
					FileInfo fi = (FileInfo) objInput.readObject();
					this.peer.fileFound(fi);
				}
				else
				{
					this.peer.fileFound(null);
				}
			}
			else  if (command.equals(P2PProtocol.DOWNLOAD_FILE))
			{
				FileInfo tmp =  (FileInfo) objInput.readObject();
				FileInfo fi;
				FileInputStream fis;
				File file;
				try{
					fi = ((new TreeSet<FileInfo>(peer.sharedFiles)).subSet(tmp, true, tmp, true)).first();
					file =	fi.file;
					fis = new FileInputStream(file);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					send(P2PProtocol.FILE_DOWNLOAD_FAILED);
					terminateConnectionWithFailure();
					return;
				}
				byte[]b = new byte[1024];
				int len = 0;
				int totalLen = 0;
				send(P2PProtocol.DOWNLOAD_FILE_ACK);
				System.out.println(fi + " FILE SIZE: " + file.length());
				send(fi);
				
				while((len = fis.read(b)) != -1)
				{
					oStream.write(b);
					System.out.println(len);
					totalLen += len;
				}
				fis.close();
				oStream.flush();
				System.out.println("UPLOAD_FILE_DONE totalLen: " + totalLen) ;
			}
			
			else  if (command.equals(P2PProtocol.FILE_DOWNLOAD_FAILED))
			{
				peer.fileDownloaded(null);
			}
			
			else  if (command.equals(P2PProtocol.DOWNLOAD_FILE_ACK))
			{
				System.out.println("DOWNLOAD_FILE_ACK");
				FileInfo fi = (FileInfo) objInput.readObject();
				System.out.println(fi);
				FileOutputStream fos;
				String pathName = peer.sharedFilesDirectory + 	"/" + fi.name + "." + fi.type;
				fos = new FileOutputStream(pathName);
				
				byte[] b = new byte[1024];
				int len = 0;
				int lenTmp = 0;
			//	System.out.println("Bufor: " + this.socket.getReceiveBufferSize());
				while ((lenTmp = iStream.read(b)) != -1)
				{
					fos.write(b);
					System.out.println("lenTmp: " + lenTmp + " TOTAL: " + len);
					len += lenTmp;
				}
			//	System.out.println("Bufor: " + this.socket.getReceiveBufferSize());
				System.out.println(pathName  + " >" + len + "<");
				fos.close();
				terminateConnectionGently();
				fi.file = new File(pathName);
				peer.fileDownloaded(fi);
			}
			else if (command.equals(P2PProtocol.FILE_DOWNLOAD_FAILED))
			{
				peer.fileDownloaded(null);
			}
			
			else if (command.equals(P2PProtocol.FILE_FIND_FAILED))
			{
				peer.fileFound(null);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public void sendFileInfo(FileInfo fi) {
		System.out.println("[P2PConnection.sendFileInfo] " + this.socket.getInetAddress());
		send(P2PProtocol.MY_FILE_INFO);
		send(fi);
		//terminateConnectionGently();
	}

	public void sendBackUpFileInfo(FileInfo fi) {
		System.out.println("[P2PConnection.sendBackUpFileInfo] " + this.socket.getInetAddress());
		send(P2PProtocol.MY_FILE_INFO_BUCKUP);
		send(fi);	
	}

	public void handleNeighbour() {
		System.out.println("[P2PConnection.handleNeighbour] " + this.socket.getInetAddress());
		send(P2PProtocol.NEIGHBOUR_RECOGNITION_INIT);

	}

	public void getBackUpFromNext(PeerInfo deadPeer) {
		// TODO Auto-generated method stub
		System.out.println("[P2PConnection.getNewBackUp] " + this.socket.getInetAddress());
		if(deadPeer != null)
		{
			send(P2PProtocol.PEER_DEATH_NOTIFICATION);
			send(deadPeer);
		}
		send(P2PProtocol.GET_NEW_BACK_UP_FROM_NEXT);
	}

	public void sendBackUpToPrev(PeerInfo deadPeer) {
		// TODO Auto-generated method stub
		System.out.println("[P2PConnection.setNewBackUp] " + this.socket.getInetAddress());
		if(deadPeer != null)
		{
			send(P2PProtocol.PEER_DEATH_NOTIFICATION);
			send(deadPeer);
		}
		send(P2PProtocol.SEND_BACK_UP_TO_PREV);
		send(new TreeSet<FileInfo>(peer.someoneFiles));
	}

	public void obtainFilesInfo() {
		System.out.println("[P2PConnection.obtainFilesInfo] " + this.socket.getInetAddress());
		send(P2PProtocol.GET_FILES_INFO);
		send(peer.myInfo);
	}

	public void obtainBackUp() {
		System.out.println("[P2PConnection.obtainBackUp] " + this.socket.getInetAddress());
		send(P2PProtocol.GET_INITIAL_BACK_UP_FROM_NEXT);
		send(peer.myInfo);
		getBackUpFromNext(null);		
	}

	public void searchForFile(String fileNameMD) {
		System.out.println("[P2PConnection.searchForFile] " + this.socket.getInetAddress());
		send(P2PProtocol.GET_FILE_OWNER);
		send(fileNameMD);		
	}

	public void downloadFile(FileInfo soughtFileInfo) {
		System.out.println("[P2PConnection.downloadFile] " + this.socket.getInetAddress());
		send(P2PProtocol.DOWNLOAD_FILE);
		send(soughtFileInfo);			
	}
}
