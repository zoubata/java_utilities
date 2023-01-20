package com.zoubworld.Crypto.server.account;

import java.io.File;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.Http.APage;
import com.zoubworld.Crypto.server.Http.JavaHTTPServer;
import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.Crypto.server.account.Iwallet;
import com.zoubworld.Crypto.server.account.Wallet;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class BuildPages {

	/**
	 * 
	 */
	public BuildPages() {
		
	}

	/**
	 * @param args
	 */
	public static APage getAccountPage() {
	
		
		APage phome=new APage("Home page of account : "+PathService.getAccount().getId12s()
				,"",(PathService.getHttpPath(PathService.getAccount())+JavaHTTPServer.DEFAULT_FILE));
		phome.add(phome);
		phome.save(PathService.getHomeDir());
	//	JavaUtils.saveAs(, phome.toString());
		return phome;
	}

}
