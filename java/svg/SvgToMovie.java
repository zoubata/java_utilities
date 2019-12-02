/**
 * 
 */
package com.zoubworld.java.svg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.orzlabs.java.media.AviWriter;

/**
 * @author  Pierre Valleau
 *
 */
public class SvgToMovie {
 
	/**
	 * 
	 */
	public SvgToMovie() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File outFile = new File("C:\\Temp\\svg\\hoge.avi");
		System.out.println("start");
		try {
			AviWriter aviWriter = new AviWriter(outFile, 2, true);
			for (int i = 1; i < 229; i++) {
				BufferedImage bi =
					ImageIO.read(new File(
							"C:\\Temp\\svg\\lidar-" + i + ".jpg"));
				aviWriter.writeFrame(bi);
				System.out.print(".");
			}
			aviWriter.setFramesPerSecond(4, 1);
			aviWriter.setSamplesPerSecond(1);
			aviWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		System.out.println("\nend");
}

	public static void main2(String[] args) {
		int fps = 25;
		SvgToMovie s=new SvgToMovie();
		System.out.println("start");
		AWTSequenceEncoder enc;
		try {
			enc = AWTSequenceEncoder.createSequenceEncoder((new File("c:\\temp\\svg\\test.mp4")), fps);

			int framestoEncode = 100;
			for (int i = 1; i < framestoEncode; i++) {
				BufferedImage img = ImageIO.read(new File("C:\\Temp\\svg\\filename-" + i + ".jpg"));
				
				enc.encodeImage(img);
			}
			enc.finish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}
}
