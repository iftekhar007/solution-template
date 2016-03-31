package com.tigerit.exam;

import static com.tigerit.exam.IO.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * All of your application logic should be placed inside this class. Remember we
 * will load your application from our custom container. You may add private
 * method inside this class but, make sure your application's execution points
 * start from inside run method.
 */
public class Solution implements Runnable {
	@Override
	public void run() {
		// your application entry point

		// sample input process
		// /String string = readLine();
		
		
		Integer noOfTest = readLineAsInteger();
		
		for (int i = 1; i <= noOfTest; i++) 
		{

			Map<String, Integer[][]> tableMartix = new HashMap<String, Integer[][]>();
			Map<String, String[]> columnsArray = new LinkedHashMap<String, String[]>();
			List<String[]> queryArray = new ArrayList<String[]>();
			Integer noOfTable = readLineAsInteger();
			
			for (int n = 0; n < noOfTable; n++) 
			{				
				String tableName=readLine();
				String[] columnRow=readLine().split("\\s+");
				int no_of_column = Integer.valueOf(columnRow[0]);
				int no_of_row=Integer.valueOf(columnRow[1]);				
				columnsArray.put(tableName, readLine().split("\\s+"));
				Integer[][] temp = new Integer[no_of_row][no_of_column];
				for (int j = 0; j < no_of_row; j++) {
						String[] row = readLine().split("\\s+");
						for (int k = 0; k < row.length; k++) {
							temp[j][k] = Integer.valueOf(row[k]);
						}
				}				
				tableMartix.put(tableName, temp);
			}
			Integer noOfQuery = readLineAsInteger();
			
			
			for (int j = 0; j < noOfQuery; j++) {
				String[] queryLine = new String[4];
				for (int k = 0; k < 4; k++) {
					queryLine[k] = readLine();
				}
				 queryArray.add(queryLine);
				 readLine();
			}
			
			//Processing start......
			
			printLine("Test: " + i );		
			for (String []query : queryArray) 
			{				
				String onClause = query[3].substring(3, query[3].length());
				String selectCaluse = query[0];
				String fromAlias = "", fromTable="", joinAlias = "", joinTable="";
				String[] fromSection = query[1].split("\\s+");				
				if(fromSection.length>2){
					fromAlias = fromSection[2].trim();
					fromTable = fromSection[1].trim();
				}
				String[] joinSection = query[2].split("\\s+");
				if(joinSection.length>2){
					joinAlias = joinSection[2].trim();
					joinTable = joinSection[1].trim();
				}
				if(!fromAlias.equals("")){
					Integer[][] f = tableMartix.get(fromTable);
					tableMartix.remove(fromTable);
					if(f !=null)
						tableMartix.put(fromAlias, f);
					String[] c = columnsArray.get(fromTable);
					columnsArray.remove(fromTable);
					if(c !=null) 
						columnsArray.put(fromAlias, c);
				}
				if(!joinAlias.equals("")){
					Integer[][] f = tableMartix.get(joinTable);
					tableMartix.remove(joinTable);
					if(f !=null)
						tableMartix.put(joinAlias, f);
					String[] c = columnsArray.get(joinTable);
					columnsArray.remove(joinTable);
					if(c !=null) 
						columnsArray.put(joinAlias, c);
				}
				
				//printLine(onClause);
				String[] onClauseArray = onClause.split("\\=");
				String[] onClauseLeft = onClauseArray[0].split("\\.");
				String[] onClauseRight = onClauseArray[1].split("\\.");
				String leftTableKey = onClauseLeft[0].trim();
				String rightTableKey = onClauseRight[0].trim();
				String[] leftColumnArray = columnsArray.get(leftTableKey);
				String[] rightColumnArray = columnsArray.get(rightTableKey);			
				String leftTableIndex = onClauseLeft[1].trim();
				String rightTableIndex = onClauseRight[1].trim();
				int leftTableIndexI = returnIndex(leftColumnArray, leftTableIndex);
				int rightTableIndexI = returnIndex(rightColumnArray, rightTableIndex);
				
				Integer[][] leftTable = tableMartix.get(leftTableKey);
				Integer[][] rightTable = tableMartix.get(rightTableKey);
				//System.out.println(selectCaluse);
				String[] selectedColumn = selectCaluse.substring(7,selectCaluse.length()).split("\\,"); 	
				if(selectedColumn[0].trim().equals("*")){
					for (Map.Entry<String, String[]> entry : columnsArray.entrySet()) {
						for (String string : (String[])entry.getValue()) {
							System.out.print(string+" ");
						}
					}
				}else{
					for (String string : selectedColumn) {
						String[] s = string.split("\\.");
						System.out.print(s.length>1 ? s[1].trim()+" " : string+" ") ;
					}
				}
				System.out.println();
				
				for (int row = 0; row < leftTable.length; row++) {
					if(leftTable[row][leftTableIndexI] == rightTable[row][rightTableIndexI]){
						printRow(leftTable, rightTable, row, selectedColumn, columnsArray,tableMartix);
					}
				}
				System.out.println();
			}
		}

		// sample output process
		// printLine(string);

	}

	private void printRow(Integer[][] leftTable, Integer[][] rightTable, int row, String[] selectedColumn, Map<String, String[]> columnsArray, Map<String, Integer[][]> tableMartix) {
		if(selectedColumn[0].trim().equals("*")){
			for (int i = 0; i < 3; i++) {
				System.out.print(leftTable[row][i]+" ");
			}
			for (int i = 0; i < 3; i++) {
				System.out.print(rightTable[row][i]+" ");
			}
		}else{
			for (String col : selectedColumn)
			{
				String[] cols = col.split("\\.");
				String tableKey  = cols[0].trim(); //ta
				String column = cols[1].trim(); // a1
				String[] originalColumn = columnsArray.get(tableKey); //1
				int index = returnIndex(originalColumn, column);
				
				Integer[][] p = tableMartix.get(tableKey);
				System.out.print(p[row][index] +" ");
			}
		}
		System.out.print("\n");	
	}
	
	private int returnIndex(String[] columnArray, String tableIndex){
		int leftTableIndexI = 0;
		for (int j = 0; j < columnArray.length; j++) {
			if(columnArray[j].equals(tableIndex)){
				leftTableIndexI = j;break;				
			}	
		} 
		return leftTableIndexI;
	}
}
