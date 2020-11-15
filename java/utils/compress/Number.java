package com.zoubworld.java.utils.compress;

public class Number implements ISymbol{

	public Number() {
	}
	Long value=null;
	public Number(int value) {
		this.value=(long) value;
	}

	public String toString()
	{
		return "#"+value.toString();
	}

	public Number(Long n) {
		this.value= value;
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
 * @see java.lang.Object#equals(java.lang.Object)
 *  2 symbol are equal if they have the same id, there is on length care
 */
@Override
public boolean equals(Object obj) {
	if(Number.class.isInstance(obj))
	{
		Number c=(Number)obj;
		/*if (c.symbol.length!=symbol.length)
			return false;*/
		if(c.getId()!=(getId()))
		return false;
		else
			return true;
	}
	return super.equals(obj);
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
