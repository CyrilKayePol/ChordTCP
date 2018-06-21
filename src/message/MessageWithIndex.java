package message;

import node.Node;

public class MessageWithIndex extends MessageWithNode{

	private static final long serialVersionUID = 1L;
	int index;
	Node nodeFixing;
	public MessageWithIndex(int type, Node node, Node nodeFixing, int index) {
		super(type, node);
		this.index = index;
		this.nodeFixing = nodeFixing;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Node getNodeFixing() {
		return nodeFixing;
	}
}
