package src;


public class Host {
	private Client myClient;
	private Server myServerService;
	
	public Host( Client inpClient, Server inpServerService) {
		myClient = inpClient;
		myServerService = inpServerService;
	}
	
	public Client getClient() {
		return myClient;
	}
	
	public Server getServerService() {
		return myServerService;
	}
	
	public boolean isBidirectional() {
		if((myClient != null) && (myServerService != null)) {
			return true;
		}
		else {
			return false;
		}
	}
}
