package com.banking.utility;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class containing utility methods for gathering user input.
 */
public final class InputUtility {

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


	/**
	 * Loops user prompt until a non-empty string is provided.
	 * 
	 * @param  sc         Scanner instance to use
	 * @param  prompt     Prompt output to console
	 * @param  usePrintLn If true, there will be a new line after the prompt output
	 * @return            Non-empty String
	 */
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
	 * Loops until the user gives input which satisfies the given {@link Predicate}.
	 * 
	 * @param  sc                       Scanner to be used
	 * @param  pred                     Predicate that input must fulfill
	 * @param  newLine                  If true, a newline is output before
	 * @return                          Input that satisfies the predicate
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> T getConditionalInput(
		final Scanner sc,
		final Predicate<T> pred,
		final Class<T> clazz,
		boolean newLine,
		final String errMessage
	) throws IllegalArgumentException {

		T input = null;

		while ( !pred.test( input ) ) {

			final String line = sc.nextLine();

			try {
				if ( clazz == String.class ) {
					input = (T) line;

				} else if ( clazz == Integer.class ) {
					input = (T) Integer.valueOf( line );

				} else if ( clazz == Double.class ) {
					input = (T) Double.valueOf( line );

				} else {
					throw new IllegalArgumentException( "Unsupported conditional input type" );
				}

			} catch ( Exception e ) {
				System.out.println( ANSI_RED + errMessage + ANSI_RESET );
				continue;
			}

			if ( !pred.test( input ) ) {
				if ( newLine ) {
					System.out.print( '\n' );
				}
				System.out.println( ANSI_RED + errMessage + ANSI_RESET );
			}
		}

		return input;

	}


	/**
	 * Alias for
	 * {@link #getConditionalInput(Scanner, Predicate, Class, boolean, String)} call
	 * to get a non-empty String from the user.
	 */
	public static String getNonEmptyString( final Scanner sc, boolean newLine ) {
		return getConditionalInput(
			sc,
			s -> ( s != null && s.length() > 0 ),
			String.class,
			newLine,
			"\nInvalid string; cannot be empty"
		);
	}


	/**
	 * Dialogue to select an item from a list. Prints input as a numbered list,
	 * selecting based on user input.
	 * 
	 * @param  lst List to select from
	 * @return     User-selected item
	 */
	public static <T> T selectFromList( final Scanner sc, final List<? extends T> lst ) {
		return selectFromList( sc, lst, T::toString );
	}


	/**
	 * Dialogue to select an item from a list. Prints input as a numbered list,
	 * selecting based on user input.
	 * 
	 * @param  sc                   Scanner to use
	 * @param  lst                  List of items to select from
	 * @param  mapper               Maps List items to a display string
	 * @return                      Selected item
	 * @throws NullPointerException If any argument is null
	 */
	public static <T> T selectFromList(
		final Scanner sc,
		final List<? extends T> lst,
		Function<T, String> mapper
	) throws NullPointerException {

		// Null Scanner is checked here, unlike other arguments
		// because it isn't used until after the main loop through the collection.
		// It would be inefficient to wait until after a potentially large collection is iterated
		// through to validate.
		if ( sc == null ) {
			throw new NullPointerException( "Invalid Scanner; must not be null" );
		}

		// Catching empty list
		if ( lst.isEmpty() ) {
			return null;
		}

		// Displaying collection items
		int count = 0;

		for ( final T item : lst ) {
			count++;
			System.out.printf( "\t%-6s%5.50s\n", (count + ")"), mapper.apply( item ) );
		}

		// Required for the compiler not to get upset
		final int numSelections = count;

		// Prompting user selection
		System.out.print( "\nEnter selection [1-" + numSelections + "]: " );

		final Integer choice = getConditionalInput(
			sc,
			x -> ( x != null && x >= 1 && x <= numSelections ),
			Integer.class,
			false,
			"\nInvalid choice"
		);

		return lst.get( choice - 1 );

	}

}
