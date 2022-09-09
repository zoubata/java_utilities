/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public class Resistor extends Component2 {

	/**
	 * @param pa
	 * @param pb
	 */
	public Resistor(Point pa, Point pb) {
		super(pa, pb);
		// TODO Auto-generated constructor stub
	}
	public Resistor(Double value,Point pa, Point pb) {
		super(pa, pb);
		this.Resistor=value;
	}
	public Resistor(Double value) {
		super();
		this.Resistor=value;
	}

	@Override
	public Double getVoltage() throws Exception {
		return Resistor*pa.getCurrent(this);
	}
	@Override
	public Double getCurrent(Point ref) throws Exception {
		
		return getVoltage()/Resistor;
	}
}
