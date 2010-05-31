package common;

/**
 * @author czarek
 * @TODO moze warto przerobic na protokol numerkowy tak, aby uzywac switch
 * @TODO wydzielic klase bazowa z protokolem P2P 
 * Protokol komunikacji peera z serwerem
 */
public class P2SProtocol extends ProtocolBase {
	
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
	
	//Peer
	public static final String CERTINFO = "certINFO";
	
	//Server
	public static final String CERT = "cert";
	
	//Peer
	public static final String GETPEERSINFO = "getpeersinfo";
		
	//Server
	public static final String PEERSINFO = "peersinfo";
	
	//klient
	public static final String PEER_DEATH_NOTIFICATION = "peerDeathNotification";

	public static final String PEER_DEATH_NOTIFICATION_ACK = "peerDeathNotificationACK";
	
}
