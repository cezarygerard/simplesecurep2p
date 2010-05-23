//package serwer.Serwer_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;


/**
 * Klasa tak samo jak w kliencie, s�u�y jedynie do komunikacji. W�tek ten jedynie nas�uchuje, je�li nadejdzie po��czenie, to tworzy si� nowy w�tek i ob�uga klienta
 * jest realizowana w oddzielnym w�tku. W�tek nas�uchu ca�y czas dzia�a jak dzia�a�a. Og�lnie w�tek jest tworzony w tych dw�ch linijkach
 * 
 * 		Klient newClient = new Klient(myServerSocket.accept());
 *		myExecutorService.execute(newClient);
 *
 * metoda accept tworzy nowe gniazdko i przekazuje je do tego ca�ego nowego w�tku. Execute wiadomo
 */
public class Net {
	private final int myServerSocketTimeout = 1000;
	private ExecutorService myExecutorService;
	private boolean isRunning;
	private ServerSocket myServerSocket;
	
	public Net(int inpPort) {
		myExecutorService = Executors.newCachedThreadPool();
		isRunning = false;
		
		try {
			myServerSocket = new ServerSocket(inpPort);
			myServerSocket.setSoTimeout(myServerSocketTimeout);
			isRunning = true;
			System.out.println("Serwer nas�uchuje na porcie " + inpPort);
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "B��d po��czenia", "Serwer - B��d po��czenia", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void startListening() {
		while (isRunning) {
			try {
				Klient newClient = new Klient(myServerSocket.accept());
				myExecutorService.execute(newClient);
			}
			catch (SocketTimeoutException e) {
				continue;
			}
			catch (IOException e) {
				continue;
			}
		}
	}
}
