/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public class VoltageGen extends Component2 {

	Double value=null;
	/**
	 * @param pa
	 * @param pb
	 */
	public VoltageGen(Point pa, Point pb) {
		super(pa, pb);
		// TODO Auto-generated constructor stub
	}
	public VoltageGen(Double value,Point pa, Point pb) {
		super(pa, pb);
		this.value=value;
	}
	public VoltageGen(Double value) {
		super();
		this.value=value;
	}
	@Override
	public Double getVoltage() {
		return value;
	}
	public Double getCurrent(Point ref) {
		return null;//any one
	}
	
	}
