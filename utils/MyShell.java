package com.zoubworld.utils;
import java.io.*;

import com.zoubworld.utils.JavaUtils;

public class MyShell
{
 public static void main(String[] args) throws java.io.IOException {
  String commandLine,commandLineold;
  commandLine="";
  BufferedReader console = new BufferedReader
	(new InputStreamReader(System.in));

// we break out with <control><C>
while (true) {
  // read what the user entered
  System.out.print("jsh>");
  commandLineold=commandLine;
  commandLine = console.readLine();

  // if the user entered a return, just loop again
  if (commandLine.equals("exit"))
		System.exit(0);
  else if (commandLine.equals(""))
	  JavaUtils.executeCommandColor( commandLine=commandLineold);
  else if (commandLine.equals("^[[A"))
	  JavaUtils.executeCommandColor( commandLineold);
  else 
	  JavaUtils.executeCommandColor( commandLine);
  }
 }
}
