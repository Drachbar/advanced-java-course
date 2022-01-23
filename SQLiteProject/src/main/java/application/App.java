package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;



public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		Scanner scan = new Scanner(System.in);
		/*
		int loopExit = 1;
		String inputName;
		int loopCount = 0;
		
		*/
		
		int[] ids = {0, 1, 2};
		String[] names = {"Sue", "Bob", "Charlie"};
		
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		String dbUrl = "jdbc:mysql://localhost:3306/people";
		
		//var conn = DriverManager.getConnection(dbUrl, "advancedjava", "hello");
		
		System.out.println("Enter username");
		String usrnm = scan.nextLine();
		System.out.println("Enter password");
		String passwd = scan.nextLine();

		Connection conn; 
		try {
			
			conn = DriverManager.getConnection(dbUrl, usrnm, passwd);
		} catch (Exception e) {
			System.out.println("Wrong password");
			System.exit(0);
		}
		conn = DriverManager.getConnection(dbUrl, usrnm, passwd);

		
		
		
		var stmt = conn.createStatement();
		conn.setAutoCommit(false);
		
		
		var sql = "insert into user (id, name) values (?, ?)";
		var insertStmt = conn.prepareStatement(sql);
		
		
		for (int i = 0; i < ids.length; i++) {
			insertStmt.setInt(1, ids[i]);
			insertStmt.setString(2, names[i]);
			
			//insertStmt.executeUpdate();
		}
		
		/*
		sql = "select id from user";
		var rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int id = rs.getInt("id");
			loopCount = id+1;
		}
		
		
		
		while (loopExit == 1)
		{
			System.out.println("Enter name");
			inputName = scan.next();
			insertStmt.setInt(1, loopCount);
			insertStmt.setString(2, inputName);
			insertStmt.executeUpdate();
			loopCount++;
			System.out.println("Continue? 0=no 1=yes");
			loopExit = Integer.parseInt(scan.next());
		}
		*/
		scan.close();
		
		
		conn.commit();
		insertStmt.close();
		
		sql = "select id, name from user";
		var rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			System.out.println(id + ": " + name);
		}
		
		
		stmt.close();
		
		conn.close();
	}

}
