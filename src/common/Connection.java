package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.util.Timer;

import javax.net.ssl.SSLSocket;

/**
 * @author czarek
 *	Klasa bazowa dla P2S Connection i S2PConnection, nie jest uzywana
 *	
 */

public abstract class Connection implements Runnable {

	protected BufferedReader input;
	protected PrintWriter output;
	protected SSLSocket socket ;
	protected ObjectOutput objOutput;
	protected ObjectInput objInput;
	protected Timer timer;
	protected boolean close;
	protected Thread thread;
	
	protected Connection() {
		timer = new Timer();
		close = false;
	}

	protected void HandleCommand(String command) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
	}

	protected void send(String command) {
        if (output != null)
            output.println(command);
    }

	protected void send(Object obj) {
	    if (objOutput != null)
			try {
				objOutput.writeObject(obj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	protected void terminateConnection()
	{
		send(P2SProtocol.FAILURE);
		send(P2SProtocol.EXIT);
		close = true;
		try {
			thread.join(1000);
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
