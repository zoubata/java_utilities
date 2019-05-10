package com.zoubworld.java.utils.svg;

import java.util.List;

public class Svg extends ComplexSvg implements ISvgObject{

	public Svg(ISvgObject o) {
		super();
		getList().add(o);
		
	}
	/**
	 * @return the width
	 */
	public Double getWidth() {
		return width;
	}



	/**
	 * @param width the width to set
	 */
	public void setWidth(Double width) {
		this.width = width;
	}



	/**
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}



	/**
	 * @param height the height to set
	 */
	public void setHeight(Double height) {
		this.height = height;
	}
	String desc=null;
	String title=null;
	private Double width=300.0;
	private Double height=300.0;
	
		public String toSVG()
		{
			String s="";
			s+="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
			s+="<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\""+BasicSvg.DoubletoSVG(width)+""+getUnit()+"\" height=\""+BasicSvg.DoubletoSVG(height)+""+getUnit()+"\">\n";
			if (title!=null) s+="<title>"+title+"</title>\n";
			if (desc!=null)
			{s+="<desc>\n";
			 	s+=desc+"\n";
			
			 s+="</desc>\n";}
			s+=super.toSVG();
			  s+="			</svg>\n";
			return s;
			
		
		}

		

		/**
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}

		/**
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		
}
