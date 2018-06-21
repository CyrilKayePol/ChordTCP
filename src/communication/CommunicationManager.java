package communication;

import java.io.ObjectInputStream;
import java.net.Socket;

import fileEvent.FileEvent;
import message.Message;
import message.MessageWithFileNode;
import message.MessageWithIndex;
import message.MessageWithNode;
import message.MessageWithText;
import node.Node;
import peer.Host;
import utilities.SendOperation;
import utilities.Type;

public class CommunicationManager {
	
	private Socket socket;
	private Object input;
	
	private Node myNode;
	private SendOperation sendOperation;
	private FileEvent fileEvent;
	
	public CommunicationManager(Socket socket, Node myNode) {
		this.socket = socket;
		this.myNode = myNode;
		sendOperation = SendOperation.getInstance();
		fileEvent = FileEvent.getInstance();
		communicationProcess();
	
	}
	
	
	private void communicationProcess() {
		receiveMessage();
		
		
		 if(input instanceof MessageWithIndex) {
			MessageWithIndex msg = (MessageWithIndex) input;
			
			if(msg.getType() == Type.FIND_FINGER_SUCCESSOR) {
				
				myNode.findSuccessor(msg.getNode(), msg.getNodeFixing(), msg.getIndex());
				
			}else if(msg.getType() == Type.NEW_FINGER_VALUE) {
				
				myNode.setFinger(msg.getNode(), msg.getIndex());
			}
		}else if(input instanceof MessageWithFileNode) {
			 MessageWithFileNode msg = (MessageWithFileNode) input;
			 if(msg.getType() == Type.FIND_FILE_SUCCESSOR) {
				 myNode.findSuccessor(msg.getNode(), msg.getFileEventNode());
			 }
		}else if(input instanceof MessageWithText) {
			MessageWithText msg = (MessageWithText) input;
			if(msg.getType() == Type.REQUEST_TO_SEND_FILE) {
				sendOperation.sendMessageWithNode(Type.READY_TO_SAVE_FILE, myNode, msg.getNode());
				//should i send message with text?
				fileEvent.saveFile(msg.getNode().getIP(), msg.getNode().getPort(), msg.getText());
				
			}
		}else if(input instanceof MessageWithNode) {
			
			MessageWithNode msg = (MessageWithNode) input;
			if(msg.getType() == Type.REQUEST_FOR_RANDOM_NODE){
				
				Host host = Host.getInstance();
				Node randomNode = host.getRandomNode();
				host.addNewConnectedNode(msg.getNode());
				sendOperation.sendMessageWithNode(Type.RANDOM_NODE, randomNode, msg.getNode());
				
			}else if(msg.getType() == Type.RANDOM_NODE) {
				
				sendOperation.sendMessageWithNode(Type.FIND_SUCCESSOR, myNode, msg.getNode());
				
			}else if(msg.getType() == Type.FIND_SUCCESSOR) {
				
				myNode.findSuccessor(msg.getNode());
				
			}else if(msg.getType() == Type.FOUND_SUCCESSOR) {
				
				myNode.setSuccessor(msg.getNode());
				sendOperation.sendMessageWithNode(Type.UPDATE_PREDECESSOR, myNode, msg.getNode());
			
			}else if(msg.getType() == Type.UPDATE_PREDECESSOR) {
				
				Node oldPredecessor = myNode.getPredecessor();
				myNode.setPredecessor(msg.getNode());
				
				if(oldPredecessor != null) 
					sendOperation.sendMessageWithNode(Type.UPDATE_SUCCESSOR, myNode.getPredecessor(), oldPredecessor);
				else 
					sendOperation.sendMessageToHost(Type.FIX_FINGERS);
				
				
			}else if(msg.getType() == Type.UPDATE_SUCCESSOR) {
				myNode.setSuccessor(msg.getNode());
				sendOperation.sendMessageWithNode(Type.UPDATE_PREDECESSOR, myNode, msg.getNode());
			}else if(msg.getType() == Type.FOUND_FILE_SUCCESSOR) {
				sendOperation.sendMessageWithText(Type.REQUEST_TO_SEND_FILE, myNode, myNode.getFileName(), msg.getNode());
				
			}else if(msg.getType() == Type.READY_TO_SAVE_FILE) {
				fileEvent.sendFile(msg.getNode().getIP(), msg.getNode().getPort(), myNode.getFilePath());
			}
			
		}else if(input instanceof Message) {
			Message msg = (Message) input;
			if(msg.getType() == Type.FIX_FINGERS) {
				
				Host host = Host.getInstance();
				host.notifyNodesToFixFinger();
				
			}else if(msg.getType() == Type.FIX_YOUR_FINGER_TABLE) {
				
				myNode.fixFingers(myNode);
			
			}
		}
	}
	

	private void receiveMessage() {
		
		try {
			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());  
			input = in.readObject();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
}
