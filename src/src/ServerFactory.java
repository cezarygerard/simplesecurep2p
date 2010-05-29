package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
/**
 * to jest gnizdo które nas³uchuje jak ktoœ siê pod³¹cza to tworzy nowy w¹tek i puszcza po³¹czenie które przysz³o w nowym watku
 * 
 * 

 * */
public class ServerFactory implements Runnable {
	private HostAuthorizationPair myHostAuthorizationPair; // TODO, change to HostPair in it??
	private String myPassword;
	private int myLocalListenPort;
	private int mySocketTimeout;
	private ExecutorService myExecutorService;
	private boolean isWaiting;
	private boolean isRunning;
	private ServerSocket myServerSocket;
	
	public ServerFactory(String inpPassword, int inpLocalListenPort, HostAuthorizationPair inpHostPair, int inpSocketTimeout) {
		specify(inpPassword, inpLocalListenPort, inpHostPair, inpSocketTimeout);
	}
	
	public ServerFactory(String inpPassword, int inpLocalListenPort, int inpSocketTimeout) {
		specify(inpPassword, inpLocalListenPort, inpSocketTimeout);
	}
	
	private void specify(String inpPassword, int inpLocalListenPort, HostAuthorizationPair inpHostPair, int inpSocketTimeout) {
		myHostAuthorizationPair = inpHostPair;
		initialize(inpPassword, inpLocalListenPort, inpSocketTimeout);
	}
	
	private void specify(String inpPassword, int inpLocalListenPort, int inpSocketTimeout) {
		myHostAuthorizationPair = null;
		initialize(inpPassword, inpLocalListenPort, inpSocketTimeout);
	}
	
	private void initialize(String inpPassword, int inpLocalListenPort, int inpSocketTimeout) {
		myPassword = inpPassword;
		myLocalListenPort = inpLocalListenPort;
		mySocketTimeout = inpSocketTimeout;
		myExecutorService = Executors.newCachedThreadPool();
		isRunning = true;
		
		tryToInitializeSocket();
	}
	
	private void tryToInitializeSocket() {
		try {
			myServerSocket = new ServerSocket(myLocalListenPort);
			myServerSocket.setSoTimeout(mySocketTimeout);
		}
		catch(SocketException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Socket Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch(SecurityException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Security Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		System.out.println("[i] ServerFactory: Server factory is listening on port " + myLocalListenPort + "...");
		
		while (isRunning) {
			try {
				Socket tempAcceptedSocket = myServerSocket.accept();
				System.out.println("[i] ServerFactory: Connection request on port " + myLocalListenPort);
				Server tempServer;
				
				if((myHostAuthorizationPair != null) && (tempAcceptedSocket.getInetAddress().equals(myHostAuthorizationPair.getHostId().getInetAddress()))) {
					isWaiting = true;
					tempServer = new Server(myPassword, tempAcceptedSocket, myHostAuthorizationPair, this, mySocketTimeout);
				}
				else {
					isWaiting = false;
					tempServer = new Server(myPassword, tempAcceptedSocket, mySocketTimeout);
				}
				
				myExecutorService.execute(tempServer);
			}
			catch(SocketTimeoutException ex) {
				if(isWaiting) {
					System.out.println("[i] ServerFabric: Time passed, unfortunatelly");
					//JOptionPane.showMessageDialog(null, "Host does not respond is " + mySocketTimeout + "msec' time", "Socket Timeout Exception", JOptionPane.ERROR_MESSAGE);
					//System.exit(0);
				}
				else {
					continue;
				}
			}
			catch(IOException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			catch(SecurityException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Security Exception", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			catch(IllegalBlockingModeException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Illegal Blocking Mode Exception", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
	
	public synchronized void stopWaiting() {
		myHostAuthorizationPair = null;
		isWaiting = false;
	}
}
