package com.banking;

import java.util.List;
import java.util.Scanner;

import com.banking.enums.AccountType;
import com.banking.exception.BackendException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.operations.BankingOperations;
import com.banking.utility.InputUtility;

public class Main {

	// Initiates "Create Customer" dialogue 
	private static void createCustomer( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting properties
		System.out.println( "Creating customer:" );

		System.out.print( "\tEnter name: " );
		final String name = InputUtility.getNonEmptyString( sc, false );

		System.out.print( "\tEnter email address: " );
		final String email = InputUtility.getNonEmptyString( sc, false );

		System.out.print( "\tEnter phone number: " );
		final String phone = InputUtility.getNonEmptyString( sc, false );

		// Building object
		final Customer c = new Customer( name, email, phone );

		// Adding to database
		try {
			backend.createCustomer( c );
			System.out.println( InputUtility.ANSI_GREEN + "\nSuccessfully created new Customer" );

		} catch ( BackendException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() );

			// DEBUG
			//			e.printStackTrace();

		}

		// Reset console text color
		System.out.print( InputUtility.ANSI_RESET );

	}


	// Initiates "View Customer Details" dialogue
	private static void viewCustomerDetails( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Prompting user for Customer ID
		System.out.print( "\tEnter Customer ID: " );
		Integer customerId = null;

		try {
			customerId = InputUtility.getConditionalInput(
				sc,
				x -> ( x != null && x > 0 ),
				Integer.class,
				false,
				"\nInvalid ID; must be greater than 0"
			);
		} catch ( IllegalArgumentException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n\n" + e.getMessage() + InputUtility.ANSI_RESET );
			e.printStackTrace();
			return;
		}

		System.out.println();

		// Finding matching Customer in backend
		Customer c = null;

		try {
			c = backend.getCustomer( customerId );
		} catch ( BackendException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() + InputUtility.ANSI_RESET );
			return;
		}

		// No Customer matching the given ID was found
		if ( c == null ) {
			System.out.println(
				InputUtility.ANSI_YELLOW + "No Customer with ID " + customerId + " found" + InputUtility.ANSI_RESET
			);
			return;
		}

		// Displaying details
		// Header
		System.out.printf(
			InputUtility.ANSI_YELLOW + "%20.20s %20.20s %20.20s %20.20s" + InputUtility.ANSI_RESET + "\n",
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


	// Initiates "Create Account" dialogue
	private static void createAccount( final Scanner sc ) throws BackendException {

		// Connecting to backend service
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting data
		System.out.println( "Creating Account:" );

		final Integer customerId;
		final AccountType type;
		final Double balance;

		// Getting valid Customer ID
		System.out.print( "\tEnter Customer ID: " );

		try {
			customerId = InputUtility.getConditionalInput(
				sc,
				x -> ( x != null && x > 0 ),
				Integer.class,
				false,
				"\nInvalid ID; must be greater than 0"
			);
		} catch ( IllegalArgumentException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n\n" + e.getMessage() + InputUtility.ANSI_RESET );
			e.printStackTrace();
			return;
		}

		// Making sure this Customer exists
		try {
			if ( backend.getCustomer( customerId ) == null ) {
				System.out.println(
					InputUtility.ANSI_RED + "\nCustomer with ID " + customerId + " not found" + InputUtility.ANSI_RESET
				);
				return;
			}
		} catch ( BackendException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() + InputUtility.ANSI_RESET );

			// DEBUG
			//			e.printStackTrace();

			return;
		}

		// Getting valid AccountType
		System.out.print( "\tEnter account type " + "(" + AccountType.SAVINGS + ", " + AccountType.CURRENT + "): " );
		type = AccountType.valueOf(
			InputUtility.getConditionalInput(
				sc,
				( s ) -> {
					try {
						AccountType.valueOf( s.toUpperCase() );
						return true;
					} catch ( Exception e ) {
						return false;
					}
				},
				String.class,
				false,
				"\nInvalid Account type"
			).toUpperCase()
		);

		// Getting Account balance
		System.out.print( "\tEnter balance: " );
		balance = InputUtility.getConditionalInput(
			sc,
			x -> ( x != null ),
			Double.class,
			false,
			"\nInvalid balance"
		);

		// Building Account object
		final Account acc = new Account( customerId, type, balance );
		System.out.println();

		// Saving to backend
		try {
			backend.createAccount( acc );
			System.out.println( InputUtility.ANSI_GREEN + "Successfully created new " + type.getValue() + " account" );

		} catch ( BackendException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() );
		}

		// Reset console text color
		System.out.print( InputUtility.ANSI_RESET );

	}


	// Dialogue for the user to select a Customer Account.
	// Returns null if unsuccessful.
	private static Account selectCustomerAccount(
		final Scanner sc,
		final BankingOperations backend
	) throws BackendException {

		// Getting Customer ID
		System.out.print( "\tEnter Customer ID: " );
		Integer customerId;

		try {
			customerId = InputUtility.getConditionalInput(
				sc,
				x -> ( x != null && x > 0 ),
				Integer.class,
				false,
				"\nInvalid ID; must be greater than 0"
			);
		} catch ( IllegalArgumentException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n\n" + e.getMessage() + InputUtility.ANSI_RESET );
			e.printStackTrace();
			return null;
		}

		// Getting Accounts
		System.out.println( "\tLoading accounts..." );

		List<Account> accounts = null;

		try {
			accounts = backend.getAccountsOf( customerId );
		} catch ( BackendException e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() + InputUtility.ANSI_RESET );

			// DEBUG
			System.out.println( "\n" );
			e.printStackTrace();

			return null;
		}

		// This Customer has no Accounts
		if ( accounts.size() <= 0 ) {
			return null;
		}

		// Prompts user to select an account
		System.out.println( "\nSelect an account:" );
		return InputUtility.selectFromList( sc, accounts );
	}


	// Initiates "Deposit Money" dialogue
	private static void makeDeposit( final Scanner sc ) throws BackendException {

		// Connecting to backend
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting details from user
		final Account acc = selectCustomerAccount( sc, backend );

		// No accounts were found
		if ( acc == null ) {
			System.out.println( InputUtility.ANSI_YELLOW + "\nNo accounts found" + InputUtility.ANSI_RESET );
			return;
		}

		System.out.print( "\tEnter Amount: " );
		final Double amount = InputUtility.getConditionalInput(
			sc,
			x -> ( x != null && x > 0.0 ),
			Double.class,
			false,
			"\nInvalid amount; must be greater than 0"
		);

		// Making deposit
		try {

			backend.deposit( acc, amount );
			System.out.println( InputUtility.ANSI_GREEN + "\nDeposit successful" + InputUtility.ANSI_RESET );

		} catch ( Exception e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() + InputUtility.ANSI_RESET );
			// DEBUG
			//			System.out.println( "\n" );
			//			e.printStackTrace();

			return;
		}

	}


	// Initiates "Withdraw Money" dialogue
	private static void makeWithdrawal( final Scanner sc ) throws BackendException {

		// Connecting to backend
		final BankingOperations backend = BankOperations.getInstance();

		// Collecting details from user
		final Account acc = selectCustomerAccount( sc, backend );

		// No accounts were found
		if ( acc == null ) {
			System.out.println( InputUtility.ANSI_YELLOW + "\nNo accounts found" + InputUtility.ANSI_RESET );
			return;
		}

		System.out.print( "\tEnter Amount: " );
		final Double amount = InputUtility.getConditionalInput(
			sc,
			x -> ( x != null && x > 0.0 ),
			Double.class,
			false,
			"Invalid amount; must be greater than 0"
		);

		// Making withdrawal
		try {

			backend.withdraw( acc, amount );
			System.out.println( InputUtility.ANSI_GREEN + "\nWithdrawal successful" + InputUtility.ANSI_RESET );

		} catch ( Exception e ) {
			System.out.println( InputUtility.ANSI_RED + "\n" + e.getMessage() + InputUtility.ANSI_RESET );
			return;
		}

	}


	// Initiates "Check Account Balance" dialogue
	private static void checkAccountBalance( final Scanner sc ) throws BackendException {

	}


	// Initiates "Transfer Money" dialogue
	private static void makeTransfer( final Scanner sc ) throws BackendException {

	}


	// Initiates "View Transaction History" dialogue
	private static void viewTransactionHistory( final Scanner sc ) throws BackendException {

	}


	public static void main( String[] args ) {

		// Scanner for user input
		final Scanner sc = new Scanner( System.in );

		// Main menu loop
		int choice = -1;

		while ( choice != 9 ) {

			InputUtility.clearScreen();

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
						makeDeposit( sc );
						break;

					case 5:
						makeWithdrawal( sc );
						break;

					case 6:
						checkAccountBalance( sc );
						;
						break;

					case 7:
						makeTransfer( sc );
						;
						break;

					case 8:
						viewTransactionHistory( sc );
						;
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
