/**
 * 
 */
package com.zoubworld.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow;


/**
 * @author Pierre Valleau
 *
 */
public class ExcelArray {

	
	List<String> header;
	List<List<String>> data;
	String filename="";
	
	public List<String> getHeader() {
		if (header==null)
			header=new ArrayList<String>();
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}
	public void setHeader(String myheader[]) {
		for(String h:myheader)
		getHeader().add(h) ;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<List<String>> getData() {
		if (data==null)
			data=new ArrayList<List<String>>();
		return data;
	}

	/**
	 * 
	 */
	public ExcelArray() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	String separator=",";
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String toString()
	{
		String flow="";
		{
		String line="";
		for(String s:getHeader())
			if(s.contains(separator))
				line+="\""+s+"\""+separator;
			else
			line+=s+separator;
		flow+=line+"\r\n";
	}
		for(List<String> lines:getData())
		{
			String line="";
				for(String s:lines)
					if((s!=null)&& s.contains(separator))
						line+="\""+s+"\""+separator;
					else
					line+=s+separator;
				
				
			
			flow+=line.replaceAll("null", "")+"\r\n";
		}
		return flow;
	}
	public void save() {
		String flow=toString();
		JavaUtils.saveAs(filename, flow);

		
	}

	public int newrow() {
		List<String> lines=new ArrayList<String>();
		for(String s:getHeader())
		lines.add(null);
		getData().add(lines);
		return getData().indexOf(lines);
	}

	public void setCell(int row, String colunm, String replace) {
	/*	if (!getHeader().contains(colunm))
			return;*/
		int icolunm=getHeader().indexOf(colunm);
		if (icolunm<0)
			icolunm=addColumn(colunm);
		setCell( row,  icolunm,  replace);
		
		
	}
	/** return all cell value
	 * */
	public List<String> getColunm(String colunm )
	{
		List<String> l=new ArrayList();
		for(int i=0;i<=this.rowMax();i++)
			if(getCell(i,  colunm )!=null)
			l.add(getCell(i,  colunm ));
		return l;
	}
	public String getCell(int row, String colunm ) {
	/*	if (!getHeader().contains(colunm))
			return;*/
		int icolunm=getHeader().indexOf(colunm);
		
		return getCell( row,  icolunm);
		
		
	}
	public String getCell(int row, int colunm) {
		if (row<0)
			return null;
		if (colunm<0)
			return null;
		
		if (getData()==null)
			return null;
		if (getData().get(row)==null)
			return null;
		if (getData().get(row).size()<=colunm)
			return null;

		if (getData().get(row).get(colunm)==null)
			return null;

		return getData().get(row).get(colunm).toString();
		
	}
	public void setCell(int row, int colunm, String replace) {
		
		
		while (getData().size()<=row)
			newrow();
		while (getData().get(row).size()<=colunm)
			getData().get(row).add(null);
		getData().get(row).remove(colunm);
		getData().get(row).add(colunm,replace);
		
		
	}

	public int rowMax() {
		
		return getData().size()-1;
	}

	public void read(String filename2, String sheetname) throws EncryptedDocumentException, InvalidFormatException, IOException {
		{
			filename=filename2;
	        // Creating a Workbook from an Excel file (.xls or .xlsx)
			Workbook workbook = WorkbookFactory.create(new File(filename2));
	        // Retrieving the number of sheets in the Workbook
			System.out.println("\t- Read " + filename2 + " sheet '"+sheetname+"' as ExcelArray ");
//System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

	        /*
	           =============================================================
	           Iterating over all the sheets in the workbook (Multiple ways)
	           =============================================================
	        */

	        // 1. You can obtain a sheetIterator and iterate over it
	        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
	      //  System.out.println("Retrieving Sheets using Iterator");
	        while (sheetIterator.hasNext()) {
	            Sheet sheet = sheetIterator.next();
	    //        System.out.println("\t\t=> " + sheet.getSheetName());
	        }
	/*
	        // 2. Or you can use a for-each loop
	        System.out.println("Retrieving Sheets using for-each loop");
	        for(Sheet sheet: workbook) {
	            System.out.println("=> " + sheet.getSheetName());
	        }

	        // 3. Or you can use a Java 8 forEach with lambda
	        System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
	        workbook.forEach(sheet -> {
	            System.out.println("=> " + sheet.getSheetName());
	        });
	*/
	        /*
	           ==================================================================
	           Iterating over all the rows and columns in a Sheet (Multiple ways)
	           ==================================================================
	        */

	        // Getting the Sheet at index zero
	        Sheet sheet = workbook.getSheetAt(0);
	        sheet = workbook.getSheet(sheetname);
	       // sheet=workbook.getSheet("$tests");
	        // Create a DataFormatter to format and get each cell's value as String
	        DataFormatter dataFormatter = new DataFormatter();

	        // 2. Or you can use a for-each loop to iterate over the rows and columns
	    //    System.out.println("\n\nIterating over Rows and Columns \n");
	        int index=0;//index on list<list<string>>
	        int rowcount=0;// index on csv
	        Row rowheader=null;
	        if(sheet!=null)
	        	
	        for (Row row: sheet) {
	        	
	        	{
	        		if (rowcount==0)
	        			rowheader=row;
	        		index=newrow();
	        		int icolunm=0;// index in object
	        		int size=row.getLastCellNum();
	        		for(int  i=0;i<size;i++ ) //index in file
	        		{

	        			if(row.getCell(i)!=null)
	        			{
	        				String headerValue = "";	
	        				
	        				if (rowheader.getCell(i) != null) {
	        					
	        					try {
	        						headerValue = rowheader.getCell(i).getStringCellValue();//
	        					} catch (java.lang.IllegalStateException e) {
	        						headerValue = dataFormatter.formatCellValue(row.getCell(i));
	        						// TODO: handle exception
	        					}
		        				icolunm=getHeader().indexOf(headerValue);
			        				
		        				if (icolunm<0)
		        				{
		        					getHeader().add(headerValue);
		        				icolunm=getHeader().indexOf(headerValue);}
	        				}
	        				if (row.getCell(i) != null) {
	        					String cellValue = "";
	        					try {
	        						cellValue = row.getCell(i).getStringCellValue();//
	        					} catch (java.lang.IllegalStateException e) {
	        						cellValue = dataFormatter.formatCellValue(row.getCell(i));
	        						// TODO: handle exception
	        					}
	        					setCell(index, icolunm, cellValue);	        				
	        				}	
	        				//        System.out.print(cellValue + "\t");
	        				}
	        		}
	        	//	System.out.println(d.toString());
	        		if (rowcount==0) 
	        		{			
	        			if (header.isEmpty()) // should never happen
	        				header=getData().remove(index);
	        			else // merge
	        			{
	        				getData().remove(index);// remove the line merge already done in if (icolunm<0)
	        				
	        			}
	        		}
	        	}
	        	rowcount++;	
	        }
	        	

	        workbook.close();
	        }
		
	}
	public int addColumn(String columnTitle) {
		getHeader().add(columnTitle);
		return getHeader().indexOf(columnTitle);
		
	}
	public void addColumn(String[] columnsTitles) {
		for(String column:columnsTitles)
			 addColumn(column) ;		
	}
  // search cellValue in Column ColumnTitle, return the  row data
	public Integer findiRow(String ColumnTitle, String cellValue) {
		List<String> row=findRow( ColumnTitle,  cellValue);
		if(row!=null)
			return getData().indexOf(row);
		return -1;//		return null;
		
	}
	 // search cellValue in Column ColumnTitle, return the n� of the row
	public List<String> findRow(String ColumnTitle, String cellValue) {
		int icolunm=getHeader().indexOf(ColumnTitle);
		if (icolunm<0)
			return null;
		for(List<String> row:getData())
			if(row!=null)
				if(row.size()>icolunm)
					if(row.get(icolunm)!=null)
		if(row.get(icolunm).equals(cellValue))
			return row;
		return null;
		
	}
	public List<String> getRow(int irow)
	{
		if (getData().size()>irow)
		return getData().get(irow);
		return null;
		
	}

	public void flush() {
		getData().clear();
		filename="";
		getHeader().clear();
		
	}

	public Map<? extends String, ? extends String> RowtoMap(int indexrow) {
		 Map<String, String> map= new HashMap<String,String>();
		 for(int i=0;i<Math.min(getHeader().size(), getRow(indexrow).size());i++)
		 {
			String key=getHeader().get(i);
			String value=getCell(indexrow, i);
			if(key!=null)
				if(value!=null)
							
		 map.put(key.trim(), value.trim());
		 }
		return map;
	}

	public void sort(String colunm) {
		 List<String>  l=getColunm( colunm );
		 
		 List<String> l2=JavaUtils.asSortedList(l);
		 
		 List<List<String>> data2=new ArrayList<List<String>>();
		 for(String e2:l2)
		 {
			 
		
			 List<String> r= findRow(colunm, e2);
			 data2.add(r);
			 data.remove(r);
		 }
		 data2.addAll(data);
			 data=data2;
		
	}

}
