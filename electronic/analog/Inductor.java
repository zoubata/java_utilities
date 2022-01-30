/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public class Inductor extends Component2 {

	/**
	 * @param pa
	 * @param pb
	 */
	public Inductor(Point pa, Point pb) {
		super(pa, pb);
		// TODO Auto-generated constructor stub
	}
	public Inductor(Double value,Point pa, Point pb) {
		super(pa, pb);
		this.Inductor=value;
	}
	public Inductor(Double value) {
		super();
		this.Inductor=value;
	}
	
	
	@Override
	public Double getVoltage() {
		return Inductor*getDerivateCurrent();
	}
	
}
