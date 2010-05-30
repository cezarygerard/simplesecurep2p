package peer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;

import common.Connection;
import common.P2SProtocol;
import common.PeerInfo;

public class P2SConnection extends Connection implements Runnable {

	private Peer peer;
	private InetAddress addr;
	private int port;
	private STATE state;

	private enum STATE {
		CONNECTING, IDLE, CONNECTED, LOGGEDIN, DONE, LOGGING
	}

	P2SConnection(Peer p, InetAddress addr, int port) {
		super();
		this.addr = addr;
		this.port = port;
		this.peer = p;
	}

	void Connect() throws Exception {

		socket = (SSLSocket) peer.sf.createSocket(addr, port);
		socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
			public void handshakeCompleted(HandshakeCompletedEvent arg0) {
				// System.out.println("handshakeCompleted " + arg0.toString() +
				// arg0);
			}
		});
		// s.startHandshake();

		// to moznaby inicjalizowac po wstepnym postawieniu nasluchiwania -
		// wiemy jakie gniazdko dostaniemy od systemu
		peer.myInfo = new PeerInfo(socket.getLocalAddress(), peer.listeningPort);

		input = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);

		objOutput = new ObjectOutputStream(socket.getOutputStream());
		objInput = new ObjectInputStream(socket.getInputStream());

		state = STATE.LOGGING;
		System.out.println("[P2SConnection.Connect()] ");
		send(P2SProtocol.LOGIN);
		send(peer.peerLogin);
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (!close) {
			try {
				System.out.println("P2SConnection");
				String command = input.readLine();
				HandleCommand(command);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close = true;
			}
		}
	}

	protected void HandleCommand(String command) {
		try {
			System.out.println("[P2SConnection.HandleCommand] command: "
					+ command);
			// timeoutTask.cancel();
			if ((state == STATE.LOGGING && command.equals(P2SProtocol.LOGINACK))
					|| command.equals(P2SProtocol.GETYOURINFO)) {
				System.out
						.println("[P2SConnection.HandleCommand.HandleCommand] MYINFO sending");
				state = STATE.LOGGEDIN;
				send(P2SProtocol.MYINFO);
				send(peer.myInfo);
				send(P2SProtocol.GETPEERSINFO);
			} else if (command.equals(P2SProtocol.PEERSINFO)) {
				System.out.println("[P2SConnection.HandleCommand] PEERSINFO");
				TreeMap<String, PeerInfo> pi = (TreeMap<String, PeerInfo>) objInput.readObject();
				this.peer.peersInfo = Collections.synchronizedSortedMap(pi);
				System.out
						.println("[P2SConnection.HandleCommand.HandleCommand] PeersInfo: "
								+ pi + "  " + pi.size());

				if (!peer.hasValidCert) {
					send(P2SProtocol.GETCERT);
					send(peer.certInfo);
				} else {
					terminateConnectionGently();
					peer.StatrListening();
				}
			} else if (command.equals(P2SProtocol.CERT)) {

				peer.storeX509cert((X509Certificate) objInput.readObject(),
						(KeyPair) objInput.readObject());
				terminateConnectionGently();
				peer.StatrListening();
			} else if (command.equals(P2SProtocol.EXIT)) {
				terminateConnectionGently();
				peer.StatrListening();
			}
		} catch (Exception e) {
			e.printStackTrace();
			terminateConnectionWithFailure();
		}
	}
}
