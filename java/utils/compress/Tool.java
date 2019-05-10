package com.zoubworld.java.utils.compress;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;

import java.nio.file.Path;
public class Tool {


    /**
     * This method uses java.io.FileInputStream to read
     * file content into a byte array
     * @param file
     * @return
     */
    private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
            
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
    
	public Tool() {
		
/*
		
		BinaryStdIn i=new BinaryStdIn();

        BinaryStdOut o=new BinaryStdOut();
        // read one 8-bit char at a time
        while (!i.isEmpty()) {
            char c = i.readChar();
            o.write(c);
        }
        o.flush();
		 
		 File targetFile = new File("src/main/resources/targetFile.tmp");
		    OutputStream outStream = new FileOutputStream(targetFile);
		    
		Path path = Paths.get("path/to/file");
		byte[] data = Files.readAllBytes(path);

		List<Symbol> alfile= new ArrayList<Symbol>();
			for(int i=0;i<data.length;i++)
				alfile.add(new Symbol(data[i]));
			

		List<File> lf;
		alfile.stream()
		.map(n->n.getCode())
		//.flatMap(n->n.getbinary())		
		
		.forEach(n->outStream.write(n.lenbit ) );
		
	
		
		
		 Arrays.stream(data)			 
				 
				 .map(n->n)
				 
			//	 .toArray()
				 .forEach(n->outStream.write((int) n ) );
	
		    
		
		    
		    outStream.close();
		 
		/*
		 try ( FileInputStream fileInput = new FileInputStream("C://example.jpg");
				 FileOutputStream fileOutput = new FileOutputStream("C://anewexample.jpg"))
				 {
				 int data;
				 while ((data = fileInput.read()) != -1) {
				 fileOutput.write(data);
				 }
				 }
				 catch (IOException e)
				 {
				 System.out.println("Error message: " + e.getMessage());
				 }
		 
		 
		    File initialFile = new File("src/main/resources/sample.txt");
		    InputStream targetStream = new FileInputStream(initialFile);
		    
		    File targetFile = new File("src/main/resources/targetFile.tmp");
		    OutputStream outStream = new FileOutputStream(targetFile);
		    
		     targetStream.map(n -> n )
				.forEach(outStream::write);
		    
		    outStream.close();
		
		    // Create stream to read file.
	        FileInputStream fis = new FileInputStream(
	                "C:/Test/testObjectStream.txt");
		    // Create ObjectInputStream object wrap 'fis'.
	        ObjectInputStream ois = new ObjectInputStream(fis);
	     // Read String.
	        String s = ois.readUTF();
	        
	        
	        // Stream to read file.
	         FileInputStream fis2 = new FileInputStream("C:/Test/cities.txt");
	 
	         // Create DataInputStream object wrap 'fis'.
	         DataInputStream dis = new DataInputStream(fis2);
	       /*
	        * // Input stream read a file - File1.txt .
InputStream is1=new FileInputStream("File1.txt");
// Input stram read a file - File2.txt  
InputStream is2=new FileInputStream("File2.txt");
 
// Create new Stream from two stream
SequenceInputStream sis=new SequenceInputStream(is1,is2);

	        */
	}

	
	   public static void main(String[] args) {
	 File FileIn = new File("C:\\Temp\\fuses.c");
	 File FileOut = new File("C:\\Temp\\fuses.c.tmp");
	 
	 
	 Huffman hm = new Huffman();
		Map<Symbol, Long> table = hm.getTableEntropie(FileIn);
	//	table.clear();
	/*	table.put(new Symbol(' '), (long) 10000);
		table.put(new Symbol('a'), (long) 1000);
		table.put(new Symbol('z'), (long) 1001);*/
		//  #pos,offset, size
		//dic,index
		table.put(new Symbol("#pos16_8"), (long) 101);//n/q8  n=sizefiles/ratio
		table.put(new Symbol("#pos32_16"), (long) 110);//n/q7  n=sizefiles/ratio
		table.put(new Symbol("#pos32_8"), (long) 100);//n/q6  n=sizefiles/ratio
		table.put(new Symbol("#pos24_8"), (long) 111);//n/q5  n=sizefiles/ratio
		table.put(new Symbol("#pos64_32"), (long) 110);//n/q4  n=sizefiles/ratio
		table.put(new Symbol("#pos64_8"), (long) 10);//n/q3  n=sizefiles/ratio
		table.put(new Symbol("#dic16"), (long) 300);//n/q0  n=sizefiles/ratio
		table.put(new Symbol("#dic24"), (long) 100);//n/q1  n=sizefiles/ratio
		table.put(new Symbol("#dic32"), (long) 1000);//n/q2  n=sizefiles/ratio
		table.put(new Symbol("#end"), (long) 10);// nb special
		table.put(new Symbol("#FileList"), (long) 1);//1
		table.put(new Symbol("#File"), (long) 101);//nb file
		table.put(new Symbol("#HuffmanTable"), (long) 101);//nb file
		//#HuffmanTable N,sym[N]; 
		//sym : S=0..255,W: (N/8+1),Code 8x(S/8+1)
		
		hm.BuildCode(table);
	 
	 BinaryStdIn i=new BinaryStdIn(FileIn);

       BinaryStdOut o=new BinaryStdOut(FileOut);
     /*  // read one 8-bit char at a time
       while (!i.isEmpty()) {
           char c = i.readChar();
           o.write(c);
       }*/
       
       hm.compress(  i, o);
       o.flush();
	   }
}
