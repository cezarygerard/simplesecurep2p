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

	/**
	 * przesylanie wiadomosci protokolu
	 */
	protected BufferedReader input;
	/**
	 * odczyt wiadomosci protokolu
	 */
	protected PrintWriter output;
	/**
	 * gniazdo sieciowe na ktorym odbywa sie komunikacja
	 */
	protected SSLSocket socket ;
	/**
	 * przesylanie obiektow przez siec
	 */
	protected ObjectOutput objOutput;
	/**
	 *  odczyt obiektow z siec
	 */
	protected ObjectInput objInput;
	/**
	 * timer do mierzenia timeoutow
	 */
	protected Timer timer;
	/**
	 * flaga zamykajaca polaczenie (konczy petle w metodzie run)
	 * nie powinna byc zmieniana bezposrednio
	 * w celu zakonczenia polaczeni nalezy wykonac terminateConnection()
	 */
	protected boolean close;
	
	/**
	 * obiekt watku glownego
	 */
	protected Thread thread;
	
	/**
	 * konstryktoe
	 */
	protected Connection() {
		timer = new Timer();
		close = false;
	}

	/**
	 * metoda obslugujaca komendy protokolu
	 */
	protected void HandleCommand(String command) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * wyslanie komendy
	 */
	protected void send(String command) {
        if (output != null)
            output.println(command);
    }

	/**
	 * wyslanie objektu
	 */
	protected void send(Object obj) {
	    if (objOutput != null)
			try {
				objOutput.writeObject(obj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * zakonczenie polaczenia
	 */
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