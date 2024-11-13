package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
	public static Connection getConexion() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/eventos";  
		String user = "root"; 
		String password = "elis.amuel";  

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Error en la conexión: " + e);
		}
		return con;
	}

	public static void main(String[] args) {
		Connection conexion = getConexion();
		if (conexion != null) {
			System.out.println("Conexión exitosa a la base de datos.");
			try {
				conexion.close();
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexión.");
				e.printStackTrace();
			}
		} else {
			System.out.println("No se pudo establecer la conexión a la base de datos.");
		}
	}
}
