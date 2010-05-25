package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author czarek
 *	
 */
public class PeerInfo implements Serializable, Comparable {

	private static final long serialVersionUID = 1L;
//	boolean  isActive;
	InetAddress addr;
	int listeningPort;
	String addrMd;
//	TreeMap<String, FileInfo> files;
	
	public PeerInfo(InetAddress addr, int listeningPort) {
		super();
//		this.isActive = isActive;
		this.addr = addr;
		this.listeningPort = listeningPort;
		try {
			addrMd = utils.toHexString(utils.MDigest("SHA1", addr.getAddress()));	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
//		this.files = files_;
	}

	public String toString() {
		return "PeerInfo [addr=" + addr + ", addrMd=" + addrMd
				+ ", listeningPort=" + listeningPort + "]";
	}

	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof PeerInfo)
		{
			if (((PeerInfo)arg0).addr == this.addr && ((PeerInfo)arg0).listeningPort == this.listeningPort)
				return 0;
			else
				return addrMd.compareToIgnoreCase(((PeerInfo)arg0).addrMd);
		}
		else
			return -1;
	}	
}
