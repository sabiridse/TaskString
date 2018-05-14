package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import pojo.RowOfList;

public class Grouping {
	private int i=1;
	
	private List<String> outPutList = new ArrayList<>();

	private Set<RowOfList> allEl = new HashSet<>();
	
	private ConcurrentMap<String, List<RowOfList>> firstConcur = new ConcurrentHashMap<String, List<RowOfList>>();
	private ConcurrentMap<String, List<RowOfList>> secondConcur = new ConcurrentHashMap<String, List<RowOfList>>();
	private ConcurrentMap<String, List<RowOfList>> thirdConcur = new ConcurrentHashMap<String, List<RowOfList>>();
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
		 firstConcur.putAll(first);// Concur для удаления при итерации
		 
		 
		 second = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getSecondElement().isEmpty())
					.collect(Collectors.groupingBy(RowOfList::getSecondElement));
		 secondConcur.putAll(second);// Concur для удаления при итерации
		 
		 third = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getThirdElement().isEmpty())
					.collect(Collectors.groupingBy(RowOfList::getThirdElement));		 
		 thirdConcur.putAll(third);// Concur для удаления при итерации
		 
		 //***группировка коллекций по всем элементам, результаты в MAP по размерам элементов групп
		
		 firstConcur.values().stream()
		.filter(rol -> rol.size()>1)
		.forEachOrdered(row -> {
								this.putFinalMap(Service.groupList(row,
														param1 -> secondConcur.remove(param1.getSecondElement()),
														param2 -> thirdConcur.remove(param2.getThirdElement())));
								this.remove();	
								});

		secondConcur.values().stream()
		.filter(rol -> rol.size()>1)
		.forEachOrdered(row -> {this.putFinalMap(Service.groupList(row,
														param1 -> firstConcur.remove(param1.getFirstElement()),
														param2 -> thirdConcur.remove(param2.getThirdElement())));
								this.remove();
								});
		
		thirdConcur.values().stream()
		.filter(rol -> rol.size()>1)
		.forEachOrdered(row -> {this.putFinalMap(Service.groupList(row,
														param1 -> firstConcur.remove(param1.getFirstElement()),
														param2 -> secondConcur.remove(param2.getSecondElement())));
								this.remove();
								});
		 	
		 //****готовлю список для вывода************************
		
		 this.sortingMap(group ->outPutList.add(group.getValue().size()+" групп с количеством элементов = "+group.getKey()));
		 this.sortingMap(group ->{							 
								 	group.getValue().forEach(row -> {	
									 								  outPutList.add("ГРУППА "+i);
									 								  row.forEach(el -> outPutList.add(el.toString()));
									 								  i++;
								 								    });								 
								});	
		 
		 FileByTask.setOfRow.removeAll(allEl);		 
		 System.out.println("Всего не одинарных групп: "+(i-1));
		 System.out.println("Одинарных элементов : "+FileByTask.setOfRow.size());		 
		 System.out.println("Всего элементов: "+(allEl.size()+FileByTask.setOfRow.size()));
		 
		 //****вывожу в файл
			try {
				new FileByTask().writeCsv(outPutList);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	private void remove() {
		allEl.addAll(Service.getListRemover());
		Service.getListRemover().stream()
					.forEach(el -> {
									firstConcur.remove(el.getFirstElement());
									secondConcur.remove(el.getSecondElement());
									thirdConcur.remove(el.getThirdElement());
									});
	}
		
	private void putFinalMap(List<RowOfList> list){
		
		//чтоб при удалении из SET в MAP оставалось
		List<RowOfList> forRemoveList = new ArrayList<>();
		forRemoveList.addAll(list);
		
		//**ADD к value*,если еще нет ключа ПУТ в карту
		if (finalMap.computeIfPresent(forRemoveList.size(), (key, value) -> Service.addNewList(value, forRemoveList))==null)
			finalMap.put(forRemoveList.size(), Service.addNewListOfList(forRemoveList));											
	}
		
	 private void sortingMap(Consumer<Entry<Integer, List<List<RowOfList>>>> cons ){	 
		 finalMap.entrySet().stream()
		 .sorted(Map.Entry.<Integer,List<List<RowOfList>>>comparingByKey().reversed())//***по уменьшению размера
		 .forEach(cons);
	 } 
	 
}
