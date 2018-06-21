package peer;

import java.util.ArrayList;
import java.util.Random;

import node.Node;
import utilities.SendOperation;
import utilities.Type;

public class Host extends Peer{
	
	private static Host host;
	private ArrayList<Node> connectedNodes;
	private SendOperation sendOperation;
	
	public Host(String ip, int port) {
		super(ip, port, Type.HOST);
		initialize();
		addNewConnectedNode(host.getMyNode());
	}
	
	private void initialize() {
		connectedNodes = new ArrayList<Node>();
		sendOperation = SendOperation.getInstance();
		host = this;
	}
	
	public static Host getInstance() {
		return host;
	}
	
	public Node getRandomNode() {
		Random rand = new Random();
		
		return (connectedNodes.size() == 1)? 
				connectedNodes.get(0):connectedNodes.get(rand.nextInt(connectedNodes.size()-1));
	}
	
	public void notifyNodesToFixFinger() {
		for(int i = 0; i<connectedNodes.size(); i++) {
			sendOperation.sendMessage(Type.FIX_YOUR_FINGER_TABLE, connectedNodes.get(i));
		}
	}
	public void addNewConnectedNode(Node n) {
		connectedNodes.add(n);
	}
	
}
