package fr.aphp.tumorotek.action.sip;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Oracle8Driver implements Driver {
	
	private final Driver driver;
	
	public Oracle8Driver(Driver d) {
		if (d == null) {
            throw new IllegalArgumentException("Driver must not be null.");
        }
        this.driver = d;
	}
	
	public Connection connect(String u, Properties info) throws SQLException {
		return this.driver.connect(u.replace("orahack8", "jdbc:oracle:thin:"), info);
	}
	
	public boolean acceptsURL(String u) throws SQLException {
		return u != null && u.contains("orahack8");
		// return this.driver.acceptsURL(u);
	}
	
	public DriverPropertyInfo[] getPropertyInfo(String u, Properties info) throws SQLException {
		return this.driver.getPropertyInfo(u, info);
	}
	
	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}
	
	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}
	
	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return driver.getParentLogger();
	}
}
