package common;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author czarek
 * Klasa przechowujaca informacje o pliki
 */
public class FileInfo implements Serializable, Comparable<FileInfo>{
	

	public FileInfo(File file2) {
		super();
		if(file2.exists() && file2.isFile())
		{
			StringTokenizer st = new StringTokenizer(file2.getName(), ".");
			String nameWithoutType = new String();
			for (int i = 0 ; i < st.countTokens() -1; i++)
			{
				nameWithoutType += st.nextToken();
			}
			this.name = nameWithoutType;
			this.type = st.nextToken();
			this.file = file2;
			try {
				this.nameMD = utils.toHexString(utils.MDigest("SHA1", this.name.getBytes()));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			size = file2.length();
		}
	}

	public FileInfo(String nameMD_)
	{
		super();
		this.nameMD = nameMD_;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	String desc;
	String type;
	public long size;
	transient File file;
	public String nameMD;
	public List<PeerInfo> ownersInfo = Collections.synchronizedList(new ArrayList<PeerInfo>());

	
	public int compareTo(FileInfo arg0) {
		if (arg0 ==null)
			throw new NullPointerException() ;

			return nameMD.compareTo(((FileInfo)arg0).nameMD);
	}	
	
	public boolean equals(FileInfo arg0)
	{
		return nameMD.equals(((FileInfo)arg0).nameMD);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FileInfo [name=" + name + ", nameMD=" + nameMD
				+ ", ownersInfo=" + ownersInfo + "]";
	}

}
