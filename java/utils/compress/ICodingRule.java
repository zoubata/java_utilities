package com.zoubworld.java.utils.compress;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.zoubworld.java.utils.compress.algo.BytePairEncoding;
import com.zoubworld.java.utils.compress.algo.ByteTripleEncoding;
import com.zoubworld.java.utils.compress.algo.LZ4;
import com.zoubworld.java.utils.compress.algo.LZS;
import com.zoubworld.java.utils.compress.algo.LZSe;
import com.zoubworld.java.utils.compress.algo.LZW;
import com.zoubworld.java.utils.compress.algo.LZWBasic;
import com.zoubworld.java.utils.compress.algo.None;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.java.utils.compress.blockSorting.BWT;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.java.utils.compress.blockSorting.MTF;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryStream;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

import sandbox.TxtDiffInc;

/**
 * describe the coding rules between symbol and code and translation of code in
 * bit stream. the id/value of a symbol is a class constant the id/value of a
 * code is real time define by algos
 * 
 */
public interface ICodingRule {
	public final static int iRecallCodingSet = 0;
	public final static int iCodingSet = 1;
	public final static int iCodeNumberSet = 2;
	public final static int iHuffmanCode = 3;
	public final static int iShannonFanoEliasCode= 4;
	public final static int iAdaptativeHuffmanCode= 5;	
	public final static int iSubSetSymbol = 6;
	public final static int iSubSetNumber = 7;
	
	/** List of class available to build ICodingRule*/
	public static Class list[]= 
		{
				null,
			CodingSet.class,
			CodeNumberSet.class,
			HuffmanCode.class,
			ShannonFanoEliasCode.class,
			AdaptativeHuffmanCode.class,
			SubSetSymbol.class,
			SubSetNumber.class,
			
				}; 
	
	public static Long getClassNumber(ICodingRule cs)
	{
		for(long l=0;l<list.length;l++)
			if (list[(int)l]==cs.getClass())
			return l;
		throw new NotImplementedException("oups " );
		//return null;
	}
	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	ICode get(ISymbol sym);

	ISymbol get(ICode code);

	/** the sprout is the class used to buil ISymbol,
	 * a coding can be used for Symbol or Number,.... 
	 * the sprout is used to revert from a code the Symbol
	 * */
	void setSprout(ISymbol sprout);
	/**
	 * read a code from bit stream (with additional data on it for complex code)
	 */
	ICode getCode(IBinaryReader binaryStdIn);

	/**
	 * read a code from bit stream without any additional data(s2)
	 */
	public ICode getGenericCode(IBinaryReader binaryStdIn);

	/**
	 * read a symbol from bit stream
	 */
	ISymbol getSymbol(IBinaryReader binaryStdIn);


	@Override
	public boolean equals(Object obj);
	static public ICodingRule Factory(List<ISymbol> ls) {
		 Map<ISymbol, Long> m = ISymbol.Freq(ls);
		 return Factory(m);
	}
	static public ICodingRule Factory(Map<ISymbol, Long>  m) {
		ICodingRule cr=new CodeNumberSet(m);
		ICodingRule cr2=null;
		///CodingSet doesn't supper number
		if(!ISymbol.FreqClass(m.keySet()).keySet().contains(Number.class))
		{
		cr2=new CodingSet(m);
		Long l1=Symbol.length(m,cr);
		Long l2=Symbol.length(m,cr2);
		
		if( (l2!=null) && ((l1==null) || (l1>l2)))
			cr=cr2;
		}
		///@todo  today I can't code huffman with number. because no coding rules include symbol and number
		if(!ISymbol.FreqClass(m.keySet()).keySet().contains(Number.class))
		{
		cr2=new HuffmanCode(m);
		Long l1=Symbol.length(m,cr)+cr.toSymbol().getCode().length();
		Long l2=Symbol.length(m,cr2)+cr2.toSymbol().getCode().length();
		
		if( (l2!=null) && ((l1==null) || (l1>l2)))
			cr=cr2;
		}
		return cr;
	}
	
	

	/**
	 * write the coding rules information (the coding table)
	 * 
	 * N.B. a write of a coding set must be independant from bo.getCodingRules()
	 * 
	 * 
	 * CodingSet
	 * ICodingRule.getClassNumber(this)
	 * ISymbol.getClassNumber(sprout)
	 * =>
	 */
	public default void writeCodingRule(IBinaryWriter bo)
	{
		Long classNumber=ICodingRule.getClassNumber(this);
		Long configNumber=this.getParam();
		//iRecallCodingSet
		if (bo.getCodingRules().indexOf(this)>=0)
			{
			configNumber=(long)bo.getCodingRules().indexOf(this);
			classNumber=(long) ICodingRule.iRecallCodingSet;
			}
		if(   (this.getClass()==CodingSet.class)
				 ||  (this.getClass()==CodeNumberSet.class)
				 ||  (this.getClass()==HuffmanCode.class)
				 ||  (this.getClass()==ShannonFanoEliasCode.class)
				 ||  (this.getClass()==SubSetSymbol.class)
				 ||  (this.getClass()==SubSetNumber.class)
				 ||(classNumber==ICodingRule.iRecallCodingSet)
				  )
		{
		bo.write(Symbol.CodingSet);
		ICodingRule cr = bo.getCodingRule();
		bo.setCodingRule(null);
		bo.setCodingRule(new CodeNumberSet(CodeNumber.Golomb4Coding));
		bo.write(new Number(classNumber));// classe of coding set
		bo.write(new Number(configNumber));
	//	bo.write(new Number(classSymbolNumber));Symbol/Number/
		
		bo.setCodingRule(cr);
		}
		
	}
	/**
	 * read the coding rules information (the coding table) so read the Huffman tree
	 * based on coding rules of binaryStdIn
	 */
static ICodingRule ReadCodingRule(IBinaryReader binaryStdin) {
		 	ISymbol sym = binaryStdin.readSymbol();
return ReadCodingRule( sym, binaryStdin);
	}	
	/** This method must be call on getcode(IBinaryReader)/getSymbol(IBinaryReader)/ when the associated symbol is Symbol.CodingSet
	 * */
		static ICodingRule ReadCodingRule(ISymbol sym,IBinaryReader binaryStdin) {
			
		ICodingRule h = null;
		if (sym == Symbol.HUFFMAN)

		{
			HuffmanCode h2 = new HuffmanCode();
			h2.root = h2.readTrie(binaryStdin);
			h2.buildCode();

			return h2;
		}
		else
		if (sym == Symbol.CodingSet)// change on the fly

		{
			ICodingRule cr = binaryStdin.getCodingRule();
			binaryStdin.setCodingRule(new CodeNumberSet(CodeNumber.Golomb4Coding));
			ISymbol classNumber = binaryStdin.readSymbol();
			ISymbol configNumber = binaryStdin.readSymbol();

			switch ((int) classNumber.getId()) {
			case ICodingRule.iRecallCodingSet:
				h = binaryStdin.getCodingRules().get((int) configNumber.getId());
				break;
		
			case ICodingRule.iCodeNumberSet:
				h = new CodeNumberSet((int) configNumber.getId());
				break;
			case ICodingRule.iCodingSet:
				h = new CodingSet((long) configNumber.getId());
				break;
			case ICodingRule.iHuffmanCode:
				HuffmanCode h2 = new HuffmanCode();
				h2.root = h2.readTrie(binaryStdin);
				h2.buildCode();
				h = h2;
				break;
				

			case ICodingRule.iShannonFanoEliasCode:
				h = new ShannonFanoEliasCode( configNumber.getId(),binaryStdin);
				break;
			case ICodingRule.iSubSetSymbol:
				/** @todo to review and recode */
				h = (ICodingRule) new SubSetSymbol((int) configNumber.getId(),binaryStdin);
				break;
			case ICodingRule.iSubSetNumber:
				/** @todo to review and recode */
				h = (ICodingRule) new SubSetNumber((int) configNumber.getId(),binaryStdin);
				break;
				
			default:
				h=null;
				break;
			}
			binaryStdin.setCodingRule(cr);
			return h;
		}
		else 
		if (CompositeCode.isit(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			if (cs.getS0().equals(Symbol.INT12))

			{
				CompositeSymbol NbSym = (CompositeSymbol) sym;
				ISymbol symbitLen = binaryStdin.readSymbol();
				CompositeSymbol bitLen = (CompositeSymbol) symbitLen;

				h = new CodingSet((int) NbSym.getS1().getId(), (int) bitLen.getS1().getId(), binaryStdin);


				return h;
			}
		}

		return null;
	}
	/** parameter that describe the coding rules
	 * */
	default Long getParam( ) {return 0L;}

	/** represent the coding set by a symbol, it is used to change the coding set on the fly
	 * */
	public default ISymbol toSymbol()
	{
		ISymbol s2 =new Symbol();
		ICode code2=null;
		IBinaryStream bo=new BinaryFinFout();
		bo.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		this.writeCodingRule(bo);
		bo.flush();
		code2=Code.Factory(bo);//bo.toCode()
		code2.setSymbol(s2);
		s2.setCode(code2);
		CompositeSymbol cs=new CompositeSymbol(Symbol.CodingSet,s2 );
		cs.getS0().setCode(bo.getCodingRule().get(cs.getS0()));
		cs.setCode(new CompositeCode(cs));
		cs.setObj(this);
		return cs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}