package common;

import java.io.File;
import java.io.Serializable;

/**
 * @author czarek
 *
 */
public class FileInfo implements Serializable, Comparable<FileInfo>{
	
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

	public int compareTo(FileInfo o) {
	
		int ret;
		if((ret = this.name.compareTo(o.name) )!=0)
			return ret;
		else
			if(this.size < o.size)
				return 1;
			else
				return 0;
		
	}
	
}
