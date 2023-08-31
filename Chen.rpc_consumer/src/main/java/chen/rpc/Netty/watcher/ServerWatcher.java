package chen.rpc.Netty.watcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import chen.rpc.Netty.client.ChannelManager;
import chen.rpc.Netty.client.TcpClient;
import chen.rpc.Netty.contants.Constant;
import chen.rpc.Netty.zookeeperfactory.ZookeeperFactory;
import io.netty.channel.ChannelFuture;

public class ServerWatcher implements Watcher {

	@Override
	public void process(WatchedEvent event) {
		 
		try {
			CuratorFramework client = ZookeeperFactory.createClient();
		List<String> serverPaths = client.getChildren().forPath(Constant.path);
		
		client.getChildren().usingWatcher(this).forPath(event.getPath());
		ChannelManager.realServerPath.clear();
		ChannelManager.clearChannel();
		Set<String> setServerPaths =  new HashSet<>();
		//利用set去重
		
		for(String path: serverPaths) {		
			setServerPaths.add(path);
		}
			
		for(String path : setServerPaths) {
			
			String [] hostandport = path.split("#");
      	  ChannelFuture channelfuture = TcpClient.b.connect(hostandport[0], Integer.valueOf(hostandport[1]));
      	  ChannelManager.realServerPath.add(hostandport[0]+"#"+hostandport[1]);
      	  ChannelManager.channelFutures.add(channelfuture);
			
			
		}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
