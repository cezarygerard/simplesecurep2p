/**
 * 
 */
package common;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author czarek
 *
 */
public class ServerInfo extends PeerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param addr
	 * @param listeningPort
	 */
	public ServerInfo(InetAddress addr, int listeningPort) {
		super(addr, listeningPort);
		// TODO Auto-generated constructor stub
	}

}
