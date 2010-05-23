//package serwer.Serwer_1;

import javax.swing.JOptionPane;


/**
 * Wszystko tak samo jak w kliencie, najpierw s� sprawdzane parametry wej�ciowe
 */
public class Main {
	private static Net myNet;
	
	public static void main(String[] args) {		
		if(args.length == 1) {
			int tempListeningPort = Integer.parseInt(args[0]);
			
			if(tempListeningPort > 0) {
				myNet = new Net(tempListeningPort);
				myNet.startListening();
			}
			else {
				JOptionPane.showMessageDialog(null, "B��dny port nas�uchu", "Serwer - B��d po��czenia", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "B��dna ilo�� parametr�w", "Serwer - B��d rozruchu aplikacji", JOptionPane.ERROR_MESSAGE);
		}
	}
}
