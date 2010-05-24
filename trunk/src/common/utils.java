package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class utils {
	
	public static byte[] MDigest(String mdFun,byte[] input) throws NoSuchAlgorithmException  
	{
		MessageDigest md;
		if(mdFun != null)
		{	
			md = MessageDigest.getInstance(mdFun);
		}
		else
			md = MessageDigest.getInstance("SHA1");
		
		return md.digest(input);
	}

}
