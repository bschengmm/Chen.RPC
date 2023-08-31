package chen.rpc.Netty.client;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
	private Long id ;
	private Object content;
	private String command;
	static AtomicLong aid = new AtomicLong(1);
	
	public ClientRequest() {
		
		this.id = aid.getAndIncrement();
		
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
	
	

}
