package in.arbii;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tset{

	public static void main(String[] args) throws Exception {
		
		Callable<Object> c = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				System.out.println("I am from thread");
				return "Hi";
			}

		};
		ExecutorService exService = Executors.newFixedThreadPool(2);
		exService.submit(c);
		exService.submit(c);

		
		exService.awaitTermination(10, TimeUnit.SECONDS);
		

	}

	
}
