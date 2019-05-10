/**
 * 
 */
package com.zoubworld.java.utils.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.Map;

import com.zoubwolrd.java.utils.compress.Symbol;
import com.zoubworld.utils.ArgsParser;

/**
 * @author M43507
 *
 */
public class ArgsParserTest {
	Map<String,String> optionparam= null;
	ArgsParser args=null;
	/**
	 * @throws java.lang.Exception
	 */

	@Before
	public void setUp() throws Exception {
		
		
		{
			optionparam= new HashMap<String,String>();
			// parameter "="
				//	optionparam.put("filter="," regular expression to filter file to parse");
					
					// argument : ""
			optionparam.put("?","this app read filein and convert binary number to hexa number.");
			optionparam.put("Filein","path to files to read");
			optionparam.put("Fileout","path to files to read");
			optionparam.put("Filelog","path to files to read");
			optionparam.put("param1=0","help param1");
			optionparam.put("param2=99","help param2");
			optionparam.put("param3=","help param3");
			optionparam.put("--option1","help option1");
			optionparam.put("-option2","help option2");
			optionparam.put("+option3","help option3");
			optionparam.put("+option4","help option4");
			
			//	optionparam.put("Action"," list of action: \n\t- SaveLot : save lot info\n\t- SaveAllCsv : save csv\n\t- SaveAll : save all");
					
						// option"+"
					optionparam.put("-help"," this help");
					
			args=new ArgsParser(optionparam);		
		}
	}



	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#getArgument(int)}.
	 */
	@Test
	public void testGetArgument() {
		String argsmain[]= {"-help","filein1","fileout1"};
		args.parse(argsmain);
		
		assertEquals("filein1", args.getArgument(1));
		assertEquals("fileout1", args.getArgument(2));
		assertEquals(null, args.getArgument(3));
		assertEquals(null, args.getArgument(0));
		
	}

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#getParam(java.lang.String)}.
	 */
	@Test
	public void testGetParam() {
		String argsmain[]= {"-help","filein1","fileout1","param2=512"};
		args.parse(argsmain);
		
		assertEquals(null, args.getParam("param0"));
		assertEquals("0", args.getParam("param1"));
		assertEquals("512", args.getParam("param2"));
		assertEquals("", args.getParam("param3"));
		
	}

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#getOption(java.lang.String)}.
	 */
	@Test
	public void testGetOption() {
		String argsmain[]= {"+help","filein1","fileout1","-option4"};
		args.parse(argsmain);
		
		assertEquals(true, args.getOption("help"));
		assertEquals(false, args.getOption("option2"));
		assertEquals(true, args.getOption("option1"));
		assertEquals(true, args.getOption("option3"));
		assertEquals(false, args.getOption("option4"));
		assertEquals(null, args.getOption("option0"));
	}

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#check()}.
	 */
	@Test
	public void testCheck() {
		String argsmain[]= {"-help","filein1","fileout1","-option4"};
		
		String argsmain2[]= {"--help","filein1","-option4"};
		args.parse(argsmain2);
		assertEquals(false, args.check());
		
		args.parse(argsmain);
		
		assertEquals(true, args.check());
		
	}

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#displayConfig()}.
	 */
	@Test
	public void testDisplayConfig() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#help()}.
	 */
	@Test
	public void testHelp() {
	//	fail("Not yet implemented");
	}

	

	/**
	 * Test method for {@link com.zoubworld.utils.ArgsParser#isMap(java.lang.String)}.
	 */
	@Test
	public void testIsMap() {
	//	fail("Not yet implemented");
	}

}
