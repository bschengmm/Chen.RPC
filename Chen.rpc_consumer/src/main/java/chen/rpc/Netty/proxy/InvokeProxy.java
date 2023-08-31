package chen.rpc.Netty.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import chen.rpc.Netty.annotation.RemoteInvoke;
import chen.rpc.Netty.client.ClientRequest;
import chen.rpc.Netty.client.Response;
import chen.rpc.Netty.client.TcpClient;



@Component
public class InvokeProxy implements BeanPostProcessor {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			Field[] fields = bean.getClass().getDeclaredFields();
			final Map<Method, Class> methodClassMap = new HashMap<>();
			
			for(Field f : fields ) {
				if(f.isAnnotationPresent(RemoteInvoke.class)) {
					putMethodClassMap(f,methodClassMap);
					f.setAccessible(true);
					
					Enhancer enhancer = new  Enhancer();
					enhancer.setInterfaces(new Class[] {f.getType()});
					enhancer.setCallback(new MethodInterceptor() {
						
						@Override
						public Object intercept(Object instance, Method method, Object[] methodArg, MethodProxy proxy) throws Throwable {
							
						
							ClientRequest request = new ClientRequest();
							request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
							request.setContent(methodArg[0]);
							
							Response r = TcpClient.send(request);
							return r;
						}
					});
					
					
					
					try {
						f.set(bean, enhancer.create());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
			
			return bean;
	}
	private void putMethodClassMap(Field f,Map<Method, Class> map) {
		Method[] methods  = f.getType().getDeclaredMethods();
		for(Method m : methods) {
			map.put(m , f.getType());
		}		
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}

}
