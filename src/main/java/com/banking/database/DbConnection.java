package com.banking.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {
	
	// JDBC property keys
	private static final String JDBC_DRIVER_CLASSNAME_KEY = "jdbc.driverClassName";
	private static final String JDBC_URL_KEY = "jdbc.url";
	private static final String JDBC_USERNAME = "jdbc.username";
	private static final String JDBC_PASSWD = "jdbc.password";
	
	// Connection properties
	private static Properties dbProperties = null;
	

	/**
	 * @return                        The database connection
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Connection get() throws SQLException, IOException, ClassNotFoundException {
		
		// Loading properties from configuration file
		if ( dbProperties == null ) {
			dbProperties = new Properties();
			dbProperties.load( ClassLoader.getSystemClassLoader().getResourceAsStream( "db.properties" ) );
			
			// Sets driver
			// NOTE not strictly necessary because the MySQL driver
			// is automatically found and registered
			Class.forName( dbProperties.getProperty( JDBC_DRIVER_CLASSNAME_KEY ) );
		}
		
		return DriverManager.getConnection( 
			dbProperties.getProperty( JDBC_URL_KEY ), 
			dbProperties.getProperty( JDBC_USERNAME ), 
			dbProperties.getProperty( JDBC_PASSWD ) 
		);
	}

}
