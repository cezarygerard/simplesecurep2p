package common;

/**
 * @author czarek
 * @TODO moze warto przerobic na protokol numerkowy tak, aby uzywac switch
 * 
 */
public class P2SProtocol {
	
	//Peer
	public static final String LOGIN = "login";
	
	//Server
	public static final String LOGINACK = "loginack";

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
	public static final String GETPEERSINFO = "getpeersinfo";
		
	//Server
	public static final String PEERSINFO = "peersinfo";
	
	//Peer or Server
	public static final String FAILURE = "failure";
	
	//Peer or Server
	public static final String TIMEOUT = "timeout";
	
	//Peer or Server
	public static final String EXIT = "exit";
}
