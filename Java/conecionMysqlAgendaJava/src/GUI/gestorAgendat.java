package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Clases.ConexionMySQL;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class gestorAgendat {

	private JFrame frmAgenda;
	private JTextField textFieldNombreEvento;
	private JTextField textFieldMatriculaEvento;
	private JTextField textFieldDescripcion;
	private JTable tableEventos;
	private DefaultTableModel model;  // Modelo de la tabla

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gestorAgendat window = new gestorAgendat();
					window.frmAgenda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gestorAgendat() {
		initialize();
		cargarEventos();  // Llamamos al método que carga los eventos al iniciar la aplicación
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAgenda = new JFrame();
		frmAgenda.setTitle("Agenda");
		frmAgenda.setResizable(false);
		frmAgenda.setType(Type.UTILITY);
		frmAgenda.setBounds(100, 100, 911, 700);
		frmAgenda.setLocationRelativeTo(null);
		frmAgenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setForeground(new Color(0, 0, 0));
		panelPrincipal.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		panelPrincipal.setBackground(new Color(112, 128, 144));
		frmAgenda.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(null);

		JLabel lblNewLabelTituloAgenda = new JLabel(" Agenda Eventos ");
		lblNewLabelTituloAgenda.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		lblNewLabelTituloAgenda.setForeground(new Color(0, 0, 0));
		lblNewLabelTituloAgenda.setFont(new Font("Dubai", Font.BOLD, 50));
		lblNewLabelTituloAgenda.setBounds(260, 32, 376, 69);
		panelPrincipal.add(lblNewLabelTituloAgenda);

		JPanel panelAgregarEditar = new JPanel();
		panelAgregarEditar.setBackground(new Color(47, 79, 79));
		panelAgregarEditar.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panelAgregarEditar.setBounds(31, 140, 835, 102);
		panelPrincipal.add(panelAgregarEditar);
		panelAgregarEditar.setLayout(null);

		JLabel lblNewLabelNombreEvento = new JLabel("Nombre: ");
		lblNewLabelNombreEvento.setForeground(new Color(0, 0, 0));
		lblNewLabelNombreEvento.setFont(new Font("Dubai", Font.BOLD, 20));
		lblNewLabelNombreEvento.setBounds(20, 17, 96, 29);
		panelAgregarEditar.add(lblNewLabelNombreEvento);

		textFieldNombreEvento = new JTextField();
		textFieldNombreEvento.setBounds(109, 17, 218, 29);
		panelAgregarEditar.add(textFieldNombreEvento);
		textFieldNombreEvento.setColumns(10);

		JLabel lblNewLabelMatricualEvento = new JLabel("Matricula: ");
		lblNewLabelMatricualEvento.setForeground(Color.BLACK);
		lblNewLabelMatricualEvento.setFont(new Font("Dubai", Font.BOLD, 20));
		lblNewLabelMatricualEvento.setBounds(20, 56, 96, 29);
		panelAgregarEditar.add(lblNewLabelMatricualEvento);

		textFieldMatriculaEvento = new JTextField();
		textFieldMatriculaEvento.setColumns(10);
		textFieldMatriculaEvento.setBounds(119, 56, 208, 29);
		panelAgregarEditar.add(textFieldMatriculaEvento);

		JLabel lblNewLabelDescripcionEvento = new JLabel("Descripcion: ");
		lblNewLabelDescripcionEvento.setForeground(Color.BLACK);
		lblNewLabelDescripcionEvento.setFont(new Font("Dubai", Font.BOLD, 20));
		lblNewLabelDescripcionEvento.setBounds(343, 17, 117, 29);
		panelAgregarEditar.add(lblNewLabelDescripcionEvento);

		textFieldDescripcion = new JTextField();
		textFieldDescripcion.setColumns(10);
		textFieldDescripcion.setBounds(470, 17, 343, 29);
		panelAgregarEditar.add(textFieldDescripcion);

		JButton btnNewButtonAgregarEvento = new JButton("AGREGAR EVENTO");
		btnNewButtonAgregarEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreparedStatement ps;
				Connection con = ConexionMySQL.getConexion(); 
				try {
					// Consulta SQL para insertar el evento en la base de datos
					ps = con.prepareStatement("INSERT INTO eventos (nombre, matricula, descripcionEvento) VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, textFieldNombreEvento.getText());
					ps.setString(2, textFieldMatriculaEvento.getText());
					ps.setString(3, textFieldDescripcion.getText());

					int res = ps.executeUpdate(); // Ejecuta la consulta

					if (res > 0) {
						// Obtener el ID generado automáticamente
						ResultSet generatedKeys = ps.getGeneratedKeys();
						if (generatedKeys.next()) {
							int idGenerado = generatedKeys.getInt(1); // El ID generado

							// Agregar el evento a la tabla
							Object[] fila = new Object[4]; // Tamaño 4, ya que tienes 4 columnas en la tabla
							fila[0] = idGenerado; // Agregar el ID a la fila
							fila[1] = textFieldNombreEvento.getText();
							fila[2] = textFieldMatriculaEvento.getText();
							fila[3] = textFieldDescripcion.getText();

							model.addRow(fila); // Añadir la fila al modelo de la tabla
						}

						JOptionPane.showMessageDialog(null, "Evento Guardado");

						// Limpiar los campos de texto
						textFieldNombreEvento.setText("");
						textFieldMatriculaEvento.setText("");
						textFieldDescripcion.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Error al Guardar evento");
					}

					con.close(); // Cerrar conexión

				} catch (HeadlessException | SQLException e1) {
					System.err.println(e1);
				}
			}
		});
		btnNewButtonAgregarEvento.setFont(new Font("Dubai", Font.BOLD, 20));
		btnNewButtonAgregarEvento.setBounds(341, 56, 472, 29);
		panelAgregarEditar.add(btnNewButtonAgregarEvento);

		JLabel lblNewLabelTituloAgregarEditar = new JLabel("AGREGAR E EDITAR");
		lblNewLabelTituloAgregarEditar.setForeground(new Color(0, 0, 0));
		lblNewLabelTituloAgregarEditar.setFont(new Font("Arial Black", Font.PLAIN, 20));
		lblNewLabelTituloAgregarEditar.setBounds(31, 113, 234, 29);
		panelPrincipal.add(lblNewLabelTituloAgregarEditar);

		JScrollPane scrollPaneTablaEventos = new JScrollPane();
		scrollPaneTablaEventos.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		scrollPaneTablaEventos.setBounds(46, 291, 804, 345);
		panelPrincipal.add(scrollPaneTablaEventos);

		String[] columnNames = {"ID","Nombre", "Matricula", "Descripcion"}; // Nombres de las columnas
		model = new DefaultTableModel(columnNames, 0); 
		tableEventos = new JTable(model);
		scrollPaneTablaEventos.setViewportView(tableEventos);

		JPanel panelDelMenu = new JPanel();
		panelDelMenu.setBounds(31, 252, 835, 29);
		panelPrincipal.add(panelDelMenu);
		panelDelMenu.setLayout(new GridLayout(0, 1, 0, 0));

		JMenuBar menuBar = new JMenuBar();
		panelDelMenu.add(menuBar);

		JMenu mnNewMenuEditar = new JMenu("EDITAR");
		menuBar.add(mnNewMenuEditar);

		JMenuItem mntmNewMenuItemEditar = new JMenuItem("EDITACION");
		mnNewMenuEditar.add(mntmNewMenuItemEditar);

		JMenu mnNewMenuBorrar = new JMenu("BORRAR");
		menuBar.add(mnNewMenuBorrar);

		JMenuItem mntmNewMenuItemBorrar = new JMenuItem("BORRACION");
		mnNewMenuBorrar.add(mntmNewMenuItemBorrar);
		
		JMenu mnNewMenuBuscar = new JMenu("BUSCAR");
		menuBar.add(mnNewMenuBuscar);
		
		JMenuItem mntmNewMenuItemBuscar = new JMenuItem("BUSCACION");
		mnNewMenuBuscar.add(mntmNewMenuItemBuscar);
	}

	/**
	 * Método para cargar los eventos ya existentes desde la base de datos
	 * y mostrarlos en la tabla.
	 */
	private void cargarEventos() {
		PreparedStatement ps;
		ResultSet rs;
		Connection con = ConexionMySQL.getConexion();

		try {
			ps = con.prepareStatement("SELECT id, nombre, matricula, descripcionEvento FROM eventos");
			rs = ps.executeQuery();

			while (rs.next()) {
				// Añadir cada evento existente a la tabla
				Object[] fila = new Object[4];
				fila[0] = rs.getString("id");
				fila[1] = rs.getString("nombre");
				fila[2] = rs.getString("matricula");
				fila[3] = rs.getString("descripcionEvento");

				model.addRow(fila);
			}

			con.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
}
