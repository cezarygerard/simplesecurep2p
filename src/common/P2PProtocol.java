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
}
