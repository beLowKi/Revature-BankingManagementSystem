package com.banking;

//import java.sql.Date;
//import java.time.Instant;
import java.util.Scanner;

import com.banking.enums.AccountType;
import com.banking.model.Account;
import com.banking.model.Customer;

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

	/**
	 * Creates a new Customer in the database.
	 * 
	 * @param sc Scanner to intake user input
	 */
	public static void createCustomer(final Scanner sc) {

		System.out.println("Creating customer:");

		// Collecting properties
		final String name;
		final String email;
		final String phone;

		System.out.print("\tEnter name: ");
		name = sc.nextLine();

		System.out.print("\tEnter email address: ");
		email = sc.nextLine();

		System.out.print("\tEnter phone number: ");
		phone = sc.nextLine();

		// Building object
		final Customer c = new Customer(name, email, phone);

		System.out.println("\n" + c);

		// TODO add to database

	}

	/**
	 * Creates a new Account in the database.
	 * 
	 * @param sc Scanner to intake user input.
	 */
	public static void createAccount(final Scanner sc) {

		System.out.println("Creating Account:");

		// Collecting data
//		final long number;  TBD I'm pretty sure this is gonna be DB-generated
		final int customerId;
		AccountType type = null;
		final double balance;
//		final Date date;

//		System.out.print("\tEnter account number: ");
//		number = sc.nextLong();

		System.out.print("\tEnter customer ID: ");
		customerId = sc.nextInt();
		sc.nextLine();
		
		System.out.print(
			"\tEnter acount type " + 
			"(" + AccountType.SAVINGS + ", " + AccountType.CURRENT + "): "
		);
		
		// TODO verify that a Customer with this ID exists in DB

		// Loops until a valid match to AccountType enum is received
		// TODO extract as general method to match input string
		// to StrEnum for TransactionType later?
		do {
			// TODO try and put print statement in here and have
			// it reset cursor on invalid input with bonus ERROR msg?

			final String input = sc.nextLine().toUpperCase();

			if (input.equals(AccountType.CURRENT.getValue())) {
				type = AccountType.CURRENT;

			} else if (input.equals(AccountType.SAVINGS.getValue())) {
				type = AccountType.SAVINGS;

			} else {
				System.err.println("\nInvalid input");
			}

		} while (type == null);

		System.out.print("\tEnter balance: ");
		balance = sc.nextDouble();
		sc.nextLine();

		// Building Account object
		final Account acc = new Account(customerId, type, balance);
		System.out.println("\n" + acc);

		// TODO save to DB
	}

	public static void main(String[] args) {

		// Scanner for user input
		final Scanner sc = new Scanner(System.in);

		// Main menu loop
		int choice = -1;

		while (choice != 9) {
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

			sc.nextLine();
			System.out.println();

			// Performing action
			switch (choice) {
				case 1:
					createCustomer(sc);
					break;

				case 2:
					createAccount(sc);
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
			
			// Prompts to continue so any output
			// by the action isn't overwritten immediately
			if (choice != 9) {
				System.out.print("\nPress ENTER to continue");
				sc.nextLine();
			}
		}

		System.out.println("\nExiting Program");
	}

}
