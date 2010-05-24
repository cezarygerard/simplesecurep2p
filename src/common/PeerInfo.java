package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.TreeMap;

public class PeerInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean  isActive;
	InetAddress addr;
	int listeningPort;
	TreeMap<String, FileInfo> files;
	
	public PeerInfo(boolean isActive, InetAddress addr, int listeningPort,
			TreeMap<String, FileInfo> files_ ) {
		super();
		this.isActive = isActive;
		this.addr = addr;
		this.listeningPort = listeningPort;
		this.files = files_;
	}
	
}
