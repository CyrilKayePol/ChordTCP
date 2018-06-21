package utilities;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Operation operation;
	
	@SuppressWarnings("unused")
	public String extractFileName(String path) {
		int index = path.lastIndexOf("/");
		String fileName = path.substring(index+1);
		return fileName;
	}
	
	@SuppressWarnings("unused")
	public BigInteger computeID(String text){
		MessageDigest digest;
		byte[] hash = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		StringBuffer hex = new StringBuffer();
		
		for(int i = 0; i< hash.length; i++) {
			String s = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hex.append('0');
			hex.append(s);
		}
		
		return (new BigInteger(hex.toString(),16));
		
	}
	
	public static Operation getInstance() {
		return (operation == null)? (operation = new Operation()):operation;
	}
	
	public BigInteger computeFingerKey(int i, BigInteger id) {
		BigInteger big = new BigInteger("0");
		BigInteger two = new BigInteger("2");
		big = big.add(id.add(two.pow(i)));
		big = big.mod(two.pow(160));
		
		
		return big;
	}
	

}
