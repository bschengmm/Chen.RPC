package Chen.rpc.Netty;

import org.apache.zookeeper.server.Request;

import chen.rpc.Netty.client.ClientRequest;
import chen.rpc.Netty.client.ResultFuture;

public class testfinal {
	public static void main(String[] args) {
		ClientRequest request1 = new ClientRequest();
		ClientRequest request2 = new ClientRequest();
		
		ResultFuture r1 = new ResultFuture(request1);
		ResultFuture r2 = new ResultFuture(request2);
		System.out.println(r1.lock == r2.lock);
		
	}

}
