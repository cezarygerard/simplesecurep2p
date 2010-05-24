package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * @author czarek
 *	
 */
public class PeerInfo implements Serializable {

	private static final long serialVersionUID = 1L;
//	boolean  isActive;
	InetAddress addr;
	int listeningPort;
	byte[] addrMd;
//	TreeMap<String, FileInfo> files;
	
	public PeerInfo(InetAddress addr, int listeningPort) {
		super();
//		this.isActive = isActive;
		this.addr = addr;
		this.listeningPort = listeningPort;
		try {
			addrMd = utils.MDigest("SHA1", addr.getAddress());	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
//		this.files = files_;
	}
	
}
