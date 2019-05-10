package com.zoubworld.utils.csvtool;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.utils.JavaUtils;

public class ACsvFile
{
	public List<String> columns=null;
	public List<List<String>> values=null;
	public String separator=",";
	public String toString()
	{
		String s="";
		for (String e:columns)
		s+=e+separator;
		s+="\n";
		{
			
			for(int index=0;index<values.get(0).size();index++)
			{
			for (List<String> value:values)
			{
			String e=(value.size()>index)?value.get(index):"";
				s+=e+separator;
			
			}	s+="\n";}
		}
		
	return s;
	}
	public String getSeparator(String filename)
	{
		
		int countComma=JavaUtils.readAndCount(filename,",");
				int countsemiComma=JavaUtils.readAndCount(filename,";");
				int counttab=JavaUtils.readAndCount(filename,"\t");
						if (countComma>countsemiComma)	
{
							if (countComma>counttab)		
								return ",";
								else
									return "\t";
						}
						else
						{
							if (countsemiComma>counttab)		
								return ";";
								else
									return "\t";
						}
	}
	
	public  ACsvFile(String filename)
	{
		readFile( filename);
	}
	/** load a CSV file 
	 * can autodetect separator, but slower, alse define it
	 * */
	public void readFile(String filename)
	{
		if (separator==null)
		separator=getSeparator( filename);
		String s=JavaUtils.read(filename);
		String tab[]=s.split(System.getProperty("line.separator"));
		columns=new ArrayList<String>();
		values=new ArrayList<List<String>>();
		for(String e:tab[0].split(separator))
		{
			columns.add(e);
		values.add(new ArrayList<String>());
		}
		
		for(int i=1;i<tab.length;i++)
		{
			values.add(new ArrayList<String>());
			String e[]=tab[i].split(separator);
			for(	int index=0;index<columns.size();index++)
			{
				if (e.length>index)
			values.get(index).add(e[index]);
				else
					values.get(index).add("");
					
			}
		}
	}
	public Integer getColunmIndex(String key)
	{
		int i=0;
		for(String e:columns)
			{ if (e.contentEquals(key))
				return i;
			i++;
			}
		return null;
		
	}
	public List<String> getColunm(String key)
	{
		Integer i=getColunmIndex( key);
		if (i!=null)
			return values.get(i);
		return null;
	}
	}