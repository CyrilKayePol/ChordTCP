package node;

import java.io.Serializable;
import java.math.BigInteger;

public class Finger implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger key;
	private Node successor;
	
	public Finger(BigInteger key) {
		
		this.key = key;
		
	}
	
	public BigInteger getKey() {
		return key;
	}
	
	public Node getKeySuccessor() {
		return successor;
	}
	
	public void setKeySuccessor(Node successor) {
		this.successor = successor;
	}
}
