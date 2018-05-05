package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import pojo.RowOfList;

public class Grouping {
	private int i=1;
	public List<String> outPutList = new ArrayList<>();	
	private Map<String, List<RowOfList>> first = new HashMap<String, List<RowOfList>>();
	private Map<String, List<RowOfList>> second = new HashMap<String, List<RowOfList>>();
	private Map<String, List<RowOfList>> third = new HashMap<String, List<RowOfList>>();
	private Map<Integer, List<List<RowOfList>>> finalMap = new HashMap<Integer, List<List<RowOfList>>>();
		
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
									 								  row.forEach(el -> outPutList.add(el.toString()));
									 								  i++;
								 								 });
								 
								});	
		 //****вывожу в файл
			try {
				new FileByTask().writeCsv(outPutList);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
		
	private void putFinalMap(List<RowOfList> list){		
		if (finalMap.computeIfPresent(list.size(), (key, value) -> Service.addNewList(value, list))==null)//**ADD к value*если еще нет ключа
			finalMap.put(list.size(), Service.addNewListOfList(list));											//**ПУТ в карту
	}
		
	 private void sortingMap(Consumer<Entry<Integer, List<List<RowOfList>>>> cons ){	 
		 finalMap.entrySet().stream()
		 .sorted(Map.Entry.<Integer,List<List<RowOfList>>>comparingByKey().reversed())//***по уменьшению размера
		 .forEach(cons);
	 } 
}
