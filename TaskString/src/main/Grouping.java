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
		this.addGroup(RowOfList -> !RowOfList.getFirstElement().isEmpty(), RowOfList::getFirstElement);	
		this.addGroup(RowOfList -> !RowOfList.getSecondElement().isEmpty(), RowOfList::getSecondElement);	
		this.addGroup(RowOfList -> !RowOfList.getThirdElement().isEmpty(), RowOfList::getThirdElement);

		queAll.addAll(setAfterGrouping);//********коллекция БЕЗ одиночных строк		
		FileByTask.setOfRow.removeAll(queAll);
	}
  private void addGroup(Predicate<? super RowOfList> pred,Function<RowOfList,String> func){
	  Map<String, List<RowOfList>> grouping = FileByTask.setOfRow.stream()
				.filter(pred)												//***группировка
				.collect(Collectors.groupingBy(func));
				
	  grouping.entrySet().stream()
				.filter(list -> list.getValue().size()>1)					
				.forEach(row -> setAfterGrouping.addAll(row.getValue()));
				
				grouping.clear();

  }

  
  
	
}
