package com.zoubworld.utils;

import java.util.List;

public abstract class ExcelArrayOperatorAbstract {

	protected ExcelArray excel;

	public abstract void setUp();

	public abstract void process();

	public abstract void tearDown();

	List<String> currentRow,previousRow;

	public Double getCellDouble(String column) {
		String s = getCellString(column);
		Double d = Double.parseDouble(s);
		return d;
	}

	public Integer getCellInteger(String column) {
		String s = getCellString(column);
		Integer d = Integer.parseInt(s);
		return d;
	}

	public String getCellString(String column) {

		return excel.getCell(getCurrentRow(), column);
	}

	public void setCell(String column, Object value) {
		if (!excel.getHeader().contains(column))
			excel.addColumn(column);
		excel.setCell(getCurrentRow(), column, value.toString());
	}

	public ExcelArrayOperatorAbstract() {
		super();
		currentRow=null;
		previousRow=null;
		excel=null;
	}

	/**
	 * @return the excel
	 */
	public ExcelArray getExcel() {
		return excel;
	}

	/**
	 * @param excel
	 *            the excel to set
	 */
	public void setExcel(ExcelArray excel) {
		this.excel = excel;
	}

	/**
	 * @return the currentRow
	 */
	public List<String> getCurrentRow() {
		return currentRow;
	}
	public List<String> getPreviousRow() {
		return previousRow;
	}

	/**
	 * @param currentRow
	 *            the currentRow to set
	 */
	public void setCurrentRow(List<String> currentRow) {
		previousRow=this.currentRow ;
		this.currentRow = currentRow;
	}

}