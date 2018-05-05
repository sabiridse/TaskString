package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import pojo.RowOfList;

public class FileByTask {

	public static Set<RowOfList> setOfRow = new HashSet<RowOfList>();
			
		public void readCsv() throws IOException{	
			
			CSVReader reader = new CSVReader(new FileReader("src/resources/lng-big.csv"), ';','\t',0);						
			String[] record = null;
			while ((record = reader.readNext()) != null) {
				if(record.length>2){
					RowOfList row = new RowOfList(this.replaceChar(record[0]),
												  this.replaceChar(record[1]),
												  this.replaceChar(record[2]));				
							setOfRow.add(row);
				}				
			}				
			reader.close();

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
