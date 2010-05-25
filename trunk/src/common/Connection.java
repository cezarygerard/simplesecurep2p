package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;

/**
 * @author czarek
 *	Klasa bazowa dla P2S Connection i S2PConnection, nie jest uzywana
 *	@deprecated nie jest uzywana
 */

public abstract class Connection implements Runnable {

	protected BufferedReader input;
	protected PrintWriter output;
	protected SSLSocket s ;
	protected ObjectOutput objOutput;
	protected ObjectInput objInput;
	
/*	Connection(Socket accept) {
		//this.s = (SSLSocket) accept;

	}
*/
	protected void HandleCommand(String command) {
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
	
}
