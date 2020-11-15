package com.zoubworld.java.utils.compress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.zoubworld.java.utils.compress.file.IBinaryWriter;
/**
 * @author
 *
 * 		This class contains several codes from several symbols inside a compositesymbols.
 *         
 */
public class CompositeCodes implements ICode {

	List<ICode> codes;
	CompositeSymbols sym;

	public CompositeCodes(CompositeSymbols compositeSymbols) {
		setSymbol(compositeSymbols);
	}

	public CompositeCodes(ICode code1, ICode code2) {
		codes = new ArrayList<ICode>();
		codes.add(code1);
		codes.add(code2);
		sym = null;// undefined symbol
	}

	@Override
	public int length() {
		int s = 0;
		for (ICode c : codes)
			s += c.length();
		return s;
	}

	@Override
	public ISymbol getSymbol() {
		// if (sym==null)

		return sym;
	}

	@Override
	public void setSymbol(ISymbol compositeSymbols) {
		if (CompositeSymbols.class.isInstance(compositeSymbols)) {
			sym = (CompositeSymbols) compositeSymbols;
			codes = new ArrayList<ICode>();
			for (ISymbol s : sym.getSs()) {
				codes.add(s.getCode());
			}
		}

	}

	@Override
	public char[] toCode() {
		throw new NotImplementedException("");
		// TODO Auto-generated method stub
		// return null;
	}

	@Override
	public String toRaw() {
		String s = "";
		for (ICode c : codes)
			s += c.toRaw();
		return s;
	}

	@Override
	public Integer getMsb(int index) {
		// TODO Auto-generated method stub
		throw new NotImplementedException("");
		// return null;
	}

	@Override
	public int compareToCode(ICode s2) {
		// TODO Auto-generated method stub
		throw new NotImplementedException("");
		// return 0;
	}

	@Override
	public int compareToInt(ICode iCode) {
		throw new NotImplementedException("");
		// return
		// ((CompositeSymbols)getSymbol()).getS0().getCode().compareToCode(iCode);
	}

	@Override
	public void write(IBinaryWriter out) {
		for (ICode c : codes)
			c.write(out);

	}

	public String toString() {
		String s = "Composites(";
		for (ICode c : codes)
			s += c.toRaw() + "+";

		s += ")";

		return s;
	}

	@Override
	public Long getLong() {
		if ((length() > 64))
			return null;
		long l = 0;
		int s = 0;
		for (ICode c : codes) {
			l += c.getLong() << s;
			s = c.length();
		}
		return l;
	}

	@Override
	public void huffmanAddBit(char c) {
		if (codes != null)
			codes.get(codes.size() - 1).huffmanAddBit(c);
		else
			throw new NotImplementedException("");
	}

	@Override
	public void write(FileOutputStream out) throws IOException {
		for (ICode c : codes)
			c.write(out);
	}
}