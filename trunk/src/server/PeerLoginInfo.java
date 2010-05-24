package server;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author czarek
 *	
 */
public class PeerLoginInfo implements Serializable, Comparable<PeerLoginInfo> {

	@Override
	public String toString() {
		return "PeerLoginInfo [login=" + login + ", passwdHash="
				+ Arrays.toString(passwdHash) + "]";
	}

	private static final long serialVersionUID = 1L;
	String login;
	byte[] passwdHash;
	
	public PeerLoginInfo(String login, byte[] passwdHash) {
		super();
		this.login = login;
		this.passwdHash = passwdHash;
	}
	
	public int compareTo(PeerLoginInfo o) {
		return this.login.compareTo(o.login);
	}


}
