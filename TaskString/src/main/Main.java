package main;

import java.util.concurrent.ExecutionException;

import test.Parallel;

public class Main {

	public static void main(String[] args) {
		System.out.println("Начал работу");		
		long curTime1 = System.currentTimeMillis(); 
		
		new Grouping();///parallel
			
//		Parallel paral = new Parallel();
//		try {
//			paral.test1();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
		
		long curTime2 = System.currentTimeMillis(); 		
		int msek = (int) (curTime2 - curTime1);
		System.out.println("Выполнено за "+msek+" мс");
	}
}
