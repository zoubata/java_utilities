package com.zoubworld.sandbox;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferedInputStreamExample {
	public static void main(String[] args) {
		//File file = new File("src/com/zoubworld/sandbox/data.txt");
		File file = new File("res\\test\\small_ref\\pie2.txt");
		
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			// Create buffer
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
				System.out.println(new String(buffer, 0, bytesRead));
				System.out.println("read"+bytesRead+" on buffer of size "+buffer.length);
				for(int i=0;i<buffer.length;i++)
				{
				System.out.print(String.format("%2x", buffer[i]));
				if (i%16==0)System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}