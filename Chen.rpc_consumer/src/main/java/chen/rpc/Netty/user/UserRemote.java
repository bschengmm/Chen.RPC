package chen.rpc.Netty.user;

import chen.rpc.Netty.client.Response;

public interface UserRemote {
	public Response saveUser(User user) ;

}
