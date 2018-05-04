package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Set;
import java.util.stream.Collectors;

import pojos.RowOfList;

public class GroupTest {
	private int i=1;
	public List<String> outPutList = new ArrayList<>();	
	private Map<String, List<RowOfList>> first = new HashMap<String, List<RowOfList>>();
	private Map<String, List<RowOfList>> second = new HashMap<String, List<RowOfList>>();
	private Map<String, List<RowOfList>> third = new HashMap<String, List<RowOfList>>();
	private Map<Integer, List<List<RowOfList>>> finalMap = new HashMap<Integer, List<List<RowOfList>>>();
		
	public GroupTest() {
		FileByTask readF = new FileByTask();		  
		try {
			readF.readCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		 first = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getFirstElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getFirstElement));
		 
		 second = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getSecondElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getSecondElement));
		 
		 third = FileByTask.setOfRow.stream()
					.filter(RowOfList -> !RowOfList.getThirdElement().isEmpty())												//***группировка
					.collect(Collectors.groupingBy(RowOfList::getThirdElement));
		 
		 long curTime1 = System.nanoTime();
		 
		 
//		System.out.println("Размер first "+first.size());
//		first.entrySet().forEach(System.out::println);
//		System.out.println("-------------------------------------");
//		
//		System.out.println("Размер second "+second.size());
//		second.entrySet().forEach(System.out::println);
//		System.out.println("-------------------------------------");
//		
//		System.out.println("Размер third "+third.size());
//		third.entrySet().forEach(System.out::println);
//		System.out.println("-------------------------------------");

			
		
		first.values().stream()
				//.limit(100)
				.filter(rol -> rol.size()>1)
				.forEach(row -> this.putFinalMap(this.groupList(row)));
		
		second.values().stream()
		//.limit(100)
		.filter(rol -> rol.size()>1)
		.forEach(row -> this.putFinalMap(this.groupListSecond(row)));
		
		third.values().stream()
		//.limit(100)
		.filter(rol -> rol.size()>1)
		.forEach(row -> this.putFinalMap(this.groupListThird(row)));
		
		
				
		 System.out.println("размер карты "+finalMap.size());
		 
		 //finalMap.entrySet().forEach(System.out::println);
		
		 first.clear();
		 second.clear();
		 third.clear();

		 this.sortingMap(group -> outPutList.add(group.getValue().size()+" групп с количеством элементов = "+group.getKey()));
			this.sortingMap(group ->{							 
								 group.getValue().forEach(row -> {	
									 								  outPutList.add("ГРУППА "+i);
									 								  row.forEach(el -> outPutList.add(el.toString()));
									 								  i++;
								 								 });
								 
								});	

			try {
				new FileByTask().writeCsv(outPutList);//****вывожу в файл
			} catch (IOException e) {
				e.printStackTrace();
			}
		 long curTime2 = System.nanoTime(); 		
		 int msek = (int) ((curTime2 - curTime1)/1000);
		 System.out.println("выполнено за "+msek+" микросек");
		 System.out.println("Размер second "+second.size());
		 System.out.println("Размер third "+third.size());
		 
	}

	
	private List<List<RowOfList>> addNewList (List<List<RowOfList>> value, List<RowOfList> list){
		value.add(list);
		return value;
	}
	
	private void putFinalMap(List<RowOfList> list){
		
		if (finalMap.computeIfPresent(list.size(), (key, value) -> this.addNewList(value, list))==null)//**ADD к value*если еще нет ключа
			finalMap.put(list.size(), addNewListOfList(list));											//**ПУТ в карту
	}
	
	private List<RowOfList> groupList(List<RowOfList> inputList){
		Set<RowOfList> subList = new HashSet<>();//***чтоб без дублей
		
		subList.addAll(inputList);
		inputList.stream()
		.forEach(row -> { try{
							subList.addAll(second.remove(row.getSecondElement()));
							} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						  try{
							subList.addAll(third.remove(row.getThirdElement()));
							} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						});
		
		return subList.stream().collect(Collectors.toList());
	}
	private List<RowOfList> groupListSecond(List<RowOfList> inputList){
		Set<RowOfList> subList = new HashSet<>();//***чтоб без дублей
		
		subList.addAll(inputList);
		inputList.stream()
		.forEach(row -> { try{
							subList.addAll(first.remove(row.getFirstElement()));
							} catch(Exception e){}//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						  try{
							subList.addAll(third.remove(row.getThirdElement()));
							} catch(Exception e){}//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						});
		
		return subList.stream().collect(Collectors.toList());
	}
	
	private List<RowOfList> groupListThird(List<RowOfList> inputList){
		Set<RowOfList> subList = new HashSet<>();//***чтоб без дублей
		
		subList.addAll(inputList);
		inputList.stream()
		.forEach(row -> { try{
							subList.addAll(first.remove(row.getFirstElement()));
							} catch(Exception e){}//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						  try{
							subList.addAll(second.remove(row.getSecondElement()));
							} catch(Exception e){}//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
						});
		
		return subList.stream().collect(Collectors.toList());
	}

 private List<List<RowOfList>> addNewListOfList (List<RowOfList> list){	 
	 List<List<RowOfList>> listOfList = new ArrayList<>();
	 listOfList.add(list);
	 return listOfList;
 }
 
 private void sortingMap(Consumer<Entry<Integer, List<List<RowOfList>>>> cons ){
	 
	 finalMap.entrySet().stream()
		.sorted(Map.Entry.<Integer,List<List<RowOfList>>>comparingByKey().reversed())//***по уменьшению размера
		.forEach(cons);

 }
 
}
