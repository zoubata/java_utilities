package com.zoubwolrd.java.utils.svg;

import java.util.ArrayList;
import java.util.List;

public class ComplexSvg  extends BasicSvg implements ISvgObject{

	public ComplexSvg() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the list
	 */
	public List<ISvgObject> getList() {
		if (list==null)
			list=new ArrayList<ISvgObject>();
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ISvgObject> list) {
		this.list = list;
	}
	public void move(Double dx, Double dy) {
		
		super.move(dx, dy);
		  for(ISvgObject o : list)
		  {
			  if (o!=null)
				  o.move(dx, dy);
		
		
	}
		  }
	List<ISvgObject> list=null;
	@Override
	public String toSVG() {
		String s="";
		  if (list!=null)
			  for(ISvgObject o : list)
			  {
				  if (o!=null)
				  s+=o.toSVG()+"\n";  
			  }
		return s;
	}

}
