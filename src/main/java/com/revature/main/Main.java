package com.revature.main;

import com.revature.ui.*;

public class Main {

	
	//habitual main from c and c++ days
	public static void main(String[] args) {
		
		//initializing the necessary ui classes
		Login startUP = new Login();
		Register signUP = new Register();
		InfoPage infoUP = new InfoPage();
		BalanceManagement balanceUP = new BalanceManagement();
		Gamble gambleUP = new Gamble();
		
		//setting up which state the client is in
		int choice = startUP.setupUI();
		if(choice == 2) {
			choice = signUP.setupUI();
		}
		
		//unless client wishes to quit, it will change states
		
		while(choice != 0) {
			switch(choice) {
			case 3:
				choice = infoUP.setupUI();
				break;
			case 4:
				choice = balanceUP.setupUI();
				break;
			case 5:
				choice = gambleUP.setupUI();
				break;
			
			default:
				
			}
				
		}
		
		System.exit(0);
	}
}
