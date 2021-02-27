package com.zoubworld.java.utils.compress.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegrityTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testXorLong() {

		Integrity i=new Integrity();
		assertEquals(true, i.xor((long)0x11));
		assertEquals(true, i.xor((long)0x81));
		assertEquals(true, i.xor((long)0xFF));
		assertEquals(true, i.xor((long)0xF0));
		assertEquals(true, i.xor((long)0x00));
		
		assertEquals(false, i.xor((long)0x1));
		assertEquals(false, i.xor((long)0xFE));
		assertEquals(false, i.xor((long)0x7F));
		
		assertEquals(true, i.xor((long)0x1000000000000001L));
		assertEquals(true, i.xor((long)0x8000000000000001L));
		assertEquals(true, i.xor((long)0xF0F0F0F0F0F0F00FL));
		assertEquals(true, i.xor((long)0xFFFFFFFFFFFFFFFFL));
		assertEquals(true, i.xor((long)0x00L));
		
		assertEquals(false, i.xor((long)0x1000000000000000L));
		assertEquals(false, i.xor((long)0xF00000000000000EL));
		assertEquals(false, i.xor((long)0x700000000000000FL));
		
		
		
			}

	@Test
	final void testBool2long() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testXorChar() {
		Integrity i=new Integrity();
		assertEquals(true, i.xor((char)0x1));
		assertEquals(true, i.xor((char)0xFE));
		assertEquals(true, i.xor((char)0x7F));
		
		assertEquals(false, i.xor((char)0x11));
		assertEquals(false, i.xor((char)0x81));
		assertEquals(false, i.xor((char)0xFF));
		assertEquals(false, i.xor((char)0xFF));
		assertEquals(false, i.xor((char)0x00));
		
		
		
	}

	@Test
	final void testGetIntegrity() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetRedondancy() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testProcessLongArrayArray() {
		Integrity itgt=new Integrity(16,8);
		long t[][] = new long[16][16];
		for (int y = 0; y < 16; y++)
			for (int x = 0; x < 16; x++)
					t[y][x] = (long) (Math.random() * Long.MAX_VALUE);
/*
		t[0][0] = (long) 8537278394529489920L;
		t[0][1] = (long) 6569763965576828928L;
		t[0][2] = (long) 4296722193856336896L;
		t[0][3] = (long) 7635694646563585024L;
		t[0][4] = (long) 2671147989649694720L;
		t[0][5] = (long) 5228184628252489728L;
		t[0][6] = (long) 904258564990001152L;
		t[0][7] = (long) 8403602102955625472L;
		t[0][8] = (long) 3242432829627846656L;
		t[0][9] = (long) 1062961110667441152L;
		t[0][10] = (long) 6396009066537731072L;
		t[0][11] = (long) 203772039081948160L;
		t[0][12] = (long) 8971144994970088448L;
		t[0][13] = (long) 261350709597505536L;
		t[0][14] = (long) 2879431320219249664L;
		t[0][15] = (long) 7353456036274715648L;
		t[1][0] = (long) 6258797913160462336L;
		t[1][1] = (long) 7689122505124885504L;
		t[1][2] = (long) 1890745084838887424L;
		t[1][3] = (long) 2508267929703845888L;
		t[1][4] = (long) 6943492650710978560L;
		t[1][5] = (long) 5098882552446340096L;
		t[1][6] = (long) 4770826990805666816L;
		t[1][7] = (long) 5383040248224588800L;
		t[1][8] = (long) 7531937733652883456L;
		t[1][9] = (long) 8244079310177520640L;
		t[1][10] = (long) 9130530209305590784L;
		t[1][11] = (long) 6901276103106045952L;
		t[1][12] = (long) 7086077448155206656L;
		t[1][13] = (long) 912522581054412800L;
		t[1][14] = (long) 7382782540024213504L;
		t[1][15] = (long) 1955318920239428608L;
		t[2][0] = (long) 6363497191443239936L;
		t[2][1] = (long) 2404737350122643456L;
		t[2][2] = (long) 8644011073458263040L;
		t[2][3] = (long) 6770903835856567296L;
		t[2][4] = (long) 3197611606617852928L;
		t[2][5] = (long) 1493051486352778240L;
		t[2][6] = (long) 4924219666551383040L;
		t[2][7] = (long) 361506909870635008L;
		t[2][8] = (long) 1850843325166823424L;
		t[2][9] = (long) 4325491558035783680L;
		t[2][10] = (long) 3718850848711773184L;
		t[2][11] = (long) 5475437616757009408L;
		t[2][12] = (long) 4075032902781214720L;
		t[2][13] = (long) 3835958783942052864L;
		t[2][14] = (long) 5569907156751950848L;
		t[2][15] = (long) 7434395362412179456L;
		t[3][0] = (long) 3665524674432141312L;
		t[3][1] = (long) 8759342383037898752L;
		t[3][2] = (long) 2322387464756155392L;
		t[3][3] = (long) 8574220860734802944L;
		t[3][4] = (long) 7601418786875549696L;
		t[3][5] = (long) 6268157532736102400L;
		t[3][6] = (long) 8688651533333991424L;
		t[3][7] = (long) 8794184966433094656L;
		t[3][8] = (long) 84126398246993920L;
		t[3][9] = (long) 8020713427358046208L;
		t[3][10] = (long) 815290356700199936L;
		t[3][11] = (long) 7461721131465444352L;
		t[3][12] = (long) 2864868472153137152L;
		t[3][13] = (long) 1341595372758500352L;
		t[3][14] = (long) 1896896541645204480L;
		t[3][15] = (long) 2673201566546829312L;
		t[4][0] = (long) 6958380587837800448L;
		t[4][1] = (long) 5062224925355322368L;
		t[4][2] = (long) 6099497637024219136L;
		t[4][3] = (long) 1710601488772353024L;
		t[4][4] = (long) 5244298234887763968L;
		t[4][5] = (long) 599401427507452928L;
		t[4][6] = (long) 6508566361282096128L;
		t[4][7] = (long) 2427646900931613696L;
		t[4][8] = (long) 799214203112252416L;
		t[4][9] = (long) 7137010929240688640L;
		t[4][10] = (long) 6020357127381470208L;
		t[4][11] = (long) 8278353106124591104L;
		t[4][12] = (long) 2576726116325487616L;
		t[4][13] = (long) 3052105254954736640L;
		t[4][14] = (long) 8567020449085762560L;
		t[4][15] = (long) 3160726957114945536L;
		t[5][0] = (long) 2623860848815960064L;
		t[5][1] = (long) 5270759719007764480L;
		t[5][2] = (long) 3062919790887150592L;
		t[5][3] = (long) 8037895257001151488L;
		t[5][4] = (long) 6421123035863503872L;
		t[5][5] = (long) 5974744375939396608L;
		t[5][6] = (long) 2332453624854029312L;
		t[5][7] = (long) 2421064538851292160L;
		t[5][8] = (long) 448568152790044672L;
		t[5][9] = (long) 2413721773423284224L;
		t[5][10] = (long) 7327246890203185152L;
		t[5][11] = (long) 3759318497955286016L;
		t[5][12] = (long) 8134743437703114752L;
		t[5][13] = (long) 2144195875514919936L;
		t[5][14] = (long) 6469037817432687616L;
		t[5][15] = (long) 1225445856013618176L;
		t[6][0] = (long) 134457672992576512L;
		t[6][1] = (long) 7874833974159236096L;
		t[6][2] = (long) 6977277079531683840L;
		t[6][3] = (long) 6264279932693747712L;
		t[6][4] = (long) 1495208220958134272L;
		t[6][5] = (long) 2175632771636265984L;
		t[6][6] = (long) 7715666400481352704L;
		t[6][7] = (long) 6194522882755659776L;
		t[6][8] = (long) 7761218388144403456L;
		t[6][9] = (long) 956286896275582976L;
		t[6][10] = (long) 1227238556410684416L;
		t[6][11] = (long) 7588076680233946112L;
		t[6][12] = (long) 5323149052968001536L;
		t[6][13] = (long) 3019007737109993472L;
		t[6][14] = (long) 1267559327102673920L;
		t[6][15] = (long) 555689895434444800L;
		t[7][0] = (long) 7263577238829274112L;
		t[7][1] = (long) 7363293193799973888L;
		t[7][2] = (long) 7991373988875688960L;
		t[7][3] = (long) 2290265751702845440L;
		t[7][4] = (long) 3419328595059604480L;
		t[7][5] = (long) 1226386063687397376L;
		t[7][6] = (long) 4253926648572688384L;
		t[7][7] = (long) 1647165552340532224L;
		t[7][8] = (long) 8594701967771485184L;
		t[7][9] = (long) 771172263110877184L;
		t[7][10] = (long) 6745444213595470848L;
		t[7][11] = (long) 456705929436436480L;
		t[7][12] = (long) 7360519109736064000L;
		t[7][13] = (long) 4332831689862428672L;
		t[7][14] = (long) 4013080752066078720L;
		t[7][15] = (long) 2045115901314718720L;
		t[8][0] = (long) 4178864204703657984L;
		t[8][1] = (long) 8320490016633888768L;
		t[8][2] = (long) 815249040995564544L;
		t[8][3] = (long) 5911855021786874880L;
		t[8][4] = (long) 7282416538663778304L;
		t[8][5] = (long) 5222770966800452608L;
		t[8][6] = (long) 8086411658357873664L;
		t[8][7] = (long) 8793254208847985664L;
		t[8][8] = (long) 5762926836704720896L;
		t[8][9] = (long) 7906849819272536064L;
		t[8][10] = (long) 7454994410300000256L;
		t[8][11] = (long) 29605092746592256L;
		t[8][12] = (long) 2358814258995398656L;
		t[8][13] = (long) 6563438084631489536L;
		t[8][14] = (long) 5446976762694185984L;
		t[8][15] = (long) 915186593573661696L;
		t[9][0] = (long) 4040667085227432960L;
		t[9][1] = (long) 647759782108118016L;
		t[9][2] = (long) 7745108714777323520L;
		t[9][3] = (long) 1506894808296308736L;
		t[9][4] = (long) 620835349025247232L;
		t[9][5] = (long) 225402203066612736L;
		t[9][6] = (long) 1272436708640485376L;
		t[9][7] = (long) 5853876835700438016L;
		t[9][8] = (long) 101069719594927104L;
		t[9][9] = (long) 7802178506543509504L;
		t[9][10] = (long) 1325410906726322176L;
		t[9][11] = (long) 7292393802875212800L;
		t[9][12] = (long) 9193066194571552768L;
		t[9][13] = (long) 2606842593180356608L;
		t[9][14] = (long) 3953374037989012480L;
		t[9][15] = (long) 8949724563597556736L;
		t[10][0] = (long) 1803148786801133568L;
		t[10][1] = (long) 1978604058913167360L;
		t[10][2] = (long) 8914110501413723136L;
		t[10][3] = (long) 326652723014088704L;
		t[10][4] = (long) 1435795760703986688L;
		t[10][5] = (long) 53026112652859392L;
		t[10][6] = (long) 2131207451126650880L;
		t[10][7] = (long) 2670407032362750976L;
		t[10][8] = (long) 389538508914531328L;
		t[10][9] = (long) 6134334225015538688L;
		t[10][10] = (long) 6219532063015983104L;
		t[10][11] = (long) 9141762591603300352L;
		t[10][12] = (long) 6585726888542475264L;
		t[10][13] = (long) 2133054142012144640L;
		t[10][14] = (long) 8759238693870472192L;
		t[10][15] = (long) 4996129499866363904L;
		t[11][0] = (long) 297164178845509632L;
		t[11][1] = (long) 5878569224252303360L;
		t[11][2] = (long) 5741607317629872128L;
		t[11][3] = (long) 3760237326224867328L;
		t[11][4] = (long) 8624769514150162432L;
		t[11][5] = (long) 6236794729330902016L;
		t[11][6] = (long) 2455166997833741312L;
		t[11][7] = (long) 7796982552011769856L;
		t[11][8] = (long) 2185084413323180032L;
		t[11][9] = (long) 5427394412182265856L;
		t[11][10] = (long) 7203930267198399488L;
		t[11][11] = (long) 4934474827506967552L;
		t[11][12] = (long) 7495158015000749056L;
		t[11][13] = (long) 2078771365668689920L;
		t[11][14] = (long) 4547625950833220608L;
		t[11][15] = (long) 2812387959865363456L;
		t[12][0] = (long) 7526130606206679040L;
		t[12][1] = (long) 6462202844413063168L;
		t[12][2] = (long) 831611853659064320L;
		t[12][3] = (long) 2087381310225507328L;
		t[12][4] = (long) 7789161527054327808L;
		t[12][5] = (long) 8651051512712139776L;
		t[12][6] = (long) 6792924858534162432L;
		t[12][7] = (long) 4564154986807016448L;
		t[12][8] = (long) 4734351462545950720L;
		t[12][9] = (long) 5043613032485602304L;
		t[12][10] = (long) 7081833765753191424L;
		t[12][11] = (long) 6266970907908358144L;
		t[12][12] = (long) 6718782107692838912L;
		t[12][13] = (long) 122434722381477888L;
		t[12][14] = (long) 1999606360894917632L;
		t[12][15] = (long) 1063888679765568512L;
		t[13][0] = (long) 563202919125969920L;
		t[13][1] = (long) 2581683050213664768L;
		t[13][2] = (long) 389259898274088960L;
		t[13][3] = (long) 4107905362859632640L;
		t[13][4] = (long) 136442593920418816L;
		t[13][5] = (long) 3762197751698548736L;
		t[13][6] = (long) 3169386077213028352L;
		t[13][7] = (long) 5289000121259106304L;
		t[13][8] = (long) 6017946160000288768L;
		t[13][9] = (long) 1498953617435698176L;
		t[13][10] = (long) 292795223180846080L;
		t[13][11] = (long) 5373892603635398656L;
		t[13][12] = (long) 13395687022052352L;
		t[13][13] = (long) 413086076508649472L;
		t[13][14] = (long) 3632493677323419648L;
		t[13][15] = (long) 4549168596847836160L;
		t[14][0] = (long) 8235391697597739008L;
		t[14][1] = (long) 4645550380985995264L;
		t[14][2] = (long) 6873467969261627392L;
		t[14][3] = (long) 802455018431030272L;
		t[14][4] = (long) 3748193456222777344L;
		t[14][5] = (long) 7437211814755262464L;
		t[14][6] = (long) 8205344596168119296L;
		t[14][7] = (long) 3775933616552467456L;
		t[14][8] = (long) 8206831267589235712L;
		t[14][9] = (long) 2937939717256740864L;
		t[14][10] = (long) 3918006011741954048L;
		t[14][11] = (long) 8990928927043704832L;
		t[14][12] = (long) 5557355385674464256L;
		t[14][13] = (long) 6941352134512360448L;
		t[14][14] = (long) 226519536427900928L;
		t[14][15] = (long) 1362440841972920320L;
		t[15][0] = (long) 3611663428345005056L;
		t[15][1] = (long) 2562366350700700672L;
		t[15][2] = (long) 6497475322216280064L;
		t[15][3] = (long) 5986774524624265216L;
		t[15][4] = (long) 4155633105947768832L;
		t[15][5] = (long) 2359901289461897216L;
		t[15][6] = (long) 6780790046094164992L;
		t[15][7] = (long) 5963988325038465024L;
		t[15][8] = (long) 1249236577147459584L;
		t[15][9] = (long) 7039277454710495232L;
		t[15][10] = (long) 2828985193350001664L;
		t[15][11] = (long) 3943093180794483712L;
		t[15][12] = (long) 132601142199446528L;
		t[15][13] = (long) 9164235485161152512L;
		t[15][14] = (long) 2451642962706107392L;
		t[15][15] = (long) 2061682507452068864L;*/

		for (int y = 0; y < 16; y++)
			for (int x = 0; x < 16; x++)
				System.out.println("t["+y+"]["+x+"] = (long) "+t[y][x]+"L;");
		long l=itgt.process(t);
		assertEquals(""+l,true, itgt.check(l));
		assertEquals(0L, itgt.check(l,t));
		for(int i=0;i<32;i++)
			assertEquals(""+i,false, itgt.check(l^(1L<<i)));
		
		for(int i=48;i<64;i++)
			assertEquals(""+i,false, itgt.check(l^(1L<<i)));
		
		for(int i=0;i<32;i++)
			assertNotEquals(""+i,0, itgt.check(l^(1L<<i),t));
		for(int i=48;i<64;i++)
			assertNotEquals(""+i,0, itgt.check(l^(1L<<i),t));
		
	}

	@Test
	final void testProcessLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCheckLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCheckLongLongArrayArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testMap() {
		fail("Not yet implemented"); // TODO
	}

}
