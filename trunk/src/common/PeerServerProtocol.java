package common;

public class PeerServerProtocol {
	
	//Peer
	static final String LOGIN = "login";

	//Server
	static final String LOGGEDIN = "loggedin";

	//Server
	static final String LOGINFAILED = "loginfailed";
	
	//Server
	static final String GETYOURINFO = "getyourinfo";
	
	//Peer
	static final String MYINFO = "myinfo";
	
	//Peer
	static final String GETCERT = "getcert";
	
	//Server
	static final String CERT = "cert";
		
	//Peer
	static final String GETPEERSINFO = "getpeers";
		
	//Server
	static final String PEERSINFO = "peersinfo";
	
	//Peer or Server
	static final String FAILURE = "failure";
	
	//Peer or Server
	static final String TIMEOUT = "timeout";
	
	//Peer or Server
	static final String EXIT = "exit";
}
