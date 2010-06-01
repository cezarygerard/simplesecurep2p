package common;

public interface PeerActionObserver {
	
	void peerActionPerformed(String action);
	void fileActionPerformed(FileInfo file, String actionType);
	enum ACTION_TYPE
	{
		FILE_FOUND,
		FILE_DOWNLOADED,
		CONNECTION_ESTABLISHED,
		LOGIN_FAILED		
	}
	static final String FILE_FOUND = "fileFound";
	static final String FILE_DOWNLOADED = "fileDownloaded";
	static final String CONNECTION_ESTABLISHED = "connected";
	static final String LOGIN_FAILED = "LOGIN_FAILED";
}
