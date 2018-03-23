package fr.aphp.tumorotek.action.sip;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class Oracle8Driver implements Driver
{

   private final Driver driver;

   public Oracle8Driver(final Driver d){
      if(d == null){
         throw new IllegalArgumentException("Driver must not be null.");
      }
      this.driver = d;
   }

   @Override
   public Connection connect(final String u, final Properties info) throws SQLException{
      return this.driver.connect(u.replace("orahack8", "jdbc:oracle:thin:"), info);
   }

   @Override
   public boolean acceptsURL(final String u) throws SQLException{
      return u != null && u.contains("orahack8");
      // return this.driver.acceptsURL(u);
   }

   @Override
   public DriverPropertyInfo[] getPropertyInfo(final String u, final Properties info) throws SQLException{
      return this.driver.getPropertyInfo(u, info);
   }

   @Override
   public int getMajorVersion(){
      return this.driver.getMajorVersion();
   }

   @Override
   public int getMinorVersion(){
      return this.driver.getMinorVersion();
   }

   @Override
   public boolean jdbcCompliant(){
      return this.driver.jdbcCompliant();
   }

   @Override
   public Logger getParentLogger() throws SQLFeatureNotSupportedException{
      return driver.getParentLogger();
   }
}
