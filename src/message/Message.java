package message;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private int type;
	
	public Message(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
}
