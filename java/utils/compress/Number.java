package com.zoubworld.java.utils.compress;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Number implements ISymbol{

	public Number() {
	}
	protected Long value=null;
	public Number(int value) {
		this.value=(long) value;
	}
	static public List<ISymbol> from(long[] d)
	{
		List<ISymbol> l=new ArrayList<ISymbol> ();
		for(long i:d)
			l.add(new Number(i));
		return l;
		
	}
	static public Long getValue(ISymbol e)
	{
		if (Number.class.isInstance(e))
		return e.getLong();
		else
			return null;
		
	}
	public String toString()
	{
		return "#"+value.toString();
	}

	public Number(Long n) {
		this.value= n;
	}



	@Override
	public boolean isChar() {
		return false;
	}

	@Override
	public boolean isInt() {
		return false;
	}

	@Override
	public boolean isShort() {
		return false;
	}

	@Override
	public char getChar() {
		return (char) getLong().byteValue();
	}

	@Override
	public Integer getInt() {
		return getLong().intValue();
	}

	@Override
	public Long getLong() {
		
		return value;
	}

	@Override
	public short getShort() {
	return getLong().shortValue() ;
	}

	@Override
	public long getId() {
		return value.longValue();
	}

	@Override
	public char[] toSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICode getCode() {
		// TODO Auto-generated method stub
		return code;
	}
	ICode code=null;
	@Override
	public void setCode(ICode code2) {
		code=code2;
		
	}

/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Number other = (Number) obj;
	if (value == null) {
		if (other.value != null)
			return false;
	} else if (!value.equals(other.value))
		return false;
	return true;
}
@Override
public ISymbol Factory(Long nId) {
	if (nId==null)
	return null;
	return new Number(nId);
}
@Override
public int compareTo(ISymbol o) {
	if (o==null)
		return -1;
	if(Number.class.isInstance(o))
	return (int)(getId()-o.getId());
	return 0;
}
}
