package peer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import node.Node;
import utilities.SendOperation;
import utilities.Type;

public class Host extends Peer{
	
	private static Host host;
	private ArrayList<Node> connectedNodes;
	private SendOperation sendOperation;
	private CleanCache cleanCache;
	
	public Host(String ip, int port) {
		super(ip, port, Type.HOST);
		initialize();
		cleanCache.start();
		addNewConnectedNode(host.getMyNode());
	}
	
	private void initialize() {
		connectedNodes = new ArrayList<Node>();
		sendOperation = SendOperation.getInstance();
		cleanCache = new CleanCache();
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
	
	public void removeNodeFromConnectedNodes(Node n) {
		for(int i = 0;i<connectedNodes.size();i++) {
			if(connectedNodes.get(i).getID().compareTo(n.getID())==0) {
				connectedNodes.remove(i);
				break;
			}
		}
	}
	
	public void print() {
		 for(int i = 0;i<connectedNodes.size();i++) {
			 System.out.println("nodes: "+connectedNodes.get(i).getID());
		 }
	}
	
	private class CleanCache extends Thread{
		public void run() {
			while(true) {
				try {
					Thread.sleep(1000 * 3600 * 8);
					FileUtils.cleanDirectory(new File("cached files/"));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
}
