package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	//protected Timer timer;
	protected TimerTask timeoutTask;
	
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
	//	timer = new Timer();
		close = false;
	}
	
	List<TerminationListener> listeners = Collections.synchronizedList(new ArrayList<TerminationListener>());

	/**
	 * metoda obslugujaca komendy protokolu
	 */
	protected abstract void HandleCommand(String command) throws ClassNotFoundException, IOException;
/*
	protected void HandleCommand(String command) throws ClassNotFoundException, IOException
	{
	//	this.timer.cancel();
	}
*/
	
/*	
	protected void setTimeout(int time)
	{
		timer = new Timer("TimeOut") ;
		timer.schedule(new TimeOutTask(), time);
	}
	
	private class TimeOutTask extends TimerTask
	{
		public void run() {
			send(P2SProtocol.TIMEOUT);
			terminateConnectionWithFailure();
		}
	}
*/
	/**
	 * wyslanie komendy
	 */
	protected void send(String command) {
	//	setTimeout(10000);
		if (output != null)
            output.println(command);
    }

	/**
	 * wyslanie objektu
	 */
	protected void send(Serializable obj) {
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
	protected void terminateConnectionWithFailure()
	{
		send(P2SProtocol.FAILURE);
		send(P2SProtocol.EXIT);
		close = true;
		closeConnection();		
	}
	protected void terminateConnectionGently()
	{
		send(P2SProtocol.OK);
		send(P2SProtocol.EXIT);
		close = true;
		closeConnection();
	}
	
	private void closeConnection()
	{
		System.out.println(this + "closeConnection");
		close = true;
		try {
			thread.join(1000);
			socket.close();
			for(int i = 0; i< listeners.size() ; i++)
			{
				listeners.get(i).connectionTerminated();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTerminationListener(TerminationListener listener)
	{
		listeners.add(listener);
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
