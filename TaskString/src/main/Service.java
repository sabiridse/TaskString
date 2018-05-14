package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import pojo.RowOfList;

public class Service {
	
	private static List<RowOfList> listRemover = new ArrayList<>();
	public static Set<RowOfList> checkDoubleList = new HashSet<>();
	static int iter = 1;
	
	public static List<RowOfList> groupList(List<RowOfList> inputList,
				  Function<RowOfList,List<RowOfList>> func1,
				  Function<RowOfList,List<RowOfList>> func2){

			Set<RowOfList> subList = new HashSet<>();//***чтоб без дублей
			listRemover.clear();
				
			subList.addAll(inputList);
			inputList.stream()
			.forEachOrdered(row -> { try{
									subList.addAll(func1.apply(row));
									checkDoubleList.addAll(subList);
								} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
							  try{
									subList.addAll(func2.apply(row));
									checkDoubleList.addAll(subList);
								} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
							});	
			listRemover = subList.stream().collect(Collectors.toList());
			return listRemover;
	}

	
	public static List<List<RowOfList>> addNewList (List<List<RowOfList>> value, List<RowOfList> list){
		value.add(list);
		return value;
	}
	
	public static List<List<RowOfList>> addNewListOfList (List<RowOfList> list){	 
		 List<List<RowOfList>> listOfList = new ArrayList<>();
		 listOfList.add(list);
		 return listOfList;
	}

	public static List<RowOfList> getListRemover() {
		return listRemover;
	}
}
