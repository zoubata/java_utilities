/**
 * 
 */
package com.zoubworld.chemistry.atoms;

import java.util.List;

import com.zoubworld.chemistry.IAtom;

/**
 * @author Pierre Valleau
 *
 */
public class Electron implements IOpenScad {

	List<IAtom> owner;
	
	/**
	 * 
	 */
	public Electron() {
		// TODO Auto-generated constructor stub
	}
	Double Radius=2e-12;//m
	Double masse=null;//kg 
	Double charge=1.0;//e
	
	

String color="Gray";
	

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
