package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.PIE.Tree;

public class CompressPIE {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testaddWord() {
		Tree<ISymbol, Long> tree=new Tree<ISymbol, Long>();
		String s="ABCDAAABBDD";
		tree.addWord(0L,Symbol.from(s) );
		s="ABCDBAABBDD";
		tree.addWord(11L,Symbol.from(s) );
		
	//	System.out.println(tree.toGraphViz());
		
		assertEquals("digraph tree {\r\n" + 
				"\"null_null_26117480\" -> \"'A'_0_870698190\"\r\n" + 
				"\"'A'_0_870698190\" -> \"'B'_1_1514322932\"\r\n" + 
				"\"'B'_1_1514322932\" -> \"'C'_2_654582261\"\r\n" + 
				"\"'C'_2_654582261\" -> \"'D'_3_1389647288\"\r\n" + 
				"\"'D'_3_1389647288\" -> \"'A'_4_1330278544\"\r\n" + 
				"\"'D'_3_1389647288\" -> \"'B'_15_1634198\"\r\n" + 
				"\"'A'_4_1330278544\" -> \"'A'_5_110456297\"\r\n" + 
				"\"'A'_5_110456297\" -> \"'A'_6_1989972246\"\r\n" + 
				"\"'A'_6_1989972246\" -> \"'B'_7_1791930789\"\r\n" + 
				"\"'B'_7_1791930789\" -> \"'B'_8_762152757\"\r\n" + 
				"\"'B'_8_762152757\" -> \"'D'_9_12209492\"\r\n" + 
				"\"'D'_9_12209492\" -> \"'D'_10_314337396\"\r\n" + 
				"\"'B'_15_1634198\" -> \"'A'_16_232824863\"\r\n" + 
				"\"'A'_16_232824863\" -> \"'A'_17_1282788025\"\r\n" + 
				"\"'A'_17_1282788025\" -> \"'B'_18_519569038\"\r\n" + 
				"\"'B'_18_519569038\" -> \"'B'_19_1870252780\"\r\n" + 
				"\"'B'_19_1870252780\" -> \"'D'_20_1729199940\"\r\n" + 
				"\"'D'_20_1729199940\" -> \"'D'_21_97730845\"\r\n" + 
				"}\r\n", tree.toGraphViz());
	}

	@Test
	public final void testadd() {
		Tree<ISymbol, Long> tree=new Tree<ISymbol, Long>();
		String s="ABCDAAABBDD";
			tree.add(0L,Symbol.from(s) );
		s="ABCDBAABBDD";
			tree.add(0L,Symbol.from(s) );
		
		System.out.println(tree.toGraphViz());
		
		assertEquals("digraph tree {\r\n" + 
				"\"null_null_1288141870\" -> \"'A'_0_2054881392\"\r\n" + 
				"\"null_null_1288141870\" -> \"'C'_2_966808741\"\r\n" + 
				"\"null_null_1288141870\" -> \"'B'_1_1908153060\"\r\n" + 
				"\"null_null_1288141870\" -> \"'D'_3_116211441\"\r\n" + 
				"\"'A'_0_2054881392\" -> \"'A'_5_607635164\"\r\n" + 
				"\"'A'_0_2054881392\" -> \"'B'_1_529116035\"\r\n" + 
				"\"'A'_5_607635164\" -> \"'A'_6_242481580\"\r\n" + 
				"\"'A'_5_607635164\" -> \"'B'_7_1627800613\"\r\n" + 
				"\"'A'_6_242481580\" -> \"'B'_7_2065530879\"\r\n" + 
				"\"'B'_7_2065530879\" -> \"'B'_8_697960108\"\r\n" + 
				"\"'B'_8_697960108\" -> \"'D'_9_943010986\"\r\n" + 
				"\"'D'_9_943010986\" -> \"'D'_10_1807837413\"\r\n" + 
				"\"'B'_7_1627800613\" -> \"'B'_8_2066940133\"\r\n" + 
				"\"'B'_8_2066940133\" -> \"'D'_9_48612937\"\r\n" + 
				"\"'D'_9_48612937\" -> \"'D'_10_325333723\"\r\n" + 
				"\"'B'_1_529116035\" -> \"'C'_2_1937962514\"\r\n" + 
				"\"'B'_1_529116035\" -> \"'B'_8_274064559\"\r\n" + 
				"\"'C'_2_1937962514\" -> \"'D'_3_1018081122\"\r\n" + 
				"\"'D'_3_1018081122\" -> \"'A'_4_242131142\"\r\n" + 
				"\"'D'_3_1018081122\" -> \"'B'_4_1782113663\"\r\n" + 
				"\"'A'_4_242131142\" -> \"'A'_5_1433867275\"\r\n" + 
				"\"'A'_5_1433867275\" -> \"'A'_6_476800120\"\r\n" + 
				"\"'A'_6_476800120\" -> \"'B'_7_1744347043\"\r\n" + 
				"\"'B'_7_1744347043\" -> \"'B'_8_1254526270\"\r\n" + 
				"\"'B'_8_1254526270\" -> \"'D'_9_662441761\"\r\n" + 
				"\"'D'_9_662441761\" -> \"'D'_10_1618212626\"\r\n" + 
				"\"'B'_4_1782113663\" -> \"'A'_5_1129670968\"\r\n" + 
				"\"'A'_5_1129670968\" -> \"'A'_6_1023714065\"\r\n" + 
				"\"'A'_6_1023714065\" -> \"'B'_7_2051450519\"\r\n" + 
				"\"'B'_7_2051450519\" -> \"'B'_8_99747242\"\r\n" + 
				"\"'B'_8_99747242\" -> \"'D'_9_1837543557\"\r\n" + 
				"\"'D'_9_1837543557\" -> \"'D'_10_1971489295\"\r\n" + 
				"\"'B'_8_274064559\" -> \"'D'_9_985655350\"\r\n" + 
				"\"'D'_9_985655350\" -> \"'D'_10_804611486\"\r\n" + 
				"\"'C'_2_966808741\" -> \"'D'_3_2008017533\"\r\n" + 
				"\"'D'_3_2008017533\" -> \"'A'_4_370988149\"\r\n" + 
				"\"'D'_3_2008017533\" -> \"'B'_4_1395089624\"\r\n" + 
				"\"'A'_4_370988149\" -> \"'A'_5_1476011703\"\r\n" + 
				"\"'A'_5_1476011703\" -> \"'A'_6_1603195447\"\r\n" + 
				"\"'A'_6_1603195447\" -> \"'B'_7_792791759\"\r\n" + 
				"\"'B'_7_792791759\" -> \"'B'_8_1191747167\"\r\n" + 
				"\"'B'_8_1191747167\" -> \"'D'_9_1094834071\"\r\n" + 
				"\"'D'_9_1094834071\" -> \"'D'_10_1761061602\"\r\n" + 
				"\"'B'_4_1395089624\" -> \"'A'_5_1330106945\"\r\n" + 
				"\"'A'_5_1330106945\" -> \"'A'_6_1279149968\"\r\n" + 
				"\"'A'_6_1279149968\" -> \"'B'_7_59559151\"\r\n" + 
				"\"'B'_7_59559151\" -> \"'B'_8_1450821318\"\r\n" + 
				"\"'B'_8_1450821318\" -> \"'D'_9_668849042\"\r\n" + 
				"\"'D'_9_668849042\" -> \"'D'_10_434176574\"\r\n" + 
				"\"'B'_1_1908153060\" -> \"'A'_5_2096057945\"\r\n" + 
				"\"'B'_1_1908153060\" -> \"'C'_2_1689843956\"\r\n" + 
				"\"'B'_1_1908153060\" -> \"'B'_8_766572210\"\r\n" + 
				"\"'B'_1_1908153060\" -> \"'D'_9_1020391880\"\r\n" + 
				"\"'A'_5_2096057945\" -> \"'A'_6_977993101\"\r\n" + 
				"\"'A'_6_977993101\" -> \"'B'_7_429313384\"\r\n" + 
				"\"'B'_7_429313384\" -> \"'B'_8_859417998\"\r\n" + 
				"\"'B'_8_859417998\" -> \"'D'_9_5592464\"\r\n" + 
				"\"'D'_9_5592464\" -> \"'D'_10_1830712962\"\r\n" + 
				"\"'C'_2_1689843956\" -> \"'D'_3_1112280004\"\r\n" + 
				"\"'D'_3_1112280004\" -> \"'A'_4_1013423070\"\r\n" + 
				"\"'D'_3_1112280004\" -> \"'B'_4_380936215\"\r\n" + 
				"\"'A'_4_1013423070\" -> \"'A'_5_142638629\"\r\n" + 
				"\"'A'_5_142638629\" -> \"'A'_6_707806938\"\r\n" + 
				"\"'A'_6_707806938\" -> \"'B'_7_705265961\"\r\n" + 
				"\"'B'_7_705265961\" -> \"'B'_8_428746855\"\r\n" + 
				"\"'B'_8_428746855\" -> \"'D'_9_317983781\"\r\n" + 
				"\"'D'_9_317983781\" -> \"'D'_10_987405879\"\r\n" + 
				"\"'B'_4_380936215\" -> \"'A'_5_1555845260\"\r\n" + 
				"\"'A'_5_1555845260\" -> \"'A'_6_874088044\"\r\n" + 
				"\"'A'_6_874088044\" -> \"'B'_7_104739310\"\r\n" + 
				"\"'B'_7_104739310\" -> \"'B'_8_1761291320\"\r\n" + 
				"\"'B'_8_1761291320\" -> \"'D'_9_1451043227\"\r\n" + 
				"\"'D'_9_1451043227\" -> \"'D'_10_783286238\"\r\n" + 
				"\"'B'_8_766572210\" -> \"'D'_9_1500056228\"\r\n" + 
				"\"'D'_9_1500056228\" -> \"'D'_10_1749186397\"\r\n" + 
				"\"'D'_9_1020391880\" -> \"'D'_10_1464642111\"\r\n" + 
				"\"'D'_3_116211441\" -> \"'A'_4_105704967\"\r\n" + 
				"\"'D'_3_116211441\" -> \"'B'_4_392292416\"\r\n" + 
				"\"'D'_3_116211441\" -> \"'D'_10_1818402158\"\r\n" + 
				"\"'A'_4_105704967\" -> \"'A'_5_1590550415\"\r\n" + 
				"\"'A'_5_1590550415\" -> \"'A'_6_1058025095\"\r\n" + 
				"\"'A'_6_1058025095\" -> \"'B'_7_665576141\"\r\n" + 
				"\"'B'_7_665576141\" -> \"'B'_8_1599771323\"\r\n" + 
				"\"'B'_8_1599771323\" -> \"'D'_9_1876631416\"\r\n" + 
				"\"'D'_9_1876631416\" -> \"'D'_10_1359044626\"\r\n" + 
				"\"'B'_4_392292416\" -> \"'A'_5_692342133\"\r\n" + 
				"\"'A'_5_692342133\" -> \"'A'_6_578866604\"\r\n" + 
				"\"'A'_6_578866604\" -> \"'B'_7_353842779\"\r\n" + 
				"\"'B'_7_353842779\" -> \"'B'_8_1338823963\"\r\n" + 
				"\"'B'_8_1338823963\" -> \"'D'_9_1156060786\"\r\n" + 
				"\"'D'_9_1156060786\" -> \"'D'_10_1612799726\"\r\n" + 
				"}\r\n" + 
				"", tree.toGraphViz());
	}

	@Test
	public final void testAddVT() {
		Tree<ISymbol, Long> tree = new Tree<ISymbol, Long>();
		String s=":10004000A9040020AD040020B1040020B504002064\r\n" + 
				":10005000B9040020BD040020C1040020C504002014\r\n" + 
				":10006000C9040020CD040020D1040020D5040020C4\r\n" + 
				":10007000D9040020DD040020E1040020E504002074\r\n" + 
				":10008000E9040020ED040020F1040020F504002024\r\n" + 
				":10009000F9040020FD0400200105002005050020D2\r\n" + 
				":1000A000090500200D050020110500201505002080\r\n" + 
				":1000B000190500201D050020210500202505002030\r\n" + 
				":1000C000290500202D0500203105002035050020E0\r\n" + 
				":1000D000390500203D050020410500204505002090\r\n" + 
				":1000E000490500204D050020510500205505002040\r\n" + 
				":1000F000590500205D0500206105002065050020F0\r\n" + 
				":10010000690500206D05002071050020750500209F\r\n" + 
				":10011000790500207D05002081050020850500204F\r\n" + 
				":10012000890500208D0500209105002095050020FF\r\n" + 
				":10013000990500209D050020A1050020A5050020AF\r\n" + 
				":10014000A9050020AD050020B1050020B50500205F\r\n" + 
				":10015000B9050020BD050020C1050020C50500200F\r\n" + 
				":10016000C9050020CD050020D1050020D5050020BF\r\n" + 
				":10017000D9050020DD050020E1050020E50500206F\r\n" + 
				":10018000E9050020ED050020F1050020F50500201F\r\n" + 
				":10019000F9050020FD0500200106002005060020CD\r\n" + 
				":1001A000090600200D06002011060020150600207B\r\n" + 
				":1001B000190600201D06002021060020250600202B\r\n" + 
				":1001C000290600202D0600203106002035060020DB\r\n" + 
				":1001D000390600203D06002041060020450600208B\r\n" + 
				":1001E000490600204D06002051060020550600203B\r\n" + 
				":1001F000590600205D0600206106002065060020EB\r\n" + 
				":10020000690600206D06002071060020750600209A\r\n" + 
				":10021000790600207D06002081060020850600204A\r\n" + 
				":10022000890600208D0600209106002095060020FA\r\n" + 
				":10023000990600209D060020A1060020A5060020AA\r\n" + 
				":10024000A9060020AD060020B1060020B50600205A\r\n" + 
				":10025000B9060020BD060020C1060020C50600200A\r\n" + 
				":10026000C9060020CD060020D1060020D5060020BA\r\n" + 
				":10027000D9060020DD060020E1060020E50600206A\r\n" + 
				":10028000E9060020ED060020F1060020F50600201A\r\n" ; 
			s=	":1003100000000000000000000000000000000000DD\r\n" + 
				":1003200000000000000000000000000000000000CD\r\n" + 
				":1003300000000000000000000000000000000000BD\r\n" + 
				":1003400000000000000000000000000000000000AD\r\n" + 
				":10035000000000000000000000000000000000009D\r\n" + 
				":10036000000000000000000000000000000000008D\r\n" + 
				":10037000000000000000000000000000000000007D\r\n" + 
				":10038000000000000000000000000000000000006D\r\n" + 
				":10039000000000000000000000000000000000005D\r\n" + 
				":1003A000000000000000000000000000000000004D\r\n" + 
				":1003B000000000000000000000000000000000003D\r\n" + 
				":1003C000000000000000000000000000000000002D\r\n" + 
				":1003D000000000000000000000000000000000001D\r\n" + 
				":1003E000000000000000000000000000000000000D\r\n" + 
				":1003F00000000000000000000000000000000000FD\r\n";// + 
				/*":1004000000040020310700201D07002021070020E4\r\n" + 
				":10041000390B002000000020DC0C00203D040020EF\r\n" + 
				":1004200041040020000000FF27474E554320562777\r\n" + 
				":10043000342E372E33000000704700BF704700BFD6"*/;
			tree.add(0L,Symbol.from(s) );
		/*s="ABCDBAABBDD";
			tree.add(0L,Symbol.from(s) );
		*/
		System.out.println(tree.toGraphViz());
		
		

	}

}
