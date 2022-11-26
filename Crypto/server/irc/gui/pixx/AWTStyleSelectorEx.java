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
import java.awt.event.*;

import com.zoubworld.Crypto.server.irc.*;

/**
 * Extension for the style selector.
 */
public class AWTStyleSelectorEx extends Panel implements ActionListener,FontSelectorListener
{
  private ListenerGroup _lis;
  private AWTStyleSelector _selector;
  private FontSelector _fs;
  private Button _b;

  /**
   * Create a new AWTStyleSelectorEx.
   * @param config global irc configuration.
   */
  public AWTStyleSelectorEx(PixxConfiguration config)
  {
    _fs=new FontSelector(config);
    _selector=new AWTStyleSelector(config);
    setLayout(new BorderLayout());
    add(_selector,BorderLayout.CENTER);
    _b=new NonFocusableButton(config.getText(PixxTextProvider.GUI_FONT));
    _b.setForeground(config.getColor(PixxColorModel.COLOR_WHITE));
    _b.setBackground(config.getColor(PixxColorModel.COLOR_FRONT));
    _b.addActionListener(this);
    if(config.getB("setfontonstyle")) add(_b,BorderLayout.EAST);
    _lis=new ListenerGroup();
  }

  /**
   * Release this object.
   */
  public void release()
  {
    removeAll();
    _b.removeActionListener(this);
    _selector.release();
    _fs.release();
    _selector=null;
    _fs=null;
  }

  /**
   * Set the style context.
   * @param ct style context.
   */
  public void setStyleContext(StyleContext ct)
  {
    _selector.setStyleContext(ct);
  }

  /**
   * Get prefix to use.
   * @return style prefix.
   */
  public String getPrefix()
  {
    return _selector.getPrefix();
  }

  /**
   * Get the style selector.
   * @return the style selector.
   */
  public AWTStyleSelector getStyleSelector()
  {
    return _selector;    
  }

  /**
   * Add a listener.
   * @param lis listener to add.
   */
  public void addAWTStyleSelectorExListener(AWTStyleSelectorExListener lis)
  {
    _lis.addListener(lis);
  }

  /**
   * Remove a listener.
   * @param lis listener to remove.
   */
  public void removeAWTStyleSelectorExListener(AWTStyleSelectorExListener lis)
  {
    _lis.removeListener(lis);
  }

  public void actionPerformed(ActionEvent e)
  {
    EventDispatcher.dispatchEventAsync(_fs,"selectFont",new Object[] {this});
  }

  public void fontSelected(Font f)
  {
    if(f!=null) _lis.sendEvent("fontSelected",f);
  }
}
