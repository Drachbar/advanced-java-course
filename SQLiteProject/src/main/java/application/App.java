package application;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;



public class App {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Scanner scan = new Scanner(System.in);
		int loopExit = 0;
		String inputName;
		int loopCount = 0;
		int age;
		
		Class.forName("org.sqlite.JDBC");
		
		String dbUrl = "jdbc:sqlite:people.db";
		
		var conn = DriverManager.getConnection(dbUrl);
		
		var stmt = conn.createStatement();
		
		var sql = "create table if not exists user (id integer primary key, name text not null, age integer not null)";
		stmt.execute(sql);
		
		/*sql = "insert into user (id, name) values (0, 'Bob')";
		stmt.execute(sql);

		sql = "insert into user (id, name) values (1, 'Mary')";
		stmt.execute(sql);*/
		
		
		while (loopExit == 0)
		{
			System.out.println("Enter name");
			inputName = scan.next();
			System.out.println("Enter age");
			age = Integer.parseInt(scan.next());
			sql = "insert into user (id, name, age) values (" + loopCount + ", '" + inputName + "', " + age + ")";
			stmt.execute(sql);
			loopCount++;
			System.out.println("Continue? 1=no 0=yes");
			loopExit = Integer.parseInt(scan.next());
		}
		
		scan.close();
		
		sql = "select id, name, age from user";
		var rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			age = rs.getInt("age");
			System.out.println(id + ": " + name + ", " + age);
		}
		
		sql = "drop table user";
		stmt.execute(sql);
		
		stmt.close();
		
		conn.close();
	}

}
