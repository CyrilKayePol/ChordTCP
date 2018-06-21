package message;

import node.Node;

public class MessageWithFileNode extends MessageWithNode {
	
	private static final long serialVersionUID = 1L;
	private Node fileEventNode;
	
	public MessageWithFileNode(int type, Node node, Node fileEventNode) {
		super(type, node);
		this.fileEventNode = fileEventNode;
	}
	
	public Node getFileEventNode() {
		return fileEventNode;
	}
}
