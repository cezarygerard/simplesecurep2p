//package Klient_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;


/**
 * Klasa w�tku klienta w po��czeniu TCP/IP
 *
 */
public class Net implements Runnable {
	/**
	 * Klasa jest jedynakiem, oznacza to, �e istnieje tylko jedna instancja tego obiektu, przechowywana jest ona w myInstance
	 * myHost przechoduje nazw� hosta lub string reprezentuj�cy numer IP
	 * myPort przechoduje numer portu, do kt�rego ��czy si� aplikacja z serwerem
	 * mySocket przechowuje gniazdo, kt�re s�u�y do komunikacji z serwerem
	 * myOutputStream jest strumieniem danych wychodz�cych do serwera
	 * myInputStream jest strumieniem danych przychodz�cych od serwera
	 * isRunning flaga, kt�ra je�li jest ustawiona na true, powoduje, �e w�tek nie zamyka si�
	 */
	private static Net myInstance;
	private String myHost;
	private int myPort;
	
	private Socket mySocket;
	private OutputStream myOutputStream;
	private InputStream myInputStream;
	
	private boolean isRunning;
	
	/**
	 * Poniewa� jest to jedynak, nale�y stworzy� instancj� tego obiektu
	 */
	public static Net createNetProtocol(String inpHost, int inpPort) {
		myInstance = new Net(inpHost, inpPort);
		return myInstance;
	}
	
	/**
	 * Poniewa� klasa jest jedynakiem, t� metod� mo�na pobra� jedyn� instancj� tego obiektu
	 */
	public static Net getInstance() {
		return myInstance;
	}
	
	/**
	 * Konstruktor bezargumentowy jedynie inicjalizuje zmienne
	 */
	private Net(String inpHost, int inpPort) {
		myHost = inpHost;
		myPort = inpPort;
		mySocket = null;
		myOutputStream = null;
		myInputStream = null;
		isRunning = false;
	}
	
	/**
	 * Metoda nawi�zuje po��czenie, je�li cokolwiek p�jdzie nie tak, wyrzuca wyj�tek. Dodatkowo w tej metodzie tworzony jest nowy w�tek, podpinane s� strumienie danych,
	 * ustawiany jest tak�e TimeOut, je�li op�nienia na linii przekrocz� jedn� sekund� i  serwer w tym czasnie nie odpowie, po��czenie zostaje przewane
	 * Po nawi�zaniu po��czenia, Klient wysy�a komunikat powitalny
	 */
	public void connectToServer() {
		try {
			mySocket = new Socket(myHost, myPort);			
			mySocket.setSoTimeout(1000);
			
			myOutputStream = mySocket.getOutputStream();
			myInputStream = mySocket.getInputStream();
			
			isRunning = true;
			createNetProtocolThread();
			
			sendMessage("Po��czono");
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Nie mo�na po��czy� z serwerem", "Klient - B��d po��czenia z serwerem", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	/**
	 * Metoda tworz�ca nowy w�tek, eqzekutory zarz�dzaj� pulami w�tk�w, nikt tak naprawd� nie wie, dla czego u�ywa si� tu egzekutor�w, ale tak ma by�
	 */
	private void createNetProtocolThread() {
		ExecutorService tempExecutorService = Executors.newCachedThreadPool();
	    tempExecutorService.execute(this);
	}
	
	/**
	 * Metoda wysy�aj�ca dane do serwera. Wpuchamy w strumie� danych wyj�ciowych string, wa�ne jest, �� string zaka�czany est automatycznie ko�cem linii, i serwer b�dzie
	 * reagowa� na ten w�a�nie koniec linii. Innymi s�owy, kwantem informacji dla serwera i dla klienta jest jedna linijka, przechowywana w stringu.
	 * o co chodzi z tym BufferedWriter nikt nie wie, ale tak ma by� w�a�nie, bo tak dzia�a
	 */
	public void sendMessage(String inpString) {
		BufferedWriter tempBufferedWriter = new BufferedWriter(new OutputStreamWriter(myOutputStream));
		
		try {
			tempBufferedWriter.write(inpString);
			tempBufferedWriter.write('\n');
			tempBufferedWriter.flush();
		}
		catch(IOException inpIOException) {
			JOptionPane.showMessageDialog(null, "B��d wysy�ania", "Klient - B��d wysy�ania", JOptionPane.ERROR_MESSAGE);
			closeConnection();
		}
	}
	
	/**
	 * Metoda wywo�ywana, gdy co� zostanie odebrane z serwera. Metoda jedynie wy�wietla co zosta�o odebrane
	 */
	private void processMessage(String inpMessage) {
		System.out.println("Odebrano z serwera: " + inpMessage);
		closeConnection();
	}

	/**
	 * Metoda run jest mega wa�na bo jak klasa jest runnable, czyli da si� j� odpali� w w�tku, to musi mie� tak� w�a�nie metod�. Og�lnie odbiera ona jedynie wszystko
	 * co wpadnie z strumienia danych wej�ciowego, leci w p�tli jak wida� i do p�ki flaga isRunnig jest prawdziwa to ta p�telka si� wykonuje, jak si� j� zmieni na
	 * false, to petla si� ko�czy, czyli automatycznie zakonczy si� metoda Run i zako�czy si� ten w�tek. Jak po��czenie np si� zako�czy to wywalany jest b��d, co jest nie tak
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
			catch(IOException inpIOException) {
				if(isRunning) {
					JOptionPane.showMessageDialog(null, "B��d odbierania", "Klient - B��d odbierania", JOptionPane.ERROR_MESSAGE);
					closeConnection();
				}
			}
		}
	}
	
	/**
	 * Bez komentarza
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
