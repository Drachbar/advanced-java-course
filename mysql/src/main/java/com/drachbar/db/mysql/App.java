package com.drachbar.db.mysql;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		var db = Database.instance();

		try {
			db.connect();
		} catch (SQLException e) {
			System.out.println("Cannot connect to database");
			e.printStackTrace();
		}

		System.out.println("Connected");

		UserDao userDao = new UserDaoImpl();

		// userDao.save(new User("Mars"));
		// userDao.save(new User("Mercury"));
		// userDao.save(new User("Neptune"));

		var users = userDao.getAll();

		users.forEach(System.out::println);

		var userOpt = userDao.findById(4);

		if (userOpt.isPresent()) {
			
			User user = userOpt.get();
			
			System.out.println("Retrieved: " + user);
			user.setName("Snoopy");
			
			userDao.update(user);
		} else {
			System.out.println("No users retrieved");
		}

		userDao.delete(new User(5, null));
		users = userDao.getAll();
		users.forEach(System.out::println);

		
		
		try {
			db.close();
		} catch (SQLException e) {
			System.out.println("Cannot close database connection");
		}
	}
}
