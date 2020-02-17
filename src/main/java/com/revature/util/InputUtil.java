package com.revature.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtil {
	protected Scanner input;
	
	public InputUtil(){
		input = new Scanner(System.in);
	}
	
	public boolean getIntInput(Integer inputNum) {
		try {
			input.nextLine();
			inputNum = input.nextInt();
			return true;
			
		}
		catch(InputMismatchException e) {
			System.out.println("You have not Selected a Valid input. Please Try again\n");
			return false;
		}
	}
	
	public boolean getDoubleInput(Double inputDouble) {
		try {
			input.nextLine();
			inputDouble = input.nextDouble();
			return true;
			
		}
		catch(InputMismatchException e) {
			System.out.println("You have not Selected a Valid input. Please Try again\n");
			return false;
		}
	}
	
}
