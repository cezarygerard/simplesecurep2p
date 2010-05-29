package src;


public class HostAuthorizationPair {
	private HostId myHostId;
	private Long mySequenceNumber;
	
	HostAuthorizationPair(HostId inpHostId, Long inpSequenceNumber) {
		myHostId = inpHostId;
		mySequenceNumber = inpSequenceNumber;
	}
	
	public HostId getHostId() {
		return myHostId;
	}
	
	public Long getSequenceNumber() {
		return mySequenceNumber;
	}
}
