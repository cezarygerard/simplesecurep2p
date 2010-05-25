package common;

import java.io.File;
import java.io.Serializable;

/**
 * @author czarek
 * Klasa przechowujaca informacje o pliki
 */
public class FileInfo implements Serializable{
	
	public FileInfo(String name, String desc, long size) {
		super();
		this.name = name;
		this.desc = desc;
		this.size = size;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String desc;
	long size;
	transient File file;

}
