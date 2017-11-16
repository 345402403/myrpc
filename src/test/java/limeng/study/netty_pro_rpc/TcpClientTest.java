package limeng.study.netty_pro_rpc;


import com.alibaba.fastjson.JSONObject;
import com.lm.rpc.netty.client.ClientRequest;
import com.lm.rpc.netty.client.TcpClient;
import com.lm.rpc.netty.entity.User;
import com.lm.rpc.netty.server.Response;
import org.junit.Test;

public class TcpClientTest {

	//@Test
	public void testGetResponse(){
		ClientRequest request = new ClientRequest();
		request.setContent(JSONObject.toJSONString(new User()));
		
		Response resp = TcpClient.send(request);
		System.out.println(resp.getMsg());
	}
	
	@Test
	public void testRpc() {
		ClientRequest request = new ClientRequest();
		
		String clzPath = "com.lm.rpc.netty.controller.UserController.saveUser";
		request.setCommand(clzPath);
		User user = new User();
		user.setUsername("u1");
		user.setUserpwd("p1");
		request.setContent(user);
		TcpClient.send(request);
	}
}
