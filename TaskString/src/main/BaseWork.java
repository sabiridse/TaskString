package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.function.Consumer;
import pojos.RowOfList;

public class BaseWork {
	
	private int i=1;
	private static Grouping group = new Grouping();	
	private List<RowOfList> result = new ArrayList<>();	
	public List<String> outPutList = new ArrayList<>();	
	private Queue<RowOfList> queue = new LinkedList<RowOfList>();
	private Map<Integer,List<List<RowOfList>>> cheetMap = new HashMap<>();
	
	int iter = 1;
	
	private void grouping(RowOfList rowOfL){
		long curTime1 = System.currentTimeMillis(); 
		result.clear();	
		//***группирую элементы
		group.queAll.parallelStream()
			    .filter(row -> row.toGroup(rowOfL))
			    .forEach(row -> result.add(row));
		
		long curTime8 = System.currentTimeMillis(); 		
		int msek9 = (int) (curTime8 - curTime1);
		System.out.println("одна группировка "+msek9);
		
		
		
		this.delDeepQueue(result);//*****удалил из очереди всё что сгруппировалось чтоб не бегать вновь								  
		
		queue.addAll(result);///***служебная очередь для расширения группы по другим столбцам	
		while(!queue.isEmpty()) {
			this.second(queue.remove());//***группирую вторые и последующие элементы		
		}	
		//***создаю карту: если ключ новый - создаётся, если ключ уже есть - в значения добавляется группа
		int resultSize = result.size();

		
		if(resultSize>1){
			List<RowOfList> res = new ArrayList<>();
			res.addAll(result);		
			if (cheetMap.containsKey(resultSize)){                       //******быстро
				cheetMap.get(resultSize).add(res);
			}else {
				List<List<RowOfList>> newList = new ArrayList<>();
				newList.add(res);
				cheetMap.put(resultSize,newList);
			}			
		}	
		
		long curTime2 = System.currentTimeMillis(); 		
		int msek = (int) (curTime2 - curTime1);
		System.out.println("ИТЕРАЦИЯ "+iter+" выполнеа за : "+msek+" мс");
		System.out.println("--------------------------------");
		iter++;
	}
	
	public void addDataForOutPut(){				
		
		System.out.println("РАЗМЕР рабочей очереди "+group.queAll.size());


		while(!group.queAll.isEmpty()) {
			grouping(group.queAll.peek());//***прохожу очередь "группируемых" строк (одиночные уже не нужны)
		}		
		//****готовлю LIST для вывода в файл
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
	}
	 private void sortingMap(Consumer<Entry<Integer, List<List<RowOfList>>>> cons ){
		 
		 cheetMap.entrySet().parallelStream()
			.sorted(Map.Entry.<Integer,List<List<RowOfList>>>comparingByKey().reversed())//***по уменьшению размера
			.forEach(cons);

	 }
	
	private void delDeepQueue(List<RowOfList> delList){
		long curTime14 = System.currentTimeMillis(); 
		
		
		List<RowOfList> changeList = new ArrayList<>();
		changeList.addAll(group.queAll);
		group.queAll.clear();
		delList.forEach(row -> changeList.remove(changeList.indexOf(row)));
		group.queAll.addAll(changeList);
		changeList.clear();
		
		long curTime15 = System.currentTimeMillis(); 		
		int msek15 = (int) (curTime15 - curTime14);
		System.out.println("одно удаление из глубиныы "+msek15);
	}
	
	private void second(RowOfList rowOf){
		long curTime11 = System.currentTimeMillis();
		
		List<RowOfList> subresult = new ArrayList<>();
		group.queAll.parallelStream()
			    .filter(row -> row.toGroup(rowOf))
			    .forEachOrdered(row -> subresult.add(row));
		this.delDeepQueue(subresult);//*****удалил из очереди всё что сгруппировалось				
		if(subresult.size()>0) {	
			result.addAll(subresult);
			queue.addAll(subresult);
		}
		
		long curTime12 = System.currentTimeMillis(); 		
		int msek11 = (int) (curTime12 - curTime11);
		System.out.println("один sekond "+msek11);
	}	
}
