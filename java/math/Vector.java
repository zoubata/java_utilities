/**
 * 
 */
package com.zoubworld.java.math;

/**
 * @author  Pierre Valleau
 *
 */
public class Vector {
	Double data[] = null;

	public Vector(int size) {
		data = new Double[size];
	}

	public Matrix toDiagonalMatrix() {
		Matrix m = new Matrix(size());
		m.init(0);
		for (int i = 0; i < size(); i++)
			m.getData()[i][i] = getData()[i];
		return m;

	}
	@Override
	public String toString() {
		String s="";
		s+="{";
		for(int x=0;x<data.length;x++)
		{
			
				s+=String.format("%3.3f",data[x])+",";
		
		
		}
		s+="}";
		return s;
	}
	public Vector multiply(Matrix m) {
		Vector vo = new Vector(m.getData()[0].length);
		for (int i = 0; i < getData().length; i++) {
			vo.getData()[i] = 0.0;
			for (int j = 0; j < m.getData()[i].length; j++)
				vo.getData()[i] += getData()[i] * m.getData()[i][j];
		}
		return vo;
	}

	/**
	 * @return the data
	 */
	public Double[] getData() {
		return data;
	}

	public static void main(String[] args) {
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Double[] data) {
		this.data = data;
	}

	public int size() {
		if (data == null)
			return 0;
		return getData().length;
	}

	public Vector Multiply(double d) {
		for (int i = 0; i < data.length; i++)
			data[i] *= d;
		return this;
	}

	public Vector init(double d) {
		for (int i = 0; i < data.length; i++)
			data[i] = d;
		return this;
	}

}
