/**
 * 
 */
package com.zoubworld.java.math;


/**
 * @author M43507
 *
 */
public class Vector {
 Double data[]=null;
 public Vector(int size)
 {
	 data=new Double[size];
 }
 public Matrix toDiagonalMatrix()
 {
	Matrix m=new Matrix(size());
	m.init(0);
	for(int i=0;i<size();i++)
	m.getData()[i][i]=getData()[i];
	return m;
	 
 }
 public Vector multiply(Matrix m)
	{
		Vector vo=new Vector(m.getData()[0].length);
		for(int i=0;i<getData().length;i++) {
			vo.getData()[i]=0.0;
			for(int j=0;j<m.getData()[i].length;j++)
		vo.getData()[i]+=getData()[i]*m.getData()[i][j];
		}
			return vo;
	}
/**
 * @return the data
 */
public Double[] getData() {
	return data;
}
public static void main(String[] args) {
Vector v=new Vector(3);
v.init();
System.out.println(v.m);
System.out.println(v.mi);

System.out.println(v.m.multiply(v.mi));
}
/**
 * @param data the data to set
 */
public void setData(Double[] data) {
	this.data = data;
}






public int size() {
	if (data==null)
			return 0;
	return getData().length;
}

public Vector Multiply(double d) {
	for(int i=0;i<data.length;i++)
		data[i]*=d;
	return this;
}

public Vector init(double d) {
	for(int i=0;i<data.length;i++)
		data[i]=d;
	return this;
}




/** W1:x,y,theta
 *  W2:x,y,theta
 *  W3:x,y,theta
 *  x,y, location of wheel versus the center of robot(at 0,0)
 *  theta, the angle of the wheel direction with axe x
 * */
static Matrix WheelLocation=new Matrix(3);
static Vector WheelSize=new Vector(3);
void init()
{Double L=0.110;//distance between the center of robot and the wheel
Double aad[][]={{Math.sin(-Math.PI/3)*L,Math.cos(-Math.PI/3)*L,Math.PI/6},
		{Math.sin(Math.PI/3)*L,Math.cos(Math.PI/3),Math.PI*5/6},
		{Math.sin(Math.PI)*L,Math.cos(Math.PI)*L,-Math.PI/2}};
WheelLocation.setData(aad);

WheelSize.init(1);
WheelSize.Multiply(0.063);


Double aad1[][]={{Math.sin(-Math.PI/3),Math.cos(-Math.PI/3),L},
		{Math.sin(Math.PI/3),Math.cos(Math.PI/3),L},
		{Math.sin(Math.PI),Math.cos(Math.PI),L}};
m.setData(aad1);


 Double aad2[][]={{Math.sin(-Math.PI/3),Math.sin(Math.PI/3),L},
		{1.0/3,1.0/3,-2.0/3},
		{1.0/3/L,1.0/3/L,-2.0/3/L}};
mi.setData(aad2);
}
/** convertion matrix wheel speed to robot speed */
Matrix m=new Matrix(3);
/** convertion matrix robot speed to wheel speed */
Matrix mi=new Matrix(3);
/**
 * robotLinearSpeed={vx,vy,vtetha}
 * whellLinearSpeed2={vleft,vrigth,vback}
 * */
Vector whellLinearSpeed2(Vector robotLinearSpeed)
{
	return m.multiply(robotLinearSpeed);
}

Vector robotLinearSpeed(Vector whellLinearSpeed)
{
return mi.multiply(whellLinearSpeed);
}

/**
 * whellRotationSpeed={rpmleft,rpmrigth,rpmback} in round per minute
 * whellLinearSpeed={vleft,vrigth,vback} in m/minutes
 * */
Vector whellLinearSpeed(Vector whellRotationSpeed)
{
	Vector WheelSize=new Vector(3);
	WheelSize.init(1);
	WheelSize.Multiply(1/0.063);
	return whellRotationSpeed.multiply(WheelSize.toDiagonalMatrix());	 
}

/**
 * whellRotationSpeed={rpmleft,rpmrigth,rpmback} in round per minute
 * whellLinearSpeed={vleft,vrigth,vback} in m/minutes
 * */
Vector whellRotationSpeed(Vector whellLinearSpeed)
{
	return whellLinearSpeed.multiply(WheelSize.toDiagonalMatrix());	 
	
	}

}
