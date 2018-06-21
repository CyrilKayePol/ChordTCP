package message;

import java.math.BigInteger;

public class MessageWithID extends Message {

	private static final long serialVersionUID = 1L;
	private BigInteger id;
	
	public MessageWithID(int type, BigInteger id) {
		super(type);
		this.id = id;
	}
	
	public BigInteger getID() {
		return this.id;
	}
}
