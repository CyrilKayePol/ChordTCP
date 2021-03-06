package utilities;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import fileEvent.FileEvent;
import message.Message;
import message.MessageWithFileEvent;
import message.MessageWithFileNode;
import message.MessageWithIndex;
import message.MessageWithNode;
import message.MessageWithText;
import message.MessageWithTwoNodes;
import node.Node;

@SuppressWarnings("resource")
public class SendOperation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static SendOperation sendOperation;
	private String hostIP = "localhost";
	private int hostPort = 4444;
	
	public static SendOperation getInstance() {
		return (sendOperation == null)? (sendOperation = new SendOperation()):sendOperation;
	}
	
	public void sendMessageWithNode(int message, Node msgNode, Node receiverNode) {
		try {
			
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithNode(message, msgNode));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithNode(message, msgNode, receiverNode);
		}
	}
	
	public void sendMessage(int message, Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new Message(message));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessage(message, receiverNode);
		}
	}
	
	public void sendMessageWithNodeForExit(int message, Node msgNode, Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithNode(message, msgNode));
		} catch (IOException e) {
			
			msgNode.setPredecessor(null);
			System.out.println("sent dead node");
			sendOperation.sendMessageWithNodeToHost(Type.DEAD_NODE, receiverNode);
			sendMessageWithTwoNodes(Type.CHECKING_ZOMBIE_SUCCESSOR, receiverNode, msgNode, msgNode.getSuccessor());
		}
	}
	
	public void sendMessageWithTwoNodes(int message,Node msgNode, Node msgNode2, Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithTwoNodes(message, msgNode, msgNode2));
		} catch (IOException e) {
			msgNode2.setPredecessor(msgNode2);
			msgNode2.setSuccessor(msgNode2);
		}
	}
	public void sendMessageToHost(int message) {
		try {
			Socket s = new Socket(hostIP, hostPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new Message(message));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageToHost(message);
		}
	}
	
	public void sendMessageWithNodeToHost(int message, Node msgNode) {
		try {
			Socket s = new Socket(hostIP, hostPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithNode(message, msgNode));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithNodeToHost(message, msgNode);
		}
	}
	
	public void sendMessageWithIndex(int message, Node msgNode, Node receiverNode, Node nodeFixing, int index) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithIndex(message, msgNode,nodeFixing, index));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithIndex(message, msgNode, receiverNode, nodeFixing, index);
		}
	}
	
	public void sendMessageWithFileNode(int message, Node msgNode, Node fileEventNode, Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithFileNode(message, msgNode,fileEventNode));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithFileNode(message, msgNode, fileEventNode,receiverNode);
		}
	}
	
	public void sendMessageWithFileEvent(int message, Node msgNode, FileEvent fevent , Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			System.out.println("sending file ...");
			out.reset();
			out.writeObject(new MessageWithFileEvent(message,fevent));
			out.reset();
			System.out.println("sent the fudging file");
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithFileEvent(message, msgNode, fevent, receiverNode);
		}
	}
	
	public void sendMessageWithFileEventToHost(int message, Node msgNode, FileEvent fevent) {
		try {
			Socket s = new Socket(hostIP, hostPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			System.out.println("sending file ...");
			out.reset();
			out.writeObject(new MessageWithFileEvent(message,fevent));
			out.reset();
			System.out.println("sent the fudging file");
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithFileEventToHost(message, msgNode, fevent);
		}
	}
	
	public void sendMessageWithText(int message, Node msgNode, String text, Node receiverNode) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithText(message, msgNode,text));
		} catch (IOException e) {
			//e.printStackTrace();
			sendMessageWithText(message, msgNode, text, receiverNode);
		}
	}
}
