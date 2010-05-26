package common;

import java.io.File;
import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * @author czarek
 * Klasa przechowujaca informacje o pliki
 */
public class FileInfo implements Serializable{
	
	public FileInfo(String nameWithType, String desc, long size) {
		super();
		StringTokenizer st = new StringTokenizer(nameWithType, ".");
		String nameWithoutType = new String();
		for (int i = 0 ; i < st.countTokens() -1; i++)
		{
			nameWithoutType += st.nextToken();
		}
		this.name = nameWithoutType;
		this.type = st.nextToken();
		this.desc = desc;
		this.size = size;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String desc;
	String type;
	long size;
	transient File file;

}
