package com.zoubworld.utils;

import java.util.List;

import com.zoubworld.utils.IParsable;
/**
 * @author Pierre Valleau
 *
 */
public interface IParsable
{
	/*public IParsable()
	{}*/
	/** parse the line and store on object the date
	 * */
	public abstract  IParsable Parse(String line);
	/** return the data string(or 1st data in case of several one)
	 * 
	 * */
	public abstract String get();
	/** return the list of data collected by the parsing
	 * 
	 * */
	public abstract List<String> getList();
	/** return true if multiline is supported, multiline imply a start and a stop flag*
	public abstract boolean ismultiline();
	/** start flag detected*
	public abstract boolean isMatchBegin();
	/** stop flag detected*
	public abstract boolean isMatchEnd();
	/**/
	
}