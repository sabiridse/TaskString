package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import pojo.RowOfList;

public class Service {
	public static Set<RowOfList> checkDoubleList = new HashSet<>();
	
	
	public static Set<RowOfList> groupList(Set<RowOfList> inputList,
				  Function<RowOfList,Set<RowOfList>> func1,
				  Function<RowOfList,Set<RowOfList>> func2){

			Set<RowOfList> subList = new HashSet<>();//***чтоб без дублей
			
			subList.addAll(inputList);
			inputList.stream()
			.forEach(row -> { try{
									subList.addAll(func1.apply(row));
									checkDoubleList.addAll(subList);
								} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
							  try{
									subList.addAll(func2.apply(row));
									checkDoubleList.addAll(subList);
								} catch(Exception e){};//***если елемент "" или нет совпадений будет NULL и ничего не добавиться
							});	
			
			return subList.stream().collect(Collectors.toSet());
	}
	
	
	public static List<Set<RowOfList>> addNewList (List<Set<RowOfList>> value, Set<RowOfList> list){
		value.add(list);
		return value;
	}
	
	public static List<Set<RowOfList>> addNewListOfList (Set<RowOfList> list){	 
		 List<Set<RowOfList>> listOfList = new ArrayList<>();
		 listOfList.add(list);
		 return listOfList;
	}
}
