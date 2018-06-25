package communication;

import java.io.ObjectInputStream;
import java.net.Socket;

import fileEvent.FileEvent;
import fileEvent.SendFile;
import fileEvent.StoreFile;
import message.Message;
import message.MessageWithFileEvent;
import message.MessageWithFileNode;
import message.MessageWithIndex;
import message.MessageWithNode;
import message.MessageWithText;
import message.MessageWithTwoNodes;
import node.Node;
import peer.Host;
import utilities.SendOperation;
import utilities.Type;

public class CommunicationManager extends Thread {
	
	private Socket socket;
	private Object input;
	
	private Node myNode;
	private SendOperation sendOperation;
	
	
	public CommunicationManager(Socket socket, Node myNode) {
		this.socket = socket;
		this.myNode = myNode;
		sendOperation = SendOperation.getInstance();
	}
	
	public void run() {
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
				 System.out.println("finding file successor of "+msg.getNode().getID());
				 myNode.findSuccessor(msg.getNode(), msg.getFileEventNode());
			 }
		}else if(input instanceof MessageWithFileEvent) {
			MessageWithFileEvent msg = (MessageWithFileEvent) input;
			if(msg.getType() == Type.FILE_TO_STORE) {
				System.out.println("received file event");
				if (msg.getFileEvent().getStatus().equalsIgnoreCase("Error")) {
					System.out.println("\n[Failed to download or upload file]");
					
				}else {
					System.out.println("Downloading file ...");
					new StoreFile().createAndWriteFile(msg.getFileEvent());
					System.out.println("Successfully downloaded file!");
				}
				
			}
		}else if(input instanceof MessageWithText) {
			MessageWithText msg = (MessageWithText) input;
		    if(msg.getType() == Type.REQUESTING_TO_DOWNLOAD) {
				SendFile sendFile = new SendFile("file/"+msg.getText(), "files/");
				FileEvent fevent = sendFile.getFileEvent();
				sendOperation.sendMessageWithFileEvent(Type.FILE_TO_STORE, myNode, fevent, msg.getNode());
				System.out.println("sent file event");
			}
		}else if(input instanceof MessageWithTwoNodes) {
			MessageWithTwoNodes msg = (MessageWithTwoNodes) input;
			
			if(msg.getType() == Type.CHECKING_ZOMBIE_SUCCESSOR) {
				if(msg.getNode().getID().compareTo(myNode.getSuccessor().getID()) == 0) {
					myNode.setSuccessor(msg.getNode2());
					sendOperation.sendMessageWithNode(Type.UPDATE_PREDECESSOR, myNode, msg.getNode2());
				}else {
					sendOperation.sendMessageWithTwoNodes(Type.CHECKING_ZOMBIE_SUCCESSOR, msg.getNode(), msg.getNode2(), myNode.getSuccessor());
				}
			}
	    }else if(input instanceof MessageWithNode) {
			
			MessageWithNode msg = (MessageWithNode) input;
			if(msg.getType() == Type.REQUEST_FOR_RANDOM_NODE){
				
				Host host = Host.getInstance();
				Node randomNode = host.getRandomNode();
				host.addNewConnectedNode(msg.getNode());
				sendOperation.sendMessageWithNode(Type.RANDOM_NODE, randomNode, msg.getNode());
				
			}else if(msg.getType() == Type.RANDOM_NODE) {
				System.out.println("received the random node "+msg.getNode().getID());
				sendOperation.sendMessageWithNode(Type.FIND_SUCCESSOR, myNode, msg.getNode());
				
			}else if(msg.getType() == Type.FIND_SUCCESSOR) {
				
				myNode.findSuccessor(msg.getNode());
				
			}else if(msg.getType() == Type.FOUND_SUCCESSOR) {
				System.out.println("received my newly found successor "+msg.getNode().getID());
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
				
				if(myNode.getFileOperationType() == Type.UPLOAD) {
					SendFile sendFile = new SendFile(myNode.getFilePath(), "files/");
					FileEvent fevent = sendFile.getFileEvent();
					sendOperation.sendMessageWithFileEvent(Type.FILE_TO_STORE, myNode, fevent, msg.getNode());
					System.out.println("sent file event");
					
				    sendFile = new SendFile(myNode.getFilePath(), "cached files/");
				    fevent = sendFile.getFileEvent();
					sendOperation.sendMessageWithFileEventToHost(Type.FILE_TO_STORE, myNode, fevent);
					System.out.println("sent file event");
				}else if(myNode.getFileOperationType() == Type.DOWNLOAD) {
					sendOperation.sendMessageWithText(Type.REQUESTING_TO_DOWNLOAD, myNode, myNode.getFileName(), msg.getNode());
				}
			}else if(msg.getType() == Type.ARE_YOU_STILL_ALIVE) {
				sendOperation.sendMessageWithNode(Type.IM_ALIVE_ALERT_AWAKE, myNode, msg.getNode());
			}else if(msg.getType() == Type.DEAD_NODE) {
				System.out.println("DEAD NODE");
				Host host = Host.getInstance();
				host.removeNodeFromConnectedNodes(msg.getNode());
				host.print();
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
