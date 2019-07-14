/**
 * 
 */
package com.zoubworld.terrain;

import java.util.Set;

/**
 * @author pierre valleau
 *
 */
public interface ITerrain extends IObject{

	public Set<IObject> getObject();
	public Set<IRobot> getRobot();
	public Set<IBalise> getBalise();
	public boolean add(IObject e);
}
