/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 * a net have 2 points
 * the resistor is 0 Ohm, the inductor is 0 H,
 * the potentiel is te same at 2 points.
 */
public abstract class Component2 implements Iconnect {

	Point pa=null,pb=null;
	Double potentiela=null;
	Double potentielb=null;
	
	Double Resistor=null;
	Double Inductor=null;
	Double Capacitor=null;
	
	/**
	 * 
	 */
	public Component2(Point pa,Point pb) {
		setPa(pa);
		setPb(pb);
		
		
	}
	public Component2() {
		this.pa=null;
		this.pa=null;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * @return the pb
	 */

	public Point getPb() {
		return pb;
	}

	/**
	 * @param pb the pa to set
	 */

	public void setPb(Point pb) {
		this.pb = pb;
		if (pb!=null)
			pb.add(this);
	}
	/**
	 * @return the pa
	 */
	@Override
	public Point getPa() {
		return pa;
	}

	/**
	 * @param pa the pa to set
	 */
	@Override
	public void setPa(Point pa) {
		this.pa = pa;
		if (pa!=null)
			pa.add(this);
	}
	@Override
	public abstract Double getCurrent(Point ref) throws Exception;
	@Override
	public Double getPotential(Point ref,Point measurepoint) throws Exception {
		if (getVoltage()==null)
			return null;
		if (pa==ref && pb==measurepoint)
			return getVoltage();
		if (pb==ref && pa==measurepoint)
			return -getVoltage();
		if ( pb==measurepoint)
			return pa.getPotential(ref)+getVoltage();
		if ( pa==measurepoint)
			return pb.getPotential(ref)-getVoltage();
		throw new Exception("should not happen");
	//	return null;
	}
	/** return the volatage between a,b
	 * @throws Exception 
	 * */
	public abstract Double getVoltage() throws Exception;
}
