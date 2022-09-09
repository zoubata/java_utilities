/**
 * 
 */
package com.zoubworld.chemistry.atoms;

import com.zoubworld.chemistry.IAtom;

/**
 * @author  Pierre Valleau
 *
 */
public class Neutron  implements IOpenScad {

	/**
	 * 
	 */
	public Neutron() {
		// TODO Auto-generated constructor stub
	}
	
	Double Radius=0.80e-15;//m	
	Double masse=null;//kg 
	Double charge=1.0;//e
	
String color="Blue";
	
	IAtom owner;

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	

	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return Radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Double radius) {
		Radius = radius;
	}

	/**
	 * @return the masse
	 */
	public Double getMasse() {
		return masse;
	}

	/**
	 * @param masse the masse to set
	 */
	public void setMasse(Double masse) {
		this.masse = masse;
	}

	/**
	 * @return the charge
	 */
	public Double getCharge() {
		return charge;
	}

	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Double charge) {
		this.charge = charge;
	}

}
