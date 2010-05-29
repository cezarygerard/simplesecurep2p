package src;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	private String myPassword;
	private HostId myRemoteHostId;
	
	private Socket mySocket;
	private OutputStream myOutputStream;
	
	public Client(String inpPassword, int inpLocalListenPort, HostId inpRemoteHostId, int inpSocketTimeout) {
		initialize(inpPassword, inpRemoteHostId);
		tryToConnect();
		
		HostSet tempHostSet = HostSet.getInstatnce();
		tempHostSet.addHost(myRemoteHostId);
		tempHostSet.bindOutput(myRemoteHostId, this);
		System.out.println("[i] Client: New host added to set: " + myRemoteHostId.getInetAddress() + ":" + myRemoteHostId.getPortNumber() + ", connection is not bidirectional yet");
		Long tempSequenceNumber = HostSet.getInstatnce().generateNewSequenceNumber();
		
		MainClass.launchServerFactory(new ServerFactory(myPassword, inpLocalListenPort, new HostAuthorizationPair(myRemoteHostId, tempSequenceNumber), inpSocketTimeout));
		
		String tempMessage = inpLocalListenPort + " " + tempSequenceNumber;
		System.out.println("[i] Client: Sending message: " + tempMessage);
		sendMessage(tempMessage);
	}

	public Client(String inpPassword, HostId inpHostRemoteId, Long inpRemoteDesiredSequenceNumber) {
		initialize(inpPassword, inpHostRemoteId);
		tryToConnect();
		
		HostSet tempHostSet = HostSet.getInstatnce();
		tempHostSet.bindOutput(myRemoteHostId, this);
		System.out.println("[i] Client: Host " + myRemoteHostId.getInetAddress() + ":" + myRemoteHostId.getPortNumber() + " modyfied in set, connection is bidirectional now");
		
		String tempMessage = "" + inpRemoteDesiredSequenceNumber;
		System.out.println("[i] Client: Sending message: " + tempMessage);
		sendMessage(tempMessage);
	}
	
	private void initialize(String inpPassword, HostId inpHostRemoteId) {
		myPassword = inpPassword;
		myRemoteHostId = inpHostRemoteId;
	}
	
	private void tryToConnect() {
		try {
			System.out.println("[i] Client: Connecting to " + myRemoteHostId.getInetAddress() + ":" + myRemoteHostId.getPortNumber());
			mySocket = new Socket(myRemoteHostId.getInetAddress(), myRemoteHostId.getPortNumber());			
			myOutputStream = mySocket.getOutputStream();
			System.out.println("[i] Client: Connected");
		}
		catch(UnknownHostException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Unknown Host Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0); // TODO: has to exit??
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0); // TODO: has to exit??
		}
		catch(SecurityException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Security Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0); // TODO: has to exit??
		}
	}
	
	public void sendMessage(String inpString) {
		BufferedWriter tempBufferedWriter = new BufferedWriter(new OutputStreamWriter(myOutputStream));
		
		try {
			tempBufferedWriter.write(inpString);
			tempBufferedWriter.write('\n');
			tempBufferedWriter.flush();
		}
		catch(IOException inpIOException) {
			JOptionPane.showMessageDialog(null, inpIOException.getMessage(), "Sending error", JOptionPane.ERROR_MESSAGE);
			closeConnection();
		}
	}

	private void closeConnection() {
		try {
			myOutputStream.close();
			mySocket.close();
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
}
