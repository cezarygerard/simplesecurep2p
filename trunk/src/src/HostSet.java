package src;

import java.util.HashMap;
import java.util.Random;

public class HostSet {
	private static HostSet myInstance;
	private Random myRandom;
	private HashMap<HostId, Host> myHostHashMap;
	
	public static HostSet getInstatnce() {
		if(myInstance == null) {
			myInstance = new HostSet();
		}
		
		return myInstance;
	}
	
	private HostSet() {
		myRandom = new Random();
		myHostHashMap = new HashMap<HostId, Host>();
	}
	
	public synchronized Long generateNewSequenceNumber() {
		return myRandom.nextLong();
	}

	public synchronized void addHost(HostId inpHostId) {
		myHostHashMap.put(inpHostId, new Host(null, null));
	}
	
	public synchronized void delHost(HostId inpHostId) {
		myHostHashMap.remove(inpHostId);
	}
	
	public synchronized void bindOutput(HostId inpHostId, Client inpClient) {
		myHostHashMap.put(inpHostId, new Host(inpClient, myHostHashMap.get(inpHostId).getServerService()));
	}
	
	public synchronized void bindInput(HostId inpHostId, Server inpServerService) {
		myHostHashMap.put(inpHostId, new Host(myHostHashMap.get(inpHostId).getClient(), inpServerService));
	}
	/*
	public synchronized boolean contains(HostId inpHostId) {
		return myHostHashMap.containsKey(inpHostId);
	}
	*/
}
