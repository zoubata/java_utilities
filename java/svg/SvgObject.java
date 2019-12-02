package com.zoubworld.java.svg;

/* class that do nothing
 * */
public class SvgObject implements ItoSvg{

	String datasvg="";
	public static final String Blue="rgb(0,0,255)";
	public static final String Black="rgb(0,0,0)";
	public static final String White="rgb(255,255,255)";
	public static final String Lime="rgb(0,255,0)";
	public static final String Yellow="rgb(255,255,0)";
	public static final String Magenta ="rgb(255,0,255)";
	public static final String Red="rgb(255,0,0)";
	public static final String Grey="rgb(128,128,128)";
	public static final String Silver="rgb(192,192,192)";
	public static final String Cyan="rgb(0,255,255)";
	public static final String Maroon="rgb(128,0,0)";
	public static final String Olive="rgb(128,128,0)";
	public static final String Green="rgb(0,128,0)";
	public static final String Purple="rgb(128,0,128)";
	public static final String Teal="rgb(0,128,128)";
	public static final String Navy="rgb(0,0,128)";
	
	
	
	
	
	
	public static final String maroon="rgb(128,0,0)";
	public static final String dark_red="rgb(139,0,0)";
	public static final String brown="rgb(165,42,42)";
	public static final String firebrick="rgb(178,34,34)";
	public static final String crimson="rgb(220,20,60)";
	public static final String red="rgb(255,0,0)";
	public static final String tomato="rgb(255,99,71)";
	public static final String coral="rgb(255,127,80)";
	public static final String indian_red="rgb(205,92,92)";
	public static final String light_coral="rgb(240,128,128)";
	public static final String dark_salmon="rgb(233,150,122)";
	public static final String salmon="rgb(250,128,114)";
	public static final String light_salmon="rgb(255,160,122)";
	public static final String orange_red="rgb(255,69,0)";
	public static final String dark_orange="rgb(255,140,0)";
	public static final String orange="rgb(255,165,0)";
	public static final String gold="rgb(255,215,0)";
	public static final String dark_golden_rod="rgb(184,134,11)";
	public static final String golden_rod="rgb(218,165,32)";
	public static final String pale_golden_rod="rgb(238,232,170)";
	public static final String dark_khaki="rgb(189,183,107)";
	public static final String khaki="rgb(240,230,140)";
	public static final String olive="rgb(128,128,0)";
	public static final String yellow="rgb(255,255,0)";
	public static final String yellow_green="rgb(154,205,50)";
	public static final String dark_olive_green="rgb(85,107,47)";
	public static final String olive_drab="rgb(107,142,35)";
	public static final String lawn_green="rgb(124,252,0)";
	public static final String chart_reuse="rgb(127,255,0)";
	public static final String green_yellow="rgb(173,255,47)";
	public static final String dark_green="rgb(0,100,0)";
	public static final String green="rgb(0,128,0)";
	public static final String forest_green="rgb(34,139,34)";
	public static final String lime="rgb(0,255,0)";
	public static final String lime_green="rgb(50,205,50)";
	public static final String light_green="rgb(144,238,144)";
	public static final String pale_green="rgb(152,251,152)";
	public static final String dark_sea_green="rgb(143,188,143)";
	public static final String medium_spring_green="rgb(0,250,154)";
	public static final String spring_green="rgb(0,255,127)";
	public static final String sea_green="rgb(46,139,87)";
	public static final String medium_aqua_marine="rgb(102,205,170)";
	public static final String medium_sea_green="rgb(60,179,113)";
	public static final String light_sea_green="rgb(32,178,170)";
	public static final String dark_slate_gray="rgb(47,79,79)";
	public static final String teal="rgb(0,128,128)";
	public static final String dark_cyan="rgb(0,139,139)";
	public static final String aqua="rgb(0,255,255)";
	public static final String cyan="rgb(0,255,255)";
	public static final String light_cyan="rgb(224,255,255)";
	public static final String dark_turquoise="rgb(0,206,209)";
	public static final String turquoise="rgb(64,224,208)";
	public static final String medium_turquoise="rgb(72,209,204)";
	public static final String pale_turquoise="rgb(175,238,238)";
	public static final String aqua_marine="rgb(127,255,212)";
	public static final String powder_blue="rgb(176,224,230)";
	public static final String cadet_blue="rgb(95,158,160)";
	public static final String steel_blue="rgb(70,130,180)";
	public static final String corn_flower_blue="rgb(100,149,237)";
	public static final String deep_sky_blue="rgb(0,191,255)";
	public static final String dodger_blue="rgb(30,144,255)";
	public static final String light_blue="rgb(173,216,230)";
	public static final String sky_blue="rgb(135,206,235)";
	public static final String light_sky_blue="rgb(135,206,250)";
	public static final String midnight_blue="rgb(25,25,112)";
	public static final String navy="rgb(0,0,128)";
	public static final String dark_blue="rgb(0,0,139)";
	public static final String medium_blue="rgb(0,0,205)";
	public static final String blue="rgb(0,0,255)";
	public static final String royal_blue="rgb(65,105,225)";
	public static final String blue_violet="rgb(138,43,226)";
	public static final String indigo="rgb(75,0,130)";
	public static final String dark_slate_blue="rgb(72,61,139)";
	public static final String slate_blue="rgb(106,90,205)";
	public static final String medium_slate_blue="rgb(123,104,238)";
	public static final String medium_purple="rgb(147,112,219)";
	public static final String dark_magenta="rgb(139,0,139)";
	public static final String dark_violet="rgb(148,0,211)";
	public static final String dark_orchid="rgb(153,50,204)";
	public static final String medium_orchid="rgb(186,85,211)";
	public static final String purple="rgb(128,0,128)";
	public static final String thistle="rgb(216,191,216)";
	public static final String plum="rgb(221,160,221)";
	public static final String violet="rgb(238,130,238)";
	public static final String magenta="rgb(255,0,255)";
	public static final String fuchsia=magenta;
	public static final String orchid="rgb(218,112,214)";
	public static final String medium_violet_red="rgb(199,21,133)";
	public static final String pale_violet_red="rgb(219,112,147)";
	public static final String deep_pink="rgb(255,20,147)";
	public static final String hot_pink="rgb(255,105,180)";
	public static final String light_pink="rgb(255,182,193)";
	public static final String pink="rgb(255,192,203)";
	public static final String antique_white="rgb(250,235,215)";
	public static final String beige="rgb(245,245,220)";
	public static final String bisque="rgb(255,228,196)";
	public static final String blanched_almond="rgb(255,235,205)";
	public static final String wheat="rgb(245,222,179)";
	public static final String corn_silk="rgb(255,248,220)";
	public static final String lemon_chiffon="rgb(255,250,205)";
	public static final String light_golden_rod_yellow="rgb(250,250,210)";
	public static final String light_yellow="rgb(255,255,224)";
	public static final String saddle_brown="rgb(139,69,19)";
	public static final String sienna="rgb(160,82,45)";
	public static final String chocolate="rgb(210,105,30)";
	public static final String peru="rgb(205,133,63)";
	public static final String sandy_brown="rgb(244,164,96)";
	public static final String burly_wood="rgb(222,184,135)";
	public static final String tan="rgb(210,180,140)";
	public static final String rosy_brown="rgb(188,143,143)";
	public static final String moccasin="rgb(255,228,181)";
	public static final String navajo_white="rgb(255,222,173)";
	public static final String peach_puff="rgb(255,218,185)";
	public static final String misty_rose="rgb(255,228,225)";
	public static final String lavender_blush="rgb(255,240,245)";
	public static final String linen="rgb(250,240,230)";
	public static final String old_lace="rgb(253,245,230)";
	public static final String papaya_whip="rgb(255,239,213)";
	public static final String sea_shell="rgb(255,245,238)";
	public static final String mint_cream="rgb(245,255,250)";
	public static final String slate_gray="rgb(112,128,144)";
	public static final String light_slate_gray="rgb(119,136,153)";
	public static final String light_steel_blue="rgb(176,196,222)";
	public static final String lavender="rgb(230,230,250)";
	public static final String floral_white="rgb(255,250,240)";
	public static final String alice_blue="rgb(240,248,255)";
	public static final String ghost_white="rgb(248,248,255)";
	public static final String honeydew="rgb(240,255,240)";
	public static final String ivory="rgb(255,255,240)";
	public static final String azure="rgb(240,255,255)";
	public static final String snow="rgb(255,250,250)";
	public static final String black="rgb(0,0,0)";
	public static final String dim_gray="rgb(105,105,105)";
	public static final String gray="rgb(128,128,128)";
	public static final String dark_gray="rgb(169,169,169)";
	public static final String silver="rgb(192,192,192)";
	public static final String light_gray="rgb(211,211,211)";
	public static final String gainsboro="rgb(220,220,220)";
	public static final String white_smoke="rgb(245,245,245)";
	public static final String white="rgb(255,255,255)";

	/* get in parameter an svg data string
	 * */
	public SvgObject(String svg) {
		datasvg=svg;
	}
	public SvgObject() {
		datasvg="";
	}

	@Override
	public String toSvg() {
		
		return datasvg;
	}

}
