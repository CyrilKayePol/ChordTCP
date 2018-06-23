package peer;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import communication.CommunicationManager;
import node.Node;
import utilities.Type;

public class Peer extends Thread {
	
	private Node myNode;
	private ServerSocket serverSocket;
	private Socket clientSocket; 
	private Scanner scan;
	private Menu m;
	private Stabilize stabilize;
	public Peer(String ip, int port, int peerType) {
		myNode = new Node(ip,port);
		
		if(peerType == Type.HOST) {
			myNode.setID(new BigInteger("20"));
			myNode.initializeNeighbors();
		}else {
			myNode.setPort(5000);
			myNode.setID(new BigInteger("0"));
			myNode.joinRing();
		}
		
		initialize();
		start();
		m.start();
		//if(peerType == Type.HOST)
			stabilize.start();
	}
	
	private void initialize() {
		scan = new Scanner(System.in);
		m = new Menu();
		stabilize = new Stabilize();
		try {
			serverSocket = new ServerSocket(myNode.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				clientSocket = serverSocket.accept();
				new CommunicationManager(clientSocket, myNode).start();
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Node getMyNode() {
		return this.myNode;
	}
	
	private class Menu extends Thread{
		
		public void run() {
			while(true) {
					menu();
			}
		}
		
		private void menu() {
			System.out.println("----CHORD----");
			System.out.println("[1] view information");
			System.out.println("[2] download file");
			System.out.println("[3] upload file");
			System.out.println("[4] exit");
			System.out.println("enter choice:");
			
			String choice = scan.nextLine();
			
			System.out.println();
			switch(choice) {
			case "1":
				viewInformation();
				break;
			case "2":
				downloadFile();
				break;
			case "3":
				uploadFile();
				break;
			case "4":
				exitRing();
				break;
			default:
				warning();
				break;
			}
		}

	
		private void viewInformation() {
			System.out.println("============node info=============");
			System.out.println("Predecessor: "+myNode.getPredecessor().getID());
			System.out.println("ID:          "+myNode.getID());
			System.out.println("Successor:   "+myNode.getSuccessor().getID());
			System.out.println("==================================");
		}

		
		private void downloadFile() {
			System.out.println(">>>>>>>download a file<<<<<<<");
			System.out.print("Enter file name: ");
			String fileName = scan.nextLine();
			System.out.println();
			System.out.println(">>>>>>>>>>>>><<<<<<<<<<<<<<");
			myNode.downloadFile(fileName);
		}

		
		private void uploadFile() {
			System.out.println(">>>>>>>upload a file<<<<<<<");
			System.out.print("Enter file's path: ");
			String filePath = scan.nextLine();
			System.out.println();
			System.out.println(">>>>>>>>>>>>><<<<<<<<<<<<<<");
			myNode.uploadFile(filePath);
		}

		
		private void exitRing() {
			System.out.println("B Y E ^.^");
			System.exit(0);
		}

		
		private void warning() {
			System.out.println("[Invalid input! Please try again.]");
		}
		
	}
	
	private class Stabilize extends Thread{
		public void run() {
			while(true) {
				try {
					Thread.sleep(5000);
					myNode.checkPredecessor();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
}
