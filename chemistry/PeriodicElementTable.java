/**
 * 
 */
package com.zoubwolrd.chemistry;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.zoubworld.utils.ExcelArray;

/**
 * @author M43507
 *
 */
public class PeriodicElementTable {

	String dataint="    1 Hydrog�ne H\r\n" + 
			"    2 H�lium He\r\n" + 
			"    3 Lithium Li\r\n" + 
			"    4 B�ryllium Be\r\n" + 
			"    5 Bore B\r\n" + 
			"    6 Carbone C\r\n" + 
			"    7 Azote N\r\n" + 
			"    8 Oxyg�ne O\r\n"+
			"    9 Fluor F\r\n" + 
			"    10 N�on Ne\r\n" + 
			"    11 Sodium Na\r\n" + 
			"    12 Magn�sium Mg\r\n" + 
			"    13 Aluminium Al\r\n" + 
			"    14 Silicium Si\r\n" + 
			"    15 Phosphore P\r\n" + 
			"    16 Soufre S\r\n" + 
			"    17 Chlore Cl\r\n" + 
			"    18 Argon Ar\r\n" + 
			"    19 Potassium K\r\n" + 
			"    20 Calcium Ca\r\n" + 
			"    21 Scandium Sc\r\n" + 
			"    22 Titane Ti\r\n" + 
			"    23 Vanadium V\r\n" + 
			"    24 Chrome Cr\r\n" + 
			"    25 Mangan�se Mn\r\n" + 
			"    26 Fer Fe\r\n" + 
			"    27 Cobalt Co\r\n" + 
			"    28 Nickel Ni\r\n" + 
			"    29 Cuivre Cu \r\n" + 
			"    30 Zinc Zn\r\n" +
			"    31 Gallium Ga\r\n"+
			"    32 Germanium Ge\r\n" + 
			"    33 Arsenic As\r\n" + 
			"    34 S�l�nium Se\r\n" + 
			"    35 Brome Br\r\n" + 
			"    36 Krypton Kr\r\n" + 
			"    37 Rubidium Rb\r\n" + 
			"    38 Strontium Sr\r\n" + 
			"    39 Yttrium Y\r\n" + 
			"    40 Zirconium Zr\r\n" + 
			"    41 Niobium Nb\r\n" + 
			"    42 Molybd�ne Mo\r\n" + 
			"    43 Techn�tium Tc\r\n" + 
			"    44 Ruth�nium Ru\r\n" + 
			"    45 Rhodium Rh\r\n" + 
			"    46 Palladium Pd\r\n" + 
			"    47 Argent Ag\r\n" + 
			"    48 Cadmium Cd\r\n" + 
			"    49 Indium In\r\n" + 
			"    50 �tain Sn\r\n" + 
			"    51 Antimoine Sb\r\n" + 
			"    52 Tellure Te\r\n" + 
			"    53 Iode I\r\n" + 
			"    54 X�non Xe\r\n" + 
			"    55 C�sium Cs\r\n" + 
			"    56 Baryum Ba\r\n" + 
			"    57 Lanthane La\r\n" + 
			"    58 C�rium Ce\r\n" + 
			"    59 Pras�odyme Pr \r\n" + 
			"    60 N�odyme Nd\r\n" + 
			"    61 Prom�thium Pm\r\n" + 
			"    62 Samarium Sm\r\n" + 
			"    63 Europium Eu\r\n" + 
			"    64 Gadolinium Gd\r\n" + 
			"    65 Terbium Tb\r\n" + 
			"    66 Dysprosium Dy\r\n" + 
			"    67 Holmium Ho\r\n" + 
			"    68 Erbium Er\r\n" + 
			"    69 Thulium Tm\r\n" + 
			"    70 Ytterbium Yb\r\n" + 
			"    71 Lut�cium Lu\r\n" + 
			"    72 Hafnium Hf\r\n" + 
			"    73 Tantale Ta\r\n" + 
			"    74 Tungst�ne W\r\n" + 
			"    75 Rh�nium Re\r\n" + 
			"    76 Osmium Os\r\n" + 
			"    77 Iridium Ir\r\n" + 
			"    78 Platine Pt\r\n" + 
			"    79 Or Au\r\n" + 
			"    80 Mercure Hg\r\n" + 
			"    81 Thallium Tl\r\n" + 
			"    82 Plomb Pb\r\n" + 
			"    83 Bismuth Bi\r\n" + 
			"    84 Polonium Po\r\n" + 
			"    85 Astate At\r\n" + 
			"    86 Radon Rn\r\n" + 
			"    87 Francium Fr\r\n" + 
			"    88 Radium Ra\r\n" + 
			"    89 Actinium Ac \r\n" +			
			"    90 Thorium Th\r\n" + 
			"    91 Protactinium Pa\r\n" + 
			"    92 Uranium U\r\n" + 
			"    93 Neptunium Np\r\n" + 
			"    94 Plutonium Pu\r\n" + 
			"    95 Am�ricium Am\r\n" + 
			"    96 Curium Cm\r\n" + 
			"    97 Berk�lium Bk\r\n" + 
			"    98 Californium Cf\r\n" + 
			"    99 Einsteinium Es\r\n" + 
			"    100 Fermium Fm\r\n" + 
			"    101 Mend�l�vium Md\r\n" + 
			"    102 Nob�lium No\r\n" + 
			"    103 Lawrencium Lr\r\n" + 
			"    104 Rutherfordium Rf\r\n" + 
			"    105 Dubnium Db\r\n" + 
			"    106 Seaborgium Sg\r\n" + 
			"    107 Bohrium Bh\r\n" + 
			"    108 Hassium Hs\r\n" + 
			"    109 Meitn�rium Mt\r\n" + 
			"    110 Darmstadtium Ds\r\n" + 
			"    111 Roentgenium Rg\r\n" + 
			"    112 Ununbium Uub\r\n" + 
			"    113 Ununtrium Uut\r\n" + 
			"    114 Ununquadium Uuq\r\n" + 
			"    115 Ununpentium Uup\r\n" + 
			"    116 Ununhexium Uuh\r\n" + 
			"    117 Ununseptium Uus (#)\r\n" + 
			"    118 Ununoctium Uuo \r\n";
	Atom[] table;
	/**
	 * 
	 */
	public PeriodicElementTable() {
		String tabs[]=dataint.split("\r\n");
		table=new Atom[tabs.length];
		for(String atom:tabs)
			if (atom!=null)
		{
			String tab[]=atom.split("\\s+");
			String number=tab[1];
			String name=tab[2];
			String sym=tab[3];
			int num=Integer.parseInt(number);
			Atom a=new Atom(name,sym,num);
			table[num-1]=a;			
		}
		try {
		ExcelArray e= new ExcelArray();
			e.read("C:\\Users\\M43507\\Documents\\chimie.xls.xlsx", "info");
			
		for(Atom a:table)
			if (a!=null)
		{
			String sym=	a.getSymbol();
			Integer indexrow=-1;
			indexrow=e.findiRow("Symbole",sym);
			if (indexrow!=null)
			if (indexrow>=0)
			a.getProperty().putAll(e.RowtoMap(indexrow));
		}
		e.flush();
		e.read("C:\\Users\\M43507\\Documents\\chimie.xls.xlsx", "nucleaire");
		for(Atom a:table)
			if (a!=null)
		{
			int indexrow=e.findiRow("Symbole", a.getSymbol());
			if (indexrow>=0)
			a.getProperty().putAll(e.RowtoMap(indexrow));
		}
		e.flush();
		e.read("C:\\Users\\M43507\\Documents\\chimie.xls.xlsx", "chimique");
		for(Atom a:table)
			if (a!=null)
		{
			int indexrow=e.findiRow("Symbole", a.getSymbol());
			if (indexrow>=0)
			a.getProperty().putAll(e.RowtoMap(indexrow));
		}
		e.flush();
		e.read("C:\\Users\\M43507\\Documents\\chimie.xls.xlsx", "electronique");
		for(Atom a:table)
			if (a!=null)
		{
			int indexrow=e.findiRow("Symbole", a.getSymbol());
			if (indexrow>=0)
			a.getProperty().putAll(e.RowtoMap(indexrow));
		}
		e.flush();
		
			e.read("C:\\Users\\M43507\\Documents\\chimie.xls.xlsx", "physique");
		
		for(Atom a:table)
			if (a!=null)
		{
			int indexrow=e.findiRow("Symbole", a.getSymbol());
			if (indexrow>=0)
			a.getProperty().putAll(e.RowtoMap(indexrow));
		}
		e.flush();
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public Atom getAtom(int Z)
	{
		return getTable()[Z-1];
	}
	public Atom getAtom(String symbolOrName)
	{
		for(Atom a: getTable())
			if (a.getSymbol().equalsIgnoreCase(symbolOrName))
			return a;
		
		for(Atom a: getTable())
			if (a.getName().equalsIgnoreCase(symbolOrName))
			return a;
		
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PeriodicElementTable t=new PeriodicElementTable();
		for( Atom a:t.getTable())
			System.out.println(a.toString());
	}

	/**
	 * @return the table
	 */
	public Atom[] getTable() {
		return table;
	}

}
