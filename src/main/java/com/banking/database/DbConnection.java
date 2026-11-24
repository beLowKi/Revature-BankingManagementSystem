package com.banking.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {

	// private final static String URL =
	// "jdbc:mysql://localhost:3306/bms?createIfNotExist=true";
	private static Connection conn = null;


	/**
	 * @return                        The database connection
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
		
		// Creating connection
		if ( conn == null ) {

			// Loading properties from configuration file
			final Properties ps = new Properties();
			ps.load( ClassLoader.getSystemClassLoader().getResourceAsStream( "db.properties" ) );

			final String driver = ps.getProperty( "jdbc.driverClassName" );
			final String url = ps.getProperty( "jdbc.url" );
			final String username = ps.getProperty( "jdbc.username" );
			final String passwd = ps.getProperty( "jdbc.password" );

			// DEBUG
			// System.out.println( "Driver: " + driver );
			// System.out.println( "URL: " + url);
			// System.out.println( "Username: " + username );
			// System.out.println( "Password: " + passwd );

			// Sets driver
			// NOTE not strictly necessary because the MySQL driver
			// is automatically found and registered
			Class.forName( driver );

			conn = DriverManager.getConnection( url, username, passwd );
		}

		return conn;
	}

}
