package com.banking;

import java.util.Scanner;

import com.banking.enums.AccountType;
import com.banking.exception.BackendException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.operations.BankingOperations;

public class Main {

	// src https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	// ANSI Escape sequence to reset text color
	public static final String ANSI_RESET = "\u001B[0m";

	// ANSI Escape sequences for various colors
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";


	// "Clears" console output
	public static void clearScreen() {

		// This kind of ANSI escape doesn't work
		// with Eclipse's console :(
		// System.out.print("\033[H\033[2J");
		// System.out.flush();

		for ( int i = 0; i < 50; i++ ) {

			System.out.println();

		}

	}


	// Loops user prompt until a non-empty string is provided
	public static String getNonEmptyString( final Scanner sc, final String prompt, boolean usePrintLn ) {

		if ( usePrintLn ) {
			System.out.println( prompt );
		} else {
			System.out.print( prompt );
		}

		String input = "";

		while ( input.isEmpty() ) {
			input = sc.nextLine();
			if ( input.isEmpty() ) {
				System.out.println( ANSI_RED + "\nInvalid input; cannot be empty string\n" + ANSI_RESET );
			}
		}

		return input;

	}


	/**
	 * Creates a new Customer in the database.
	 * 
	 * @param  sc               Scanner to intake user input
	 * @throws BackendException
	 */
	public static void createCustomer( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting properties
		System.out.println( "Creating customer:" );

		final String name = getNonEmptyString( sc, "\tEnter name: ", false );
		final String email = getNonEmptyString( sc, "\tEnter email address: ", false );
		final String phone = getNonEmptyString( sc, "\tEnter phone number: ", false );

		// Building object
		final Customer c = new Customer( name, email, phone );

		// Adding to database
		try {
			backend.createCustomer( c );
			System.out.println( ANSI_GREEN + "\nSuccessfully created new Customer" );

		} catch ( BackendException e ) {
			System.out.println( ANSI_RED + "\n" + e.getMessage() );

			// DEBUG
			//			e.printStackTrace();

		}

		// Reset console text color
		System.out.print( ANSI_RESET );

	}


	/**
	 * Handles when customer details are requested. Prompts the user to input a
	 * Customer ID, searches the backend for a match, then finally prints the
	 * results to console.
	 * 
	 * @throws BackendException
	 */
	public static void viewCustomerDetails( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Prompting user for Customer ID
		int customerId = -1;

		System.out.print( "\tEnter Customer ID: " );

		// Loops until valid ID is input
		while ( customerId <= 0 ) {

			customerId = sc.nextInt();
			if ( customerId > 0 ) {
				break;
			}

			System.out.println( ANSI_RED + "\nInvalid input; must be greater than 0\n" + ANSI_RESET );

		}

		sc.nextLine();
		System.out.println();

		// Finding matching Customer in backend
		Customer c = null;

		try {
			c = backend.getCustomer( customerId );
		} catch ( BackendException e ) {
			System.out.println( ANSI_RED + "\n" + e.getMessage() + ANSI_RESET );
			return;
		}

		// No Customer matching the given ID was found
		if ( c == null ) {
			System.out.println( ANSI_YELLOW + "No Customer with ID " + customerId + " found" + ANSI_RESET );
			return;
		}

		// Displaying details
		// Header
		System.out.printf(
			ANSI_YELLOW + "%20.20s %20.20s %20.20s %20.20s" + ANSI_RESET + "\n",
			"ID",
			"NAME",
			"EMAIL",
			"PHONE"
		);
		System.out
			.println( String.format( "+%20.20s|%20.20s|%20.20s|%20.20s+", " ", " ", " ", " " ).replaceAll( " ", "-" ) );

		// Content
		System.out
			.printf( "%20d | %18.18s | %18.18s | %18.18s \n", c.getId(), c.getName(), c.getEmail(), c.getPhone() );
	}


	/**
	 * Creates a new Account in the database.
	 * 
	 * @param  sc               Scanner to intake user input.
	 * @throws BackendException
	 */
	public static void createAccount( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting data
		System.out.println( "Creating Account:" );

		final int customerId;
		AccountType type = null;
		final double balance;

		System.out.print( "\tEnter customer ID: " );
		customerId = sc.nextInt();
		sc.nextLine();

		// Making sure this Customer exists
		try {
			if ( backend.getCustomer( customerId ) == null ) {
				System.out.println( ANSI_RED + "\nCustomer with ID " + customerId + " not found" + ANSI_RESET );
				return;
			}
		} catch ( BackendException e ) {
			System.out.println( ANSI_RED + "\n" + e.getMessage() + ANSI_RESET );

			// DEBUG
			//			e.printStackTrace();

			return;
		}

		System.out.print( "\tEnter account type " + "(" + AccountType.SAVINGS + ", " + AccountType.CURRENT + "): " );

		// Loops until a valid match to AccountType enum is received
		// TODO extract as general method to match input string
		// to StrEnum for TransactionType later?
		do {
			final String input = sc.nextLine().toUpperCase();

			if ( input.equals( AccountType.CURRENT.getValue() ) ) {
				type = AccountType.CURRENT;

			} else if ( input.equals( AccountType.SAVINGS.getValue() ) ) {
				type = AccountType.SAVINGS;

			} else {
				System.err.println( "\nInvalid input" );
			}

		} while ( type == null );

		System.out.print( "\tEnter balance: " );
		balance = sc.nextDouble();
		sc.nextLine();

		// Building Account object
		final Account acc = new Account( customerId, type, balance );
		System.out.println();

		// Saving to backend
		try {
			backend.createAccount( acc );
			System.out.println( ANSI_GREEN + "Successfully created new " + type.getValue() + " account" );

		} catch ( BackendException e ) {
			System.out.println( ANSI_RED + "\n" + e.getMessage() );
		}

		// Reset console text color
		System.out.print( ANSI_RESET );

	}


	public static void main( String[] args ) {

		// Connecting to backend service
		BankingOperations backend = null;

		try {
			backend = BankOperations.getInstance();
		} catch ( BackendException e ) {
			e.printStackTrace();
			System.exit( 1 );
		}

		// Scanner for user input
		final Scanner sc = new Scanner( System.in );

		// Main menu loop
		int choice = -1;

		while ( choice != 9 ) {

			clearScreen();

			// Printing header /w title
			System.out.println();
			System.out.println( "====================================" );
			System.out.println( "       BANKING MANAGEMENT SYSTEM" );
			System.out.println( "====================================" );
			System.out.println( '\n' );

			// Displaying Options
			System.out.println( "\t1. Create Customer" );
			System.out.println( "\t2. Create Bank Account" );
			System.out.println( "\t3. View Customer Details" );
			System.out.println( "\t4. Deposit Money" );
			System.out.println( "\t5. Withdraw Money" );
			System.out.println( "\t6. Check Account Balance" );
			System.out.println( "\t7. Transfer Money" );
			System.out.println( "\t8. View Transaction History" );
			System.out.println( "\t9. Exit" );
			System.out.println( '\n' );

			// Getting input
			System.out.print( "Enter your choice: " );

			// Assumes that any failure in getting
			// input was on the user
			try {
				choice = sc.nextInt();
			} catch ( Exception e ) {
				choice = -1;
			}

			sc.nextLine();
			System.out.println();

			// Performing action
			try {

				switch ( choice ) {

					case 1:
						createCustomer( sc );
						break;

					case 2:
						createAccount( sc );
						break;

					case 3:
						viewCustomerDetails( sc );
						;
						break;

					case 4:
						System.out.println( "Making Deposit" );
						break;

					case 5:
						System.out.println( "Making Withdrawal" );
						break;

					case 6:
						System.out.println( "Showing Account Balance" );
						break;

					case 7:
						System.out.println( "Making Transfer" );
						break;

					case 8:
						System.out.println( "Showing Transaction History" );
						break;

					case 9:
						break;

					default:
						System.err.println( "\nInvalid input" );
						sc.nextLine();

				}
			} catch ( BackendException e ) {
				System.err.println( "Error connecting to backend: " + e.getMessage() );
			}

			// Prompts to continue so any output
			// by the action isn't overwritten immediately
			if ( choice != 9 ) {

				System.out.print( "\nPress ENTER to continue" );
				sc.nextLine();

			}

		}

		System.out.println( "\nExiting Program" );
	}

}
