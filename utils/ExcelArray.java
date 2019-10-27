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
import org.apache.commons.collections4.Get;
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
		
		args=new String[5];
		/*
		args[0]="Merge";
		args[1]="outputfile=C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataT0FT1.csv";
		args[2]="inputfiles=["
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\T0FT1\\dataT0FT1.csv,"
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\T0FT1\\dataHBMRET0FT1.csv"
				+ "]";
		/*
		args[0]="Merge";
		args[1]="outputfile=C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataT0FT2.csv";
		args[2]="inputfiles=["
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\T0FT2\\dataT0FT2.csv,"
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\T0FT2\\dataHBMRET0FT2.csv"
				+ "]";
		/*
		args[0]="Merge";
		args[1]="outputfile=C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataFT2.csv";
		args[2]="inputfiles=["
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataCDMFT2R1.csv"
				+","+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataCDMFT2R0.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataHBMREFT2.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataHBMFT2R2.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataHBMFT2R1.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataHBMFT2R0.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT2\\dataCDMFT2R3.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\T0FT1\\dataHBMRET0FT1.csv"
				+","+""
				+ "]";
		/*
		args[0]="Merge";
		args[1]="outputfile=C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataFT1.csv";
		args[2]="inputfiles=["
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT1\\dataHBMREFT1.csv"
				+","+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT1\\dataHBMFT1R2.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT1\\dataHBMFT1R1.csv"
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT1\\dataHBMFT1R0.csv"				
				+","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\FT1\\dataHBMFT1R0.csv"
				+","+""
				+ "]";
				*/
		
		args[0]="Align";
		args[1]="outputfile=none.csv";
		args[2]="inputfiles=["
				+ "C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataT0FT2.csv"
				+ ","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataFT2.csv"
				+ "]";
		/*
		args[0]="Align";
		args[1]="outputfile=none.csv";
		args[2]="inputfiles=["
				+ ""+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataFT1.csv"
				+ ","+"C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Validation\\Qualifcation\\SG802-ESD_Latchup\\drift\\dataT0FT1.csv"
		+ "]";
		
		/**/
		
		args[3]="+Sort";
		args[4]="+Move";
		
		
		ExcelArray ea=new ExcelArray();
		ExcelArray ea2=new ExcelArray();
		
		//manage parameter
		  // declare
		  HashMap<String, String> optionparam = new HashMap<String,String>();
		  optionparam.put("inputfiles=[]","list of input files");
		  optionparam.put("keys=[WAFERNUM,X COORDINATE,Y COORDINATE]","the set of colunm that identify a row as unique ");
		  optionparam.put("-Sort","sort table based on keys");
		  optionparam.put("-Move","move collunm keys at begning");
		  optionparam.put("action","action to do : Merge,Diff,Sort\n"
		  		+ "		- Sort by keys inputfiles[0]\n"
		  		+ "		- Merge the list of input files\n"
		  		+ "		- Diff : do difference between inputfiles[0] and inputfiles[0], it compare row that have the same keys\n"
		  		+ "		- Align : order collunm on the fisrt file of ");
		  optionparam.put("outputfile=data.csv","the output file inputfiles");		  
		  ArgsParser arg=new ArgsParser(optionparam);
		  //manage
		  arg.parse(args);
		  arg.check();
		  if(args.length==0) arg.help();
		  	
		  String outputfile=arg.getParam("outputfile");
	 		
		  List<String> filenames=arg.getParamAsList("inputfiles");
		  List<String> keys=arg.getParamAsList("keys");
	 		String action=arg.getArgument(1);
	 		
	   // do the job
			
	 		
	 		if("Merge".equalsIgnoreCase(action))
	 		{
	 		for(String filenameCsv:filenames)
	 		ea.append(filenameCsv);
	 		}
	 		else if("Diff".equalsIgnoreCase(action))
	 		{
	 		String filenameCsv=filenames.get(0);
	 		ea.append(filenameCsv);
	 		filenameCsv=filenames.get(1);
	 		ea2.append(filenameCsv);
	 		/*
	 		List<List<String>> ll=new ArrayList();
	 		for(String key:keys)
	 			ll.add(JavaUtilList.setToList(ea.getSetOfColunm(key)));*/
	 		Set<List<String>> ll=ea2.getkeys(keys);
	 		for(List<String> l2:ll)
	 		{	
	 			List<List<String>> rows1=ea.findRows(l2, keys);
	 			List<List<String>> rows2=ea2.findRows(l2, keys);
	 		    if(rows1.size()!=rows2.size())
	 		    {
	 		    	//error
	 		    	}
	 		   if(rows1.size()==1)
	 		    {
	 			  compare(ea.getHeader(),rows1.get(0),ea2.getHeader(),rows2.get(0),1,0.1);
	 			   }
	 		    
	 		}
	 			
	 		}
	 		else if("Align".equalsIgnoreCase(action))
	 		{
	 		String filenameCsv1=filenames.remove(0);
	 		ea.append(filenameCsv1);
	 		for(String filenameCsv:filenames)
	 		{
	 		ea2.read(filenameCsv);	 		 		
	 		int ilocationnewColumn=0;
	 		for(String Columnname:ea.getHeader())
	 		{ea2.moveColumn(Columnname, ilocationnewColumn);ilocationnewColumn++;}
	 		
	 		if (arg.getOption("Sort"))
	 			ea2.sort(keys);
	 		if (arg.getOption("Move"))
	 		{
	 			Collections.reverse(keys);
	 			for(String Columnname:keys)
	 			ea2.moveColumn(Columnname, 0);
	 		}
	 		ea2.save();
	 		}
	 		
	 		}
	 		else if("None".equalsIgnoreCase(action))
	 		{}
	 		else
	 			System.out.println(arg.help());
	 		
	 		if (arg.getOption("Sort"))
	 			ea.sort(keys);
	 		if (arg.getOption("Move"))
	 		{
	 			Collections.reverse(keys);
	 			for(String Columnname:keys)
	 			ea.moveColumn(Columnname, 0);
	 		}
	 			
	 		if("Merge".equalsIgnoreCase(action)||"None".equalsIgnoreCase(action))
	 		ea.saveAs(outputfile);
	 		
	 		
		/*
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
		*/

	}
	
	public static String compare(List<String> header1, List<String> data1, List<String> header2, List<String> data2,
			int absoluteError, double ratioError) {
		String out="";
		Set<String> header;
		header=JavaUtilList.interSection(header1,header2);
		header=JavaUtilList.xor(header1,header2);
		if(header.size()!=0)
		{
			out+="Missing collunm "+JavaUtilList.interSection(header1,header)+"\n";
			out+="new collunm "+JavaUtilList.interSection(header2,header)+"\n";
		}
		header=JavaUtilList.union(header1,header2);
			
			for( String collunm:header)
			{
				String d1="";
				if(header1.indexOf(collunm)>=0)
				d1=data1.get(header1.indexOf(collunm));
				String d2="";
				if(header2.indexOf(collunm)>=0&&header2.indexOf(collunm)<data2.size())
					d2=data2.get(header2.indexOf(collunm));
				if(!d1.equalsIgnoreCase(d2))
				{
					out+="diff collunm '"+collunm+"' "+d1+"<=>"+d2+"\n";
				}
			}
			return out;
		}
		
	

	private Set<List<String>> getkeys(List<String> keys) {
		Set<List<String>> keyss=new HashSet();
		List<Integer> keyi=new ArrayList();
		for(String  colunm:keys)
			keyi.add(getHeader().indexOf(colunm));
		for(List<String>  row:getData())
		{	
			List<String> key=new ArrayList();
			for(int i=0;i<keyi.size();i++)
			key.add(row.get(keyi.get(i)));
			keyss.add(key);
			
		}
		return keyss;
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
		
		for(String s:getHeader())
			if(s.contains(separator))
				flow.append("\""+s+"\""+separator);
			else
				flow.append(s+separator);
		flow.append("\r\n");
	}
		for(List<String> lines:getData())
		{
			
				for(String s:lines)
					if((s!=null)&& s.contains(separator))
						flow.append("\""+s+"\""+separator);
					else
					if(s!=null)
						flow.append(s+separator);
					else
						flow.append(separator);
			
				flow.append("\r\n");
		}
		
		return flow.toString();//.replaceAll("null", "");
	}
	public void save() {
		String flow=toString();
		JavaUtils.saveAs(getFilename(), flow);

		
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
		/** return the reference of the equal element in the set.
		 * if doesn't exist it return null
		 * */
		public String find(Set<String> set, String e)
		{
		 for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
			 String f = it.next();
		        if (f.equals(e))
		            return f;
		        }
		        return null;
		    }
	/** 
	 * this function read a file but before flush previous data
	 * see append()
	 * */	
	public void read(String filenameCsv)
	{
		flush();
		boolean compress=true;
		int Ncomplexity=200;
		System.out.println("\t-  :read File : "+filenameCsv);
		if ((getFilename()==null)|| (getFilename().compareTo("")==0))
			setFilename(filenameCsv);
	CSVParser parser;
	List<Set<String>> cache=new ArrayList();
	try {
		parser = new CSVParser(new FileReader(filenameCsv), CSVFormat.DEFAULT);

	List<CSVRecord> list = parser.getRecords();
	int rowcount = 0;
	int rowcount2 = 0;
	if (rowcount==0) 
	    if (compress)
			for(String elmt:list.get(0))
				cache.add(new HashSet());
	String e=null;
for (CSVRecord record : list) {
/*	for (Iterator<CSVRecord> it = list.iterator(); it.hasNext(); )
	{
		CSVRecord record = it.next();
		it.remove();*/
	    int icolunm = 0;
	    for (String cellValue : record) {
	    	if(cellValue!=null) 
	    		cellValue=cellValue.trim();
	    	if (!compress)
	        setCell(rowcount2, icolunm++, cellValue);
	    	else
	    		if(cache.size()>icolunm)
	    	{
	        if((e=find(cache.get(icolunm),cellValue)) != null)
	        	setCell(rowcount2, icolunm++, e);
	        else
	        	{
	        	
	        	if(cache.get(icolunm)!=null)
	        	{
	        		if(cache.get(icolunm).size()<Ncomplexity)
	        	cache.get(icolunm).add(cellValue);
	        	}
	        	setCell(rowcount2, icolunm++, cellValue);
	        	}
	    	}	
	    }
	    
	    if (rowcount==0) 
		{			
			if (header.isEmpty()) // should never happen
				{
					header=getData().remove(rowcount);
					rowcount2--;					
				}
			else // merge
			{
				getData().remove(rowcount);rowcount2--;// remove the line merge already done in if (icolunm<0)
				
			}
		}
	    rowcount++;rowcount2++;
	}  
	list=null;
	parser.close();  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	parser=null;
	cache=null;
	System.gc();
	}
	/** create empty cell at end of row to be sure that row is completely define*/
	private void adjustRowwide()
	{
		int size=getHeader().size();
		for(List<String> row:getData())
			while(row.size()<size)
				row.add("");
	}
	/** concatenate 2 file
	 * 
	 * this function read a file without flushing previous data
	 * 
	 * */
	public void append(String filenameCsv)
	{
			setFilename(null);
	CSVParser parser;
	try {
		parser = new CSVParser(new FileReader(filenameCsv), CSVFormat.DEFAULT);

	List<CSVRecord> list = parser.getRecords();
	int rowcount = 0;
	int rowcount2 = 0;
	List<String> localheader=new ArrayList();
	
	for (CSVRecord record : list) {
	//    String[] arr = new String[record.size()];
	    int icolunm = 0;
	    if (rowcount==0) 
		{		
	    	//get local header
	    	for (String cellValue : record) {
	    		if(cellValue!=null)
	    			cellValue=cellValue.trim();
	    		localheader.add(cellValue);	 
	    		//merge headers
	    		if (getHeader().contains(cellValue))
	    		{}
	    		else
	    			getHeader().add(cellValue);
		    }
	    	
		}
	    else
	    {
	    
	   // append at end 
	    rowcount2=newrow();	    
	  for (String cellValue : record) {
	      if(icolunm<localheader.size())
	        {setCell(rowcount2, localheader.get(icolunm), cellValue);icolunm++;	}
	        else
	        	if(!"".equalsIgnoreCase(cellValue))
	        	{System.err.print("Error "+icolunm);icolunm++;}
	    }	   
		}
	    rowcount++;rowcount2++;
	}  
	parser.close();  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	adjustRowwide();
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
	/** return the list of row that match cellValue for column ColumnTitle
	 * */
	public List<List<String> >  findRows(List<String>  ColumnTitle,List<String>  cellValue) 
		{
		if(ColumnTitle==null || cellValue==null || ColumnTitle.size()!=cellValue.size())
			return null;
		List<List<String> > data1=getData();
		for(int i=0;i<ColumnTitle.size();i++)
		data1=findRows(data1,ColumnTitle.get(i),  cellValue.get(i));
		return data1;
		}
	/*** search cellValue in Column ColumnTitle, return the database of all rows that match
	 * data1 : the database
	*/
	private List<List<String> >  findRows(List<List<String> > data1,String ColumnTitle, String cellValue)
	{
		int icolunm=getHeader().indexOf(ColumnTitle);
		List<List<String> > data2=new ArrayList();
		if (icolunm<0)
			return null;
		for(List<String> row:getData())
			if(row!=null)
				if(row.size()>icolunm)
					if(row.get(icolunm)!=null)
		if(row.get(icolunm).equals(cellValue))
			data2.add( row);
		
		return data2;
		
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

	public void sort(List<String> colunms)
{
		Collections.reverse(colunms);
		 for(String colunm:colunms)
		sort( colunm);
		 Collections.reverse(colunms);
}
	
		public void sort(String colunm) {
				 List<String>  l=getColunm( colunm );
		 
		 List<String> l2=JavaUtils.asSortedList(l);
		 
		 List<List<String>> data2=new ArrayList<List<String>>();
		 
		 for(String e2:l2)
		 {
			/* 		
			 List<String> r= findRow(colunm, e2);
			 data2.add(r);
			 data.remove(r);*/
			 List<List<String>> r=findRows(getData(),colunm,e2);
			 data2.addAll(r);
			 data.removeAll(r);
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
	datatoDel=null;
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
			System.err.println("error Column "+oldname+" doesn't exist in "+getFilename());
	}
/** move a colonn,
 * */
	public void moveColumn(String Columnname, int ilocationnewColumn) {
		if (getHeader().get(ilocationnewColumn).equals(Columnname))
			return;//nothing to do;
		if(getHeader().contains(Columnname))
		{
		renameColumn(Columnname, Columnname+"old87614321654681");
		 copyColunm(Columnname+"old87614321654681",ilocationnewColumn,Columnname) ; 
		 int icol=getHeader().indexOf(Columnname+"old87614321654681");
			rmColumn(icol);
		}
		else
		{//do nothing
		/*	this.addColumn(Columnname);
			this.adjustRowwide();
			moveColumn( Columnname, ilocationnewColumn);*/
			
		}
			
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
