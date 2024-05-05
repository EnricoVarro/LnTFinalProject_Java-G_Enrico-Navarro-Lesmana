package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Menu;

public class Database {

	private final String URL = "jdbc:mysql://localhost:3306/projectenrico";
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	
	private static Database instance;
	private static Connection con;

	private Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance; 
	}
	
	public PreparedStatement preparestatement(String query) throws SQLException {
		return con.prepareStatement(query);
	}
	
	public static ObservableList<Menu> getall(){
		String query = "SELECT * FROM puddingenrico";
		ObservableList<Menu> menulist = FXCollections.observableArrayList();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				menulist.add(new Menu(rs.getString("code"), rs.getString("name"), rs.getInt("price"), rs.getInt("stock")));
 			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menulist;
	}
	
	public static void add(Menu menu) {
		String query = "INSERT INTO puddingenrico VALUES (?, ? , ? ,?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, menu.getCode());
			ps.setString(2, menu.getName());
			ps.setInt(3, menu.getPrice());
			ps.setInt(4, menu.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void update(Menu menu) {
		String query = "UPDATE puddingenrico SET name = ?, price = ?, stock = ? WHERE code = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, menu.getName());
			ps.setInt(2, menu.getPrice());
			ps.setInt(3, menu.getStock());
			ps.setString(4, menu.getCode());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void delete(Menu menu) {
		String query = "DELETE FROM puddingenrico WHERE code = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, menu.getCode());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
