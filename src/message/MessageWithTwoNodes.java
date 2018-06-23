package message;

import node.Node;

public class MessageWithTwoNodes extends MessageWithNode {

	private static final long serialVersionUID = 1L;
	private Node node2;
	public MessageWithTwoNodes(int type, Node node, Node node2) {
		super(type, node);
		this.node2 = node2;
	}
	
	public Node getNode2() {
		return node2;
	}

}
