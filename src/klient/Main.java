//package Klient_1;

import javax.swing.JOptionPane;



public class Main {	
	public static void main(String[] args) {
		/**
		 * Aplikacja klienta pobiera dwa parametry wej�ciowe: nawa hosta lub numer IP(zapisany w stringu) oraza numer portu(tak�e zapisany w stringu)
		 * Jesli do aplikacji nie poda si� dw�ch paramtr�w, aplikacja wy�uca okienko informuj�ce o b��dzie. Je�li u�ytkownik poda jako numer portu
		 * liczb� ujemn� lub b��dny string sk�adaj�cy si� z liter a nie cyfr, aplikacja wyrzuca b��d
		 */
		if(args.length == 2) {
			int tempPort = Integer.parseInt(args[1]);
			
			if(tempPort < 0) {
				JOptionPane.showMessageDialog(null, "Nie prawid�owy numer portu", "Klient - B��dne paramtety wej�ciowe", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			Net.createNetProtocol(args[0], tempPort);
			Net.getInstance().connectToServer();
		}
		else {
			JOptionPane.showMessageDialog(null, "B��dna ilo�� parametr�w", "Klient - B��d rozruchu aplikacji", JOptionPane.ERROR_MESSAGE);
		}
	}
}
