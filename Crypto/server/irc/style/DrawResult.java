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

package com.zoubworld.Crypto.server.irc.style;

import java.util.*;

/**
 * A drawing result.
 */
public class DrawResult
{
  /**
   * The list of all update handles for this draw result.
   */
  public Vector updateHandles;

  /**
   * The result rectangle.
   */
  public StyledRectangle rectangle;

  /**
   * An array of words in the rectangle.
   */
  public DrawResultItem[] items;

  public String toString()
  {
    String res="";
    for(int i=0;i<items.length;i++) res+=items[i].originalstrippedword;
    return res;
  }
}

