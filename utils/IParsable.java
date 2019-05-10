package com.zoubworld.utils;

import com.zoubworld.utils.IParsable;
/**
 * @author Pierre Valleau
 *
 */
public interface IParsable
{
	/*public IParsable()
	{}*/
	public abstract  IParsable Parse(String line);
	public abstract String get();
	
	/** return true if multiline is supported, multiline imply a start and a stop flag*
	public abstract boolean ismultiline();
	/** start flag detected*
	public abstract boolean isMatchBegin();
	/** stop flag detected*
	public abstract boolean isMatchEnd();
	/**/
	
}