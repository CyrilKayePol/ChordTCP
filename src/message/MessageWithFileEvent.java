package message;

import fileEvent.FileEvent;

public class MessageWithFileEvent extends Message {
	
	
	private static final long serialVersionUID = 1L;
	private FileEvent fevent;
	public MessageWithFileEvent(int type, FileEvent fevent) {
		super(type);
		this.fevent = fevent;
	}
	
	public FileEvent getFileEvent() {
		return fevent;
	}
}
