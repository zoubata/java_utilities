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
public class Waltio {

	/**
	 * 
	 */
	public Waltio() {
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
		ex.read(userHomeDir+"\\Downloads\\ExportWaltio_fr(3).xlsx", "Vos données");
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
				String target = ex.getCell(row, "Date");// 2022-07-27 10:40:59
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRENCH);
				Date date = df.parse(target);

				String Currency = ex.getCell(row, "Monnaie ou jeton reçu");
				String CurrencyPrice = ex.getCell(row, "Prix du jeton du montant recu");
				String Type = ex.getCell(row, "Type");

				String GrossAmount = ex.getCell(row, "Montant reçu");
				
				String Description = ex.getCell(row, "Description");
				String ExchangePlateforme = ex.getCell(row, "Exchange / Plateforme");
				String Label = ex.getCell(row, "Label");
				String Note=Label+"-"+Description+"-"+ExchangePlateforme;
				String Fee = ex.getCell(row, "Frais");
				String FeeCurrency = ex.getCell(row, "Monnaie ou jeton des frais");
				String FeeCurrencyPrice = ex.getCell(row, "Prix du jeton des frais");
				
				String Send = ex.getCell(row, "Montant envoyé");
				String SendCurrency = ex.getCell(row, "Monnaie ou jeton envoyé");
				
					
				

				/*
				String GrossAmountEUR = ex.getCell(row, "Gross amount (EUR)");
				String FeeEUR = ex.getCell(row, "Fee (EUR)");
				String NetAmount = ex.getCell(row, "Net amount");
				String NetAmountEUR = ex.getCell(row, "Net amount (EUR)");
				String Note = ex.getCell(row, "Note");*/
				
				IToken tCurrency = w.getToken(Currency);				
				if (tCurrency == null)
					if (Currency != null)
							tCurrency = new Token(Currency);				
				IToken tFeeCurrency = w.getToken(FeeCurrency);				
				if (tFeeCurrency == null)
					if (FeeCurrency != null)
					tFeeCurrency = new Token(FeeCurrency);
				IToken tSendCurrency = w.getToken(SendCurrency);				
				if (tSendCurrency == null)
					if (SendCurrency != null)
					tSendCurrency = new Token(SendCurrency);

				double dGrossAmount =0.0;
				if(GrossAmount!=null && !GrossAmount.isBlank())
					dGrossAmount= Double.parseDouble(GrossAmount);
					double dFee =0.0;
				if(Fee!=null && !Fee.isBlank())
					dFee = Double.parseDouble(Fee);
				double dSend = 0.0;
				if(Send!=null && !Send.isBlank())
					dSend = Double.parseDouble(Send);
				
				if (("Dépôt").equals(Type) && ("").equals(Label)  )
					w.Deposit(date, tCurrency, dGrossAmount, tCurrency, dFee, "->"+tCurrency.getSymbol()+" - "+Note);
				else if (("Dépôt").equals(Type) && ("Airdrop").equals(Label)  )
					w.Deposit(date, tCurrency, dGrossAmount, tCurrency, dFee, "+>"+tCurrency.getSymbol()+" - "+Note);
				else if (("Dépôt").equals(Type) && ("Autre gain").equals(Label) )
					w.Payouts(date, tCurrency, dGrossAmount, tCurrency, dFee, "+"+tCurrency.getSymbol()+" - "+Note);
				else if (("Dépôt").equals(Type) && ("Masternode & Staking").equals(Label) )
					w.Payouts(date, tCurrency, dGrossAmount, tCurrency, dFee, "+"+tCurrency.getSymbol()+" - "+Note);
				else if (("Échange").equals(Type) )
				{
					w.Buy(date, tCurrency, dGrossAmount, tFeeCurrency, dFee, tSendCurrency.getSymbol()+"->"+tCurrency.getSymbol()+" - "+Note);
					w.Sell(date, tSendCurrency, dSend, tSendCurrency, 0, tSendCurrency.getSymbol()+"->"+tCurrency.getSymbol()+" - "+Note);
				}
				
				else if (("Retrait").equals(Type) &&("Frais").equals(Label) )
				{
					w.Fee(date, tSendCurrency, dSend, "->"+tSendCurrency.getSymbol()+" - "+Note);
					
				}
				else if (("Retrait").equals(Type) &&("Autre perte").equals(Label) )
				{
					w.Fee(date, tSendCurrency, dSend, "->"+tSendCurrency.getSymbol()+" - "+Note);
					
				}
				
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
