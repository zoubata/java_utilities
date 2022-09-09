/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public class CurrentGen extends Component2 {

	Double value=null;
	/**
	 * @param pa
	 * @param pb
	 */
	public CurrentGen(Point pa, Point pb) {
		super(pa, pb);
		// TODO Auto-generated constructor stub
	}
	public CurrentGen(Double value,Point pa, Point pb) {
		super(pa, pb);
		this.value=value;
	}
	public CurrentGen(Double value) {
		super();
		this.value=value;
	}

	@Override
	public Double getVoltage() {
		return null;
	}
	public Double getCurrent(Point ref) throws Exception {
		if (ref==pb)
		return value;
		if (ref==pa)
			return -value;
		throw new Exception("should not happen");
//		return null;
		
	}

}
