/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public class Capacitor extends Component2 {

	/**
	 * @param pa
	 * @param pb
	 */
	public Capacitor(Point pa, Point pb) {
		super(pa, pb);
		// TODO Auto-generated constructor stub
	}
	public Capacitor(Double value,Point pa, Point pb) {
		super(pa, pb);
		this.Capacitor=value;
	}
	public Capacitor(Double value) {
		super();
		this.Capacitor=value;
	}

	double charge=0.0;	//U*C=q

	@Override
	public Double getVoltage()
	{
		return +charge/this.Capacitor;
	
	}

}
