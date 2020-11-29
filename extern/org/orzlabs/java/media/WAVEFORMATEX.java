/*
 * jPSXdec: Playstation 1 Media Decoder/Converter in Java
 * Copyright (C) 2007-2008  Michael Sabin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor,   
 * Boston, MA  02110-1301, USA.
 *
 */
/* 
 * Modified by Masayuki Igawa
 * Original source code is available at
 * http://code.google.com/p/jpsxdec/source/browse/#svn/trunk/src/jpsxdec/util
 */
/*
 * WAVEFORMATEX.java
 */

package com.zoubworld.extern.org.orzlabs.java.media;

import java.io.IOException;
import java.io.RandomAccessFile;

/** Represents the C WAVEFORMATEX structure. 
 *  http://msdn2.microsoft.com/en-us/library/ms713497(VS.85).aspx */
class WAVEFORMATEX extends AVIstruct {

    static short WAVE_FORMAT_PCM = 1;
    
    // ** VirtualDub writes 16 bytes struct (leaves off cbSize)
    
    public /*WORD */ short wFormatTag      = 0;
    public /*WORD */ short nChannels       = 0;
    public /*DWORD*/ int   nSamplesPerSec  = 0;
    public /*DWORD*/ int   nAvgBytesPerSec = 0;
    public /*WORD */ short nBlockAlign     = 0;
    public /*WORD */ short wBitsPerSample  = 0;
    //public /*WORD */ short cbSize          = 0; **
    
    
    public void write(RandomAccessFile raf) throws IOException {
        /*WORD */ write16LE(raf, wFormatTag     );
        /*WORD */ write16LE(raf, nChannels      );
        /*DWORD*/ write32LE(raf, nSamplesPerSec );
        /*DWORD*/ write32LE(raf, nAvgBytesPerSec);
        /*WORD */ write16LE(raf, nBlockAlign    );
        /*WORD */ write16LE(raf, wBitsPerSample );
        ///*WORD */ write16LE(raf, cbSize         ); **
    }

    public int sizeof() {
        return 18-2; // **
    }
    
}
