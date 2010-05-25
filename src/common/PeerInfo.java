package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

/**
 * @author czarek
 *	Klasa reprezentujaca informacje o peerze
 */
public class PeerInfo implements Serializable, Comparable<PeerInfo> {


	/**
	 * Pole wymagane prze interface {@link Serializable}
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Adress peera
	 */
	InetAddress addr;

	/**
	 * port na ktorym peera prowadzi nasluchowanie polaczen przychodzacych
	 */
	int listeningPort;

	/**
	 * Skrot z addresu MD5(addr.getAddress())
	 */
	String addrMd;


	/**
	 * 
	 */
	public PeerInfo(InetAddress addr, int listeningPort) {
		super();
		this.addr = addr;
		this.listeningPort = listeningPort;
		try {
			addrMd = utils.toHexString(utils.MDigest("SHA1", addr.getAddress()));	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 

	}

	public String toString() {
		return "PeerInfo [addr=" + addr + ", addrMd=" + addrMd
		+ ", listeningPort=" + listeningPort + "]";
	}


	/**
	 * Medota sluzy do porywnywania roznych obiektow klasy PeerInfo
	 * Jest wykorzystywana do ukladania ich w kontenerach
	 */
	public int compareTo(PeerInfo arg0) {
		if (arg0 ==null)
			throw new NullPointerException() ;

		if (((PeerInfo)arg0).addr == this.addr && ((PeerInfo)arg0).listeningPort == this.listeningPort)
			return 0;
		else
			return addrMd.compareToIgnoreCase(((PeerInfo)arg0).addrMd);

	}	
}
