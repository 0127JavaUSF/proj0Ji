package com.revature.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Random;

public class Gamble extends InfoPage{
	
	private Random rand;
	private int accountNum;
	private int choiceMenu;
	private double gambleAmount;
	private double dollarBal;
	String query;
	
	public void accountEdit(int type,double dollarEdit, double multiplier) {
		if(type == 1) {
			double newDollarEdit = dollarEdit * multiplier;
			query = "UPDATE bank_account SET funds = funds + " + newDollarEdit + " WHERE account_number = " + accountNum;
			Login.dataB.setPreparedStatement(query);
			Login.dataB.alterDatabase();
			
			query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + accountNum + ", DEFAULT," + newDollarEdit + ")";
			Login.dataB.setPreparedStatement(query);
			Login.dataB.alterDatabase();
		}
		
		if(type == 0) {
			query = "UPDATE bank_account SET funds = funds - " + dollarEdit + " WHERE account_number = " + accountNum;
			Login.dataB.setPreparedStatement(query);
			Login.dataB.alterDatabase();
			
			dollarEdit *= -1
;
			query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + accountNum + ", DEFAULT," + dollarEdit + ")";
			Login.dataB.setPreparedStatement(query);
			Login.dataB.alterDatabase();
		}
		
		
	}
	
	public Gamble() {
		rand = new Random();
	}
	
	public int setupUI() {
		System.out.println("Welcome to the Casino");
		System.out.println("The Game is a Good Ol Roulette\n");
		
  
        // Generate random integers in range 0 to 999 
        
		while(true) {
			super.printInfo();
			int randomNumber = rand.nextInt(38); 
			
			System.out.println("Please Type Which Account You wish to Play With\n");
			
			try {
				Login.input.nextLine();
				accountNum = Login.input.nextInt();
			}
			catch(InputMismatchException e) {
				System.out.println("You have not Selected a Valid input. Please Try again\n");
				continue;
			}
			
			if (Login.clientInfo.intValid(accountNum) == false) {
				System.out.println("You Cannot Access this Account\n");
			}
			
			System.out.println("Please Enter an Amount You Wish to Gamble: ");
			
			try {
				Login.input.nextLine();
				gambleAmount = Login.input.nextDouble();
			}
			catch(InputMismatchException e) {
				System.out.println("You have not Selected a Valid input. Please Try again\n");
				continue;
			}
			
			query = "SELECT * FROM bank_account WHERE account_number = " + accountNum; 
			Login.dataB.setPreparedStatement(query);
			Login.queryResult = Login.dataB.queryStatements();
			
			try {
				Login.queryResult.next();
				double curBal = Login.queryResult.getDouble("funds");
				dollarBal = curBal - gambleAmount;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(dollarBal < 0) {
				System.out.println("This Would Bring the Balance to a Negative, Please Do Something Else");
				continue;
			}
			
			System.out.println("What Game Would You Like to Play?\n");
			System.out.println("1. Pick a Number? \n");
			System.out.println("2. Black or Red?\n");
			System.out.println("3. Go Back to the Main Menu");
			
			try {
				Login.input.nextLine();
				choiceMenu = Login.input.nextInt();
				
			}
			catch(InputMismatchException e) {
				System.out.println("You have not Selected a Valid input. Please Try again\n");
				continue;
			}
			
			if (choiceMenu == 1) {
				System.out.println("Pick a Number between 0-36");
				
				try {	
					Login.input.nextLine();
					choiceMenu = Login.input.nextInt();
					
					if(choiceMenu > 36) {
						System.out.println("That Number Doesn't Exist.");
						continue;
					}
					
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}

				if(randomNumber == choiceMenu) {
					System.out.println("The Number IS! " + randomNumber + "! ");
					System.out.println("You WON!");
					this.accountEdit(1,gambleAmount,34);
					continue;
				}
				
				else {
					System.out.println("The Number IS! " + randomNumber + "! ");
					System.out.println("You LOST!");
					this.accountEdit(0,gambleAmount,1);
					continue;
				}
			}
			
			if (choiceMenu == 2) {
				System.out.println("Pick 1 for Black and 2 For Red");
				try {
					Login.input.nextLine();
					choiceMenu = Login.input.nextInt();
					
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}
				
				if(randomNumber%2 == 0 && choiceMenu == 2) {
					System.out.println("RED!");
					System.out.println("You WON!");
					this.accountEdit(1,gambleAmount,2);
					continue;
				}
				
				else if(randomNumber%2 == 1 && choiceMenu == 1) {
					System.out.println("BLACK!");
					System.out.println("You WON!");
					this.accountEdit(1,gambleAmount,2);
					continue;
				}
				
				else {
					System.out.printf("AWWWWE~ Sorry but You Lost $%.2f\n", gambleAmount);
					this.accountEdit(0,gambleAmount,gambleAmount);
					continue;
				}
				
			}
			
			if(choiceMenu == 3) {
				return 3;
			}
			
			
		}
	}
}
