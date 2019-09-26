/**
 * 
 */
package com.zoubworld.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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

	public ExcelArray(ExcelArray excelArray) {
		header=new ArrayList<>();
		header.addAll(excelArray.header);
		
		data=new ArrayList<>();
		for(List<String> row:excelArray.getData())
		{
			List<String> r=(new ArrayList<String>());
			r.addAll(row);
			data.add(r);
		}
		//data.addAll(excelArray.getData());// same row, not copy of data becafull.
		filename=excelArray.filename;
		separator=excelArray.separator;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExcelArray ea=new ExcelArray();
		String filename="C:\\Temp\\caratc_jules\\chara_log\\CharFor_osc_rc48mhz_FOSC_shadow_value.csv";
		//filename="C:\\Temp\\caratc_jules\\try1\\CharFor_osc_rc48mhz_FOSC_shadow_value_2019_06_24_18_46_10.csv";
		ea.read(filename);
		Map<String,ExcelArray> map=ea.split("osc_rc48mHz_mode");
		for(String k:map.keySet())
			map.get(k).saveAs(filename+k+".csv");
		List<String>  matchingCollunm = new ArrayList();
		matchingCollunm.add("Mask");
		matchingCollunm.add("PartNumber");
		matchingCollunm.add("Temperature");
		matchingCollunm.add("Corner");
		matchingCollunm.add("Site;");
		matchingCollunm.add("osc_rc48mhz_proc_trim_typ");
		matchingCollunm.add("osc_rc48mhz_proc_trim_typ_unit");
		matchingCollunm.add("osc_rc48mhz_temp_trim_mv_typ");
		ea=ea.transpose("osc_rc48mHz_mode", matchingCollunm, "osc_rc48mhz_FOSC_shadow_value");
		ea.deleteEmptyRow();
		ea.deleteEmptyColunm();
		
		ea.saveAs(filename+"transpose.csv");
		

	}
	
	public void saveAs(String thefilename) {
		setFilename(thefilename);
		save();		
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
		StringBuffer flow=new StringBuffer();
		{
		String line="";
		for(String s:getHeader())
			if(s.contains(separator))
				line+="\""+s+"\""+separator;
			else
			line+=s+separator;
		flow.append(line+"\r\n");
	}
		for(List<String> lines:getData())
		{
			String line="";
				for(String s:lines)
					if((s!=null)&& s.contains(separator))
						line+="\""+s+"\""+separator;
					else
					line+=s+separator;
				
				
			
				flow.append(line+"\r\n");
		}
		
		return flow.toString().replaceAll("null", "");
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
		return getData().size()-1;
	}

	public void setCell(int row, String colunm, String replace) {
	/*	if (!getHeader().contains(colunm))
			return;*/
		int icolunm=getHeader().indexOf(colunm);
		if (icolunm<0)
			icolunm=addColumn(colunm);
		setCell( row,  icolunm,  replace);
		
		
	}
	/** return all cell value, if null the data is skipped(becarefull not align on the row)
	 * */
	public List<String> getColunm(String colunm )
	{
		List<String> l=new ArrayList();
		int icolunm=getHeader().indexOf(colunm);
		int max=this.rowMax();
		for(int i=0;i<=max;i++)
		{
			String cell=getCell(i,  icolunm );
			if(cell!=null)
			l.add(cell);
			}
		return l;
	}
	/** like getColunm() but return a set instead of a list
	 * */
	public Set<String> getSetOfColunm(String colunm )
	{
		Set<String> l=new HashSet();
		int icolunm=getHeader().indexOf(colunm);
		int max=this.rowMax();
		for(int i=0;i<=max;i++)
		{
			String cell=getCell(i,  icolunm );
			if(cell!=null)
			l.add(cell);
			}
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
	/** split the ExcelArray into several ExcelArray, one by value on colunmName
	 *  *  example :
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3
	 *  1 | 0 | 0 |4
	 *  1 | 0 | 1 |5
	 *  1 | 1 | 0 |6
	 *  1 | 1 | 1 |7
	 *  become after split('a'')
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3
	 *   and
	 *  a | b | c |r 
	 *  1 | 0 | 0 |4
	 *  1 | 0 | 1 |5
	 *  1 | 1 | 0 |6
	 *  1 | 1 | 1 |7
	 *   
	 * */
	public Map<String,ExcelArray> split(String colunmName)
	{
		Map<String,ExcelArray> map=new HashMap();
		Set<String> keys=JavaUtilList.listToSet(getColunm(colunmName));
		for(String k:keys)
		{
			ExcelArray value=filter(colunmName,k);
			map.put(k, value);
		}
		return map;
		
	}
	/** filter the ExcelArray to keep row when colunmName is equal to k
	 *  *  example :
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3
	 *  1 | 0 | 0 |4
	 *  1 | 0 | 1 |5
	 *  1 | 1 | 0 |6
	 *  1 | 1 | 1 |7
	 *  become after filter('a','0')
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3	
	 *   
	 * */
	public ExcelArray filter(String colunmName, String k) {
		ExcelArray value=new ExcelArray(this);
		for(int irow=value.getData().size()-1;irow>=0;irow--)
		{
			String cell=value.getCell(irow, colunmName);
			if ((k==null) && (cell==null))
					value.getData().remove(irow);	
			else
			if((cell!=null) && cell.compareToIgnoreCase(k)!=0)
			value.getData().remove(irow);	
			}	
		return value;
	}
	/** this function transpose a ExcelArraycolunm into several colunm base on field colunmName,
	 *  one colunm by colunmName value.
	 *  and merge the result in taking in account matchingCollunm value.
	 *  example :
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3
	 *  1 | 0 | 0 |4
	 *  1 | 0 | 1 |5
	 *  1 | 1 | 0 |6
	 *  1 | 1 | 1 |7
	 *  become after transpose('a','b,c','r')
	 *   b | c |r[0]|r[1] 
	 *   0 | 0 |0   |  4
	 *   0 | 1 |1   |  5
	 *   1 | 0 |2   |  6
	 *   1 | 1 |3   |  7
	 *  
	 *  */
	public ExcelArray transpose(String colunmName,List<String> matchingCollunm,String ExcelArraycolunm)
	{
		 Map<String,ExcelArray> map=split( colunmName);
		 for(ExcelArray e:map.values())
			 e.rmColumn(colunmName);			 
		 ExcelArray ea=Merge( map, matchingCollunm,  ExcelArraycolunm);		 
		 return ea;
	}
	/** Merge a Map of ExcelArray in one ExcelArray, renaming ExcelArraycolunm into 'ExcelArraycolunm[map.key]'
	 * the row merge must have same values on colunm matchingCollunm
	 *  *  example :
	 * (0):
	 *  a | b | c |r 
	 *  0 | 0 | 0 |0
	 *  0 | 0 | 1 |1
	 *  0 | 1 | 0 |2
	 *  0 | 1 | 1 |3
	 *   and
	 *   (1):
	 *  a | b | c |r 
	 *  1 | 0 | 0 |4
	 *  1 | 0 | 1 |5
	 *  1 | 1 | 0 |6
	 *  1 | 1 | 1 |7
	 *   become after Merge(...,'b,c','r')
     *   b | c |r[0]|r[1] 
	 *   0 | 0 |0   |  4
	 *   0 | 1 |1   |  5
	 *   1 | 0 |2   |  6
	 *   1 | 1 |3   |  7
	 * */
	public ExcelArray Merge(Map<String,ExcelArray> map,List<String> matchingCollunm, String ExcelArraycolunm)
	{
		ExcelArray value=new ExcelArray();
		for(String k:map.keySet())
		{
			String newCol=ExcelArraycolunm+"["+k+"]";
			value.addColumn(newCol);
			ExcelArray ea=map.get(k);
			value.addColumn(ea.getHeader());
			for(List<String> row:ea.getData()) 
			{
				List<String> cellValue=new ArrayList();
				cellValue= ea.getValue(row,matchingCollunm);
			int irow= value.findiRow(matchingCollunm,cellValue);
				if(irow<0)
					irow=value.addRow(row,ea.getHeader());
				value.setCell(irow, newCol, ea.getValue(row,ExcelArraycolunm));
			}
		}
		value.rmColumn(ExcelArraycolunm);
		return value;
	}
	/** ReMove the Colunm excelArraycolunm
	 * */
	public void rmColumn(String excelArraycolunm) {
		 int icol=getHeader().indexOf(excelArraycolunm);
		 rmColumn( icol);
	}
	/** ReMove the Colunm excelArraycolunm
	 * */
	public void rmColumn(int icol) {
		 if(icol>=0)
		 {
		 getHeader().remove(icol);
		 for(List<String> row:getData())
		 {
			 row.remove(icol);
		 }}
		
	}

	/** return the list of data on the colunm's list colunms for the row row.
	  * */
	 public List<String> getValue(List<String> row, List<String> colunms) {
		 List<String> data=new ArrayList();
		 for(String excelArraycolunm:colunms)
		 data.add(getValue(row,  excelArraycolunm));
		return data;
	}
	 
	 /** return the data on colunm excelArraycolunm for row row.
	  * */
	public String getValue(List<String> row, String excelArraycolunm) {
		 
		 int icol=getHeader().indexOf(excelArraycolunm);
		 if (icol<0)
			 return null;
		return row.get(icol);
	}

		// search cellValue in Column ColumnTitle, return the  row data
		public Integer findiRow(List<String> ColumnTitle, List<String> cellValue) {
			List<Integer> ColumnIndex=new ArrayList();
			for(String title:ColumnTitle)
				ColumnIndex.add(getHeader().indexOf(title));
			for(int irow=0; irow<getData().size();irow++)
			{
				List<String> row=getData().get(irow);
				boolean match=true;
				int index=0;
				while(match & index<ColumnIndex.size())
				{
					if(ColumnIndex.get(index)>=0)
					{
						if (row.get(ColumnIndex.get(index))==null && null==cellValue.get(index))
						{}
						else
					if ((cellValue.get(index)!=null) && (!cellValue.get(index).equalsIgnoreCase(row.get(ColumnIndex.get(index)))))
						match=false;}
					index++;
				}
				if(match)
					return irow;
			}
				
	//		if(row!=null)
	//			return getData().indexOf(row);
			return -1;//		return null;
			
		}
	public void read(String filenameCsv)
	{
		if ((getFilename()==null)|| (getFilename().compareTo("")==0))
			setFilename(filenameCsv);
	CSVParser parser;
	try {
		parser = new CSVParser(new FileReader(filenameCsv), CSVFormat.DEFAULT);

	List<CSVRecord> list = parser.getRecords();
	int rowcount = 0;
	int rowcount2 = 0;
	for (CSVRecord record : list) {
	//    String[] arr = new String[record.size()];
	    int icolunm = 0;
	    for (String cellValue : record) {
	      
	        setCell(rowcount2, icolunm++, cellValue);	       
	    }
	   
	    if (rowcount==0) 
		{			
			if (header.isEmpty()) // should never happen
				{header=getData().remove(rowcount);rowcount2--;}
			else // merge
			{
				getData().remove(rowcount);rowcount2--;// remove the line merge already done in if (icolunm<0)
				
			}
		}
	    rowcount++;rowcount2++;
	}  
	parser.close();  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
	public void addColumn(List<String> columnTitlelist) {
		for(String columnTitle:columnTitlelist)
		{addColumn( columnTitle);}
	}
	public int addColumn(String columnTitle) {
		if(getHeader().indexOf(columnTitle)<0)
		{
			getHeader().add(columnTitle);
		}
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
/**
 * return the number of row added*/
	public int addFile(String fielname) {
		ExcelArray e2=new ExcelArray();
		e2.read(fielname);
		e2.deleteEmptyRow();
		if (getHeader().size()==0 || getHeader().toString().equalsIgnoreCase(e2.getHeader().toString()))
		{
			for(List<String> row: e2.getData())
				addRow(row);
		}
		else
		{
		//	for(List<String> row: e2.getData())
		return 0;		
		}
		return e2.getData().size();
		
	}
private boolean isempty(List<String> row)
{
	if(row==null || row.size()==0)
		return true;
	for(String s:row)
		if (s!=null && !s.trim().equalsIgnoreCase(""))			
			return false;
	return true;
}
	public void deleteEmptyRow() {
		List<List<String>> datatoDel=new ArrayList();
	for(List<String> row:getData())
		if(isempty(row))
			datatoDel.add(row);
	boolean t=getData().removeAll(datatoDel);
	
	for(List<String> e:datatoDel)
		getData().remove(e);
}
	
	public void deleteEmptyColunm() {
		List<String> datatoDel=new ArrayList();
		for(String h:getHeader())
			if ((h==null) || h.trim().equalsIgnoreCase(""))
				if(isempty(getColunm(h)))
				datatoDel.add(h);
		for(String h:datatoDel)
		rmColumn(h);
}

	/**
	 * return row index
	 * */
	public int addRow(List<String> row) {
		getData().add(row);
		return getData().size()-1;
		
	}
	/**
	 * return row index
	 * */
	public int addRow(List<String> row,List<String> header) {
		List<String> newrow=reformat( row,header);
		getData().add(newrow);
		return getData().size()-1;
		
	}

	/** reformat row from header2 format to local header format.
	 * */
	private List<String> reformat(List<String> row, List<String> header2) {
		List<String> newrow=new ArrayList();		
		for(String h:header)
		{
			int index=header2.indexOf(h);			
			String e=null;
			if (index>=0)
				e=row.get(index);			
			newrow.add(e);
		}
		return newrow;
	}

	public List<String>  getColunms() {
		
		return getHeader();
	}

	public void deleteColunm(String c) {
		rmColumn(c);
		
	}

	public void renameColumn(String oldname, String newname) {
		int icolunm=header.indexOf(oldname);
		if(icolunm>=0)
		{
		header.remove(icolunm);
		header.add(icolunm, newname);
		}
		else
			System.err.print("error Column "+oldname+" doesn't exist in "+getFilename());
	}

	public void moveColumn(String Columnname, int ilocationnewColumn) {
		
		renameColumn(Columnname, Columnname+"old87614321654681");
		 copyColunm(Columnname+"old87614321654681",ilocationnewColumn,Columnname) ; 
		 int icol=getHeader().indexOf(Columnname+"old87614321654681");
			rmColumn(icol);
			
		}
   /** copy columnname to location ilocation, with name newColumnname
    * */
	public void copyColunm(String columnname, int ilocation,String newColumnname) {
		 int icol=getHeader().indexOf(columnname);//old column
		 if(icol>=0)
		 {
		 getHeader().add(ilocation,newColumnname);
		 for(List<String> row:getData())
		 {
			 row.add(ilocation,row.get(icol)  );
		 }}
		
	
	}
}
