package com.revature.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;

public class Register extends Login {
	
	private String addOrNo;
	private int acctID;
	private String primUsername;
	private int acctNum;
	
	public Register(){
	
	}
	
	public int setupUI() {
		
		//intro to registration
		System.out.println("\nThank You for Choosing Underground Petro Bank");
		System.out.println("Just Remember");
		System.out.println("Rule Number One of Pylon Bank, You Don't Talk About Pylon Bank\n");
		
		//bool to check inputs
		boolean validInput = false;
		while(validInput == false) {

			//getting inputs
			System.out.println("What is Your First Name?");
			Login.clientInfo.setFirstName(Login.input.next());
			
			System.out.println("What is Your Last Name?");
			Login.clientInfo.setLastName(Login.input.next());
			
			//checking if username is used
			boolean userExist = true;
			
			while(userExist == true) {
				System.out.println("What would You like Your Username to be?");
				Login.clientInfo.setUserName(Login.input.next());
				
				String queryCheck = "SELECT userid From client_table WHERE userid = ?";
				
				Login.dataB.setPreparedStatement(queryCheck);
				Login.queryResult = Login.dataB.queryStatements(Login.clientInfo.getUserName());
				try {
					if(Login.queryResult.next() == true) {
						System.out.println("Username Already Exists, Please Retype your Information");
						userExist = true;
					}
					
					else {
						userExist = false;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			System.out.println("What would You like Your Password to be?");
			Login.clientInfo.setPassWord(Login.input.next());
			
			
			//checking if client already has an acct they want to link up with
			System.out.println("Are you adding on as a Joint? (Yes or No)");
			addOrNo = Login.input.next();
			
			if(addOrNo.equalsIgnoreCase("NO")) {
				System.out.println("Will this be a Checking or Saving?");
				Login.clientInfo.setSaveOrCheck(Login.input.next());
				validInput = verifySTDInput();
				
				while (validInput == false) {
					System.out.println("Please Enter Checking or Saving");
					Login.clientInfo.setSaveOrCheck(Login.input.next());
					validInput = verifySTDInput();
					
				}
			}
			
			
			//verifying the information is correct
			else if(addOrNo.equalsIgnoreCase("YES")) {
				System.out.println("Please Type in the Account Number?");
				int acctID = 0;
				
				//wrong type exception
				try {
					Login.input.nextLine();
					acctID = Login.input.nextInt();
				}
				catch (InputMismatchException e) {
					System.out.print("Account Number doesn't exist, please Retype");
					continue;
				}	
				acctNum = acctID;
				System.out.println("Please Type the Primary account holder's Username");
				primUsername = Login.input.next();
				String query = "SELECT * FROM BANK_ACCOUNT WHERE account_number = " + acctID + " AND userid = '" + primUsername +  "'";
				Login.dataB.setPreparedStatement(query);
				Login.queryResult =  Login.dataB.queryStatements();
				
				try {
					if(Login.queryResult.next() == false) {
						System.out.println("The Account Does not exist... Please Re-Enter Every Information.");
						continue;
					}
					Login.clientInfo.addAccounts(acctID);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			else {
				System.out.println("Please re-enter the informations");
				continue;
			}
			
			//checking if the inputs are valid and if sql is also valid
			validInput = verifySQLInput();
		}
		//construct the static client information
		return 3;
	}
	
	//only the checking/savings need to be validated since names/usernames/password are all strings
	public boolean verifySTDInput() {
		
		if(Login.clientInfo.getSaveOrCheck().equalsIgnoreCase("Checking") || Login.clientInfo.getSaveOrCheck().equalsIgnoreCase("Saving")) {
			return true;
		}
		else {
			System.out.println("Invalid Input, Please Retype your Information");
			return false;
		}
	}


	//sqls from here on out
	public boolean verifySQLInput() {	
		//insert the client name to client table
		String query = "INSERT INTO client_table VALUES (?, ?, ?, ?)";
		
		Login.dataB.setPreparedStatement(query);
		int check = Login.dataB.alterDatabase(Login.clientInfo.getUserName(),Login.clientInfo.getFirstName(),Login.clientInfo.getLastName(),Login.clientInfo.getPassWord());
		
		//checking if insert succeeded
		if (check <= 0) {
			System.out.println("Someone Might've taken your Username Just Now...");
			return false;
		}
		
		//creating a the first bank account for the client if not new acct
		if(addOrNo.equalsIgnoreCase("NO")) {
			query = "INSERT INTO bank_account VALUES (DEFAULT, 0.00, ?, ?)";
			Login.dataB.setPreparedStatement(query);
			
			check = Login.dataB.alterDatabase(Login.clientInfo.getSaveOrCheck(),Login.clientInfo.getUserName());
			
	
			//checking if query succeeded
			if (check <= 0) {
				return false;
			}		

			//extract the account numbers since it is the first account no need to worry about collisions
			query = "SELECT * FROM bank_account WHERE userid = ?";
			
			Login.dataB.setPreparedStatement(query);
			Login.queryResult = Login.dataB.queryStatements(Login.clientInfo.getUserName());
			
			//get account number

			try {
				if(Login.queryResult.next()) {
					acctNum = Login.queryResult.getInt("account_number");
					Login.clientInfo.addAccounts(acctNum);
					Login.clientInfo.insertAcctBalance((Integer)acctNum, (Double)0.00);
					Login.clientInfo.insertAcctTypes((Integer)acctNum, Login.clientInfo.getSaveOrCheck());
				}		
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//insert into the joint account table incase necessary
		}
		

		query = "INSERT INTO bank_account_joint VALUES (?,"+ acctNum + ")";
		Login.dataB.setPreparedStatement(query);
		check = Login.dataB.alterDatabase(Login.clientInfo.getUserName());

		//checking if query succeeded
		if (check <= 0) {
			return false;
		}
		
		return true;
	}
	
}
