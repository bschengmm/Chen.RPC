package Chen.rpc.Netty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chen.rpc.Netty.annotation.RemoteInvoke;
import chen.rpc.Netty.client.Response;
import chen.rpc.Netty.user.User;
import chen.rpc.Netty.user.UserRemote;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRemoteInvoke.class)
@ComponentScan("chen.rpc")
public class TestRemoteInvoke {
	
	@RemoteInvoke
	private UserRemote userRemote;

	@Test
	public void testinvoke() {
	
		
		User user = new User();
		user.setId(1);	
		user.setName("张三wwww");
		
		Response r = userRemote.saveUser(user);
		System.out.println(r+Thread.currentThread().getName());
		
	}
}
