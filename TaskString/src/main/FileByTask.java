package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import pojos.RowOfList;

public class FileByTask {

	public static Set<RowOfList> setOfRow = new HashSet<RowOfList>();
			
		public void readCsv() throws IOException{	
			
			int qwe = 1;
			long curTime1 = System.currentTimeMillis(); 
			
			CSVReader reader = new CSVReader(new FileReader("src/resources/test.csv"), ';','\t',0);						
			String[] record = null;
			while ((record = reader.readNext()) != null) {
				if(record.length>2){
					RowOfList row = new RowOfList(this.replaceChar(record[0]),
												  this.replaceChar(record[1]),
												  this.replaceChar(record[2]));				
							setOfRow.add(row);
							qwe++;
				}				
			}				
			reader.close();
			
			long curTime2 = System.currentTimeMillis(); 		
			int msek = (int) (curTime2 - curTime1);
			System.out.println("чтение CSV: "+msek+" мс, размер SET "+setOfRow.size()+" число итераций "+qwe);
		}		
		public void writeCsv(List<String> list) throws IOException{
			CSVWriter writer = new CSVWriter(new FileWriter("src/resources/out.csv"),';','\u0000');
			list.stream()
				.forEachOrdered(row -> writer.writeNext(new String[] {row}));					
			writer.close();
		}
		private String replaceChar(String inPut){
			if(inPut.isEmpty())
				return "";
			return inPut.substring(1, inPut.length()-1);
	}
}
