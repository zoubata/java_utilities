package com.zoubworld.java.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.fop.render.ps.EPSTranscoder;
import org.apache.fop.svg.PDFTranscoder;

import com.zoubworld.robot.Odometry;

import de.bripkens.svgexport.Format;
import de.bripkens.svgexport.SVGExport;
import de.bripkens.svgexport.SVGTranscoder;

public class SvgRender implements ItoSvg 
{

	public SvgRender() {
		// TODO Auto-generated constructor stub
	}

	List<ItoSvg> objects;
	public List<ItoSvg> getObjects() {
		if (objects==null)
		 objects=new ArrayList<ItoSvg>();
		return objects;
	}
	public void addObjects(List<ItoSvg> objects) {
		getObjects().addAll(objects);
	}
	public void addObject(ItoSvg object) {
		getObjects().add(object);
	}
	public void setObjects(List<ItoSvg> objects) {
		this.objects = objects;
	}
	@Override
	public String toSvg() {
		String s="<?xml version=\"1.0\"?>"+"\n"+
				"<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">"+"\n"+
				"<svg width=\"150mm\" height=\"100mm\" viewBox=\"0 0 3353.76 1087.76\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"+"\n";
		
		s+="<g id=\"small\" transform=\"translate(100,100) scale(0.25,0.25)\">"+"\n";
		if (objects!=null)
			for(ItoSvg obj: objects)
				s+=obj.toSvg()+"\n";
		s+="</g>"+"\n";
		s+="</svg>"+"\n";
		return s;
	}
	/** convert a svg file into a image of an other frmat :
	 * jpg,
	 * not supported : png,tiff, svg, eps,pdf
	 * */
	static public void convert(String fileSvf,String fileImage)
	{try {
		 //Step -1: We read the input SVG document into Transcoder Input
        String svg_URI_input = new File(fileSvf).toURL().toString();
        TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);        
        //Step-2: Define OutputStream to JPG file and attach to TranscoderOutput
        OutputStream jpg_ostream;
		
			jpg_ostream = new FileOutputStream(fileImage);
		
        TranscoderOutput output_jpg_image = new TranscoderOutput(jpg_ostream);              
        // Step-3: Create JPEGTranscoder and define hints
        JPEGTranscoder my_converter = new JPEGTranscoder();
        my_converter.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,new Float(.9));
        // Step-4: Write output
        my_converter.transcode(input_svg_image, output_jpg_image);
        // Step 5- close / flush Output Stream
        jpg_ostream.flush();
        jpg_ostream.close(); } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
		/*
	   SVGExport e= new SVGExport();
	   String svg_URI_input;
	try {
		svg_URI_input = new File(fileSvf).toURL().toString();
		e.setInput(svg_URI_input);
	} catch (MalformedURLException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	   
	   / *
	   InputStream inputstream;
	try {
		inputstream = new FileInputStream(fileSvf);
		e.setInput(inputstream);

	} catch (FileNotFoundException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}* /
	   
	//e.setInputAsString(svg)
    Format f=Format.JPEG;
    if (fileImage.endsWith(".jpg"))
    	f=Format.JPEG;
    else
    	 if (fileImage.endsWith(".jpeg"))
    	    	f=Format.JPEG;
    	    else
    	    	if (fileImage.endsWith(".png"))
        	f=Format.PNG;
        else
        if (fileImage.endsWith(".svg"))
    	f=Format.SVG;
    else
    if (fileImage.endsWith(".tiff"))
    	f=Format.TIFF;
    else
    if (fileImage.endsWith(".eps"))
    	f=Format.EPS;
    else
    if (fileImage.endsWith(".pdf"))
    	f=Format.PDF;
    else
    	fileImage.concat(f.
		        getFileNameExtension());
    System.out.println("Save file"+fileImage);
    {
        
            try {
				e.setOutput(new FileOutputStream(fileImage)).
				        setTranscoder(f).transcode();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}}*/ catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
	}
