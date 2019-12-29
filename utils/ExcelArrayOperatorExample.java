/**
 * 
 */
package com.zoubworld.utils;

/**
 * @author pierre valleau
 *
 */
public class ExcelArrayOperatorExample extends ExcelArrayOperatorAbstract {

	/**
	 * executed before the processing of the rows
	 */
	@Override
	public void setUp() {
		 excel.addColumn("valid");
	}
	
	/**
	 * process a row
	 */
	@Override
	public void process() {
		// String replace=excel.getCell(row, "titi");
		// excel.setCell(getCurrentRow(), "newtoto", replace);
		/*String pad_vilst_max=excel.getCell(getCurrentRow(), "pad_vilst_max");
		 String vddio_typ=excel.getCell(getCurrentRow(), "vddio_typ");
		 double dpad_vilst_max=Double.parseDouble(pad_vilst_max);
		 double dvddio_typ=Double.parseDouble(vddio_typ);
		 excel.setCell(getCurrentRow(), "valid",""+( dpad_vilst_max<dvddio_typ));
		 */
		Boolean b=getCellDouble("pad_vilst_max")<getCellDouble("dvddio_typ");
		 setCell("valid", b);
	}

	/**
	 * executed after the processing of the rows
	 */
	@Override
	public void tearDown() {
		 excel.deleteRowWhereColEqualData("valid","false");
		 excel.deleteColunm("valid");
	}
}
