package message;

import node.Node;

public class MessageWithNode extends Message {

	private static final long serialVersionUID = 1L;
	private Node node;
	
	public MessageWithNode(int type, Node node) {
		super(type);
		this.node = node;
	}
	
	public Node getNode() {
		return this.node;
	}
}
