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
				+ passwdHash + "]";
	}

	private static final long serialVersionUID = 1L;
	String login;
	String passwdHash;
	
	
	public PeerLoginInfo(String login, String passwd, boolean hashed) {
		super();
		this.login = login;
		if (hashed)
			this.passwdHash = passwd;
		else
		{
			try {
				this.passwdHash = utils.toHexString(utils.MDigest("SHA-512", passwd.getBytes()));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public String getPasswdHash() {
		return passwdHash;
	}

}
