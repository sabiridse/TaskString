package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pojos.RowOfList;

public class GroupTest {

		
	public GroupTest() {
		FileByTask readF = new FileByTask();		  
		try {
			readF.readCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		 Map<String, List<RowOfList>> first = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getFirstElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getFirstElement));
		 
		 Map<String, List<RowOfList>> second = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getSecondElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getSecondElement));
		 
		 Map<String, List<RowOfList>> third = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getThirdElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getThirdElement));
		
		 this.otherTest(third);
		 this.otherTest(first);
		
		
	}
 private List<RowOfList> qwer (List<RowOfList> list){
	 list.add(new RowOfList("666.6","555.5","444.4"));
	 return list;
 }

 private void otherTest(Map<String, List<RowOfList>> map) {
	 
	 long curTime1 = System.currentTimeMillis();  
	 
	 map.compute("104731.1", (key, value) -> this.qwer(value));
		map.get("104731.1").forEach(System.out::println);
				
		long curTime2 = System.currentTimeMillis(); 		
		int msek = (int) (curTime2 - curTime1);
		System.out.println("заняло: "+msek);
		System.out.println("--------------------------------");
		
 }
 
 
}
