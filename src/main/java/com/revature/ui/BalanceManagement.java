package com.revature.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;

public class BalanceManagement extends InfoPage {
	
	private int choice;
	private String query;
	
	//default constructor
	public BalanceManagement() {
		choice = 0;
	}
	
	public void accountEdit() {
		Login.dataB.setCommitFalse();
		int acctNum;
		int tarNum;
		double dollarEdit;
		double dollarBal;
		super.printInfo();
		if (choice == 1 || choice == 2) {
			
			while(true) {
				System.out.println("Which Account Would You Like to Make the Change?");
				try{
					Login.input.nextLine();
					acctNum = Login.input.nextInt();
					
					//checking if account holder is authorized
					try {
						query = "SELECT * FROM bank_account_joint WHERE account_number = " + acctNum + " AND userid = '" + Login.clientInfo.getUserName() + "'"; 
						Login.dataB.setPreparedStatement(query);
						Login.queryResult = Login.dataB.queryStatements();
						
						if(Login.queryResult.next() == false) {
							System.out.println("Wrong Account Number or You are not Authorized For the Transaction, Please Try Again");
							return;
						}						
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					
					//getting the dollar edit
					if(choice == 1) {
						System.out.println("How of a Deposit Would You Like to Make?");
					}
					else if(choice == 2) {
						System.out.println("How Much Would You Like to withdrawal");
					}

					Login.input.nextLine();
					dollarEdit = Login.input.nextDouble();
					
					
					//deposit route
					if(choice == 1) {
						query = "UPDATE bank_account SET funds = funds + " + dollarEdit + " WHERE account_number = " + acctNum;
						Login.dataB.setPreparedStatement(query);
						Login.dataB.alterDatabase();
						Login.dataB.commit();
						
						query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + acctNum + ", DEFAULT," + dollarEdit + ")";
						Login.dataB.setPreparedStatement(query);
						Login.dataB.alterDatabase();
						Login.dataB.commit();
						
						return;
					}
				
					//withdrawal route and first checking if balance is sufficient;
					if(choice == 2) {
						query = "SELECT * FROM bank_account WHERE account_number = " + acctNum; 
						Login.dataB.setPreparedStatement(query);
						Login.queryResult = Login.dataB.queryStatements();
						
						Login.queryResult.next();
						double curBal = Login.queryResult.getDouble("funds");
						dollarBal = curBal - dollarEdit;
						
						if(dollarBal < 0) {
							System.out.println("This Would Bring the Balance to a Negative, Please Do Something Else");
							continue;
						}
						else {
							query = "UPDATE bank_account SET funds = funds - " + dollarEdit + " WHERE account_number = " + acctNum;
							Login.dataB.setPreparedStatement(query);
							Login.dataB.alterDatabase();
							Login.dataB.commit();
							
							dollarEdit *= -1
;
							query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + acctNum + ", DEFAULT," + dollarEdit + ")";
							Login.dataB.setPreparedStatement(query);
							Login.dataB.alterDatabase();
							Login.dataB.commit();
							return;
						}	
					}	
				}			
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		
		
		else if (choice == 3) {
			while(true) {
				System.out.println("Which Account Would You Like to Make the Change?");
				try{
					Login.input.nextLine();
					acctNum = Login.input.nextInt();
					
					//checking if account holder is authorized
					query = "SELECT * FROM bank_account_joint WHERE account_number = " + acctNum + " AND userid = '" + Login.clientInfo.getUserName() + "'"; 
					Login.dataB.setPreparedStatement(query);
					Login.queryResult = Login.dataB.queryStatements();
					
					if(Login.queryResult.next() == false) {
						System.out.println("Wrong Account Number or You are not Authorized For the Transaction, Please Try Again");
						return;
					}
					
					//Checking if fund transfer acct # is correct
					System.out.println("Which Account Would You Like to Transfer the Money to?");
					Login.input.nextLine();
					tarNum = Login.input.nextInt();
					query = "SELECT * FROM bank_account WHERE account_number = " + tarNum;
					Login.dataB.setPreparedStatement(query);
					Login.queryResult = Login.dataB.queryStatements();
					
					if(Login.queryResult.next() == false) {
						System.out.println("Please Verify the Account Number, Thank You");
						return;
					}
					
					//Checking if enough money in the account
					System.out.println("How Much Would You Like to Transfer?");

					Login.input.nextLine();
					dollarEdit = Login.input.nextDouble();
					
					query = "SELECT * FROM bank_account WHERE account_number = " + acctNum; 
					Login.dataB.setPreparedStatement(query);
					Login.queryResult = Login.dataB.queryStatements();
					
					Login.queryResult.next();
					double curBal = Login.queryResult.getDouble("funds");
					dollarBal = curBal - dollarEdit;
					
					if(dollarBal < 0) {
						System.out.println("This Would Bring the Balance to a Negative, Please Do Something Else");
						continue;
					}
					
					
					query = "UPDATE bank_account SET funds = funds + " + dollarEdit + " WHERE account_number = " + tarNum;	
							
					Login.dataB.setPreparedStatement(query);
					Login.dataB.alterDatabase();

					query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + acctNum + ", DEFAULT," + dollarEdit + ")";
					Login.dataB.setPreparedStatement(query);
					Login.dataB.alterDatabase();
					Login.dataB.commit();
					
					dollarEdit*= -1;
					
					query = "UPDATE bank_account SET funds = funds + " + dollarEdit + " WHERE account_number = " + acctNum;
					
					Login.dataB.setPreparedStatement(query);
					Login.dataB.alterDatabase();
					
					query = "INSERT INTO bank_transaction VALUES (DEFAULT, " + acctNum + ", DEFAULT," + dollarEdit + ")";
					Login.dataB.setPreparedStatement(query);
					Login.dataB.alterDatabase();
					Login.dataB.commit();
					return;
					
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}
	
	public void printMenu() {
		System.out.println("Please Select From the Following Menu\n");

		System.out.println("1. To Make a Deposit\n");

		System.out.println("2. To Make a Withdrawal\n");
		
		System.out.println("3. To Make a Transfer\n");
		
		System.out.println("4. To Go back to the Main Menu\n");
		
	}
	
	public int setupUI() {
		while(true) {
			try{
				this.printMenu();
				Login.input.nextLine();
				choice = Login.input.nextInt();
				if(choice>=1 && choice <4) {accountEdit(); continue;}
				else if(choice == 4) {return 3;}
				else {
					System.out.println("Please Enter a Number in the Menu");
				}
			
			}
			catch(InputMismatchException e) {
				System.out.println("You have not Selected a Valid input. Please Try again\n");
				continue;
			}
		}		
		
	}
	
}
