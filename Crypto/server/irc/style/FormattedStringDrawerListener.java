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
 * This interface is used to notify that some drawn content has been updated.
 */
public interface FormattedStringDrawerListener
{
  /**
   * Image data is updated.
   */
  public static final int DATA=1;
  /**
   * Size is updated.
   */
  public static final int SIZE=2;
  /**
   * Frame is updated. 
   */
  public static final int FRAME=4;
  
  /**
   * The given handle has been updated.
   * @param handle updated handle.
   * @param what type of update, bitfield.
   * @return true if future handle update should be notified, false otherwise.
   */
  public Boolean displayUpdated(Object handle,Integer what);
}