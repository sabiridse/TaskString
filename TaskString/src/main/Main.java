package main;

public class Main {

	public static void main(String[] args) {
		System.out.println("Начал работу");		
		long curTime1 = System.currentTimeMillis(); 
		
		//new BaseWork().addDataForOutPut();
		
		
		new GroupTest();
		
		
		long curTime2 = System.currentTimeMillis(); 		
		int msek = (int) (curTime2 - curTime1);
		System.out.println("Всего: "+msek+" мс");
	}
}
