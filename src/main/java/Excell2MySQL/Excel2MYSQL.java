/**
 * 
 */
package Excell2MySQL;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Hemsundar
 *
 */
public class Excel2MYSQL {
	@org.testng.annotations.Test
	public void readingDataFromExcel_Writing2MySQL() throws IOException, SQLException, ClassNotFoundException {
		FileInputStream fs = new FileInputStream("test.xls");
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet ws = wb.getSheetAt(0);
		Cell cell = ws.getRow(0).getCell(0);
		String cellData = cell.getStringCellValue();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
		Statement st = (Statement) con.createStatement();
		st.executeUpdate("INSERT INTO world.tribune VALUES (1, 'Penugonda', 'HemaSundar', 'SanthiNagar', 'Bangalore');");
		System.out.println(st.execute("select * from world.city;"));
		ResultSet rs = st.executeQuery("select * from world.tribune;");
		System.out.println(rs);
		while (rs.next()) {
			System.out.println(rs.getString(2));
			
		}
		con.close();
		
	}
	
	
	
	
	
	

}
