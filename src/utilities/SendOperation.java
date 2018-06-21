package utilities;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import message.Message;
import message.MessageWithIndex;
import message.MessageWithNode;
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
			e.printStackTrace();
		}
	}
	
	public void sendMessage(int message, Node node) {
		try {
			Socket s = new Socket(node.getIP(), node.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new Message(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToHost(int message) {
		try {
			Socket s = new Socket(hostIP, hostPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new Message(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageWithNodeToHost(int message, Node msgNode) {
		try {
			Socket s = new Socket(hostIP, hostPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithNode(message, msgNode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageWithIndex(int message, Node msgNode, Node receiverNode, Node nodeFixing, int index) {
		try {
			Socket s = new Socket(receiverNode.getIP(), receiverNode.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(new MessageWithIndex(message, msgNode,nodeFixing, index));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
