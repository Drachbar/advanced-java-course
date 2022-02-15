package com.drachbar.db.mysql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDaoImplTest {

	private Connection conn;
	private List<User> users;

	private static final int NUM_TEST_USERS = 4;

	private List<User> loadUsers() throws IOException {

		// @formatter:off
		
		//map(line -> line.split("\\\\r?\\\\n"))
		//map(line -> line.split("[^A-Za-z]"))
		
		return Files
				.lines(Paths.get("../names.txt"))
				.map(line -> line.split("\\\\r?\\\\n"))
				.map(Arrays::asList)
				.flatMap(list -> list.stream())
				.filter(word -> word.length() > 3 && word.length() < 20)
				.map(word -> new User(word)).limit(NUM_TEST_USERS)
				.collect(Collectors.toList());

		// @formatter:on
	}

	@Before
	public void setUp() throws SQLException, IOException {

		users = loadUsers();

		// System.out.println(users);
		// System.out.println(users.size());

		var props = Profile.getProperties("db");

		var db = Database.instance();

		db.connect(props);

		conn = db.getConnection();
		conn.setAutoCommit(false);
	}

	@After
	public void tearDown() throws SQLException {
		Database.instance().close();
	}

	private int getMaxId() throws SQLException {

		var stmt = conn.createStatement();

		var rs = stmt.executeQuery("select max(id) as id from user");

		rs.next();

		var id = rs.getInt("id");

		stmt.close();
		return id;
	}

	private List<User> getUsersInRange(int minId, int maxId) throws SQLException {

		List<User> retrieved = new ArrayList<>();

		var stmt = conn.prepareStatement("select id, name from user where id >= ? and id <= ?");

		stmt.setInt(1, minId);
		stmt.setInt(2, maxId);

		var rs = stmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");

			var user = new User(id, name);

			retrieved.add(user);
		}

		stmt.close();

		return retrieved;
	}

	@Test
	public void testSaveMultiple() throws SQLException {
		UserDao userDao = new UserDaoImpl();

		for (var u : users) {
			userDao.save(u);
		}

		var maxId = getMaxId();

		for (int i = 0; i < users.size(); i++) {
			int id = (maxId - users.size()) + i + 1;

			users.get(i).setId(id);
		}

		var retrievedUsers = getUsersInRange((maxId - users.size()) + 1, maxId);

		assertEquals("Size of retrieved users not equal to number of test users", retrievedUsers.size(),
				NUM_TEST_USERS);

		assertEquals("retrieved users don't match saved users", users, retrievedUsers);
	}

	@Test
	public void testDelete() throws SQLException {
		UserDao userDao = new UserDaoImpl();

		for (var u : users) {
			userDao.save(u);
		}

		var maxId = getMaxId();

		for (int i = 0; i < users.size(); i++) {
			int id = (maxId - users.size()) + i + 1;

			users.get(i).setId(id);
		}

		var deleteUserIndex = NUM_TEST_USERS / 2;
		var deleteUser = users.get(deleteUserIndex);

		users.remove(deleteUser);
		System.out.println(deleteUser);
		System.out.println(users);

		userDao.delete(deleteUser);
		var retrievedUsers = getUsersInRange((maxId - NUM_TEST_USERS) + 1, maxId);

		System.out.println(retrievedUsers);

		assertEquals("Size of retrieved users not equal to number of test users", retrievedUsers.size(), users.size());

		assertEquals("retrieved users don't match saved users", users, retrievedUsers);
	}

	@Test
	public void testGetALl() throws SQLException {
		UserDao userDao = new UserDaoImpl();

		for (var u : users) {
			userDao.save(u);
		}

		var maxId = getMaxId();

		for (int i = 0; i < users.size(); i++) {
			int id = (maxId - users.size()) + i + 1;

			users.get(i).setId(id);
		}

		var dbUsers = userDao.getAll();
		dbUsers = dbUsers.subList(dbUsers.size() - users.size(), dbUsers.size());

		assertEquals("Size of retrieved users not equal to number of test users", dbUsers.size(), NUM_TEST_USERS);

		assertEquals("retrieved users don't match saved users", users, dbUsers);
	}

	@Test
	public void testFindAndUpdate() throws SQLException {
		var user = users.get(0);

		UserDao userDao = new UserDaoImpl();

		userDao.save(user);

		var maxId = getMaxId();

		user.setId(maxId);

		var retrievedUserOpt = userDao.findById(maxId);

		assertTrue("no user retrieved", retrievedUserOpt.isPresent());

		var retrievedUser = retrievedUserOpt.get();

		assertEquals("retrieved user doesn't match saved user", user, retrievedUser);

		// System.out.println(retrievedUser);

		user.setName("Mattias Andersson");

		userDao.update(user);

		retrievedUserOpt = userDao.findById(maxId);

		assertTrue("no updated user retrieved", retrievedUserOpt.isPresent());

		retrievedUser = retrievedUserOpt.get();

		assertEquals("retrieved user doesn't match updated user", user, retrievedUser);

		// System.out.println(retrievedUser);
	}

	@Test
	public void testSave() throws SQLException {
		User user = new User("Jupiter");

		UserDao userDao = new UserDaoImpl();

		userDao.save(user);

		var stmt = conn.createStatement();

		var rs = stmt.executeQuery("select id, name from user order by id desc");

		var result = rs.next();

		assertTrue("Cannot retrieve inserted user", result);

		var name = rs.getString("name");

		assertEquals("User name doesn't match retrieved", user.getName(), name);

		stmt.close();

	}
}
