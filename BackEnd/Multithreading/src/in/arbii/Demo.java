package in.arbii;


public class Demo implements Runnable{
	
	@Override
	public void run(){
		System.out.println("hi");
	}

	public static void main(String[] args) {
		Demo d = new Demo();
		Thread t1 = new Thread(d);
		t1.start();
		
		Thread t2 = new Thread(d);
		t2.start();
		
		// Anonymous implementation
		Runnable r = new Runnable() {
			@Override
			public void run(){
				System.out.println("hello");
			}
		};
		
		Thread t3 = new Thread(r);
		t3.start();
	}

}
