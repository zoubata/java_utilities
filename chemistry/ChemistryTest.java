package com.zoubworld.chemistry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChemistryTest {

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
	public final void testGetStructures() {
	//	fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAtoms() {
		PeriodicElementTable t=new PeriodicElementTable();
		Atom h1=t.getAtom("H").clone();
		Atom h2=h1.clone();
		Atom o=t.getAtom("O");
		assertEquals("H : Hydrogï¿½ne (1)\r\n" + 
				"{\r\n" + 
				"	Bloc :  s\r\n" + 
				"	Capacité thermique :  14.3 J/g/K\r\n" + 
				"	Charge valable pour le rayon ionique : \r\n" + 
				"	Conductivité thermique :  0.18 W/m/K\r\n" + 
				"	Configuration électronique à l'état fondamental : 1s1\r\n" + 
				"	Couche :  1 K\r\n" + 
				"	Date de découverte :  1766\r\n" + 
				"	Découverte :  18e siècle\r\n" + 
				"	Découvreur(s) :  Henry Cavendish\r\n" + 
				"	Electronégativité (Allred) :  2.2\r\n" + 
				"	Electronégativité (Pauling) :  2.2\r\n" + 
				"	Enegie d'ionisation de M a M+ ( kJ/mol) : 1312\r\n" + 
				"	Enthalpie de fusion (ΔHf) :  0.12 kJ/mol\r\n" + 
				"	Enthalpie de vaporisation (ΔHv) :  0.46 kJ/mol\r\n" + 
				"	Etat chimique :  gazeux\r\n" + 
				"	Famille : Hydrogène\r\n" + 
				"	Isotopes Stables :  2\r\n" + 
				"	Isotopes émeteurs α :  -\r\n" + 
				"	Isotopes émeteurs β + / p :  -\r\n" + 
				"	Isotopes émeteurs β - / n :  1\r\n" + 
				"	Masse volumique :  76 kg/m3\r\n" + 
				"	Nom : Hydrogène\r\n" + 
				"	Nombre d'isotopes :  3\r\n" + 
				"	Orbitale :  1s\r\n" + 
				"	Origine :  nucléosynthèse primordiale\r\n" + 
				"	Pays de découverte :  Grande-Bretagne\r\n" + 
				"	Point d'ébullition :  20.28 K\r\n" + 
				"	Point d'ébullition2 : -252.9°C -423.2°F\r\n" + 
				"	Point de fusion :  14.01 K\r\n" + 
				"	Point de fusion2 : -259.1°C -434.5°F\r\n" + 
				"	Rayon atomique (calculé) :  53 pm\r\n" + 
				"	Rayon atomique (mesuré) :  25 pm\r\n" + 
				"	Rayon covalent :  38 pm\r\n" + 
				"	Rayon de Van der Waals :  120 pm\r\n" + 
				"	Rayon ionique :  -\r\n" + 
				"	Rayon métallique :  -\r\n" + 
				"	Réglementation (SGH) :  SGH02 Inflammable, SGH04 Gaz sous pression\r\n" + 
				"	Stabilité :  2 isotopes stable ou quasi-stable\r\n" + 
				"	Structure cristalline principale :  hexagonal\r\n" + 
				"	Symbole : H\r\n" + 
				"	Type de charge :  cation(s)\r\n" + 
				"	Volume molaire :  13.26 cm3/mole\r\n" + 
				"	Z : 1\r\n" + 
				"	affinité electronique de M a M- :  72.8 kJ/mol\r\n" + 
				"	d'ionisation(eV) : 3,8939\r\n" + 
				"	 Português : Hidrogênio\r\n" + 
				"	 Svenska : Väte\r\n" + 
				"	 Türkçe : Hidrojen\r\n" + 
				"	 castellano : Hidrógeno\r\n" + 
				"	 deutsch : Wasserstoff\r\n" + 
				"	 english : Hydrogen\r\n" + 
				"	 français : Hydrogène\r\n" + 
				"	 italiano : Idrogeno\r\n" + 
				"	 polski : Wodór\r\n" + 
				"	 čeština : Vodík\r\n" + 
				"	 русский язык : водород\r\n" + 
				"	 العربية : هيدروجين\r\n" + 
				"	 हिन्दी : हाइड्रोजन\r\n" + 
				"	 中文 : 氫\r\n" + 
				"	 日本語 : 水素\r\n" + 
				"}\r\n" + 
				"",h1.toString());
		assertEquals("Oxygï¿½ne",o.getName());
		assertEquals("O",o.getSymbol());
		assertEquals(8,o.getElectron());
		assertEquals(0 ,o.getNeutron());
		assertEquals(8,o.getProton());
		
		assertEquals("digraph dot  {\r\n" + 
				"graph [layout = circo,  fixedsize=true ]\r\n" + 
				h1.getId()+" [shape = circle,\r\n" + 
				"      style = filled,\r\n" + 
				"      color = red,\r\n" + 
				"      label = \"H\"];\r\n" + 
				"\r\n" + 
				"node [shape = circle,\r\n" + 
				"			      style = filled,\r\n" + 
				"			      color = black,]\r\n" + 
				" 1;\r\n" + 
				"	edge [style = filled, color = \"transparent\"]\r\n" + 
				"	"+h1.getId()+" -> { 1}\r\n" + 
				"}\r\n" + 
				"",h1.toDot());

		//System.out.println();
		//System.out.println(o.toDot());
		Bond b1=new Bond(h1,o,1);
		Bond b2=new Bond(h2,o,1);
		List<Bond> lb=new ArrayList<Bond>();
		lb.add(b1);
		lb.add(b2);
		
		Molecule m=Molecule.buildb(lb);
	//	System.out.println(m.toString());
	//	System.out.println(m.toDot());
		assertEquals("OHH",m.toString());
		
			assertEquals("graph {\r\n" + 
				"	"+h1.getId()+" [label=\"H\"]\r\n" + 
				"	"+o.getId()+" [label=\"O\"]\r\n" + 
				"	"+h2.getId()+" [label=\"H\"]\r\n" + 
				"		"+h1.getId()+" -- "+o.getId()+" [style=bold,label=\"1\"];\r\n" + 
				"		"+h2.getId()+" -- "+o.getId()+" [style=bold,label=\"1\"];\r\n" + 
				"	}\r\n" + 
				"",m.toDot());
		
	}

	@Test
	public final void testToDot() {
	//	fail("Not yet implemented"); // TODO
	}

}
