package com.ITPM.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ITPM.Model.Leaves;
import com.ITPM.Model.Loans;

public class LoansDAO {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/itpm_assignment?serverTimezone=UTC";
	private String jdbcUsername = "root";
	private String jdbcPassword = "";

	private static final String INSERT_USERS_SQL = "INSERT INTO loans" + "  (eid,loneType,startDate,endDate,total) VALUES "
			+ " (?, ?, ?, ?, ?);";

	private static final String SELECT_USER_BY_ID = "select id,eid,loneType,startDate,endDate,total from loans where id =?";
	private static final String SELECT_ALL_USERS = "select * from loans";
	private static final String DELETE_USERS_SQL = "delete from loans where id = ?;";
	private static final String UPDATE_USERS_SQL = "update loans set eid = ?, loneType = ?, startDate = ?, endDate= ?, total =? where id = ?;";

	public LoansDAO() {
	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public void insertUser(Loans loans) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setInt(1, loans.getEmployeeID());
			preparedStatement.setString(2, loans.getLoanType());
			preparedStatement.setString(3, loans.getStartDate());
			preparedStatement.setString(4, loans.getEndDate());
			preparedStatement.setInt(5, loans.getTotal());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Loans selectUser(int id) {
		Loans loans = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
		
			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int eid = rs.getInt("eid");
				String loneType = rs.getString("loneType");
				String startDate = rs.getString("startDate");
				String endDate = rs.getString("endDate");
				int total = rs.getInt("total");
				loans = new Loans(id, eid, loneType, startDate, endDate, total);
				
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return loans;
	}

	public List<Loans> selectAllUsers() {

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<Loans> loans = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

				// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				int eid = rs.getInt("eid");
				String loneType = rs.getString("loneType");
				String startDate = rs.getString("startDate");
				String endDate = rs.getString("endDate");
				int total = rs.getInt("total");
				loans.add(new Loans(id, eid, loneType, startDate, endDate, total));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return loans;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateUser(Loans loans) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			System.out.println("updated USer:"+statement);
			statement.setInt(1, loans.getEmployeeID());
			statement.setString(2, loans.getLoanType());
			statement.setString(3, loans.getStartDate());
			statement.setString(4, loans.getEndDate());
			statement.setInt(5, loans.getTotal());
			statement.setInt(6, loans.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}


}
