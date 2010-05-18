/**
 * 
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author czarek
 *
 */
public class SimpleSecureP2P {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		File file = new File (".");
		System.out.println( file.getAbsolutePath());
//		FileOutputStream os = new FileOutputStream("kurwa_jakis_plik.txt");
	
		
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}

	}

}
