package server;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.TreeMap;
import java.util.TreeSet;

public class PeerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7694413519070046128L;

	String login;
	byte[] passwdHash;
	boolean  isActive;
	InetAddress addr;
	int listeningPort;
	TreeMap<String, String> files;
}
