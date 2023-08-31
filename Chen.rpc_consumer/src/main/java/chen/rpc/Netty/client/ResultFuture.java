package chen.rpc.Netty.client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.ReentrantLock;
//用来异步接受结果的类
public class ResultFuture {
	public static final ConcurrentHashMap<Long, ResultFuture> resultFutures = new ConcurrentHashMap<>();
	public final ReentrantLock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
	private Response response;
	
	private  Long timeout =2*60*1000l; 
	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	private Long starttime ;
	
	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public ResultFuture(ClientRequest request) {
		
		starttime = System.currentTimeMillis();
		resultFutures.put(request.getId(), this);
	
	}

	public Response get() {
		
		lock.lock();
			try {
				while (!done()) {
					condition.await();
				} 
			} catch (Exception e) {
			}finally {
				lock.unlock();
			}
	
		return this.response;
	}
	public Response get(Long timeout) {
		
		lock.lock();
			try {
				while (!done()) {
					condition.await(timeout,TimeUnit.MILLISECONDS);
					if(System.currentTimeMillis()-starttime > timeout) {
						break;
					}
				} 
			} catch (Exception e) {
			}finally {
				lock.unlock();
			}
	
		return this.response;
	}

	private boolean done() {
		if(this.response == null) {
			return false;
		}
		
		return true;
	}
	public static void receive(Response response) {
		
		if(response != null) {
			ResultFuture f = resultFutures.get(response.getId());
			
			if (f != null) {
				ReentrantLock lock = f.lock;
				lock.lock();
				try {
					f.setResponse(response);
					f.condition.signal();
					//remove掉的是那个集合中的对象，集合中没有了，
					//但是这个这个对象我们依然在使用就是f
					//所以依然可以调用lock
					System.out.println(Thread.currentThread().getName());
					resultFutures.remove(response.getId());

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
			
		
		}
	}
	 static class  ClearFutureThread extends Thread{

		@Override
		public void run() {
			Set<Long> ids = resultFutures.keySet();
			for(Long id : ids) {
				ResultFuture future = resultFutures.get(id);
				if(future==null) {
					resultFutures.remove(id);
				}else if(System.currentTimeMillis()-future.getStarttime()>future.getTimeout()) {
					
					Response  r = new Response();
					r.setCode("1111");
					r.setMsg("失败了");
					r.setId(id);
					receive(r);
					
				}
				
			}
		}
		 
		
	}
	 
	 static {
		new ClearFutureThread().start();
	 }
	
	
	

}
