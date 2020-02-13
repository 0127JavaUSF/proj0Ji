package test.bank;

import java.util.Scanner;
import java.util.Random; 


public class newCustomer {
	protected Scanner userInput;
	protected String confirm;
	protected String firstName;
	protected String lastName;
	
	protected int startingBalance;
	protected int pin;
	protected int bankNumber;
	
	public void getInput() {
		userInput = new Scanner(System.in);
		
		System.out.println("Please enter your First Name");
		firstName = userInput.next();
		
		System.out.println("Please enter your Last Name");
		lastName = userInput.next();
		
		System.out.println("How much starting balance would you like?");
		startingBalance = userInput.nextInt();
		
		System.out.println("Please enter your 4 digit pin#");
		pin = userInput.nextInt();
		
		
		

		System.out.println("Confirm, your name is " + firstName + " " + lastName + " and your Pin is " + pin + "\nDo you confirm?");
		
		confirm = userInput.next();
	}
	
	public void printClientInfo() {
		System.out.println("Name: " + firstName + " " + lastName);
		System.out.println("Bank Account Number: " + bankNumber);
		System.out.println("Balance: " + startingBalance);
		
	}
	
	newCustomer() {
		confirm = "no";
		while(confirm.equalsIgnoreCase("no") == true) {
			getInput();
		}
		
		if(confirm.equalsIgnoreCase("yes") == true) {
			Random rand = new Random(); 
			bankNumber = rand.nextInt(80000);
			//check if the bankNumber already exist
			  
			
			//if not there then voila~
		}
	}
}
