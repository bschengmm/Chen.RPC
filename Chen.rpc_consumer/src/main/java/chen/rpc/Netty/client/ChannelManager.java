package chen.rpc.Netty.client;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelFuture;

public class ChannelManager {
	public static CopyOnWriteArrayList<String> realServerPath = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
	
	public static AtomicInteger position = new AtomicInteger(0);
	public static void addChannel(ChannelFuture channel) {channelFutures.add(channel);};
	
	public static void clearChannel() {
		channelFutures.clear();
	}
	public static ChannelFuture getChannel(AtomicInteger i) {
		if(i.get()>=channelFutures.size()) {
			position = new AtomicInteger(1);
			return channelFutures.get(0);
		}else {
			return channelFutures.get(i.getAndIncrement());
		}
	
	}
	public static void main(String[] args) {
		
	}
	

	


}
