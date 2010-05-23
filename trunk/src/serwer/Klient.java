//package serwer.Serwer_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import javax.swing.JOptionPane;


/**
 * Klasa ta s�u�y do ob�ugi konkretnego klienta. Jak wida� jest runnable czyli mo�na j� odpali� w nowym w�tku.
 * Flaga isRunning robi to co zawsze,
 * mySocket gniazdko
 * streamy to strumienie danych robi� to co zawsze
 */
public class Klient implements Runnable {
	private boolean isRunning;
	private Socket mySocket;
	private InputStream myInputStream;
	private OutputStream myOutputStream;
	
	/**
	 * Kontruktor inicjalizuje wszystko
	 */
	Klient(Socket inpSocket) {
		isRunning = true;
		mySocket = inpSocket;
		
		try {
			myInputStream = inpSocket.getInputStream();
			myOutputStream = inpSocket.getOutputStream();
			mySocket.setSoTimeout(1000);
		}
		catch(IOException e) {
			System.out.println("[i] " + e.getMessage());
			closeConnection();
		}
	}
	
	/**
	 * metoda robi to co w przypadku klienta, wysy�a jedynie dane, zasada jest taka sama, �e koniec linijki jest informacj�, �e trzeba przetworzy� dan� i dlatego ka�dy
	 * string jest nadawany z ko�cem linii 
	 */
	public void sendMessage(String inpString) {
		BufferedWriter tempBufferedWriter = new BufferedWriter(new OutputStreamWriter(myOutputStream));
		
		try {
			tempBufferedWriter.write(inpString);
			tempBufferedWriter.write('\n');
			tempBufferedWriter.flush();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "B��d wysy�ania", "Serwer - B��d wysy�ania", JOptionPane.ERROR_MESSAGE);
			closeConnection();
		}
	}
	
	/**
	 * wysy�anie, nie ma co opisywa�
	 */
	private void processMessage(String inpMessage) {
		System.out.println("Odebrano od klienta: " + inpMessage);
		sendMessage(inpMessage);
	}
	
	/**
	 * to co zawsze, klasa jest runnable to znaczy, ze musi by� run, ona wygl�da tak jak w kliencie
	 */
	@Override
	public void run() {
		BufferedReader tempBufferedReader = new BufferedReader(new InputStreamReader(myInputStream));
		String tempReaderString;
		
		while(isRunning) {	
			try {
				tempReaderString = tempBufferedReader.readLine();
				
				if(tempReaderString != null) {
					processMessage(tempReaderString);
				}
			}
			catch(SocketTimeoutException te) {
				continue;
			}
			catch(IOException e) {
				JOptionPane.showMessageDialog(null, "B��d odczytu", "Serwer - B��d odczytu", JOptionPane.ERROR_MESSAGE);
				closeConnection();
			}
		}
	}
	
	
	/**
	 * bez komentarza
	 */
	private void closeConnection() {
		try {
			isRunning = false;
			myInputStream.close();
			myOutputStream.close();
			mySocket.close();
		}
		catch(Exception ex) { }
	}
}
