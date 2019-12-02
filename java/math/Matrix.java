package com.zoubworld.java.math;

import java.util.Arrays;

public class Matrix {

	Double data[][];
	public Matrix(int sizex,int sizey)
	 {
		 data=new Double[sizey][];
		 for(int i=0;i<sizey;i++)
		 data[i]=new Double[sizex];
		 init(0);
	 }
	public Matrix(int size) {
		data=new Double[size][];
		 for(int i=0;i<size;i++)
		 data[i]=new Double[size];
		 init(0);
	}
	public Matrix(Double[][] ar1) {
		setData(ar1);
	}
	public Matrix(Matrix matrix) {
		setData(matrix.getData().clone());
	}
	public Vector multiply(Vector v)
	{
		Vector vo=new Vector(v.getData().length);
		for(int i=0;i<v.getData().length;i++) {
			vo.getData()[i]=0.0;
			for(int j=0;j<getData()[i].length;j++)
		vo.getData()[i]+=v.getData()[i]*getData()[i][j];
		}
			return vo;
	}
	/**
	 * @return the data
	 */
	public Double[][] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Double[][] data) {
		this.data = data;
	}
	public void setData(Double[] data) {
		this.data = new Double[1][];
		this.data[0]=data;
	}
	/** Addition
	 * https://en.wikipedia.org/wiki/Matrix_(mathematics)
	 * */
	public Matrix add(Matrix d)
	{
		Matrix mr=new Matrix(d.sizeX(),d.sizeY());
		for(int x=0;x<data.length;x++)
			for(int y=0;y<data[x].length;y++)
				mr.getData()[x][y]+=d.getData()[x][y];
		
		return mr;
	}
	/** Scalar multiplication
	 * https://en.wikipedia.org/wiki/Matrix_(mathematics)
	 * */
	public Matrix multiply(double d)
	{
		Matrix mr=new Matrix(this);
		for(int x=0;x<data.length;x++)
			for(int y=0;y<data[x].length;y++)
				mr.data[x][y]*=d;
		
		return mr;
	}
	

	 
	 public Matrix multiply(Matrix b)
		{
		 if(b.sizeY()!=sizeX())
			 return null;
		 Matrix mr=new Matrix(b.sizeX(),sizeY());
			for(int x=0;x<mr.sizeX();x++)
				for(int y=0;y<mr.sizeY();y++)
				{
				mr.getData()[y][x]=0.0;
				for(int j=0;j<b.sizeY();j++)
			mr.getData()[y][x]+=getData()[y][j]*b.getData()[j][x];
			}
				return mr;
		}
	 
	 /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(data);
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
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		return true;
	}
	public int sizeY() {
		return getData().length;
	}
	 public int sizeX() {
		return getData()[0].length;
	}
	 public Matrix  inverse()
	 {
		return null;
		 
	 }
	/** Transposition
	 * https://en.wikipedia.org/wiki/Matrix_(mathematics)
	 * */
	public Matrix transposition()
	{
		Double dat[][]=new Double[data[0].length][];
		for(int i=0;i<data[0].length;i++)
			dat[i]=new Double[data.length];
		
		for(int x=0;x<data.length;x++)
			for(int y=0;y<data[x].length;y++)
				dat[y][x]=data[x][y];
		
		return new Matrix(dat);
	}
	
	/** initialize the matrix with the same data
	 * */
	public void init(double d) {
		for(int x=0;x<data.length;x++)
			for(int y=0;y<data[x].length;y++)
				data[x][y]=d;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s="";
		s+="{";
		for(int x=0;x<data.length;x++)
		{s+="{";
			for(int y=0;y<data[x].length;y++)
				s+=String.format("%3.3f",data[x][y])+",";
		
		s+="},\r\n";
		}
		s+="}";
		return s;
	}
	/** initialize a square matrix as identity matrix : 
	 * ie with 1 in the diagonal, else 0
	 * */
	public void identity() {
		if(sizeX()!=sizeY())
			return ;
		init(0);
		for(int x=0;x<data.length;x++)
			
				data[x][x]=1.0;
		//return this;
	}
	public void zero() {		
		init(0);
		//return this;
	}
	/** convert a matrix[X*1] to a [X*X] with data in diagonal
	 * */
	public Matrix toDiagonalMatrix() {
		if(getData().length!=1)
			return null;
		Matrix m = new Matrix(getData()[0].length);
		//m.init(0);
		for (int i = 0; i < getData()[0].length; i++)
			m.getData()[i][i] = getData()[0][i];
		return m;

	}
	/** invert coef diferent from 0
	 * */
	public Matrix invertCoef() {
		Matrix r=new Matrix(this);
		for(int x=0;x<sizeX();x++)
			for(int y=0;y<sizeY();y++)
			{
				if(getData()[y][x]!=0.0)
			r.getData()[y][x]=1/getData()[y][x];
				else
					r.getData()[y][x]=getData()[y][x];
					}
		
		return r;
	}
}
