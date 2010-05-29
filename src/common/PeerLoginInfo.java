package common;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
g * @author czarek
 *	Klasa reprezentuje Informacje potrzebne do logowania.
 *	Wykorzystywana zarowno w klasie Server jak i Peer
 */
public class PeerLoginInfo implements Serializable, Comparable<PeerLoginInfo> {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  Login uzytkownika
	 */
	String login;
	
	/**
	 * Skrot z hasla, uzywamy tu sha-512
	 */
	String passwdHash;

	/**
	 * Konstruktor
	 * @param login
	 * @param passwd
	 * @param hashed parametr pokazuje czy haslo jest juz zahaszowane czy trzeba jeszcze obliczyc skrot
	 * 		Uzytkownik podaje haslo plain textem, liczmy skrot, serwer przechowuje tylko skroty
	 */
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
	
	/**
	 * Medota sluzy do porywnywania roznych obiektow klasy PeerInfo
	 * Jest wykorzystywana do ukladania ich w kontenerach
	 */
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
	
	@Override
	public String toString() {
		return "PeerLoginInfo [login=" + login + ", passwdHash="
				+ passwdHash + "]";
	}


}
