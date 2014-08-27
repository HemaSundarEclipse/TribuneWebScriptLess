package dataReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Common.Property;

public class DBReader {
	static Connection  connection = null;
	
    private String  getConnection(String q) throws ClassNotFoundException, SQLException 
    { 
    	String updatedQuery = "";
    	
    	if (Property.dbConnString.contains("db2")) {
    		Class.forName ( Property.dbDB2Driver  );
    		updatedQuery = q.replace("false", "0");
    		updatedQuery = updatedQuery.replace("true", "1");
    		
    	   		
    	} else if (Property.dbConnString.contains("postgres")) {
    		Class.forName ( Property.dbPostgresDriver  ); 
    		updatedQuery = q;
    	} else if (Property.dbConnString.contains("oracle")) {
    		updatedQuery = q;
    	}
       
   	 connection = 
               DriverManager.getConnection(Property.dbConnString + "/" + Property.dbName,Property.dbUsername,Property.dbPassword); 
     
   	 return updatedQuery;
    }
    

    
    public String getQueryResult(String query) throws SQLException, ClassNotFoundException{
    	String updatedQuery = getConnection(query);
    	
    	
     Statement stmt = connection.createStatement(); 
        ResultSet rs = stmt.executeQuery( updatedQuery );
        
        String result="";
        while (rs.next())
        {
           result = rs.getString(1);
        } 
        rs.close();
        stmt.close();
         
       return result;
    } 
    
/*********************************************************************************************
Step action name: 		updateQuery 
Description:			Execute Update and Delete SQL queries
Input variables:		query
Content of variable:	SQL Query
Output:					null
*********************************************************************************************/	       
   
    public String updateQuery(String query) throws SQLException, ClassNotFoundException{
    	String updatedQuery = getConnection(query);
    	
    	
    	Statement stmt = connection.createStatement(); 
        stmt.executeUpdate(updatedQuery);

        stmt.close();
         
       return null;
    } 
   

}
