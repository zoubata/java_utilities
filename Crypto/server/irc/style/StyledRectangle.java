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

/**
 * A rectangle.
 */
public class StyledRectangle
{
  /**
   * X position.
   */
  public int x;

  /**
   * Y position.
   */
  public int y;

  /**
   * Width.
   */
  public int width;

  /**
   * Height.
   */
  public int height;

  /**
   * Create a new StyledRectangle.
   * @param ax x position.
   * @param ay y position.
   * @param w width.
   * @param h height.
   */
  public StyledRectangle(int ax,int ay,int w,int h)
  {
    this.x=ax;
    this.y=ay;
    width=w;
    height=h;
  }

  public boolean equals(Object o)
  {
    if(!(o instanceof StyledRectangle)) return false;
    StyledRectangle r=(StyledRectangle)o;
    return (r.x==x) && (r.y==y) && (r.width==width) && (r.height==height);
  }

  public int hashCode()
  {
    return x+y+width+height;
  }

  /**
   * Test wether the given point is inside the rectangle or not.
   * @param px x coordinate of point to test.
   * @param py y coordinate of point to test.
   * @return true if the point is inside the rectangle, false otherwise.
   */
  public boolean contains(int px,int py)
  {
    return (px>=x) && (py>=y) && (px<x+width) && (py<y+height);
  }

  private boolean noEmpty(int v1,int lv1,int v2,int lv2)
  {
    if(v1<v2) return v1+lv1>v2;
    return v2+lv2>v1;
  }

  /**
   * Test wether the given rectangle is partially or fully inside the rectangle or
   * not.
   * @param r rectangle to test.
   * @return true if r is partially or fully inside the rectangle, false otherwise.
   */
  public boolean hit(StyledRectangle r)
  {
    if(!noEmpty(r.x,r.width,x,width)) return false;
    return noEmpty(r.y,r.height,y,height);
  }

  /**
   * Add the given rectangle to this rectangle. This rectangle will be the union of
   * the two rectangles.
   * @param r the rectangle to add.
   */
  public void add(StyledRectangle r)
  {
    if(r.x<x)
    {
      width+=(x-r.x);
      x=r.x;
    }
    
    if(r.y<y)
    {
      height+=(y-r.y);
      y=r.y;
    }
    
    if(r.width>width) width=r.width;
    if(r.height>height) height=r.height;
  }

  public String toString()
  {
    return "StyledRectangle : "+x+","+y+","+width+","+height;
  }
}

