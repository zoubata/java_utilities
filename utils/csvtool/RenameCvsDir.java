package com.zoubworld.utils.csvtool;
import java.io.File;
import java.io.IOException;




public class RenameCvsDir
{
private File f;

public RenameCvsDir( String dirname )
{
  super( );
  this.f = new File(dirname);
}

public void executecmd()
{
  try {
    // Execute a command without arguments
    String command = "cvs";
    Process child = Runtime.getRuntime().exec(command);
    child.getOutputStream( );    
    // Execute a command with an argument
    command = "ls /tmp";
    child = Runtime.getRuntime().exec(command);
  } catch (IOException e) {
  }   
}



public void switchtoNto()
{


  mvStringtoString(this.f,"CVS" ,"CVS.nwy");
  mvStringtoString(this.f,"CVS.nto" ,"CVS");
}
public void switchtoNwy()
{
  mvStringtoString(this.f,"CVS" ,"CVS.nto");
  mvStringtoString(this.f,"CVS.nwy" ,"CVS");
}
public void mvStringtoString(File mydir,String old ,String newone)
{
  if (mydir.isDirectory( ))   
    for( File afile:mydir.listFiles( ))
    {
      if (afile.isDirectory( ))  
      {
   //     System.out.println( "DIR>"+ afile.getAbsolutePath( ) );
        if (afile.getName( ).compareToIgnoreCase( old )==0)
        {
          boolean result=false;
          File oldFile=null;
          File newFile=null;
          try {
             oldFile = afile;
             String name=new String(afile.getAbsolutePath( ));
             
             name=name.substring(0, name.lastIndexOf( old ));
             name=name+newone;
           //  System.out.println("debug3 "+ name );
             newFile = new File(name);
             result = oldFile.renameTo(newFile);

          } catch (Exception e) {
            e.printStackTrace();}
          if (result)  
          {System.out.println( "MV>"+ oldFile.getAbsolutePath( ) +" -> "
                             +newFile.getAbsolutePath( )  );}
          else
          {System.err.println( "MV>"+ oldFile.getAbsolutePath( ) +" -> "
                             +newFile.getAbsolutePath( ) + " FAIL");}
          
        }
        else
        {
         // System.out.println("#>"+afile.getName( )+"<>"+old);
          mvStringtoString(afile,old,newone);
         }
        
      } 
      else 
      {// System.out.println( "FILE>"+mydir.getAbsolutePath( ));
      }
      }
    }
 
public static void main(String[] args) {
  ;
  //RenameCvsDir test=new RenameCvsDir("c:"+File.separator+"temp"+File.separator+"test");
  RenameCvsDir test=new RenameCvsDir(args[0]);
  
  System.out.println("Welcome to CVS repository changer");
  System.out.println("1 : nwy->nto to switch from Norway root with back up to nto root");
  System.out.println("2 : nto->nwy to switch from nto root with back up to Norway root");
  int c=0;
  try
  {
    while (System.in.available( )<=0);
     c=System.in.read( );
    
  }
  catch ( IOException e )
  {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  if (c=='1')
  
  test.switchtoNto( );
  else if (c=='2')
    
  test.switchtoNwy();
  else
  {
    System.out.println("Error systaxe");
  }
//   cAvr32.test();
}
}
