package utilities;

public class Type {
	
	/*Message Types*/
	public static final int START = 0;
	public static final int REQUEST_FOR_RANDOM_NODE = 1;
	public static final int RANDOM_NODE = 2;
	public static final int FIND_SUCCESSOR_OF_NEW_NODE = 5;
	public static final int FOUND_SUCCESSOR = 7;
	public static final int FIND_SUCCESSOR = 8;
	public static final int UPDATE_PREDECESSOR  = 12;
	public static final int UPDATE_SUCCESSOR = 13;
	public static final int FIX_FINGERS = 14; 
	public static final int FIX_YOUR_FINGER_TABLE = 15;
	public static final int DONE_FIXING_MY_FINGER_TABLE = 16;
	public static final int NEW_FINGER_VALUE =17;
	public static final int FIND_FINGER_SUCCESSOR =20;
	
	
	/*Peer Types*/
	public static final int HOST = 100;
	public static final int CLIENT = 200;
	
	
	
}
