/**
 * 
 */
package com.zoubworld.java.generator.cpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.math.io.files.ASCIIFile;

import com.zoubworld.utils.JavaUtils;
import com.zoubworld.utils.csvtool.ACsvFile;

/**
 * @author Pierre Valleau
 *
 */
public class CsvToVariant {

	ACsvFile acsv = null;

	/**
	 * 
	 */
	public CsvToVariant(String filename) {
		acsv = new ACsvFile(filename);
	}

	String PADNAME = "Pad Name";
	String TIMER0_COL = "E";
	String TIMER1_COL = "F";
	String SERCOM0_COL = "C";
	String SERCOM1_COL = "D";
	String ADC0_COL = "B_ADC0";
	String ADC1_COL = "B_ADC1";
	String EXTERNAL_COL = "A";

	public void autodetect() {
		TIMER0_COL = null;
		TIMER1_COL = null;
		SERCOM0_COL = null;
		SERCOM1_COL = null;
		ADC0_COL = null;
		ADC1_COL = null;
		EXTERNAL_COL = null;
		for (int i = 0; i < acsv.columns.size(); i++)
			for (String val : acsv.values.get(i)) {
				if (val.startsWith("EIC/EXTINT"))
					EXTERNAL_COL = acsv.columns.get(i);
				if (val.startsWith("SERCOM")) {
					if (SERCOM0_COL == null)
						SERCOM0_COL = acsv.columns.get(i);
					if (SERCOM0_COL != null)
						if (!SERCOM0_COL.contentEquals(acsv.columns.get(i)))
							if (SERCOM1_COL == null)
								SERCOM1_COL = acsv.columns.get(i);
				}
				if (val.startsWith("TC")) {
					if (TIMER0_COL == null)
						TIMER0_COL = acsv.columns.get(i);
					if (TIMER0_COL != null)
						if (!TIMER0_COL.contentEquals(acsv.columns.get(i)))
							if (TIMER1_COL == null)
								TIMER1_COL = acsv.columns.get(i);
				}
				if (val.startsWith("ADC")
						&& !acsv.columns.get(i).contains("REF")) {
					if (ADC0_COL == null)
						ADC0_COL = acsv.columns.get(i);
					if (ADC0_COL != null)
						if (!ADC0_COL.contentEquals(acsv.columns.get(i)))
							if (ADC1_COL == null)
								ADC1_COL = acsv.columns.get(i);
				}
			}
	}

	public String generatePinout() {
		String packages[] = { "QFN32", "QFN48", "QFN64", "TQFP32", "TQFP100",
				"TQFP128", "QFN24_capless", "SOIC14_capless", "SOIC20_capless" };
		String s = "";
		if (acsv == null)
			return "";
		int index = 0;
		for (String pad : (acsv.getColunm(PADNAME))) {
			String PORTA = "";
			int NUMBER = -1;
			List<String> line = new ArrayList<String>(acsv.values.size());
			for (List<String> col : acsv.values)
				line.add(col.size() > index ? col.get(index) : "");

			if (pad.startsWith("P", 0)) {
				for (int j = 0; j < packages.length; j++)
					for (int i = 0; i < Math.min(line.size(),
							acsv.columns.size()); i++)
						if ((acsv.columns.get(i)).startsWith(packages[j]))
							if (!line.get(i).trim().contentEquals(""))
								s += "#define " + packages[j] + "_"
										+ line.get(i) + "\t " + pad
										+ "_ARDUINO\n";
			} else if (!pad.startsWith("VMARGIN", 0)) 
				 if (pad.contains("VDD") || pad.contains("GND"))
					 {
				for (int j = 0; j < packages.length; j++)
					for (int i = 0; i < Math.min(line.size(),
							acsv.columns.size()); i++)
						if ((acsv.columns.get(i)).startsWith(packages[j]))
							if (!line.get(i).trim().contentEquals(""))
								s += "#define " + packages[j] + "_"
										+ line.get(i) + "\t " + "SUPPLY"
										+ "_ARDUINO   //\n";// +pad+"\n";

			}
			index++;
		}

		return s;
	}

	public String generate() {
		String s = "";
		if (acsv == null)
			return "";
		int index = 0;
		timersUsed = new ArrayList<String>();// reset history
		pin2timer = new HashMap<String, String>();
		for (String pad : (acsv.getColunm(PADNAME))) {

			String PORTA = "";
			int NUMBER = -1;
			List<String> line = new ArrayList<String>(acsv.values.size());
			for (List<String> col : acsv.values)
				line.add(col.size() > index ? col.get(index) : "");

			if (pad.startsWith("P", 0)) {
				PORTA = "PORT" + pad.charAt(1);
				NUMBER = Integer.parseInt(pad.substring(2, pad.length()));

				/**
				 * PIO_NOT_A_PIN=0xff, // Not under control of a peripheral.
				 * 
				 * PIO_INPUT=INPUT, // The pin is controlled by PORT and is an
				 * input. PIO_OUTPUT=OUTPUT, // The pin is controlled by PORT
				 * and is an output. PIO_INPUT_PULLUP=INPUT_PULLUP, // The pin
				 * is controlled by PORT and is an input with internal pull-up
				 * resistor enabled. PIO_INPUT_PULLDOWN=INPUT_PULLDOWN, // The
				 * pin is controlled by PORT and is an input with internal
				 * pull-down resistor enabled.
				 * 
				 * PIO_EXTINT=4, // The pin is controlled by the EXTINT
				 * peripheral and is an input with interrupt.
				 * 
				 * PIO_ANALOG_ADC=5, // The pin is controlled by the ANALOG
				 * peripheral and is an ADC input. PIO_ANALOG_DAC=6, // The pin
				 * is controlled by the ANALOG peripheral and is a DAC output.
				 * PIO_ANALOG_REF=7, // The pin is controlled by the ANALOG
				 * peripheral and is a voltage reference input (3.3V MAX).
				 * PIO_ANALOG_AC=8, // The pin is controlled by the ANALOG
				 * peripheral and is used by the AC (analog comparator).
				 * PIO_ANALOG_PTC=9, // The pin is controlled by the ANALOG
				 * peripheral and is used by the PTC (peripheral touch
				 * controller). PIO_ANALOG_SDADC=10, // The pin is controlled by
				 * the ANALOG peripheral and is used by the PTC (peripheral
				 * touch controller).
				 * 
				 * PIO_TIMER_PWM=11, // The pin is controlled by a TIMER
				 * peripheral and is a PWM output. PIO_TIMER_CAPTURE=12, // The
				 * pin is controlled by a TIMER peripheral and is a capture
				 * input.
				 * 
				 * PIO_SERCOM=13, // The pin is controlled by a SERCOM
				 * peripheral (UART, SPI, or I2C). PIO_COM=14, // The pin is
				 * controlled by the COM peripheral (USB or I2S).
				 * PIO_AC_GCLK=15, // The pin is controlled by the AC_GCLK
				 * peripheral (I/O). PIO_CCL=16, // The pin is controlled by the
				 * CCL (configurable custom logic) peripheral (I/O).
				 * 
				 * PIO_MULTI, // The pin can be configured to any type based on
				 * the attributes.
				 * 
				 * PIO_STARTUP, // Used as parameter to pinPeripheral() only to
				 * set startup state (enable INEN only)
				 */
				String PIO_MULTI = "PIO_MULTI";
				String PER_ATTR = getPER_ATTRArduino(line);// "PER_ATTR_DRIVE_STRONG|PER_ATTR_ADC_STD";
				String PIN_ATTR = getPIN_ATTRArduino(line);// "PIN_ATTR_EXTINT |PIN_ATTR_ANALOG|PIN_ATTR_DIGITAL|PIN_ATTR_ADC"
				String ON_TIMER = getON_TIMERArduino(line);// "NOT_ON_TIMER";
				String ADC_Channel = getADC_ChannelArduino(line);// "ADC_Channel0";

				String EXTERNAL_INT = getEXTERNAL_INTArduino(line);// "EXTERNAL_INT_2";
				String GCLK_CCL = getGCLK_CCLArduino(line);// "GCLK_CCL_NONE";

				String result = "#define " + pad + "_ARDUINO"
						+ "\t{ (uint8_t) " + PORTA + ",(uint8_t)  " + NUMBER
						+ ",(uint8_t)  " + PIO_MULTI + ",(uint8_t) " + PER_ATTR
						+ ",(uint32_t)  " + PIN_ATTR + ",(uint8_t) " + ON_TIMER
						+ ",(uint8_t)" + ADC_Channel + ",(uint8_t)  "
						+ EXTERNAL_INT + "	,(uint8_t)" + GCLK_CCL
						+ "   } //";

				s += result + "\n";
			}
			index++;
		}

		System.out.println("// use of timer summary");
		Map<String, Integer> timercount = JavaUtils
				.CountElementInList(timersUsed);
		for (String e : timercount.keySet()) {
			System.out.print("//\t- " + e + " : " + timercount.get(e)
					+ " times for ");
			for (String pin : pin2timer.keySet())
				if (pin2timer.get(pin).equalsIgnoreCase(e))
					System.out.print(pin + ", ");
			System.out.println();
		}
		return s;
	}

	private String getGCLK_CCLArduino(List<String> line) {
		return "GCLK_CCL_NONE";
		/*
		 * String EXTERNAL_COL="A"; String value=line.get(
		 * acsv.getColunmIndex(EXTERNAL_COL));
		 * 
		 * 
		 * if (value.contentEquals("EIC/NMI")) return "EXTERNAL_INT_NMI"; if
		 * (value.contentEquals("EIC/EXTINT[0]")) return "EXTERNAL_INT_0"; if
		 * (value.contentEquals("EIC/EXTINT[1]")) return "EXTERNAL_INT_1"; if
		 * (value.contentEquals("EIC/EXTINT[2]")) return "EXTERNAL_INT_2"; if
		 * (value.contentEquals("EIC/EXTINT[3]")) return "EXTERNAL_INT_3"; if
		 * (value.contentEquals("EIC/EXTINT[4]")) return "EXTERNAL_INT_4"; if
		 * (value.contentEquals("EIC/EXTINT[5]")) return "EXTERNAL_INT_5"; if
		 * (value.contentEquals("EIC/EXTINT[6]")) return "EXTERNAL_INT_6"; if
		 * (value.contentEquals("EIC/EXTINT[7]")) return "EXTERNAL_INT_7"; if
		 * (value.contentEquals("EIC/EXTINT[8]")) return "EXTERNAL_INT_8"; if
		 * (value.contentEquals("EIC/EXTINT[9]")) return "EXTERNAL_INT_9"; if
		 * (value.contentEquals("EIC/EXTINT[10]")) return "EXTERNAL_INT_10"; if
		 * (value.contentEquals("EIC/EXTINT[11]")) return "EXTERNAL_INT_11"; if
		 * (value.contentEquals("EIC/EXTINT[12]")) return "EXTERNAL_INT_12"; if
		 * (value.contentEquals("EIC/EXTINT[13]")) return "EXTERNAL_INT_13"; if
		 * (value.contentEquals("EIC/EXTINT[14]")) return "EXTERNAL_INT_14"; if
		 * (value.contentEquals("EIC/EXTINT[15]")) return "EXTERNAL_INT_15";
		 * return "NOT_AN_INTERRUPT";
		 */

	}

	private String getEXTERNAL_INTArduino(List<String> line) {

		/*
		 * EXTERNAL_INT_0 = 0, EXTERNAL_INT_1, EXTERNAL_INT_2, EXTERNAL_INT_3,
		 * EXTERNAL_INT_4, EXTERNAL_INT_5, EXTERNAL_INT_6, EXTERNAL_INT_7,
		 * EXTERNAL_INT_8, EXTERNAL_INT_9, EXTERNAL_INT_10, EXTERNAL_INT_11,
		 * EXTERNAL_INT_12, EXTERNAL_INT_13, EXTERNAL_INT_14, EXTERNAL_INT_15, ,
		 * EXTERNAL_NUM_INTERRUPTS, NOT_AN_INTERRUPT = 0xff, EXTERNAL_INT_NONE =
		 * NOT_AN_INTERRUPT,
		 */

		String value = line.get(acsv.getColunmIndex(EXTERNAL_COL));

		if (value.contentEquals("EIC/NMI"))
			return "EXTERNAL_INT_NMI";
		if (value.contentEquals("EIC/EXTINT[0]"))
			return "EXTERNAL_INT_0";
		if (value.contentEquals("EIC/EXTINT[1]"))
			return "EXTERNAL_INT_1";
		if (value.contentEquals("EIC/EXTINT[2]"))
			return "EXTERNAL_INT_2";
		if (value.contentEquals("EIC/EXTINT[3]"))
			return "EXTERNAL_INT_3";
		if (value.contentEquals("EIC/EXTINT[4]"))
			return "EXTERNAL_INT_4";
		if (value.contentEquals("EIC/EXTINT[5]"))
			return "EXTERNAL_INT_5";
		if (value.contentEquals("EIC/EXTINT[6]"))
			return "EXTERNAL_INT_6";
		if (value.contentEquals("EIC/EXTINT[7]"))
			return "EXTERNAL_INT_7";
		if (value.contentEquals("EIC/EXTINT[8]"))
			return "EXTERNAL_INT_8";
		if (value.contentEquals("EIC/EXTINT[9]"))
			return "EXTERNAL_INT_9";
		if (value.contentEquals("EIC/EXTINT[10]"))
			return "EXTERNAL_INT_10";
		if (value.contentEquals("EIC/EXTINT[11]"))
			return "EXTERNAL_INT_11";
		if (value.contentEquals("EIC/EXTINT[12]"))
			return "EXTERNAL_INT_12";
		if (value.contentEquals("EIC/EXTINT[13]"))
			return "EXTERNAL_INT_13";
		if (value.contentEquals("EIC/EXTINT[14]"))
			return "EXTERNAL_INT_14";
		if (value.contentEquals("EIC/EXTINT[15]"))
			return "EXTERNAL_INT_15";
		return "NOT_AN_INTERRUPT";
	}

	private String getPER_ATTRArduino(List<String> line) {
		// "PIN_ATTR_EXTINT |PIN_ATTR_ANALOG|PIN_ATTR_DIGITAL|PIN_ATTR_ADC"
		String PER_ATTR_LOCAL = "PER_ATTR_DRIVE_STRONG";

		String value = line.get(acsv.getColunmIndex(PADNAME));
		if (value.startsWith("P")) {
		} else
			PER_ATTR_LOCAL += "|PER_ATTR_OUTPUT_TYPE_STD";
		/*
		 * if (getEXTERNAL_INTArduino( line).contentEquals("NOT_AN_INTERRUPT"))
		 * {} else PIN_ATTR+="|PIN_ATTR_EXTINT";
		 */
		if (getADC_ChannelArduino(line).contentEquals("No_ADC_Channel")) {
		} else
			PER_ATTR_LOCAL += "|" + getADC_PERArduino(line);

		if (getON_TIMERArduino(line).contentEquals("NOT_ON_TIMER")) {
		} else
			PER_ATTR_LOCAL += "|" + PER_ATTR;
		if (getON_SERCOMArduino(line).contentEquals("NOT_SERCOM")) {
		} else
			PER_ATTR_LOCAL += "|" + PER_ATTR;

		/*
		 * 
		 * #define PER_ATTR_NONE (0UL<<0)
		 * 
		 * #define PER_ATTR_SERCOM_STD (0UL<<0) #define PER_ATTR_SERCOM_ALT
		 * (1UL<<0) #define PER_ATTR_SERCOM_MASK (1UL<<0)
		 * 
		 * #define PER_ATTR_TIMER_STD (0UL<<1) #define PER_ATTR_TIMER_ALT
		 * (1UL<<1) #define PER_ATTR_TIMER_MASK (1UL<<1)
		 * 
		 * #define PER_ATTR_DRIVE_STD (0UL<<2) #define PER_ATTR_DRIVE_STRONG
		 * (1UL<<2) #define PER_ATTR_DRIVE_MASK (1UL<<2)
		 * 
		 * #define PER_ATTR_OUTPUT_TYPE_STD (0UL<<3) #define
		 * PER_ATTR_OUTPUT_TYPE_OPEN_DRAIN (1UL<<3) #define
		 * PER_ATTR_OUTPUT_TYPE_OPEN_SOURCE (2UL<<3) #define
		 * PER_ATTR_OUTPUT_TYPE_BUSKEEPER (3UL<<3) #define
		 * PER_ATTR_OUTPUT_TYPE_MASK (3UL<<3)
		 * 
		 * #define PER_ATTR_INPUT_SYNCHRONIZER_ON_DEMAND (0UL<<5) #define
		 * PER_ATTR_INPUT_SYNCHRONIZER_ALWAYS_ON (1UL<<5) #define
		 * PER_ATTR_INPUT_SYNCHRONIZER_MASK (1UL<<5)
		 * 
		 * #define PER_ATTR_ADC_STD (0UL<<6) #define PER_ATTR_ADC_ALT (1UL<<6)
		 * #define PER_ATTR_ADC_MASK (1UL<<6)
		 */
		return ("(" + PER_ATTR_LOCAL + ")").replaceAll("||", "|").replaceAll("|\\)", ")");
	}

	private String getPIN_ATTRArduino(List<String> line) {
		// "PIN_ATTR_EXTINT |PIN_ATTR_ANALOG|PIN_ATTR_DIGITAL|PIN_ATTR_ADC"
		String PIN_ATTR_LOCAL = "PIN_ATTR_NONE";

		String value = line.get(acsv.getColunmIndex(PADNAME));
		if (value.startsWith("P")) {
			PIN_ATTR_LOCAL += "|PIN_ATTR_DIGITAL";
		} else {
		}

		if (getEXTERNAL_INTArduino(line).contentEquals("NOT_AN_INTERRUPT")) {
		} else
			PIN_ATTR_LOCAL += "|PIN_ATTR_EXTINT";
		if (getADC_ChannelArduino(line).contentEquals("No_ADC_Channel")) {
		} else
			PIN_ATTR_LOCAL += "|PIN_ATTR_ANALOG|PIN_ATTR_ADC";

		if (getON_TIMERArduino(line).contentEquals("NOT_ON_TIMER")) {
		} else {

			// PIN_ATTR_LOCAL+="|"+pinoftimer2Alt.get(line.get(acsv.getColunmIndex(
			// PADNAME)));//"|PIN_ATTR_TIMER_ALT";or "|PIN_ATTR_TIMER";
			PIN_ATTR_LOCAL += "|" + PIN_ATTR;
		}
		if (getON_SERCOMArduino(line).contentEquals("NOT_SERCOM")) {
		} else
			PIN_ATTR_LOCAL += "|" + PIN_ATTR;

		/*
		 * 
		 * #define PIN_ATTR_NONE (0UL << 0)
		 * 
		 * #define PIN_ATTR_INPUT (1UL << PIO_INPUT) #define PIN_ATTR_OUTPUT
		 * (1UL << PIO_OUTPUT) #define PIN_ATTR_INPUT_PULLUP (1UL <<
		 * PIO_INPUT_PULLUP) #define PIN_ATTR_INPUT_PULLDOWN (1UL <<
		 * PIO_INPUT_PULLDOWN) #define PIN_ATTR_DIGITAL
		 * (PIN_ATTR_INPUT|PIN_ATTR_INPUT_PULLUP
		 * |PIN_ATTR_INPUT_PULLDOWN|PIN_ATTR_OUTPUT)
		 * 
		 * #define PIN_ATTR_EXTINT (1UL << PIO_EXTINT)
		 * 
		 * #define PIN_ATTR_ADC (1UL << PIO_ANALOG_ADC) #define PIN_ATTR_ANALOG
		 * PIN_ATTR_ADC
		 * 
		 * #define PIN_ATTR_TIMER_PWM (1UL << PIO_TIMER_PWM) #define
		 * PIN_ATTR_TIMER_CAPTURE (1UL << PIO_TIMER_CAPTURE) #define
		 * PIN_ATTR_TIMER_BOTH (PIN_ATTR_TIMER_PWM|PIN_ATTR_TIMER_CAPTURE)
		 * #define PIN_ATTR_TIMER PIN_ATTR_TIMER_BOTH #define PIN_ATTR_SERCOM
		 * (1UL << PIO_SERCOM)
		 * 
		 * // todo : #define PIN_ATTR_DAC (1UL << PIO_ANALOG_DAC) #define
		 * PIN_ATTR_REF (1UL << PIO_ANALOG_REF) #define PIN_ATTR_AC (1UL <<
		 * PIO_ANALOG_AC) #define PIN_ATTR_PTC (1UL << PIO_ANALOG_PTC) #define
		 * PIN_ATTR_SDADC (1UL << PIO_ANALOG_SDADC)
		 * 
		 * #define PIN_ATTR_COM (1UL << PIO_COM) #define PIN_ATTR_AC_GCLK (1UL
		 * << PIO_AC_GCLK) #define PIN_ATTR_CCL (1UL << PIO_CCL)
		 */
		return "(" + PIN_ATTR_LOCAL + ")";
	}

	/*
	 * No_ADC_Channel=0xff, ADC_Channel0=0, ADC_Channel1=1, ADC_Channel2=2,
	 * ADC_Channel3=3, ADC_Channel4=4, ADC_Channel5=5, ADC_Channel6=6,
	 * ADC_Channel7=7, ADC_Channel8=8, ADC_Channel9=9, ADC_Channel10=10,
	 * ADC_Channel11=11, ADC_Channel12=12, ADC_Channel13=13, ADC_Channel14=14,
	 * ADC_Channel15=15, ADC_Channel16=16, ADC_Channel17=17, ADC_Channel18=18,
	 * ADC_Channel19=19, DAC_Channel0, DAC_Channel1,
	 */

	private String getADC_ChannelArduino(List<String> line) {
		// ADC/0/AIN[3] ADC/1/AIN[5] SDADC/INP[1]
		// ADC/0/AIN[2] ADC/1/AIN[4] SDADC/INN[1]
		int index = acsv.getColunmIndex(ADC0_COL);
		String value = line.get(index);
		String tab[] = value.split("/");
		if (tab.length == 3)
			if (tab[0].equalsIgnoreCase("ADC")) {
				String c = tab[2];
				c = c.replace("AIN[", "");
				c = c.replace("]", "");
				return "ADC_Channel" + c;
			}
		if (tab.length == 2)
			if (tab[0].equalsIgnoreCase("ADC")) {
				String c = tab[1];
				c = c.replace("AIN[", "");
				c = c.replace("]", "");
				return "ADC_Channel" + c;
			}

		if (ADC1_COL != null) {
			index = acsv.getColunmIndex(ADC1_COL);
			value = line.get(index);
			tab = value.split("/");
			if (tab.length == 3)
				if (tab[0].equalsIgnoreCase("ADC")) {
					String c = tab[2];
					c = c.replace("AIN[", "");
					c = c.replace("]", "");
					return "ADC_Channel" + c;
				}
			if (tab.length == 2)
				if (tab[0].equalsIgnoreCase("ADC")) {
					String c = tab[1];
					c = c.replace("AIN[", "");
					c = c.replace("]", "");
					return "ADC_Channel" + c;
				}
		}

		return "No_ADC_Channel";
	}

	/*
	 * #define PER_ATTR_ADC_STD (0UL<<6) #define PER_ATTR_ADC_ALT (1UL<<6)
	 * #define PER_ATTR_ADC_MASK (1UL<<6)
	 */
	private String getADC_PERArduino(List<String> line) {
		// ADC/0/AIN[3] ADC/1/AIN[5] SDADC/INP[1]
		// ADC/0/AIN[2] ADC/1/AIN[4] SDADC/INN[1]
		int index = acsv.getColunmIndex(ADC0_COL);
		String value = line.get(index);
		String tab[] = value.split("/");
		if (tab.length == 3)
			if (tab[0].equalsIgnoreCase("ADC")) {
				String c = tab[2];
				c = c.replace("AIN[", "");
				c = c.replace("]", "");
				if (tab[1].equalsIgnoreCase("1")) {
					PER_ATTR = "PER_ATTR_ADC_ALT";

					return "PER_ATTR_ADC_ALT";
				} else {
					PER_ATTR = "PER_ATTR_ADC_STD";
					return "PER_ATTR_ADC_STD";
				}
			}
		if (ADC1_COL != null) {
			index = acsv.getColunmIndex(ADC1_COL);
			value = line.get(index);
			tab = value.split("/");
			if (tab.length == 3)
				if (tab[0].equalsIgnoreCase("ADC")) {
					String c = tab[2];
					c = c.replace("AIN[", "");
					c = c.replace("]", "");
					if (tab[1].equalsIgnoreCase("1")) {
						PER_ATTR = "PER_ATTR_ADC_ALT";

						return "PER_ATTR_ADC_ALT";
					} else {
						PER_ATTR = "PER_ATTR_ADC_STD";
						return "PER_ATTR_ADC_STD";
					}

				}
		}

		return "";
	}

	List<String> timersUsed = new ArrayList<String>();// timer
	Map<String, String> pin2timer = new HashMap<String, String>();// pin/timer
	Map<String, String> pinoftimer2Alt = new HashMap<String, String>();// pin/alt
																		// or
																		// std

	private String getON_TIMERArduino(List<String> line) {
		// TC/5/WO[0]
		// TC/4/WO[1] TCC/0/WO[7]
		// System.err.println("- "+line);
		if (pin2timer.get(line.get(acsv.getColunmIndex(PADNAME))) != null) {
			PER_ATTR = pinoftimer2Alt
					.get(line.get(acsv.getColunmIndex(PADNAME)));
			if (PER_ATTR == null)
			{
				PER_ATTR="PER_ATTR_TIMER";
				PIN_ATTR = "PIN_ATTR_TIMER";
		}
			else if (PER_ATTR.contains("_ALT"))
				PIN_ATTR = "PIN_ATTR_TIMER";
			else
				PIN_ATTR = "PIN_ATTR_TIMER";
			
			return pin2timer.get(line.get(acsv.getColunmIndex(PADNAME)));
		}

		int index = acsv.getColunmIndex(TIMER0_COL);
		String optionA = null;
		String optionB = null;
		String value = line.get(index);

		String tab[] = value.split("/");
		if (tab.length == 3)
			if (tab[0].startsWith("TC")) {// TCC2_CH3, TCC2_CH1
				String c = tab[2];
				c = c.replace("WO[", "");
				c = c.replace("]", "");
				optionA = tab[0] + tab[1] + "_CH" + c;
				// if (timersUsed.indexOf(optionB)<0)// if never used , use it
				{
					// timersUsed.add(optionA);
					// pin2timer.put(line.get(acsv.getColunmIndex(
					// PADNAME)),optionA);
					// return optionA;
				}

			}

		index = acsv.getColunmIndex(TIMER1_COL);
		value = line.get(index);
		tab = value.split("/");
		if (tab.length == 3)
			if (tab[0].startsWith("TC")) {// TCC2_CH3, TCC2_CH1
				String c = tab[2];
				c = c.replace("WO[", "");
				c = c.replace("]", "");
				optionB = tab[0] + tab[1] + "_CH" + c;
				// timersUsed.add(tab[0]+tab[1]+"_CH"+c);
				// pin2timer.put(line.get(acsv.getColunmIndex(
				// PADNAME)),optionB);
				// return optionB;
			}
		if (optionA == null) {
			if (optionB == null) {
				timersUsed.add("NOT_ON_TIMER");
				pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)),
						"NOT_ON_TIMER");
				PER_ATTR = "NOT_ON_TIMER";
				PIN_ATTR = "";
				return "NOT_ON_TIMER";
			} else {
				timersUsed.add(optionB);
				pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)), optionB);
				pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
						"PIN_ATTR_TIMER_ALT");
				PER_ATTR = "PER_ATTR_TIMER_ALT";
				PIN_ATTR = "PIN_ATTR_TIMER";
				return optionB;
			}
		} else {
			if (optionB == null) {
				timersUsed.add(optionA);
				pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)), optionA);
				pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
						"PIN_ATTR_TIMER");
				PER_ATTR = "PER_ATTR_TIMER_STD";
				PIN_ATTR = "PIN_ATTR_TIMER";
				return optionA;
			} else {
				Map<String, Integer> timercount = JavaUtils
						.CountElementInList(timersUsed);
				if (timercount.get(optionA) == null) {
					timersUsed.add(optionA);
					pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)),
							optionA);
					pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
							"PIN_ATTR_TIMER");
					PER_ATTR = "PER_ATTR_TIMER_STD";
					PIN_ATTR = "PIN_ATTR_TIMER";
					return optionA;
				} else if (timercount.get(optionB) == null) {
					timersUsed.add(optionB);
					pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)),
							optionB);
					pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
							"PIN_ATTR_TIMER_ALT");
					PER_ATTR = "PER_ATTR_TIMER_ALT";
					PIN_ATTR = "PIN_ATTR_TIMER";
					return optionB;
				}

				if (timercount.get(optionA) <= timercount.get(optionB)) {
					timersUsed.add(optionA);
					pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)),
							optionA);
					pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
							"PIN_ATTR_TIMER");
					PER_ATTR = "PER_ATTR_TIMER_STD";
					PIN_ATTR = "PIN_ATTR_TIMER";
					return optionA;
				} else {
					timersUsed.add(optionB);
					pin2timer.put(line.get(acsv.getColunmIndex(PADNAME)),
							optionB);
					pinoftimer2Alt.put(line.get(acsv.getColunmIndex(PADNAME)),
							"PIN_ATTR_TIMER_ALT");
					PER_ATTR = "PER_ATTR_TIMER_ALT";
					PIN_ATTR = "PIN_ATTR_TIMER";

					return optionB;
				}

			}
		}

	}

	String PER_ATTR = "";
	String PIN_ATTR = "";

	private String getON_SERCOMArduino(List<String> line) {
		// SERCOM/6/PAD[1] SERCOM/7/PAD[1]
		// SERCOM/6/PAD[0] SERCOM/7/PAD[0]

		int index = acsv.getColunmIndex(SERCOM0_COL);
		String value = line.get(index);
		String tab[] = value.split("/");
		if (tab.length == 3)
			if (tab[0].startsWith("SERCOM")) {// TCC2_CH3, TCC2_CH1
				String c = tab[2];
				c = c.replace("PAD[", "");
				c = c.replace("]", "");
				PER_ATTR = "PER_ATTR_SERCOM_STD";
				PIN_ATTR = "PIN_ATTR_SERCOM";
				return tab[0] + tab[1] + "_CH" + c;
			}

		index = acsv.getColunmIndex(SERCOM1_COL);
		value = line.get(index);
		tab = value.split("/");
		if (tab.length == 3)
			if (tab[0].startsWith("SERCOM")) {// TCC2_CH3, TCC2_CH1
				String c = tab[2];
				c = c.replace("PAD[", "");
				c = c.replace("]", "");
				PER_ATTR = "PER_ATTR_SERCOM_ALT";
				PIN_ATTR = "PIN_ATTR_SERCOM";
				return tab[0] + tab[1] + "_CH" + c;
			}

		PER_ATTR = "";
		PIN_ATTR = "";
		return "NOT_SERCOM";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String name[] = {
				"C:\\temp\\arduino\\xml\\rogue-U3019-pinout.csv",
				"C:\\temp\\arduino\\xml\\gambit_u3008_pinout.csv",
				 "C:\\temp\\arduino\\xml\\Beast-U3003-pinout.csv",
				"C:\\temp\\arduino\\xml\\SAMD10-U3006-pinout.csv",

				"C:\\temp\\arduino\\xml\\SAML21-U3005-pinout.csv",
				"C:\\temp\\arduino\\xml\\cyclops-U3004-pinout.csv",
				"C:\\temp\\arduino\\xml\\jubilee-U3011-pinout.csv", };
		// CsvToVariant csv=new
		// CsvToVariant("W:\\simulateur\\rogue_U3019\\xml\\pinout2.csv");
		// CsvToVariant csv=new
		// CsvToVariant("W:\\simulateur\\gambit_U3008\\xml\\pinout.csv");
		// CsvToVariant csv=new
		// CsvToVariant("W:\\simulateur\\cyclops_U3004\\xml\\U3004-pinout.csv");

		System.out.println("	#include <sam.h>");
		int MAX[] = { 32, 64, 100, 128 };
		for (int j = 0; j < MAX.length; j++)
			for (int i = 1; i <= MAX[j]; i++)
				System.out.println("#define TQFP" + MAX[j] + "_" + i
						+ "_ARDUINO\t    " + (i - 1));
		System.out.println("#if SAMxx ");
		for (String filename : name) {
			System.out.println("#elif SAMxx ");
			System.out.println("//" + filename + "\n");
			CsvToVariant csv = new CsvToVariant(filename);

			csv.autodetect();
			System.out.println(csv.generate());
			System.out
					.println("#define SUPPLY_ARDUINO	{ (uint8_t) -1,(uint8_t)  PIO_NOT_A_PIN,(uint8_t)  PIO_NOT_A_PIN,(uint8_t) (PER_ATTR_NONE),(uint32_t)  (PIN_ATTR_NONE),(uint8_t) NOT_ON_TIMER,(uint8_t)No_ADC_Channel,(uint8_t)  EXTERNAL_INT_NONE	,(uint8_t)GCLK_CCL_NONE  } ");

			System.out.println();
			System.out.println("//Pinout");
			System.out.println(csv.generatePinout());
			System.out.println();
			System.out.println();

			// System.out.println(csv.acsv.toString());

			// System.out.println(csv.acsv.getColunm(csv.PADNAME));
		}
		//
		System.out.println("	#endif");
	}

}
