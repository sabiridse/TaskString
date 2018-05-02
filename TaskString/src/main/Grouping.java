package main;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pojos.RowOfList;

public class Grouping {

	private Set<RowOfList> setAfterGrouping = new HashSet<>();
	public Queue<RowOfList> queAll = new LinkedList<RowOfList>();
		
	public Grouping() {
		FileByTask readF = new FileByTask();		  
		try {
			readF.readCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		long curTime1 = System.currentTimeMillis(); 
		System.out.println("начал группировку");
		System.out.println("------------------------------------------");
		this.addGroup(RowOfList -> !RowOfList.getFirstElement().isEmpty(), RowOfList::getFirstElement);	
		
		long curTime2 = System.currentTimeMillis(); 		
		int msek = (int) (curTime2 - curTime1);
		
		System.out.println("FerstGroup: "+msek+" мс");
		
		long curTime3 = System.currentTimeMillis(); 
		System.out.println("------------------------------------------");
		this.addGroup(RowOfList -> !RowOfList.getSecondElement().isEmpty(), RowOfList::getSecondElement);	
		
		long curTime4 = System.currentTimeMillis(); 		
		int msek1 = (int) (curTime4 - curTime3);
		System.out.println("SecondGroup: "+msek1+" мс");
		
		long curTime5 = System.currentTimeMillis();
		System.out.println("------------------------------------------");
		this.addGroup(RowOfList -> !RowOfList.getThirdElement().isEmpty(), RowOfList::getThirdElement);

		long curTime6 = System.currentTimeMillis(); 		
		int msek2 = (int) (curTime6 - curTime5);
		System.out.println("ThirdGroup: "+msek2+" мс");
		
		System.out.println("------------------------------------------");
		queAll.addAll(setAfterGrouping);//********коллекция БЕЗ одиночных строк		
		
		System.out.println("без одиночных строк "+ queAll.size());
		FileByTask.setOfRow.removeAll(queAll);
	}
  private void addGroup(Predicate<? super RowOfList> pred,Function<RowOfList,String> func){
	  Map<String, List<RowOfList>> grouping = FileByTask.setOfRow.stream()
				.filter(pred)												//***группировка
				.collect(Collectors.groupingBy(func));

	  System.out.println("размер группы "+grouping.size());
	  
				grouping.entrySet().stream()
				.filter(list -> list.getValue().size()>1)					
				.forEach(row -> setAfterGrouping.addAll(row.getValue()));
				
				grouping.clear();

  }
	
}
