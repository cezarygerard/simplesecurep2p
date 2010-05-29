package src;

import java.net.InetAddress;

public class HostId {
	private InetAddress myInetAddress;
	private int myPortNumber;
	
	HostId(InetAddress inpInetAddress, int inpPortNumber) {
		myInetAddress = inpInetAddress;
		myPortNumber = inpPortNumber;
	}
	
	public InetAddress getInetAddress() {
		return myInetAddress;
	}
	
	public int getPortNumber() {
		return myPortNumber;
	}
}
