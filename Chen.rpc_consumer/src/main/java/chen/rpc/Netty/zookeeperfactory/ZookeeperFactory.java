package chen.rpc.Netty.zookeeperfactory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class ZookeeperFactory {
	static CuratorFramework client;
	public static CuratorFramework  createClient() {
		if(client ==null) {
		String zookeeperConnectionString = "localhost:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);	
		client.start();
			
		}
		return client;

	}
	
	
	public static void main(String[] args) throws Exception {
		CuratorFramework client = createClient();
		client.create().forPath("/netty");
	}
}
