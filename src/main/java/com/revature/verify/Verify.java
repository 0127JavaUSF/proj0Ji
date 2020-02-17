package com.revature.verify;

import com.revature.clientInfo.ClientContainer;

public interface Verify {
	public boolean verifySTDInput(String input);
	public boolean verifySQLInput(ClientContainer sqlClient);
	
}
