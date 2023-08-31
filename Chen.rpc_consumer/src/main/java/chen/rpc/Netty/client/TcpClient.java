package chen.rpc.Netty.client;





import java.util.List;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.Watcher;

import com.alibaba.fastjson.JSONObject;

import chen.rpc.Netty.contants.Constant;
import chen.rpc.Netty.handler.SimpleClientHandler;
import chen.rpc.Netty.watcher.ServerWatcher;
import chen.rpc.Netty.zookeeperfactory.ZookeeperFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {
	
	static ChannelFuture f = null;
	public static Bootstrap b = new Bootstrap();
	
	static {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			
			// (1)
            b.group(worker); // (2)
            b.channel(NioSocketChannel.class) ;// (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<NioSocketChannel>() {
               

				@Override
				protected void initChannel(NioSocketChannel ch) throws Exception {
					ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Delimiters.lineDelimiter()[0]));
					
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new SimpleClientHandler());
					ch.pipeline().addLast(new StringEncoder());

				}
            });
            Watcher watcher = new ServerWatcher();
            CuratorFramework client = ZookeeperFactory.createClient();
            client.getChildren().usingWatcher(watcher).forPath(Constant.path);
            List<String> serverPaths = client.getChildren().forPath(Constant.path);
            for(String path : serverPaths) {
            	//循环遍历所有服务器地址，将他们保存到ChannelManager里面的
            	//realServerPath和连接对应的channelFutrues保存到channelFutrues
            	  String [] hostandport = path.split("#");
            	  ChannelFuture channelfuture = b.connect(hostandport[0], Integer.valueOf(hostandport[1]));
            	  ChannelManager.realServerPath.add(hostandport[0]+"#"+hostandport[1]);
            	  ChannelManager.channelFutures.add(channelfuture);
           	  
            }
            
            
    		
            
            //int port = 8081;
            //f = b.connect(host, port).sync();
			
            
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	}
		
	public static Response send(ClientRequest request) {
		
		
		f = ChannelManager.getChannel(ChannelManager.position);
		f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
		
		ResultFuture r = new ResultFuture(request);
		System.out.println("11"+Thread.currentThread().getName());
		return r.get();
	}
	public static void main(String[] args) {
		
	}



}
	
	
	
	
