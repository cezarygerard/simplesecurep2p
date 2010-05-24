package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.TreeMap;
import java.util.TreeSet;

public class PeerInfo implements Serializable {

	public PeerInfo(boolean isActive, InetAddress addr, int listeningPort,
			TreeMap<String, TreeSet<FileInfo>> files) {
		super();
		this.isActive = isActive;
		this.addr = addr;
		this.listeningPort = listeningPort;
		this.files = files;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean  isActive;
	InetAddress addr;
	int listeningPort;
	TreeMap<String, TreeSet<FileInfo> > files;
	
}
