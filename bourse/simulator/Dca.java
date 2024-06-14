package com.zoubworld.bourse.simulator;

import java.util.HashMap;
import java.util.Map;

public class Dca {
	class Action {
		double price;
		int t=0;
		public void update()
		{
			price=priceavg() + Math.random() * 10-5;
			t++;
		}
		public double priceavg() {
			return 20 + +1*t/12.0;
		}
		public double price() {
			return price;
		}
	}

	class Account {
		public Account() {

		}
		String name="";
		public Account(String name) {
			this.name=name;
		}
		double liquid = 0.0;
		Map<Action, Long> protefeuil = new HashMap<Action, Long>();

		public void investAll(Action s) {
			double p = s.price();
			long q = (long) (liquid / p);

			Long nb = buy(s, p, q);
			System.out.println(name+" Buy " + q + " Actions at " + p + " price total " + nb + " actions cash: "+liquid);

		}

		/**
		 * @param s action
		 * @param p price
		 * @param q quantity
		 * @return
		 */
		public Long buy(Action s, double p, long q) {
			liquid -= q * p;
			Long nb = protefeuil.get(s);
			if (nb == null) {
				nb = 0L;
				protefeuil.put(s, 0L);
			}
			protefeuil.put(s, nb = nb + q);
			return nb;
		}

		public void invest(Action s, int quantity) {
			Long nb = buy(s, s.price(), quantity);
			System.out.println(name+" Buy " + quantity + " Actions at " + s.price() + " price total " + nb + " actions cash: "+liquid);

		}

	}

	public Dca() {

	}

	public static void main(String[] args) {
		
		Dca d=new Dca();
		Account a1 = d.new Account("pdac");
		Action s = d.new Action();
		Account a2 = d.new Account("Qfix");
		Account a3 = d.new Account("ifdec");
		a3.buy(s, 0, 0);
		for (int i = 0; i < 120; i++) {
			s.update();
			a1.liquid += 100;
			a1.investAll(s);
			a2.liquid += 100;
			a2.invest(s,5);
			a3.liquid += 100;
			if (s.priceavg()>s.price())
				a3.investAll(s);	
		}
		s.update();
		System.out.println(a1.name+" : "+(a1.protefeuil.get(s)*s.priceavg()+a1.liquid));
		System.out.println(a2.name+" : "+(a2.protefeuil.get(s)*s.priceavg()+a2.liquid));
		System.out.println(a3.name+" : "+(a3.protefeuil.get(s)*s.priceavg()+a3.liquid));
		
	}
}
