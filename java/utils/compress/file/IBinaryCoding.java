package com.zoubworld.java.utils.compress.file;

import java.util.List;

import com.zoubworld.java.utils.compress.ICodingRule;

public interface IBinaryCoding {

	/**
	 * The list of all coding rules used up to now.(0 first one)
	 * 
	 * @return the codingRule list
	 */
	List<ICodingRule> getCodingRules();

	/**
	 * @return the codingRule
	 */
	ICodingRule getCodingRule();

	/**
	 * @param codingRule
	 *            the codingRule to set
	 */
	void setCodingRule(ICodingRule codingRule);

}