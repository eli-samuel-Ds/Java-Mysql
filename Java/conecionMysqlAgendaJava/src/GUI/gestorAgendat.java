package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

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
	private DefaultTableModel model; 

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
		cargarEventos(); 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAgenda = new JFrame();
		frmAgenda.setTitle("Agenda");
		frmAgenda.setResizable(false);
		frmAgenda.setType(Type.UTILITY);
		frmAgenda.setBounds(100, 100, 911, 701);
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
		panelAgregarEditar.setBounds(31, 140, 835, 100);
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
					ps = con.prepareStatement("INSERT INTO eventos (nombre, matricula, "
							+ "descripcionEvento) VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, textFieldNombreEvento.getText());
					ps.setString(2, textFieldMatriculaEvento.getText());
					ps.setString(3, textFieldDescripcion.getText());

					int res = ps.executeUpdate(); 

					if (res > 0) {
						ResultSet generatedKeys = ps.getGeneratedKeys();
						if (generatedKeys.next()) {
							int idGenerado = generatedKeys.getInt(1);

							Object[] fila = new Object[4];
							fila[0] = idGenerado;
							fila[1] = textFieldNombreEvento.getText();
							fila[2] = textFieldMatriculaEvento.getText();
							fila[3] = textFieldDescripcion.getText();

							model.addRow(fila); 
						}

						JOptionPane.showMessageDialog(null, "Evento Guardado");

						textFieldNombreEvento.setText("");
						textFieldMatriculaEvento.setText("");
						textFieldDescripcion.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Error al Guardar evento");
					}

					con.close(); 

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
		scrollPaneTablaEventos.setBounds(41, 281, 814, 369);
		panelPrincipal.add(scrollPaneTablaEventos);

		String[] columnNames = {"ID","Nombre", "Matricula", "Descripcion"}; 
		model = new DefaultTableModel(columnNames, 0); 
		tableEventos = new JTable(model);
		scrollPaneTablaEventos.setViewportView(tableEventos);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(31,245, 835, 29);
		panelPrincipal.add(menuBar);

		JMenu mnNewMenuBorrar = new JMenu("BORRAR");
		menuBar.add(mnNewMenuBorrar);

		JMenuItem mntmNewMenuItemBorrar = new JMenuItem("BORRAR");
		mntmNewMenuItemBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableEventos.getSelectedRow(); 
				if (selectedRow != -1) { 
					Integer idEvento;
					try {
						idEvento = (Integer) model.getValueAt(selectedRow, 0); 
					} catch (ClassCastException ex) {
						idEvento = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
					}
					int confirm = JOptionPane.showConfirmDialog(null, 
							"¿Estás seguro de que deseas eliminar este evento?", 
							"Confirmar eliminación", 
							JOptionPane.YES_NO_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						PreparedStatement ps;
						Connection con = ConexionMySQL.getConexion();

						try {
							ps = con.prepareStatement("DELETE FROM eventos WHERE id = ?");
							ps.setInt(1, idEvento);

							int res = ps.executeUpdate();  

							if (res > 0) {
								model.removeRow(selectedRow);

								JOptionPane.showMessageDialog(null, "Evento eliminado correctamente");
							} else {
								JOptionPane.showMessageDialog(null, "Error al eliminar el evento");
							}

							con.close();

						} catch (SQLException ex) {
							System.err.println(ex);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Selecciona una fila para borrar.");
				}
			}
		});
		mnNewMenuBorrar.add(mntmNewMenuItemBorrar);


		JMenu mnNewMenuBuscar = new JMenu("BUSCAR");
		menuBar.add(mnNewMenuBuscar);

		JMenuItem mntmNewMenuItemBuscar = new JMenuItem("BUSCACION");
		mntmNewMenuItemBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String textoBusqueda = JOptionPane.showInputDialog(
						null,
						"Ingresa el nombre del evento o matrícula para buscar:",
						"Buscar Evento",
						JOptionPane.QUESTION_MESSAGE
						);

				if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
					textoBusqueda = textoBusqueda.trim();

					PreparedStatement ps;
					ResultSet rs;
					Connection con = ConexionMySQL.getConexion();

					try {
						ps = con.prepareStatement(
								"SELECT id, nombre, matricula, descripcionEvento " +
										"FROM eventos WHERE nombre LIKE ? OR matricula LIKE ?"
								);
						ps.setString(1, "%" + textoBusqueda + "%");
						ps.setString(2, "%" + textoBusqueda + "%");
						rs = ps.executeQuery();

						model.setRowCount(0);

						boolean encontrado = false;
						while (rs.next()) {
							Object[] row = new Object[4];
							row[0] = rs.getInt("id");
							row[1] = rs.getString("nombre");
							row[2] = rs.getString("matricula");
							row[3] = rs.getString("descripcionEvento");

							model.addRow(row);
							encontrado = true;
						}

						if (!encontrado) {
							JOptionPane.showMessageDialog(null, "No se encontraron eventos "
									+ "con ese criterio de búsqueda.");
						}

						con.close();  
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Error al buscar eventos: " + ex.getMessage());
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, ingresa un texto para buscar.");
				}
			}
		});

		mnNewMenuBuscar.add(mntmNewMenuItemBuscar);

		JMenuItem mntmNewMenuItemVer = new JMenuItem("VEACION");
		mntmNewMenuItemVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarEventos();  
				JOptionPane.showMessageDialog(null, "Se han cargado todos los eventos.");
			}
		});
		mnNewMenuBuscar.add(mntmNewMenuItemVer);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem menuItemEditar = new JMenuItem("Editar");
		menuItemEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableEventos.getSelectedRow();
				if (selectedRow != -1) {
					int idEvento = (int) model.getValueAt(selectedRow, 0);
					String nombre = (String) model.getValueAt(selectedRow, 1);
					String matricula = (String) model.getValueAt(selectedRow, 2);
					String descripcion = (String) model.getValueAt(selectedRow, 3);

					JTextField txtNombre = new JTextField(nombre);
					JTextField txtMatricula = new JTextField(matricula);
					JTextField txtDescripcion = new JTextField(descripcion);

					Object[] message = {
							"Nombre:", txtNombre,
							"Matricula:", txtMatricula,
							"Descripcion:", txtDescripcion,
					};

					int option = JOptionPane.showConfirmDialog(
							null, message, "Editar Evento", JOptionPane.OK_CANCEL_OPTION);

					if (option == JOptionPane.OK_OPTION) {
						Connection con = ConexionMySQL.getConexion();
						PreparedStatement ps;

						try {
							ps = con.prepareStatement("UPDATE eventos SET nombre = ?, "
									+ "matricula = ?, descripcionEvento = ? WHERE id = ?");
							ps.setString(1, txtNombre.getText());
							ps.setString(2, txtMatricula.getText());
							ps.setString(3, txtDescripcion.getText());
							ps.setInt(4, idEvento);

							int res = ps.executeUpdate();
							if (res > 0) {
								model.setValueAt(txtNombre.getText(), selectedRow, 1);
								model.setValueAt(txtMatricula.getText(), selectedRow, 2);
								model.setValueAt(txtDescripcion.getText(), selectedRow, 3);

								JOptionPane.showMessageDialog(null, "Evento actualizado correctamente");
							} else {
								JOptionPane.showMessageDialog(null, "Error al actualizar el evento");
							}

							con.close();
						} catch (SQLException ex) {
							JOptionPane.showMessageDialog(null, "Error al actualizar el evento: " + ex.getMessage());
							ex.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Selecciona una fila para editar.");
				}
			}
		});

		popupMenu.add(menuItemEditar);

		tableEventos.setComponentPopupMenu(popupMenu);
	}

	private void cargarEventos() {
		PreparedStatement ps;
		ResultSet rs;
		Connection con = ConexionMySQL.getConexion();

		try {
			ps = con.prepareStatement("SELECT id, nombre, matricula, descripcionEvento FROM eventos");
			rs = ps.executeQuery();

			model.setRowCount(0);

			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("nombre");
				row[2] = rs.getString("matricula");
				row[3] = rs.getString("descripcionEvento");

				model.addRow(row);
			}

			con.close(); 

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar los eventos: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
