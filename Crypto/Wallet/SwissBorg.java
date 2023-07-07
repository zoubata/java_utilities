/**
 * 
 */
package com.zoubworld.Crypto.Wallet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.zoubworld.utils.ExcelArray;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class SwissBorg {

	/**
	 * 
	 */
	public SwissBorg() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 * @throws ParseException
	 */
	public static void main(String[] args)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		main2(args);
	}

	public static void main2(String[] args)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		ExcelArray ex = new ExcelArray();
		String userHomeDir = System.getProperty("user.home");
		
		ex.read(userHomeDir+"\\Downloads\\account_statement.xlsx", "Transactions", 8);
		IWallet w = new Wallet();
		IMarket m = new Market();
		ex.getHeader();
		boolean en = true;
		
		Map<Date,Map<IToken, Double>> h=new HashMap();
		Map<IToken, Double> a =null;
		for (List<String> row : ex.getData()) {
			/*
			 * if ("Local time".equals(row.get(0))) en=true;
			 */
			if (en) {
				String target = ex.getCell(row, "Time in UTC");// 2022-07-27 10:40:59
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				Date date = df.parse(target);

				String Currency = ex.getCell(row, "Currency");
				String Type = ex.getCell(row, "Type");

				String GrossAmount = ex.getCell(row, "Gross amount");
				String GrossAmountEUR = ex.getCell(row, "Gross amount (EUR)");
				String Fee = ex.getCell(row, "Fee");
				String FeeEUR = ex.getCell(row, "Fee (EUR)");
				String NetAmount = ex.getCell(row, "Net amount");
				String NetAmountEUR = ex.getCell(row, "Net amount (EUR)");
				String Note = ex.getCell(row, "Note");
				IToken tCurrency = w.getToken(Currency);
				if (tCurrency == null)
					tCurrency = new Token(Currency);

				double dGrossAmount = Double.parseDouble(GrossAmount);
				double dFee = Double.parseDouble(Fee);

				if (("Deposit").equals(Type))

					w.Deposit(date, tCurrency, dGrossAmount, tCurrency, dFee, Note);
				else if (("Payouts").equals(Type))

					w.Payouts(date, tCurrency, dGrossAmount, tCurrency, dFee, Note);
				else if (("Buy").equals(Type)||("Buy (Thematic)").equals(Type))
					w.Buy(date, tCurrency, dGrossAmount, tCurrency, dFee, Note);
				else if (("Sell").equals(Type)||("Sell (Thematic)").equals(Type))
					w.Sell(date, tCurrency, dGrossAmount, tCurrency, dFee, Note);
				else if (("Withdrawal").equals(Type))
					w.Withdrawal(date, tCurrency, dGrossAmount, tCurrency, dFee, Note);
				else
				{//	System.err.println("unsupported :" + row);
				
				}
				a=  w.getAsset();
				
				h.put(w.getActualDate(),a);
				ExcelArray ex2 = new ExcelArray(w.torowCsv());
				System.out.println(ex2.toMarkDown(16));// JavaUtils.Format(null)
			}
		}
		
		for(Date d:h.keySet())
		{
			Wallet.torowCsv(h.get(d),d,w);
		}
		
		
	}

}
