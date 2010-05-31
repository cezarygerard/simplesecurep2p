package common;

/**
 * @author czarek
 *
 * Protokol komunikacji pomiedzy peerami
 */
public class P2PProtocol extends ProtocolBase {

	//Peer-client side
	public static final String MY_FILE_INFO = "myFileInfo";
	
	//Peer-client side
	public static final String MY_FILE_INFO_BUCKUP = "myFileInfoBuckup";
	
	//Peer-client side
	public static final String NEIGHBOUR_RECOGNITION_INIT = "neighbourRecognitionInit";
	
	//Peer-server side
	public static final String NEIGHBOUR_RECOGNITION_ACK = "neighbourRecognitionACK";

	public static final String PEER_DEATH_NOTIFICATION = "peerDeathNotification";

	public static final String GET_NEW_BACK_UP_FROM_NEXT = "getBackUp";
	
	public static final String SEND_BACK_UP_TO_PREV = "setNewBackUp";

	public static final String GET_NEW_BACK_UP_FROM_NEXT_ACK = "getBackUpACK";

	public static final String GET_FILES_INFO = "getFilesInfo";

	public static final String GET_FILES_INFO_ACK = "getFilesInfoACK";

	public static final String GET_INITIAL_BACK_UP_FROM_NEXT = "getInitialBackUpFromNext";

	public static final String GET_FILE_OWNER = "getFileOwner";
	
	public static final String GET_FILE_OWNER_ACK = "getFileOwnerACK";
}
