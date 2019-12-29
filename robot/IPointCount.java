package com.zoubworld.robot;

public interface IPointCount {
	/** log a new action to count point.
	 * */
	void addAction(int key);

	/**
	 * @return the scoredisplayed
	 */
	int getScoreDisplayed();

	/**
	 * @return the scorefinal
	 */
	int getScoreFinal();

}