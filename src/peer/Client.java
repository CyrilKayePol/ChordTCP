package peer;

import utilities.Type;

public class Client extends Peer {

	public Client(String ip, int port, String hostIP, int hostPort) {
		super(ip, port, Type.CLIENT);
	}
	

}
