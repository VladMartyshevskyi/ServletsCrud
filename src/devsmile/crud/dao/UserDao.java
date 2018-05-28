package devsmile.crud.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import java.sql.PreparedStatement;

import devsmile.crud.models.User;

import java.sql.Connection;

public class UserDao {

	private static final String QUERY_INSERT = "INSERT INTO Users (id, name, lastName, age) VALUES (NULL, ?,?,?)";
	private static final String QUERY_UPDATE = "UPDATE Users SET name = ?, lastName = ?, age = ? WHERE id = ?";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM Users";
	private static final String QUERY_SELECT_BY_ID = "SELECT * FROM Users WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public UserDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}

	public void add(User user) throws SQLException {
		connect();
		java.sql.PreparedStatement statement = jdbcConnection.prepareStatement(QUERY_INSERT);
		statement.setString(1, user.getName());
		statement.setString(2, user.getLastName());
		statement.setInt(3, user.getAge());
		statement.executeUpdate();
		statement.close();
		disconnect();
	}

	public List<User> getAll() throws SQLException {
		connect();
		List<User> users = new ArrayList<>();
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(QUERY_SELECT_ALL);

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String lastName = resultSet.getString("lastName");
			int age = resultSet.getInt("age");

			User user = new User(id, name, lastName, age);
			users.add(user);
		}

		resultSet.close();
		statement.close();
		disconnect();
		return users;
	}
	
	public void delete(int id) throws SQLException {
        connect();
   
        PreparedStatement statement = jdbcConnection.prepareStatement(QUERY_DELETE_BY_ID);
        statement.setInt(1, id);
        statement.executeUpdate();
        
        statement.close();
        disconnect();    
    }
	
	public User getById(int id) throws SQLException {
        User user = null;
        connect();
         
        PreparedStatement statement = jdbcConnection.prepareStatement(QUERY_SELECT_BY_ID);
        statement.setInt(1, id);
         
        ResultSet resultSet = statement.executeQuery();
         
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("lastName");
            int age = resultSet.getInt("age");
             
            user = new User(id, name, lastName, age);
        }
         
        resultSet.close();
        statement.close();
         
        return user;
    }
	
	public void update(User user) throws SQLException {
        
        connect();
         
        PreparedStatement statement = jdbcConnection.prepareStatement(QUERY_UPDATE);
        statement.setString(1, user.getName());
        statement.setString(2, user.getLastName());
        statement.setFloat(3, user.getAge());
        statement.setInt(4, user.getId());
        statement.executeUpdate();
        
        statement.close();
        disconnect();  
    }

}
