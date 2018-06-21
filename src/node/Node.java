package node;

import java.io.Serializable;
import java.math.BigInteger;
import utilities.Operation;
import utilities.SendOperation;
import utilities.Type;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip, fileName, filePath;
	private int port;
	private BigInteger id;
	private Node successor, predecessor;
	private Operation operation = Operation.getInstance();
	private SendOperation sendOperation;
	
	private Finger[] finger;
	
	public Node (String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.id = operation.computeID(ip+port);
		finger = new Finger[5];
		sendOperation = SendOperation.getInstance();
		
	}
	
	public Node(BigInteger id) {
		this.id = id;
	}
	
	public void initializeNeighbors() {
		successor = this;
		predecessor = this;
	}
	
	public void setID(BigInteger id) {
		this.id = id;
	}
	
	public BigInteger getID() {
		return id;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIP() {
		return ip;
	}
	
	public Node getSuccessor() {
		return this.successor;
	}
	
	public void setSuccessor(Node successor) {
		this.successor = successor;
	}
	
	public Node getPredecessor() {
		return this.predecessor;
	}
	
	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}
	
	public void findSuccessor(Node node) {
		
		if(isIDinRange(node.getID()) || isSucEqualPredAndGreaterNodeID(node.getID())
				|| isIDGreaterThanSuccessor(node)) {
			
			sendOperation.sendMessageWithNode(Type.FOUND_SUCCESSOR, successor, node);
		}else if(isPredEqualSucAndLessNodeID(node.getID())) {
			
			sendOperation.sendMessageWithNode(Type.FOUND_SUCCESSOR, this, node);
		}else {
			
			Node pass = closestPreceedingNode(node);
			sendOperation.sendMessageWithNode(Type.FIND_SUCCESSOR, node, pass);
		}
		
	}
	
	public void findSuccessor(Node node, Node nodeFixing, int index) {
		
		if(isIDinRange(node.getID()) || isSucEqualPredAndGreaterNodeID(node.getID())
				|| isIDGreaterThanSuccessor(node)) {
	
			sendOperation.sendMessageWithIndex(Type.NEW_FINGER_VALUE,successor, nodeFixing,nodeFixing, index);
			
			
		}else if(isPredEqualSucAndLessNodeID(node.getID())) {
			sendOperation.sendMessageWithIndex(Type.NEW_FINGER_VALUE,this, nodeFixing,nodeFixing, index);
		}else {
			
			sendOperation.sendMessageWithIndex(Type.FIND_FINGER_SUCCESSOR, node, successor, nodeFixing, index);
		}
		
	}
	
	public void findSuccessor(Node node, Node fileEventNode) {
		
		if(isIDinRange(node.getID()) || isSucEqualPredAndGreaterNodeID(node.getID())
				|| isIDGreaterThanSuccessor(node)) {
			
			sendOperation.sendMessageWithNode(Type.FOUND_FILE_SUCCESSOR, successor, fileEventNode);
		}else if(isPredEqualSucAndLessNodeID(node.getID())) {
			
			sendOperation.sendMessageWithNode(Type.FOUND_FILE_SUCCESSOR, this, fileEventNode);
		}else {
			
			Node pass = closestPreceedingNode(node);
			sendOperation.sendMessageWithFileNode(Type.FIND_FILE_SUCCESSOR, node,fileEventNode, pass);
		}
		
	}
	
	
	private boolean isIDinRange(BigInteger nodeID) {
		boolean bool =  (nodeID.compareTo(id) == 1 && 
				(nodeID.compareTo(successor.getID()) == -1 || nodeID.compareTo(successor.getID())==0));
	
		return bool;
	}
	
	private boolean isSucEqualPredAndGreaterNodeID(BigInteger nodeID) {
		if(predecessor == null) return false;
		return (predecessor.getID().compareTo(successor.getID()) == 0 
				&& nodeID.compareTo(successor.getID()) == -1);
	}
	
	private boolean isPredEqualSucAndLessNodeID(BigInteger nodeID) {
		if(predecessor == null) return false;
		return (predecessor.getID().compareTo(successor.getID()) == 0 
				&& nodeID.compareTo(successor.getID()) == 1);
	}
	
	private boolean isIDGreaterThanSuccessor(Node node) {
		if(id.compareTo(successor.getID()) == 1) {
			if(node.getID().compareTo(successor.getID()) == -1) {
				return true;
			}else if(node.getID().compareTo(id) == 1) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	private Node closestPreceedingNode(Node node) {
		
		for(int i = 0;i<finger.length;i++) {
			if(finger[i].getKey().compareTo(id) == 1 && 
					finger[i].getKey().compareTo(node.getID()) == -1) {
				return finger[i].getKeySuccessor();
			}
		}
		
		return successor;
	}
	
	public void fixFingers(Node nodeFixing) {
		
		for(int i = 0; i< finger.length; i++) {
			BigInteger key = operation.computeFingerKey(i, id);
			finger[i] = new Finger(key);
			findSuccessor(new Node(key), nodeFixing , i);
			
		}
		
	}
	
	public void setFinger(Node node, int i) {
		finger[i].setKeySuccessor(node);
		
	}
	
	public void joinRing() {
		
		sendOperation.sendMessageWithNodeToHost(Type.REQUEST_FOR_RANDOM_NODE, this);
		
	}
	
	public void uploadFile(String filePath) {
		this.filePath = filePath;
		BigInteger fileID = operation.computeID(filePath);
		fileName = operation.extractFileName(filePath);
		findSuccessor(new Node(fileID), this);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}
		
	
}
