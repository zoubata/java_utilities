package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

/* FAT12 f<4096B nb<4096
 * 	 id:16=12
 *   N:12
 *   [UnCompacted Size:12] 
 *   [name+'\0':8*len] 
 *   [crc:16] 
 *   [Stream of files]
 * FAT16 f<65535B nb<65535
 * 	 id:16=16
 *   N:16
 *   [UnCompacted Size:16] 
 *   [name+'\0':8*len] 
 *   [crc:16] 
 *   [Stream of files]
 * FAT24 f<16MB nb<16M
 * 	 id:16=24
 *   N:24
 *   [UnCompacted Size:24] 
 *   [name+'\0':8*len] 
 *   [crc:16]
 *   [Stream of files]
 * FAT32 f<4GB nb<4000m
 * 	 id:16=32
 *   N:32
 *   [UnCompacted Size:32] 
 *   [name+'\0':8*len] 
 *   [crc:64]
 *   [Stream of files]
 * FAT64 f<2^64b nb<2^64
 * 	 id:16=64
 *   N:64
 *   [UnCompacted Size:64] 
 *   [name+'\0':8*len] 
 *   [crc:64]
 *   [Stream of files:list<Symbol>+EOF]
 *   
 *   
 *   ...
 *   [CodingSet]
 *   [SymbolAlgo]
 * */
class FAT
{
	
	int id=32;
	int N=0;
	ArchiveDescriptor des;
	 public void save(IBinaryWriter bin)
		{
		
			bin.write(id, 16);
			/*fat.des=new ArchiveDescriptor();
			FileDescriptor fd=new FileDescriptor();*/
			bin.write(N, getlen());
			/*long size[]=new long[N];
			String name[]=new String[N];
			long crc[]=new long[N];
			for(int i=0;i<N;i++)
			bin.write(size[i], getlen());
			for(int i=0;i<N;i++)				
			bin.write(name[i]);
			for(int i=0;i<N;i++)
				bin.write(crc[i], getlen());*/
			for(FileDescriptor f:des.files)
				bin.write(f.getSize(), getlen());
			for(FileDescriptor f:des.files)			
				bin.write(f.getName() );
			if(id<32)
			for(FileDescriptor f:des.files)
				bin.write(f.getCrc16(), 16);
			else
			for(FileDescriptor f:des.files)
				bin.write(f.getCrc64(), 64);
//	int i=0;
			for(FileDescriptor f:des.files)
			{
				f.getEntropie();
				Map<ISymbol, Long> fq = f.getFreq();
				//Symbol.add1all(fq);
				Symbol.add(fq,Symbol.HUFFMAN);
				//Symbol.add(fq,Symbol.HuffRef);
				
			//	System.out.println(i+++":"+bin.getposOut());
				HuffmanCode cr =  HuffmanCode.Factory(fq);
				Long l1=Symbol.length(fq, cr)+cr.length();
				Long l2=Symbol.length(fq, bin.getCodingRule());
				if((l2==null) || (l1<l2))
					{
					bin.write(cr);
					bin.setCodingRule(cr);
				//	System.out.println(cr);
					}
				bin.writes(f.getSymList());
				//already included : bin.write(Symbol.EOF);
			}
		} 
	 /**
	 * @param id
	 * @param des
	 */
	public FAT(int id, ArchiveDescriptor des) {
		super();
		this.id = id;
		this.des = des;
		this.N=des.files.size();
	}
	public FAT( ArchiveDescriptor des) {
		super();
		load(  des);
	}
	public FAT(IBinaryReader bin) {
		super();
	read(bin);
	load(  des);
	}
	
		public void load( ArchiveDescriptor des)
		{
		this.des = des;
		this.N=des.files.size();

		long max=N;	
		for(FileDescriptor f:des.files)
		{
			max=Math.max(max, f.getSize());
		}
		if (max>(0xffffffffL))
			this.id = 64;
		else if (max>0x00ffffff)
			this.id = 32;
		else if (max>0x0000ffff)
			this.id = 24;
		else if (max>0x00000fff)
			this.id = 16;
		else
			this.id = 12;
	}
	public void read(IBinaryReader bin)
		{
			
			id=bin.readUnsignedInt(16);
			/*fat.des=new ArchiveDescriptor();
			FileDescriptor fd=new FileDescriptor();*/
			N=bin.readUnsignedInt(getlen());
			long size[]=new long[N];
			String name[]=new String[N];
			long crc[]=new long[N];
			List<ISymbol> listSym[]=new List[N];
			for(int i=0;i<N;i++)
				size[i]=bin.readUnsignedInt(getlen());
			for(int i=0;i<N;i++)
				name[i]=bin.readString();
			for(int i=0;i<N;i++)
				crc[i]=bin.readUnsignedInt((getlen()<32)?16:64);
			
			for(int i=0;i<N;i++) {
			//	System.out.println(i+":"+bin.getposIn());
				listSym[i]=readFile(bin);
		//		System.out.println(bin.getCodingRule());
				}
			des=new ArchiveDescriptor();
			String path="c:\\temp\\FAT\\";
			for(int i=0;i<N;i++)
				des.addfile(new FileDescriptor(path,name[i],size[i],crc[i],listSym[i]));
		}
	private List<ISymbol> readFile(IBinaryReader bin) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		ISymbol e = null;
		while ((e = bin.readSymbol()) !=Symbol.EOF)
			ls.add(e);
		return ls;
	}
	private  int getlen() {
		// TODO Auto-generated method stub
		return id;
	}
	public static void main(String[] args)
	{
			ArchiveDescriptor a = new ArchiveDescriptor(new File("C:\\home_user\\work\\acpnetapp05\\prober_vision_volume\\data\\661A7_N04U"));

			System.out.println("arch size " + a.getSize());
			System.out.println("arch nb file " + a.getNumberElement());
			a.optimize();
			if (a.files != null)
				for (FileDescriptor f : a.files) {
					System.out.println("\t+ " + f.getSize() + "\t" + Long.toHexString(f.getCrc64()) +" "+ String.format("%1.2f", f.getEntropie())+"\t" + f.file);
				//	System.out.println(JavaUtils.Format(f.getDFreq(),"->",", ",s->s.toString(),z->String.format("%2.2f", z*100)));
				}
			FAT fat=new FAT(a);
			IBinaryWriter bout= new BinaryStdOut(new File("c:\\temp\\a.fat")) ;
			ICodingRule codingRule=new CodingSet(CodingSet.NOCOMPRESS) ;
			bout.setCodingRule(codingRule);
			System.out.println("save file ");
					fat.save(bout);
			bout.close();
			System.out.println("file done");
			IBinaryReader bin=new BinaryStdIn(new File("c:\\temp\\a.fat"));
			bin.setCodingRule(codingRule);
			System.out.println("read file ");
			fat=new FAT(bin);
			a=fat.des;
			System.out.println("file done");
			if (a.files != null)
				for (FileDescriptor f : a.files) {
					System.out.println("\t+ " + f.getSize() + "\t" + Long.toHexString(f.getCrc64()) +" "+ String.format("%1.2f", f.getEntropie())+"\t" + f.file);
				//	System.out.println(JavaUtils.Format(f.getDFreq(),"->",", ",s->s.toString(),z->String.format("%2.2f", z*100)));
				}
			
			System.out.println("a.files="+a.files.size());
			System.out.println("fat.id="+fat.id);
			}

}
