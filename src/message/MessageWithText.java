package message;

import node.Node;

public class MessageWithText extends MessageWithNode {
	
	private static final long serialVersionUID = 1L;
	private String text;
	public MessageWithText(int type, Node node, String text) {
		super(type, node);
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
