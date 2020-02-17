package com.revature.ui;

import java.util.Scanner;

import java.sql.*;
import java.util.ArrayList;
import com.revature.verify.*;
import com.revature.clientInfo.*;
import com.revature.doa.*;
import com.revature.util.InputUtil;

public class Login implements Verify{
	
	//static variables for all class to access
	protected static ClientContainer clientInfo;
	protected static Scanner input;
	protected static DOA dataB;
	protected static ResultSet queryResult;
	
	protected static InputUtil inUtil;
	//protected static Verify verifyData;
	
	
	public Login(){
		//initializing necesary values
		Login.clientInfo = new ClientContainer();
		Login.input = new Scanner(System.in);
		Login.dataB = new DOA();
		inUtil = new InputUtil();
	}

	
	public int setupUI() {
		String userName;
		String passWord;
		boolean isVerified = false;
		
		
		//printout the welcome statements and check if it is an existing customer
		System.out.println("Welcome to the Petro Casino Bank");
		System.out.println("Have you been with us before? (Yes/No)\n");
		
		while(isVerified == false) {
			String inputs = Login.input.next();
	
			isVerified = verifySTDInput(inputs);
			//get username & password if they are and whileloop it if they're not
			if(isVerified==true && inputs.equalsIgnoreCase("YES")) {
				do {
					System.out.println("What is your Username?");
					userName = Login.input.next();
					System.out.println("What is your Password?");
					passWord = Login.input.next();
					
					Login.clientInfo.setUserName(userName);
					Login.clientInfo.setPassWord(passWord);
					
				}while(verifySQLInput(Login.clientInfo)==false);
				//check if password match in the sql query

				System.out.println("What is your Password)");
				//fill in the container information first
				return 3;
			}
			
			//go to registration name
			else if(isVerified == true && inputs.equalsIgnoreCase("NO")){
				return 2;
				
			}
			
			//continue if wrong input not necessary
			else {
				continue;
			}
		}
		//goto info page
		return 3;
	}


	
	//interface to verify viable input
	public boolean verifySTDInput(String input) {
		if(input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("NO")) {
			return true;
		}
		
		else {
			System.out.println("You need to type Yes or No");
			return false;
		}
	}

	// verify the sql
	public boolean verifySQLInput(ClientContainer sqlClient) {
		
		//query
		String query = "SELECT * FROM client_table WHERE userid = ? AND pass = ?";
		Login.dataB.setPreparedStatement(query);
		
		Login.queryResult = Login.dataB.queryStatements(Login.clientInfo.getUserName(),Login.clientInfo.getPassWord());
		
		try {
			
			//check if login information match
			if(Login.queryResult.next() == false) {
				System.out.println("Wrong Username or Password. Please Re-Type your Username and Password");
				return false;
			}
			
			//set the value of names in the client container and other queries
			Login.clientInfo.setFirstName(Login.queryResult.getString("first_name")); 
			Login.clientInfo.setLastName(Login.queryResult.getString("last_name"));
			
			query = "SELECT * FROM bank_account INNER JOIN bank_account_joint ON bank_account.account_number = bank_account_joint.account_number WHERE bank_account_joint.userid = ?";

			Login.dataB.setPreparedStatement(query);
			Login.queryResult = Login.dataB.queryStatements(Login.clientInfo.getUserName());

			while(Login.queryResult.next()) {
				Integer acctNum = Login.queryResult.getInt("account_number");
				Double balance = Login.queryResult.getDouble("funds");
				String acctType = Login.queryResult.getString("typeofacct");
				
				Login.clientInfo.addAccounts(acctNum);
				Login.clientInfo.insertAcctBalance(acctNum, balance);
				Login.clientInfo.insertAcctTypes((Integer) acctNum, acctType);
			}
			
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


}
