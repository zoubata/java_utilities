/*****************************************************/
/*          This java file is a part of the          */
/*                                                   */
/*           -  Plouf's Java IRC Client  -           */
/*                                                   */
/*   Copyright (C)  2002 - 2004 Philippe Detournay   */
/*                                                   */
/*         All contacts : theplouf@yahoo.com         */
/*                                                   */
/*  PJIRC is free software; you can redistribute     */
/*  it and/or modify it under the terms of the GNU   */
/*  General Public License as published by the       */
/*  Free Software Foundation; version 2 or later of  */
/*  the License.                                     */
/*                                                   */
/*  PJIRC is distributed in the hope that it will    */
/*  be useful, but WITHOUT ANY WARRANTY; without     */
/*  even the implied warranty of MERCHANTABILITY or  */
/*  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU   */
/*  General Public License for more details.         */
/*                                                   */
/*  You should have received a copy of the GNU       */
/*  General Public License along with PJIRC; if      */
/*  not, write to the Free Software Foundation,      */
/*  Inc., 59 Temple Place, Suite 330, Boston,        */
/*  MA  02111-1307  USA                              */
/*                                                   */
/*****************************************************/

package com.zoubworld.Crypto.server.irc.gui.pixx;

import java.awt.*;

/**
 * PixxInterface color model.
 */
public class PixxColorModel
{
  /**
   * Black.
   */
  public static final int COLOR_BLACK=0;
  /**
   * White.
   */
  public static final int COLOR_WHITE=1;
  /**
   * Dark gray.
   */
  public static final int COLOR_DARK_GRAY=2;
  /**
   * Gray.
   */
  public static final int COLOR_GRAY=3;
  /**
   * Light gray.
   */
  public static final int COLOR_LIGHT_GRAY=4;
  /**
   * Front.
   */
  public static final int COLOR_FRONT=5;
  /**
   * Back.
   */
  public static final int COLOR_BACK=6;
  /**
   * Selected.
   */
  public static final int COLOR_SELECTED=7;
  /**
   * Event.
   */
  public static final int COLOR_EVENT=8;
  /**
   * Close.
   */
  public static final int COLOR_CLOSE=9;
  /**
   * Voice.
   */
  public static final int COLOR_VOICE=10;
  /**
   * Op.
   */
  public static final int COLOR_OP=11;
  /**
   * Semiop.
   */
  public static final int COLOR_SEMIOP=12;
  /**
   * ASL male.
   */
  public static final int COLOR_MALE=13;
  /**
   * ASL femeale.
   */
  public static final int COLOR_FEMEALE=14;
  /**
   * ASL undefined.
   */
  public static final int COLOR_UNDEF=15;

  private Color[] _colors;

  /**
   * Create a new PixxColorModel using default colors.
   */
  public PixxColorModel()
  {
    Color[] cols=new Color[16];
    cols[0]=Color.black;
    cols[1]=Color.white;
    cols[2]=new Color(0x868686);
    cols[3]=Color.gray;
    cols[4]=new Color(0xD0D0D0);
    cols[5]=new Color(0x336699);
    cols[6]=new Color(0x084079);
    cols[7]=new Color(0x003167);
    cols[8]=new Color(0xa40000);
    cols[9]=new Color(0x4B8ECE);
    cols[10]=new Color(0x008000);
    cols[11]=new Color(0x336699);
    cols[12]=new Color(0x336699);
    cols[13]=new Color(0x4040ff);
    cols[14]=new Color(0xff40ff);
    cols[15]=new Color(0x336699);
    init(cols);
  }

  /**
   * Set the i'th color.
   * @param i index.
   * @param c color.
   */
  public void setColor(int i,Color c)
  {
    if((i>=0) && (i<_colors.length)) _colors[i]=c;
  }

  /**
   * Get the number of color in the mode.
   * @return color count.
   */
  public int getColorCount()
  {
    return _colors.length;
  }

  private Color computeColor(int r,int g,int b,int i)
  {
    r*=i;
    g*=i;
    b*=i;
    r/=256;
    g/=256;
    b/=256;
    if(r>255) r=255;
    if(g>255) g=255;
    if(b>255) b=255;
    return new Color(r,g,b);
  }

  /**
   * Create a new PixxColorModel using given r g b basecolor.
   * @param r red base color.
   * @param g green base color.
   * @param b blue base color.
   */
  public PixxColorModel(int r,int g,int b)
  {
    Color[] cols=new Color[16];
    cols[0]=Color.black;
    cols[1]=Color.white;
    cols[2]=new Color(0x868686);
    cols[3]=Color.gray;
    cols[4]=new Color(0xD0D0D0);
    cols[8]=new Color(0xa40000);
    cols[10]=new Color(0x008000);
    cols[13]=new Color(0x4040ff);
    cols[14]=new Color(0xff40ff);

    cols[5]=computeColor(r,g,b,0x66);
    cols[6]=computeColor(r,g,b,0x55);
    cols[7]=computeColor(r,g,b,0x4B);
    cols[9]=computeColor(r,g,b,0x80);
    cols[11]=computeColor(r,g,b,0x66);
    cols[12]=computeColor(r,g,b,0x66);
    cols[15]=computeColor(r,g,b,0x66);
    init(cols);
  }

  /**
   * Create a new PixxColorModel using given colors.
   * @param cols colors to use.
   */
  public PixxColorModel(Color[] cols)
  {
    init(cols);
  }

  private void init(Color[] cols)
  {
    _colors=new Color[cols.length];
    for(int i=0;i<cols.length;i++) _colors[i]=cols[i];
  }

  /**
   * Get the color at the given index.
   * @param i index.
   * @return the i'th color.
   */
  public Color getColor(int i)
  {
    return _colors[i];
  }
}

