package com.banking;

import java.util.Scanner;

public class Main {

	public static void clearScreen() {
		
		// This kind of ANSI escape doesn't work
		// with Eclipse's console :(
//		System.out.print("\033[H\033[2J");
//		System.out.flush();
		
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
		
	}
	
	
	public static void main(String[] args) {
		
		// Scanner for user input
		final Scanner sc = new Scanner(System.in);
		
		// Main menu loop
		int choice = -1;
		
		while ( choice != 9 ) {
			clearScreen();
			
			// Printing header /w title
			System.out.println();
			System.out.println("====================================");
			System.out.println("       BANKING MANAGEMENT SYSTEM");
			System.out.println("====================================");
			System.out.println('\n');
			
			// Displaying Options
			System.out.println("\t1. Create Customer");
			System.out.println("\t2. Create Bank Account");
			System.out.println("\t3. View Customer Details");
			System.out.println("\t4. Deposity Money");
			System.out.println("\t5. Withdraw Money");
			System.out.println("\t6. Check Account Balance");
			System.out.println("\t7. Transfer Money");
			System.out.println("\t8. View Transaction History");
			System.out.println("\t9. Exit");
			System.out.println('\n');
			
			// Getting input
			System.out.print("Enter your choice: ");
			
			// Assumes that any failure in getting
			// input was on the user
			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				choice = -1;
			}
			
			// Performing action
			switch (choice) {
				case 1:
					System.out.println("Creating Customer");
					break;
				
				case 2:
					System.out.println("Creating Account");
					break;
					
				case 3:
					System.out.println("Showing Customer Details");
					break;
					
				case 4:
					System.out.println("Making Deposit");
					break;
					
				case 5:
					System.out.println("Making Withdrawal");
					break;
					
				case 6:
					System.out.println("Showing Account Balance");
					break;
					
				case 7:
					System.out.println("Making Transfer");
					break;
					
				case 8:
					System.out.println("Showing Transaction History");
					break;
					
				case 9:
					break;
					
				default:
					System.err.println("\nInvalid input");
					sc.nextLine();
			}
			
		}
		
		System.out.println("\nExiting Program");
	}

}
