package test.bank;
import java.util.Scanner;

public class login {
	
	
	public static void main(String[] args) {
		System.out.println("Press 1 if you already have a login id: \nPress 2 if you are new: \n");
		
		Scanner input = new Scanner (System.in);
		int number = input.nextInt();
		
		if (number == 1) {
			
		}
		
		else if (number == 2) {
			//run the new page function class 
			newCustomer customer = new newCustomer();
			customer.printClientInfo();
		}
	}
}
