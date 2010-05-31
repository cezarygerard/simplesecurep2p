package common;

public interface PeerActionObserver {
	
	public void peerActionPerformed(String action);
	public void fileActionPerformed(FileInfo file, String actionType);
	public static final String FILE_FOUND = "fileFound";
	public static final String FILE_DOWNLOADED = "fileDownloaded";
	public static final String CONNECTION_ESTABLISHED = "connected";
}
