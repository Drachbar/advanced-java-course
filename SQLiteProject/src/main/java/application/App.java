package application;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;



public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		Scanner scan = new Scanner(System.in);
		
		int loopExit = 1;
		String inputName;
		int loopCount = 0;
		
		/*
		int[] ids = {0, 1, 2};
		String[] names = {"Sue", "Bob", "Charlie"};
		*/
		
		Class.forName("org.sqlite.JDBC");
		
		String dbUrl = "jdbc:sqlite:people.db";
		
		var conn = DriverManager.getConnection(dbUrl);
		
		var stmt = conn.createStatement();
		conn.setAutoCommit(false);
		
		var sql = "create table if not exists user (id integer primary key, name text not null)";
		stmt.execute(sql);
		
		sql = "insert into user (id, name) values (?, ?)";
		var insertStmt = conn.prepareStatement(sql);
		
		/*
		for (int i = 0; i < ids.length; i++) {
			insertStmt.setInt(1, ids[i]);
			insertStmt.setString(2, names[i]);
			
			//insertStmt.executeUpdate();
		}*/
		
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
		scan.close();
		
		
		conn.commit();
		insertStmt.close();
		
		sql = "select id, name from user";
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			System.out.println(id + ": " + name);
		}
		
		sql = "drop table user";
		stmt.execute(sql);
		
		stmt.close();
		
		conn.close();
	}

}
