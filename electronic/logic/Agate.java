/**
 * 
 */
package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre V
 *
 *vppossible instance are And,NAnd,Or,NOr,Xor,Nxor,Not,One,FlopD
 *
 */
public class Agate implements Igate {

	/**
	 * list of inputs
	 */
	protected List<Bit> Inputs;
	/**
	 * list of outputs
	 */
	protected List<Bit> Outputs;

	@Override
	public List<Bit> getInputs() {
		return Inputs;
	}

	@Override
	public Bit getOutput() {
		if (Outputs != null && Outputs.size() > 0)
			return Outputs.get(0);
		return null;
	}

	@Override
	public List<Bit> getOutputs() {

		return Outputs;
	}

	/**
	 * compute the next data in next tp.
	 */
	public void refresh() {
		// @todo
	}

	/**
	 * indicate if there is a comming change on apply.
	 */
	@Override
	public boolean willChange() {
		return nextValue != getOutput().Value();
	}

	protected Boolean nextValue = null;

	@Override
	public void apply() {
		getOutput().setValue(nextValue);
	}

	@Override
	public void init() {
		nextValue = null;

		for (Bit bo : getOutputs())
			bo.init();
	}

	/**
	 * 
	 */
	public Agate(List<Bit> myInputs) {
		this.Inputs = myInputs;
		Outputs = new ArrayList<Bit>();
		Outputs.add(new Bit());
		init();
	}

	public Agate(Bit bit, Bit bit2) {
		this.Inputs = new ArrayList<Bit>();
		Inputs.add(bit);
		Inputs.add(bit2);
		Outputs = new ArrayList<Bit>();
		Outputs.add(new Bit());
		init();
	}

	public Agate(Bit bit) {
		this.Inputs = new ArrayList<Bit>();
		Inputs.add(bit);
		Outputs = new ArrayList<Bit>();
		Outputs.add(new Bit());
		init();
	}

	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo = new ArrayList<String>();
		for (int i = 0; i < Math.max(Inputs.size(), 1); i++)
			lo.add("In" + i);
		return lo;
	}

	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo = new ArrayList<String>();
		for (int i = 0; i < Math.max(Outputs.size(), 1); i++)
			lo.add("Out" + i);
		return lo;
	}

	@Override
	public void setInputs(Bit in0, Bit in1) {
		Inputs = new ArrayList<Bit>();
		Inputs.add(in0);
		Inputs.add(in1);
	}

	@Override
	public void setInputs(Bit in0, Bit in1, Bit in2) {
		Inputs = new ArrayList<Bit>();
		Inputs.add(in0);
		Inputs.add(in1);
		Inputs.add(in2);
	}

	@Override
	public void setInputs(Bit in0) {
		Inputs = new ArrayList<Bit>();
		Inputs.add(in0);

	}

	@Override
	public void setInputs(List<Bit> ins) {
		Inputs = ins;

	}

	String name = null;

	@Override
	public String getName() {
		if (name != null)
			return name;
		return this.getClass().getSimpleName() + "_" + hashCode();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isCombinatory() {
		return true;
	}

	public String[] toWave(int nb_cyclops, int nb_tp) {
		assert (nb_tp > 0);
		assert (nb_cyclops > 0);
		int nbNet = getInputsNomenclature().size() + getOutputsNomenclature().size()
		// + getInternalWires().size()
		;

		String[] waves = new String[nbNet];
		int i = 0;
		for (String name : waves)
			waves[i++] = "";

		int len = 6;

		for (String e : getInputsNomenclature())
			if (len < e.length())
				len = e.length();
		for (String e : getOutputsNomenclature())
			if (len < e.length())
				len = e.length();
		// header
		i = 0;
		for (String name : getInputsNomenclature())
			waves[i++] += String.format("%" + len + "s|", name);
		for (String name : getOutputsNomenclature())
			waves[i++] += String.format("%" + len + "s|", name);

		Bus b = new Bus(getInputs());
		for (int cyc = 0; cyc < nb_cyclops; cyc++) {
			b.setValue(cyc);

			// compute
			// for(int d=0;d<(1<<getInputs().size());d++)
			for (int j = 0; j < nb_tp; j++) {

				for (Bit e : getOutputs()) {
					Boolean oldv = null;

					{
						refresh();
						apply();
						oldv = e.Value();
					}
					refresh();
					apply();

				}
				// display
				i = 0;
				for (Bit bi : getInputs())
					waves[i++] += (bi.Value() == null) ? "x" : ((bi.Value() == true) ? "-" : "_");
				for (Bit bo : getOutputs())
					waves[i++] += (bo.Value() == null) ? "x" : ((bo.Value() == true) ? "-" : "_");
			}
		}
		return waves;

	}

	public String toTruthTable() {
		String s = "|";
		int len = 6;

		for (String e : getInputsNomenclature())
			if (len < e.length())
				len = e.length();
		for (String e : getOutputsNomenclature())
			if (len < e.length())
				len = e.length();
		for (String e : getInputsNomenclature())
			s += String.format("%" + len + "s|", e);
		s += "|";
		for (String e : getOutputsNomenclature())
			s += String.format("%" + len + "s|", e);
		s += "\r\n";
		String p = "";
		for (int i = 2; i < s.length(); i++)
			p += "-";
		s += p + "\r\n";
		Bus bbi = new Bus(getInputs());
		Bus bbo = new Bus(getOutputs());
		for (int d = 0; d < (1 << getInputs().size()); d++) {
			
			for (Bit bi : bbi.getInputs()) {
				this.init();
				bbi.setValue(d);
				for(int i=0;i<getMaxTp();i++)
				{refresh();	apply();}
				Bus bbo_old = Bus.copy(bbo);
				this.init();
				bbi.setValue(d);
				bi.toggle();
				Bus bbi_old = Bus.copy(bbi);
				for(int i=0;i<getMaxTp();i++)
				{refresh();	apply();}
								
				bi.toggle();
				for(int i=0;i<getMaxTp();i++)
				{refresh();	apply();}
				
				if (!bbo.equals(bbo_old)) {
					s += "|";
					s += displayTT(len, bbi_old, bbi, bbo_old, bbo);
					s += "\r\n";
				}
			}
			//s += "|";
			this.init();
			bbi.setValue(d);
			// for(Bit e:bbo)
			{/*
				 * Boolean oldv=null; for(int i=0;i<10;i++) {refresh();apply();oldv=e.Value();}
				 */
				for(int i=0;i<getMaxTp();i++)
				{refresh();	apply();}
	
			}
			s += "|";
			s += displayTT(len, bbi, bbo);
			s += "\r\n";
		}

		// @todo do an exception for CLK signal to manage the sequence :
		// b.setValue(d,clk=0);refresh();apply();b.setValue(d,clk=1);refresh();apply();
		return s;

	}

	public int getMaxTp() {
		
		return 1;
	}

	private String displayTT(int len, Bus bbi, Bus bbo) {
		String s = "";
		for (Bit bi : bbi.getInputs())
			s += String.format("%" + len + "s|", bi.value());
		s += "|";
		for (Bit bo : bbo.getInputs())
			s += String.format("%" + len + "s|", bo.value());

		return s;
	}

	private String displayTT(int len, Bus bbi_old, Bus bbi, Bus bbo_old, Bus bbo) {
		String s = "";
		for (int i = 0; i < bbi.getInputs().size(); i++) {
			Bit bi = bbi.getInputs().get(i);
			Bit bio = bbi_old.getInputs().get(i);
			if (bi.equals(bio))
				s += String.format("%" + len + "s|", bi.value());
			else
				s += String.format("%" + (len / 2 - 1) + "s->%" + (len - len / 2 - 1) + "s|", bio.value(), bi.value());
		}
		s += "|";
		for (int i = 0; i < bbo.getInputs().size(); i++) {
			Bit bo = bbo.getInputs().get(i);
			Bit boo = bbo_old.getInputs().get(i);
			if (bo.equals(boo))
				s += String.format("%" + len + "s|", bo.value());
			else
				s += String.format("%" + (len / 2 - 1) + "s->%" + (len - len / 2 - 1) + "s|", boo.value(), bo.value());
		}

		return s;
	}
}