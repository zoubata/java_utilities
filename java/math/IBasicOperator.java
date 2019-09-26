package com.zoubworld.java.math;

public interface IBasicOperator {

	IBasicOperator power(int n);

	// return { -1, 0, +1 } if a < b, a = b, or a > b
	int compareTo(IBasicOperator b);

	// return a * b, staving off overflow as much as possible by cross-cancellation
	IBasicOperator multiply(IBasicOperator b);

	// return a + b, staving off overflow
	IBasicOperator add(IBasicOperator b);

	// return -a
	IBasicOperator negate();

	// return |a|
	IBasicOperator abs();

	// return a - b
	IBasicOperator subtract(IBasicOperator b);

	// return a / b
	IBasicOperator divide(IBasicOperator b);

}