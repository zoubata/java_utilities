/**
 * 
 */
package com.zoubworld.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cz.jaybee.intelhex.Parser;
import cz.jaybee.intelhex.listeners.RangeData;

/**
 * @author zoubata
 * a class to manage intel hex format and bin format
 * see main
 */
public class BinToHex {

	/**
	 * 
	 */
	public BinToHex() {
		// TODO Auto-generated constructor stub
	}
	public static void main3(String[] args) {
		String[] arg1= {"upperAddress=0x08000000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\BFM@0x08000000.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\BFM@0x08000000.hex"};
		 main2(arg1);
		 String[] arg2= {"upperAddress=0x0A002000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\bootcfg1@0A002000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\bootcfg1@0A002000h.hex"};
		 main2(arg2);
		 String[] arg21= {"upperAddress=0x0A00A000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\bootcfg2@0A00A000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\bootcfg2@0A00A000h.hex"};
		 main2(arg21);
		 String[] arg3= {"upperAddress=0x0A007000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\calotp@0A007000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\calotp@0A007000h.hex"};
		 main2(arg3);
		 String[] arg41= {"upperAddress=0x0A00C000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\hsmcfg0@0A00C000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\hsmcfg0@0A00C000h.hex"};
		 main2(arg41);
		 String[] arg42= {"upperAddress=0x0A00D000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\hsmcfg1@0A00D000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\hsmcfg1@0A00D000h.hex"};
		 main2(arg42);
		 String[] arg43= {"upperAddress=0x0A00E000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\hsmcfg2@0A00E000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\hsmcfg2@0A00E000h.hex"};
		 main2(arg43);
		 String[] arg44= {"upperAddress=0x0A00F000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\hsmcfg3@0A00F000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\hsmcfg3@0A00F000h.hex"};
		 main2(arg44);
		 String[] arg5= {"upperAddress=0x0C000000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\PFM@0x0C000000.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\PFM@0x0C000000.hex"};
		 main2(arg5);

		 String[] arg61= {"upperAddress=0x0A000000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\usercfg1@0A000000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\usercfg1@0A000000h.hex"};
		 main2(arg61);
		 String[] arg62= {"upperAddress=0x0A008000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\usercfg2@0A008000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\usercfg2@0A008000h.hex"};
		 main2(arg62);

		 String[] arg71= {"upperAddress=0x0A001000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\userotp1@0A001000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\userotp1@0A001000h.hex"};
		 main2(arg71);
		 String[] arg72= {"upperAddress=0x0A009000","C:\\home_user\\work\\TPS\\sg801.dx.bu\\valid\\selftest\\device\\userotp2@0A009000h.bin","C:\\\\home_user\\\\work\\\\TPS\\\\sg801.dx.bu\\\\valid\\\\selftest\\\\device\\\\userotp2@0A009000h.hex"};
		 main2(arg72);

	}
	/**
	 * @param args
	 * it convert a bin file into an hex file see main(["--help"]);
	 */
	public static void main(String[] args) {
		/// create it
		Map<String,String> optionparam= null;
		ArgsParser myargs=null;
		
		optionparam= new HashMap<String,String>();
		// parameter "="
		optionparam.put("startAddress="," cpu start address");
		optionparam.put("upperAddress=","memory location");
		// argument : "" "string"
		optionparam.put("binfile"," the input binary file");
		optionparam.put("hexfile","the output hex file");
		// option"+" "-" "--"
		optionparam.put("--help"," this help");
		optionparam.put("-armSmart","automaticaly detect startAddress & startAddress");
		myargs=new ArgsParser(ArgsParser.class,optionparam);
		// parse it
		myargs.parse(args);
		/*if (!myargs.check())
			System.exit(-1);
	*/
		//use it
		String binfile=myargs.getArgument(1);
		String hexfile=myargs.getArgument(2);
		boolean armSmart=myargs.getOption("armSmart");
		String  strstartAddress=myargs.getParam("startAddress");
		String strupperAddress=myargs.getParam("upperAddress");
		long upperAddress=0;
		long startAddress=0;
		if( !strstartAddress.isBlank())
			if (strstartAddress.startsWith("0x")||strstartAddress.startsWith("0X"))
				startAddress=Long.parseUnsignedLong(strstartAddress.substring(2),16);
			else
				
			startAddress=Long.parseLong(strstartAddress);
		if( !strupperAddress.isBlank())
			if (strupperAddress.startsWith("0x") || (strupperAddress.startsWith("0X")))
				upperAddress=Long.parseUnsignedLong(strupperAddress.substring(2),16);
			else
			upperAddress=Long.parseLong(strupperAddress);
		myargs.SaveConfigFile(hexfile+".jconfig");
	
		//...
		
		RangeData data=new RangeData();
		File fbinfile=new File(binfile);
		data.loadbin((int)upperAddress, fbinfile);
		
		if (armSmart)
		{
			startAddress=data.getmem32(4);
			upperAddress=data.getmem32(4) & 0xffff0000;			
		}
		Parser p=new Parser();
		p.setDataListener(data);
		p.setStartAddress(startAddress);
		p.setUpperAddress(upperAddress);
		JavaUtils.saveAs(hexfile, p.toString());
		
		
		//...
		
		
		
	}

}
