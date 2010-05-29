package src;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

public class MainClass {
	private static final int mySocketTimeout = 1000;
	private static ExecutorService myExecutorService;
	
	public static void launchServerFactory(ServerFactory inpServer) {
		myExecutorService.execute(inpServer);
	}
	
	public static void main(String[] inpArgumentArray) {
		myExecutorService = Executors.newCachedThreadPool();
		
		try {
			if(inpArgumentArray.length == 2) {

				String tempPassword = inpArgumentArray[0];
				int tempLocalListenPort = Integer.parseInt(inpArgumentArray[1]);
				
				if(tempLocalListenPort > 1024) {
					System.out.println("[i] MainClass: Server mode. Pass: " + tempPassword + ", listen: " + tempLocalListenPort);
					launchServerFactory(new ServerFactory(tempPassword, tempLocalListenPort, mySocketTimeout));
				}
				else {
					throw new NumberFormatException();
				}
			}
			else if(inpArgumentArray.length == 4) {
					String tempPassword = inpArgumentArray[0];
					int tempLocalListenPort = Integer.parseInt(inpArgumentArray[1]);
					InetAddress tempRemoteInetAddress = InetAddress.getByName(inpArgumentArray[2]);
					int tempRemoteConnectPort = Integer.parseInt(inpArgumentArray[3]);
				
				if((tempLocalListenPort > 1024) && (tempRemoteConnectPort > 1024)) {
					System.out.println("[i] MainClass: Client mode. Pass: " + tempPassword + ", listen: " + tempLocalListenPort + ", host: " + tempRemoteInetAddress + ":" + tempRemoteConnectPort);
					new Client(tempPassword, tempLocalListenPort, new HostId(tempRemoteInetAddress, tempRemoteConnectPort), mySocketTimeout);
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
			JOptionPane.showMessageDialog(null, "Wrong  input arguments", "error", JOptionPane.ERROR_MESSAGE);
		}
		catch(UnknownHostException ex) {
			JOptionPane.showMessageDialog(null, "Wrong input arguments", "error", JOptionPane.ERROR_MESSAGE);
		}
	}}
	
	