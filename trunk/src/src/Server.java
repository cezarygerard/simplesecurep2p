package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import javax.swing.JOptionPane;

public class Server implements Runnable {
	private HostAuthorizationPair myHostAuthorizationPair;
	private ServerFactory myServerFactory;
	private String myPassword;
	private Socket mySocket;
	private int mySocketTimeout;
	private boolean isHandshaked;
	private boolean isRunning;
	private InputStream myInputStream;
	private HostId myRemoteHostId;

	public Server(String inpPassword, Socket inpSocket, HostAuthorizationPair inpHostAuthorizationPair, ServerFactory inpServerFactory, int inpSocketTimeout) {
		specify(inpPassword, inpSocket, inpHostAuthorizationPair, inpServerFactory, inpSocketTimeout);
	}
	
	public Server(String inpPassword, Socket inpSocket, int inpSocketTimeout) {
		specify(inpPassword, inpSocket, inpSocketTimeout);
	}
	
	private void specify(String inpPassword, Socket inpSocket, HostAuthorizationPair inpHostAuthorizationPair, ServerFactory inpServerFactory, int inpSocketTimeout) {
		myHostAuthorizationPair = inpHostAuthorizationPair;
		myServerFactory = inpServerFactory;
		initialize(inpPassword, inpSocket, inpSocketTimeout);
	}
	
	private void specify(String inpPassword, Socket inpSocket, int inpSocketTimeout) {
		myHostAuthorizationPair = null;
		myServerFactory = null;
		initialize(inpPassword, inpSocket, inpSocketTimeout);
	}
	
	private void initialize(String inpPassword, Socket inpSocket, int inpSocketTimeout) {
		myPassword = inpPassword;
		mySocket = inpSocket;
		mySocketTimeout = inpSocketTimeout;
		isHandshaked = false;
		isRunning = true;
		tryToInitializeSocket();
	}
	
	private void tryToInitializeSocket() {
		try {
			myInputStream = mySocket.getInputStream();
			mySocket.setSoTimeout(mySocketTimeout);
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			closeConnection();
		}
	}

	private void processMessage(String inpMessage) {
		System.out.println("[i] Server: Recived: " + inpMessage);
		
		if(!isHandshaked) {
			if(myHostAuthorizationPair == null) {
				verifyPortAndSequenceNumber(inpMessage);
			}
			else {
				verifySequenceNumber(inpMessage);
			}
		}
	}
	
	private void verifySequenceNumber(String inpMessage) {
		System.out.println("[i] Server: Verifying sequence number");
		
		try {
			Long tempRemoteSequenceNumber = Long.parseLong(inpMessage);
			
			System.out.println("[i] Server: Recived sequence number: " + tempRemoteSequenceNumber + ", desired sequence number: " + myHostAuthorizationPair.getSequenceNumber());
			
			if(tempRemoteSequenceNumber.equals(myHostAuthorizationPair.getSequenceNumber())) {
				HostSet.getInstatnce().bindInput(myHostAuthorizationPair.getHostId(), this);
				isHandshaked = true;
				myServerFactory.stopWaiting();
				System.out.println("[i] Server: Host " + mySocket.getInetAddress() + " modyfied in set, connection is bidirectional now");
			}
			else {
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException ex) {
			System.out.println("[!] Server: Wrong sequence number");
			closeConnection();
		}
	}
	
	private void verifyPortAndSequenceNumber(String inpMessage) {
		String[] tempMessageElementsArray = inpMessage.split(" ");
		int tempRemoteListenPort = 0;
		Long tempRemoteSequenceNumber = 0L;
		
		System.out.println("[i] Server: Verifying port and sequence number");
		
		try {
			if(tempMessageElementsArray.length == 2) {
				tempRemoteListenPort = Integer.parseInt(tempMessageElementsArray[0]);
				tempRemoteSequenceNumber = Long.parseLong(tempMessageElementsArray[1]);
				
				if(tempRemoteListenPort > 1024) {
					myRemoteHostId = new HostId(mySocket.getInetAddress(), tempRemoteListenPort);
					HostSet tempHostSet = HostSet.getInstatnce();
					tempHostSet.addHost(myRemoteHostId);
					tempHostSet.bindInput(myRemoteHostId, this);
					
					System.out.println("[i] Server: New host added to set: " + myRemoteHostId.getInetAddress() + ":" + myRemoteHostId.getPortNumber() + ", connection is not bidirectional yet");
					
					new Client(myPassword, myRemoteHostId, tempRemoteSequenceNumber);
					isHandshaked = true;
				}
				else {
					throw new NumberFormatException();
				}			
			}
			else {
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException ex) {
			System.out.println("[!] Server: Wrong initialization message");
			closeConnection();
		}
	}

	@Override
	public void run() {
		BufferedReader tempBufferedReader = new BufferedReader(new InputStreamReader(myInputStream));
		String tempReaderString;
		
		while(isRunning) {	
			try {
				tempReaderString = tempBufferedReader.readLine();
				
				if(tempReaderString != null) {
					processMessage(tempReaderString);
				}
			}
			catch(SocketTimeoutException ex) {
				continue;
			}
			catch(IOException ex) {
				
				if(myHostAuthorizationPair == null) {
					// INFO: code is for server
					System.out.println("[i] Server: Closing server connection with " + myRemoteHostId.getInetAddress() + ":" + myRemoteHostId.getPortNumber());
					HostSet.getInstatnce().delHost(myRemoteHostId);
				}
				else {
					// INFO: code is for clients
					System.out.println("[i] Server: Closing client connection with " + myHostAuthorizationPair.getHostId().getInetAddress() + ":" + myHostAuthorizationPair.getHostId().getPortNumber());
					HostSet.getInstatnce().delHost(myHostAuthorizationPair.getHostId());
				}
				
				System.out.println("[i] Server: Closed");
				closeConnection();
			}
		}
	}
	
	private void closeConnection() {
		try {
			isRunning = false;
			myInputStream.close();
			mySocket.close();
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public int getServerPort() {
		return mySocket.getLocalPort();
	}
}
