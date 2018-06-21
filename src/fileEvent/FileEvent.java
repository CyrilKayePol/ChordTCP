package fileEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class FileEvent {
	
	private static FileEvent fileEvent;
	public static FileEvent getInstance() {
		return (fileEvent == null)?(fileEvent = new FileEvent()):fileEvent;
	}
	
	@SuppressWarnings("resource")
	public void saveFile(String ip, int port, String name){
		DataInputStream dis;
		FileOutputStream fos;
		try {
			Socket socket = new Socket(ip,port);
			dis = new DataInputStream(socket.getInputStream());
			fos = new FileOutputStream("clientFiles/"+name);
			byte[] buffer = new byte[1024000];
			
			int read = 0;
			while((read = dis.read(buffer, 0, buffer.length)) > 0) {
				fos.write(buffer, 0, read);
			}
			fos.close();
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@SuppressWarnings("resource")
	public void sendFile(String ip, int port, String filePath){
		
		DataOutputStream dos;
		FileInputStream fis;
		try {
			
			Socket socket = new Socket(ip,port);
			dos = new DataOutputStream(socket.getOutputStream());
			fis = new FileInputStream(filePath);
			byte[] buffer = new byte[1024000];
			while (fis.read(buffer) > 0) {
				dos.write(buffer);
			}
			
			fis.close();
			dos.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
