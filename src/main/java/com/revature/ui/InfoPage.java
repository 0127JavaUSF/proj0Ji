package com.revature.ui;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;

public class InfoPage extends Login{
	
	private int choice;
	private String query;
	private int check;
	private int acctNum;
	
	//default constructor
	public InfoPage(){
		
	}
	
	//Print Account Info
	public void printInfo() {
		String query = "SELECT * FROM bank_account INNER JOIN bank_account_joint ON bank_account.account_number = bank_account_joint.account_number WHERE bank_account_joint.userid = ?";
		Login.dataB.setPreparedStatement(query);
		Login.queryResult = Login.dataB.queryStatements(Login.clientInfo.getUserName());
		
		try {
			while(queryResult.next()) {
				Integer acctNum = Login.queryResult.getInt("account_number");
				Double balance = Login.queryResult.getDouble("funds");
				String acctType = Login.queryResult.getString("typeofacct");
				
				Login.clientInfo.setAcctBalance(acctNum, balance);
				
				System.out.printf("Your %s Account: %d\nHas a Balance: %.2f\n\n", acctType, acctNum, balance);
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void printTransaction() {
		printInfo();
		int acctNumber;
		System.out.println("Which Account Would You Like to See Transaction History For?\n");
		while(true) {
			try {
				Login.input.nextLine();
				acctNumber = Login.input.nextInt();
			}
			catch(InputMismatchException e) {
				System.out.println("You have not Selected a Valid input. Please Try again\n");
				continue;
			}
			
			if(Login.clientInfo.intValid(acctNumber)) {
				query = "SELECT * FROM bank_account INNER JOIN bank_transaction ON bank_account.account_number = bank_transaction.account_number WHERE bank_transaction.account_number = " + acctNumber;
				Login.dataB.setPreparedStatement(query);
				Login.queryResult = Login.dataB.queryStatements();
				try {
					while(Login.queryResult.next()) {
						int tmpTransID = Login.queryResult.getInt("transid");
						Timestamp tmpTime = Login.queryResult.getTimestamp("timeoftransaction");
						Double tmpAmount = Login.queryResult.getDouble("fundsdiffer");
						String stringTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(tmpTime);
						
						
						System.out.printf("Transaction ID: %d |  Time of Transaction: %s | Amount of Transaction: %.2f \n", tmpTransID, stringTime,tmpAmount);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else {
				System.out.println("You are not Authorized to Look into this Data\n");
			}
			return;
		}		
	}
	
	//Print Menu Selection
	public void printMenu() {
		System.out.println("\nPlease Select From the Following Menu\n");

		System.out.println("1. To Create Another Account\n");
		
		System.out.println("2. To Join to Another Account\n");
		
		System.out.println("3. To Close an Account (Only the Primay Account Holder is Authorized)\n");
		
		System.out.println("4. To Kick Someone out of a Joint Account(Only the Primay Account Holder is Authorized)\n");
		
		System.out.println("5. To Make a Deposit, Withdrawal, or a Transfer\n");
		
		System.out.println("6. Transaction History\n");
		
		System.out.println("7. Last But Not Least... Wanna Gamble?\n");
	
		System.out.println("0. If You Wish to Leave\n");
		
		System.out.println("Please Press the Number Corresponding to the choices on the list.\n");
	}
	
	//choices 1-4 really bad organization
	public void accountEdit() {
		Login.dataB.setCommitFalse();
		//if adding
		if(choice == 1) {
			String acctType = "nada";
			int checkCounter = 0;
			
			while(acctType.equalsIgnoreCase("Checking") == false && acctType.equalsIgnoreCase("Saving") == false)
			{	
				if(checkCounter !=0 ) {System.out.println("Please Type Checking or Saving");}
				System.out.println("Will this be a Checking or Saving?");
				acctType = Login.input.next();
				checkCounter++;
			}
			
			
			query = "INSERT INTO bank_account VALUES (DEFAULT, 0.00, ?, ?)";
			Login.dataB.setPreparedStatement(query);
			
			check = Login.dataB.alterDatabase(acctType,Login.clientInfo.getUserName());
			
	
			//checking if query succeeded
			if (check <= 0) {
				System.out.println("Error");
			}		
			
			//extract the account numbers since it is the first account no need to worry about collisions
			query = "SELECT * FROM bank_account WHERE userid = ? ORDER BY account_number DESC";
			
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
			query = "INSERT INTO bank_account_joint VALUES (?,"+ acctNum + ")";
			Login.dataB.setPreparedStatement(query);
			check = Login.dataB.alterDatabase(Login.clientInfo.getUserName());
			Login.dataB.commit();
			Login.dataB.setCommitTrue();
			
		}
		
		
		//adding a joint bank account
		if(choice == 2) {
			int newAcctNum = 0;
			int checkCounter = 0;
			String primUsername;
			while(true) {
				if(checkCounter !=0 ) {System.out.println("The Account Number or Primary Username Cannot be Verified, Please Try Again");return;}
				System.out.println("Please Type in the Account Number You Wish to Join");
				try{
					Login.input.nextLine();
					newAcctNum = Login.input.nextInt();
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}
				System.out.println("Please Type in the Primay Acct Holder's UserID to Verify");
				primUsername = Login.input.next();
				checkCounter++;
				
				//checking if already a joint acct holder
				query = "SELECT * FROM bank_account_joint WHERE account_number = " + newAcctNum + " AND userid = '" + Login.clientInfo.getUserName() + "'";
				Login.dataB.setPreparedStatement(query);
				Login.queryResult =  Login.dataB.queryStatements();
				
				try {
					if(Login.queryResult.next()) {
						System.out.println("You're already a Joint Holder or the Primary Holder. Good Bye");
						return;
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				query = "SELECT * FROM bank_account WHERE account_number = " + newAcctNum + " AND userid = '" + primUsername +  "'";
				Login.dataB.setPreparedStatement(query);
				Login.queryResult =  Login.dataB.queryStatements();
				
				try {
					//checking if information inputed is valid
					if(Login.queryResult.next() == false) {
						System.out.println("Either account number or userid is wrong. Please try again");
						continue;
					}
					
					Login.clientInfo.addAccounts(newAcctNum);
					break;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//insert into the joint account table incase necessary
			
			query = "INSERT INTO bank_account_joint VALUES (?,"+ newAcctNum + ")";
			Login.dataB.setPreparedStatement(query);
			check = Login.dataB.alterDatabase(Login.clientInfo.getUserName());
			
			Login.dataB.commit();
			Login.dataB.setCommitTrue();
		}
		
		if(choice == 3) {
			
			int newAcctNum = 0;
			int checkCounter = 0;
			double balanceTemp = 0;
			String primUsername;
			
			while(true) {
				if(checkCounter !=0 ) {System.out.println("The Account Number or Primary Username is not Authorized, Please Try Again");return;}
				System.out.println("Please Type in the Account Number You Wish to Delete");
				try{
					Login.input.nextLine();
					newAcctNum = Login.input.nextInt();
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}
				checkCounter++;
				
				query = "SELECT * FROM bank_account WHERE account_number = " + newAcctNum;
				Login.dataB.setPreparedStatement(query);
				Login.queryResult =  Login.dataB.queryStatements();
				try {
					if(Login.queryResult.next()) {
						primUsername = Login.queryResult.getString("userid");
						balanceTemp = Login.queryResult.getDouble("funds");
						if(Login.clientInfo.getUserName().equals(primUsername)==false){
							System.out.println("You are not the Primary Account Holder and are not Authroized to Delete this Account");
							return;
						}
						
						if(balanceTemp != 0.0) {
							System.out.println("You still have Funds left in your Account. Please Transfer your Funds before Deletion");
							return;
						}
							
						else {
							query = "DELETE FROM bank_account_joint WHERE account_number = " + newAcctNum;				
							Login.dataB.setPreparedStatement(query);
							Login.dataB.alterDatabase();
							Login.dataB.commit();

							query = "DELETE FROM bank_transaction WHERE account_number = " + newAcctNum;				
							Login.dataB.setPreparedStatement(query);
							Login.dataB.alterDatabase();
							Login.dataB.commit();
							
							query = "DELETE FROM bank_account WHERE account_number = " + newAcctNum;
							Login.dataB.setPreparedStatement(query);
							Login.dataB.alterDatabase();
							Login.dataB.commit();
						}
						Login.clientInfo.remAccounts(acctNum);
						Login.clientInfo.rmAcctBalance(acctNum);
						Login.clientInfo.rmAcctType(acctNum);

						Login.dataB.commit();
						Login.dataB.setCommitTrue();
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		
		if(choice == 4) {
			
			int newAcctNum = 0;
			int checkCounter = 0;
			double balanceTemp = 0;
			String primUsername;
			String targetName;
			
			while(true) {
				if(checkCounter !=0 ) {System.out.println("The Account Number or Primary Username is not Authorized, Please Try Again");return;}
				System.out.println("Please Type in the Account Number You Wish to Remove a Junior Holder From");
				try{
					Login.input.nextLine();
					newAcctNum = Login.input.nextInt();
				}
				catch(InputMismatchException e) {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}
				checkCounter++;
				
				query = "SELECT * FROM bank_account WHERE account_number = " + newAcctNum;
				Login.dataB.setPreparedStatement(query);
				Login.queryResult =  Login.dataB.queryStatements();
				try {
					if(Login.queryResult.next()) {
						primUsername = Login.queryResult.getString("userid");
						if(Login.clientInfo.getUserName().equals(primUsername)==false){
							System.out.println("You are not the Primary Account Holder and are not Authroized\n");
							return;
						}
						
						else {
							System.out.println("Please Enter the UserID of the Individual You Wish to Remove from the Account");
							targetName = Login.input.next();
							
							if(targetName.contentEquals(Login.clientInfo.getUserName())) {
								System.out.println("Please Delete the Account if You Wish to Remove Yourself");
								return;
							}
							
							query = "DELETE FROM bank_account_joint WHERE userid = '" + targetName + "' AND account_number = " + newAcctNum;		

							Login.dataB.setPreparedStatement(query);
							if(Login.dataB.alterDatabase() <= 0 ) {
								System.out.println("User Doesn't Exist");
								return;
							}
							
							
							
							Login.dataB.commit();
							Login.dataB.setCommitTrue();

							return;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	//UI Setup
	public int setupUI() {
		System.out.println("\nWelcome to Petro Casino Bank " + Login.clientInfo.getFirstName());
		System.out.println("Reminder");
		System.out.println("Rule Number One of Petro Bank, You Don't Talk About Petro Bank\n");
		
		//Loop to run again and again until choice requires a departure from this UI to another UI
		while(true) {
			this.printInfo();
			this.printMenu();
			try {
				Login.input.nextLine();
				choice = Login.input.nextInt();
				if(choice == 5){return 4;}
				else if(choice == 6){printTransaction();}
				else if(choice == 7){return 5;}
				else if(choice == 0){System.out.println("BYE BYE\n"); return 0;}
				else if(choice > 0 && choice <=4){accountEdit();}
					
				
				else {
					System.out.println("You have not Selected a Valid input. Please Try again\n");
					continue;
				}
			}
			catch(InputMismatchException e) {

				System.out.println("You have not Selected a Valid input. Please Try again\n");
			}
		}
	}
	
	
	
	
}
