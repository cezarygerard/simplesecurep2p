package common;

public class PeerServerProtocol {
	
	//Peer
	public static final String LOGIN = "login";

	//Server
	public static final String LOGGEDIN = "loggedin";

	//Server
	public static final String LOGINFAILED = "loginfailed";
	
	//Server
	public static final String GETYOURINFO = "getyourinfo";
	
	//Peer
	public static final String MYINFO = "myinfo";
	
	//Peer
	public static final String GETCERT = "getcert";
	
	//Server
	public static final String CERT = "cert";
		
	//Peer
	public static final String GETPEERSINFO = "getpeers";
		
	//Server
	public static final String PEERSINFO = "peersinfo";
	
	//Peer or Server
	public static final String FAILURE = "failure";
	
	//Peer or Server
	public static final String TIMEOUT = "timeout";
	
	//Peer or Server
	public static final String EXIT = "exit";
}
