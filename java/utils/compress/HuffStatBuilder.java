/**
 * 
 */
package com.zoubworld.java.utils.compress;

/**
 * @author Pierre Valleau
 *
 */
public class HuffStatBuilder {

	/**
	 * 
	 */
	public HuffStatBuilder() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Symbol.initCode();// reset String dir =
		 * "src\\com\\zoubworld\\java\\utils\\compress\\data\\"; Set<String> l =
		 * JavaUtils.listFileNames(dir, "", false, true, true); ICodingRule cs8 = new
		 * CodingSet(CodingSet.UNCOMPRESS); ; ICodingRule cs10 = new
		 * CodingSet(CodingSet.COMPRESS01TO1x0); System.out.println(cs10.toString());
		 * for (String f : l) { StringBuffer sb = new StringBuffer();
		 * System.out.println("File : " + f + "\n");
		 * 
		 * 
		 * 
		 * 
		 * { IBinaryReader in = new BinaryStdIn(dir + f); IBinaryWriter out = new
		 * BinaryStdOut("c:\\temp\\" + f); in.setCodingRule(cs8);
		 * out.setCodingRule(cs10); List<ISymbol> ls = new ArrayList(); boolean b =
		 * false; try { short count = 0; while (!in.isEmpty()) { boolean b2 =
		 * in.readBoolean(); if (b == b2) count++; else { ISymbol s =
		 * Symbol.findId(count); if (s != null) ls.add(s); count = 1; } b = b2; }
		 * ISymbol s = Symbol.findId(count); if (s != null) ls.add(s); out.writes(ls);
		 * out.close(); in.close(); } catch (java.util.NoSuchElementException e) { }
		 * Map<ISymbol, Long> freq = ls.stream().parallel()
		 * .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		 * freq = JavaUtils.SortMapByValue(freq); sb.append("Entropie : " +
		 * HuffmanCode.getEntropie(freq)+": "+HuffmanCode.getEntropie(freq)*ls.size()+
		 * "\n"); for (ISymbol s : freq.keySet()) { sb.append(s + "\t:\t" + freq.get(s)
		 * + "\n"); }
		 * 
		 * } IBinaryReader in = new BinaryStdIn(dir + f); in.setCodingRule(cs8);
		 * List<ISymbol> ls = in.readSymbols(); in.close(); Map<ISymbol, Long> freq =
		 * Symbol.Freq(ls); freq = JavaUtils.SortMapByValue(freq); HuffmanCode cs2 =
		 * HuffmanCode.buildCode(freq);
		 * sb.append("--------------------------------------------" + "\n");
		 * sb.append("Ext : " + JavaUtils.ExtensionOfPath(f) + "\n");
		 * sb.append("File : " + f + "\n");
		 * 
		 * long count = 0; long size = 0; for (ISymbol s : freq.keySet()) { count +=
		 * freq.get(s);
		 * 
		 * } size = cs2.getBitSize(freq);
		 * 
		 * sb.append("size : " + ls.size() * 8 + "/" + size + " bits\n");
		 * sb.append("symbols : " + freq.keySet().size() + " \n");
		 * sb.append("Entropie : " + cs2.getEntropie() +
		 * " : "+ls.size()*cs2.getEntropie() + "\n"); sb.append("Id sym" + "\t:\t" +
		 * "Count" + "\t:\t" + "Bit stream" + "\n"); sb.append("All" + "\t:\t" + count +
		 * "\t:\t" + "    na    " + "\n");
		 * 
		 * for (ISymbol s : freq.keySet()) { sb.append(s.getId() + "\t:\t" + freq.get(s)
		 * + "\t:\t0b" + cs2.get(s).toRaw() + "\n"); }
		 * System.out.println(sb.toString()); }
		 */

	}

}
