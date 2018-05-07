package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import pojo.RowOfList;

public class Grouping {
	private int i=1;
	public List<String> outPutList = new ArrayList<>();
	
	public List<RowOfList> outList = new ArrayList<>();
	public Set<RowOfList> outSet = new HashSet<>();
	
	private Map<String, Set<RowOfList>> first = new HashMap<String, Set<RowOfList>>();
	private Map<String, Set<RowOfList>> second = new HashMap<String, Set<RowOfList>>();
	private Map<String, Set<RowOfList>> third = new HashMap<String, Set<RowOfList>>();
	private Map<Integer, List<Set<RowOfList>>> finalMap = new HashMap<Integer, List<Set<RowOfList>>>();
		
	public Grouping() {
		FileByTask readF = new FileByTask();		  
		try {
			readF.readCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//***первичная группировка коллекции по одному из элементов*************
		
		 first = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getFirstElement().isEmpty())
					.collect(Collectors.groupingBy(RowOfList::getFirstElement));
		 
		 second = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getSecondElement().isEmpty())
					.collect(Collectors.groupingBy(RowOfList::getSecondElement));
		 
		 third = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getThirdElement().isEmpty())
					.collect(Collectors.groupingBy(RowOfList::getThirdElement));

		 
		 //***группировка коллекций по всем элементам, результаты в MAP по размерам элементов групп
		
		 first.values().stream()
		.filter(rol -> rol.size()>1)
		.forEach(row -> this.putFinalMap(Service.groupList(row,
														param1 -> second.remove(param1.getSecondElement()),
														param2 -> third.remove(param2.getThirdElement()))));
		 
		 Service.checkDoubleList.stream()
		 			.forEach(row -> first.remove(row.getFirstElement()));
		 
		second.values().stream()
		.filter(rol -> rol.size()>1)
		.forEach(row -> this.putFinalMap(Service.groupList(row,
														param1 -> first.remove(param1.getFirstElement()),
														param2 -> third.remove(param2.getThirdElement()))));

		
		third.values().stream()
		.filter(rol -> rol.size()>1)
		.forEach(row -> this.putFinalMap(Service.groupList(row,
														param1 -> first.remove(param1.getFirstElement()),
														param2 -> second.remove(param2.getSecondElement()))));
		 

		
		
		 //****готовлю список для вывода************************
		 this.sortingMap(group -> outPutList.add(group.getValue().size()+" групп с количеством элементов = "+group.getKey()));
		 this.sortingMap(group ->{							 
								 group.getValue().forEach(row -> {	
									 								  outPutList.add("ГРУППА "+i);
									 								  row.forEach(el -> {outPutList.add(el.toString());
									 								  						outList.add(el);					
									 								  					}
									 										  	);
									 								  i++;
								 								 });
								 
								});	
		 
		 outSet.addAll(outList);		 
		 System.out.println("Всего групп: "+i+" размер List "+outList.size()+" размер Set "+outSet.size());
		 
		 
		 //****вывожу в файл
			try {
				new FileByTask().writeCsv(outPutList);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
		
	private void putFinalMap(Set<RowOfList> list){		
		if (finalMap.computeIfPresent(list.size(), (key, value) -> Service.addNewList(value, list))==null)//**ADD к value*,если еще нет ключа
			finalMap.put(list.size(), Service.addNewListOfList(list));											//**ПУТ в карту
	}
		
	 private void sortingMap(Consumer<Entry<Integer, List<Set<RowOfList>>>> cons ){	 
		 finalMap.entrySet().stream()
		 .sorted(Map.Entry.<Integer,List<Set<RowOfList>>>comparingByKey().reversed())//***по уменьшению размера
		 .forEach(cons);
	 } 
}
