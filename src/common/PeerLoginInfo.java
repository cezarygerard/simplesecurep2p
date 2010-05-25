package common;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
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
	
	public PeerLoginInfo(String login, String passwd) {
		super();
		this.login = login;
		try {
			this.passwdHash = utils.MDigest("SHA-512", passwd.getBytes());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int compareTo(PeerLoginInfo o) {
		return this.login.compareTo(o.login);
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the passwdHash
	 */
	public byte[] getPasswdHash() {
		return passwdHash;
	}

}
