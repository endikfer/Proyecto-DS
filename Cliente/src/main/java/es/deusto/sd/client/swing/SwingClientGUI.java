/**
 * This code is based on solutions provided by Claude Sonnet 3.5 and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.client.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import es.deusto.sd.client.data.Reto;
import es.deusto.sd.client.data.RetoAceptado;
import es.deusto.sd.client.data.Sesion;

/**
 * SwingClientGUI class is a Swing-based client that demonstrates the usage of
 * the AuctionsService. It is implemented using a classic Controller pattern.
 */
public class SwingClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	// Controller instance
	private final SwingClientController controller;

	// Default login credentials
	private String defaultEmail = "info@gmail.com";
	private String defaultPassword = "123";

	private JLabel logoutLabel;
	private JList<String> categoryList;
	private JTable jtbleArticles;

	private JButton btnBid;
	private JScrollPane articleScrollPane;
	private JPanel jPanelArticleDetails;
	private JPanel centralPanel;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	public SwingClientGUI(SwingClientController controller) {
		this.controller = controller;

		centralPanel = new JPanel();
		articleScrollPane = new JScrollPane();
		jPanelArticleDetails = new JPanel();

		if (!performLogin()) {
			System.exit(0);
		}

		setTitle("Auctions Client");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(1024, 400);
		setResizable(true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());

		// Logout Label
		logoutLabel = new JLabel("Logout", SwingConstants.RIGHT);
		logoutLabel.setForeground(Color.BLUE);
		logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				performLogout();
			}
		});
		topPanel.add(logoutLabel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// Category List
		categoryList = new JList<>(new String[] { "Sesiones", "Retos", "Retos Aceptados" });

		categoryList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel label = new JLabel(value);
			label.setBackground(list.getBackground());
			label.setOpaque(true);

			if (isSelected) {
				label.setBackground(list.getSelectionBackground());
				label.setForeground(list.getSelectionForeground());
			}

			return label;
		});

		categoryList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				String selectedOption = categoryList.getSelectedValue();
				cambioPanel(selectedOption);
			}
		});

		JScrollPane categoryScrollPane = new JScrollPane(categoryList);
		categoryScrollPane.setPreferredSize(new Dimension(200, getHeight()));
		categoryScrollPane.setBorder(new TitledBorder("Operaciones"));
		add(categoryScrollPane, BorderLayout.WEST);

		cambioPanel("Sesiones");

		setVisible(true);
	}

	private void cambioPanel(String option) {
		switch (option) {
		case "Sesiones" -> panel1();
		case "Retos" -> panel2();
		case "Retos Aceptados" -> panel3();
		}
	}

	private void panel1() {
		centralPanel.removeAll();
		centralPanel = new JPanel(new BorderLayout());
		jtbleArticles = new JTable(new DefaultTableModel(
				new Object[] { "ID", "Titulo", "Deporte", "Distancia", "FechaInicio", "Duracion" }, 0)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		sesiones();

		adjustColumnWidths(jtbleArticles);

		articleScrollPane = new JScrollPane(jtbleArticles);
		articleScrollPane.setPreferredSize(new Dimension(600, getHeight()));
		articleScrollPane.setBorder(new TitledBorder("Articles of the selected Category"));
		centralPanel.add(articleScrollPane, BorderLayout.CENTER);

		// Update East Panel
		jPanelArticleDetails.removeAll();
		jPanelArticleDetails = new JPanel(new GridLayout(7, 2, 10, 10));
		jPanelArticleDetails.setBorder(new TitledBorder("Crear sesiones"));
		jPanelArticleDetails.setPreferredSize(new Dimension(300, getHeight())); // Remaining width

		jPanelArticleDetails.add(new JLabel("Titulo:"));
		JTextArea txtTitle = new JTextArea(2, 20);
		txtTitle.setLineWrap(true);
		txtTitle.setWrapStyleWord(true);
		JScrollPane scrollTitle = new JScrollPane(txtTitle);
		jPanelArticleDetails.add(scrollTitle);

		jPanelArticleDetails.add(new JLabel("Deporte:"));
		JComboBox<String> cbArticleSport = new JComboBox<>(new String[] { "Ciclismo", "Running" });
		jPanelArticleDetails.add(cbArticleSport);

		jPanelArticleDetails.add(new JLabel("Distancia(km):"));
		JSpinner spinDistance = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		jPanelArticleDetails.add(spinDistance);

		jPanelArticleDetails.add(new JLabel("Fecha inicio:"));
		JSpinner spinStartDate = new JSpinner(new SpinnerDateModel());
		spinStartDate.setEditor(new JSpinner.DateEditor(spinStartDate, "yyyy-MM-dd"));
		jPanelArticleDetails.add(spinStartDate);

		jPanelArticleDetails.add(new JLabel("Duracion(min):"));
		JSpinner spinTime = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		jPanelArticleDetails.add(spinTime);

		btnBid = new JButton("Crear Sesion");
		btnBid.setEnabled(true);
		btnBid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Obtener los valores de los componentes
				String titulo = txtTitle.getText().trim(); // Eliminar espacios innecesarios
				String deporte = cbArticleSport.getSelectedItem().toString();
				double distancia = Double.valueOf(spinDistance.getValue().toString());
				Date selectedDate = (Date) spinStartDate.getValue();
				int duracion = Integer.parseInt(spinTime.getValue().toString());
				StringBuilder e1 = new StringBuilder();
				// Validaciones
				if (titulo.isEmpty()) {
					e1.append("El título no puede estar vacío.\n");
				}

				if (distancia <= 0) {
					e1.append("La distancia debe ser mayor que 0.\n");

				}

				if (duracion <= 0) {
					e1.append("La duración debe ser mayor que 0.");

				}
				if (e1.length() > 0) {
					JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// Formatear la fecha seleccionada
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = sdf.format(selectedDate);

					// Llamar a la función para crear la sesión
					crearSesion(titulo, deporte, distancia, formattedDate, duracion);

					// Actualizar la lista de sesiones (suponiendo que la función 'sesiones' existe)
					sesiones();

				}
			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(new JLabel("FechaInicio:"));
		JSpinner fecha1 = new JSpinner(new SpinnerDateModel());
		fecha1.setEditor(new JSpinner.DateEditor(fecha1, "yyyy-MM-dd"));
		buttonPanel.add(fecha1);

		buttonPanel.add(new JLabel("FechaFin:"));
		JSpinner fecha = new JSpinner(new SpinnerDateModel());
		fecha.setEditor(new JSpinner.DateEditor(fecha, "yyyy-MM-dd"));
		buttonPanel.add(fecha);

		JButton btnFilterByDate = new JButton("Filtrar por Fecha Final");
		buttonPanel.add(btnFilterByDate);

		btnFilterByDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date f = (Date) fecha1.getValue();
				Date f1 = (Date) fecha.getValue();

				// Convertir las fechas a String (formato "yyyy-MM-dd")
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String fechaInicio = sdf.format(f);
				String fechaFin = sdf.format(f1);

				// Establecer hora a las 00:00:00 para comparar solo las fechas
				Calendar calInicio = Calendar.getInstance();
				calInicio.setTime(f);
				calInicio.set(Calendar.HOUR_OF_DAY, 0);
				calInicio.set(Calendar.MINUTE, 0);
				calInicio.set(Calendar.SECOND, 0);
				calInicio.set(Calendar.MILLISECOND, 0);
				Date fechaInicioSinHora = calInicio.getTime();

				Calendar calFin = Calendar.getInstance();
				calFin.setTime(f1);
				calFin.set(Calendar.HOUR_OF_DAY, 0);
				calFin.set(Calendar.MINUTE, 0);
				calFin.set(Calendar.SECOND, 0);
				calFin.set(Calendar.MILLISECOND, 0);
				Date fechaFinSinHora = calFin.getTime();
				StringBuilder e2 = new StringBuilder();
				// Verificar que la fecha de inicio sea anterior a la fecha de fin
				if (fechaInicioSinHora != null && fechaFinSinHora != null) {
					if (fechaInicioSinHora.after(fechaFinSinHora)) {
						e2.append("La fecha de inicio debe ser anterior a la fecha de fin.\n");
					}

					// Verificar que la fecha de fin sea posterior a la fecha de inicio
					if (fechaFinSinHora.before(fechaInicioSinHora)) {
						e2.append("La fecha de fin debe ser posterior a la fecha de inicio.\n");
					}

					// Verificar que las fechas de inicio y fin no sean iguales
					if (fechaInicioSinHora.equals(fechaFinSinHora)) {
						e2.append("La fecha de inicio no puede ser igual a la fecha de fin.\n");
					}
				}
				if (e2.length() > 0) {
					JOptionPane.showMessageDialog(null, e2.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				} else {

					consultarSesionFecha(fechaInicio, fechaFin);

				}
			}
		});

		centralPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(centralPanel, BorderLayout.CENTER);

		JPanel jPanelBidButton = new JPanel();
		jPanelBidButton.add(btnBid);
		jPanelArticleDetails.add(jPanelBidButton);

		add(jPanelArticleDetails, BorderLayout.EAST);

		refreshPanels();
	}

	private void sesiones() {

		try {
			List<Sesion> Sesiones = controller.getSesionesRecientes();
			SwingUtilities.invokeLater(() -> {
				DefaultTableModel m = (DefaultTableModel) jtbleArticles.getModel();
				m.setRowCount(0);
				for (Sesion s : Sesiones) {
					m.addRow(new Object[] { s.id(), s.titulo(), s.deporte(), s.distancia(), s.fechaInicio(),
							s.duracion(), });
				}
			});

		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar las sesiones recientes: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	};;

	private void crearSesion(String titulo, String deporte, double distancia, String fechaInicio, int duracion) {
		try {
			controller.crearSesion(titulo, deporte, distancia, fechaInicio, duracion);

			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Reto creado correctamente!");
			});

		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	};

	public static LocalDate parseString(String dateString) throws DateTimeParseException {
		if (dateString == null || dateString.trim().isEmpty()) {
			throw new DateTimeParseException("Date string cannot be null or empty", dateString, 0);
		}
		try {
			return LocalDate.parse(dateString, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			System.err.println("Error parsing date: " + e.getMessage());
			throw e;
		}
	}

	private void consultarSesionFecha(String fechaInicioSeleccionado, String fechaFinalSeleccionada) {

		try {
			// Llama al controlador para obtener la lista de sesiones
			List<Sesion> sesiones = controller.getSesionesPorFecha(fechaInicioSeleccionado, fechaFinalSeleccionada);

			SwingUtilities.invokeLater(() -> {
				// Limpia la tabla antes de llenarla
				DefaultTableModel model = (DefaultTableModel) jtbleArticles.getModel();
				model.setRowCount(0);

				// Llena la tabla con los datos de los retos
				for (Sesion sesion : sesiones) {
					model.addRow(new Object[] { sesion.id(), sesion.titulo(), sesion.deporte(), sesion.distancia(),
							sesion.fechaInicio(), sesion.duracion() });
				}
			});
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar sesiones", JOptionPane.ERROR_MESSAGE);
		}

	};

	private void panel2() {
		centralPanel.removeAll();

		centralPanel = new JPanel(new BorderLayout());

		jtbleArticles = new JTable(new DefaultTableModel(
				new Object[] { "ID", "Nombre", "Deporte", "Fecha inicio", "Fecha fin", "Distancia", "Tiempo" }, 0)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jtbleArticles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		consultarRetos(null, null);

		adjustColumnWidths(jtbleArticles);

		articleScrollPane = new JScrollPane(jtbleArticles);
		articleScrollPane.setPreferredSize(new Dimension(600, getHeight()));
		articleScrollPane.setBorder(new TitledBorder("Retos creados"));
		centralPanel.add(articleScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JSpinner txtFecha = new JSpinner(new SpinnerDateModel());
		txtFecha.setEditor(new JSpinner.DateEditor(txtFecha, "yyyy-MM-dd"));
		buttonPanel.add(new JLabel("Fecha:"));
		buttonPanel.add(txtFecha);

		JButton btnFilterByDate = new JButton("Filtrar");
		buttonPanel.add(btnFilterByDate);

		btnFilterByDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Obtener el valor del JSpinner (una instancia de Date)
				Date selectedDate = (Date) txtFecha.getValue();

				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = sdf.format(selectedDate);

				consultarRetos(null, formattedDate);

			}
		});

		JComboBox<String> cbDeportes = new JComboBox<>(new String[] { "Running", "Ciclismo" });
		buttonPanel.add(new JLabel("Deporte:"));
		buttonPanel.add(cbDeportes);

		JButton btnFilterByDeporte = new JButton("Filtrar");
		// btnFilterByDeporte.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonPanel.add(btnFilterByDeporte);

		btnFilterByDeporte.addActionListener(e -> {
			// Obtener el valor seleccionado en el JComboBox
			String deporte = (String) cbDeportes.getSelectedItem();

			consultarRetos(deporte, null);

		});

		buttonPanel.add(Box.createHorizontalGlue());

		JButton btnAcceptRetos = new JButton("Acept");

		buttonPanel.add(btnAcceptRetos);
		
		btnAcceptRetos.addActionListener(e -> {
		    int selectedRow = jtbleArticles.getSelectedRow();
		    if (selectedRow != -1) {
		    	// Obtener el ID del reto seleccionado
		        Long retoId = (Long) jtbleArticles.getValueAt(selectedRow, 0);
		        try {
		            // Llamar al método aceptarReto en el controller
		            controller.aceptarReto(retoId);
		            JOptionPane.showMessageDialog(btnAcceptRetos, "Reto aceptado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(btnAcceptRetos, "Error al aceptar el reto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    } else {
		    	JOptionPane.showMessageDialog(btnAcceptRetos, "Por favor, selecciona un reto antes de aceptar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
		    }
		});

		centralPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(centralPanel, BorderLayout.CENTER);

		jPanelArticleDetails.removeAll();
		jPanelArticleDetails = new JPanel(new GridLayout(7, 2, 10, 10));
		jPanelArticleDetails.setBorder(new TitledBorder("Crear retos"));
		jPanelArticleDetails.setPreferredSize(new Dimension(300, getHeight())); // Remaining width

		jPanelArticleDetails.add(new JLabel("Nombre:"));
		JTextArea txtArticleName = new JTextArea(2, 20);
		txtArticleName.setLineWrap(true);
		txtArticleName.setWrapStyleWord(true);
		JScrollPane scrollArticleName = new JScrollPane(txtArticleName);
		jPanelArticleDetails.add(scrollArticleName);

		jPanelArticleDetails.add(new JLabel("Deporte:"));
		JComboBox<String> cbArticleSport = new JComboBox<>(new String[] { "Ciclismo", "Running" });
		jPanelArticleDetails.add(cbArticleSport);

		jPanelArticleDetails.add(new JLabel("Fecha inicio:"));
		JSpinner spinStartDate = new JSpinner(new SpinnerDateModel());
		spinStartDate.setEditor(new JSpinner.DateEditor(spinStartDate, "yyyy-MM-dd"));
		jPanelArticleDetails.add(spinStartDate);

		jPanelArticleDetails.add(new JLabel("Fecha fin:"));
		JSpinner spinEndDate = new JSpinner(new SpinnerDateModel());
		spinEndDate.setEditor(new JSpinner.DateEditor(spinEndDate, "yyyy-MM-dd"));
		jPanelArticleDetails.add(spinEndDate);

		jPanelArticleDetails.add(new JLabel("Distancia(km):"));
		JSpinner spinDistance = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		jPanelArticleDetails.add(spinDistance);

		jPanelArticleDetails.add(new JLabel("Tiempo(min):"));
		JSpinner spinTime = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		jPanelArticleDetails.add(spinTime);

		btnBid = new JButton("Crear Reto");
		btnBid.setEnabled(true);
		btnBid.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Obtener los valores de cada componente
		        String nombre = txtArticleName.getText(); // Texto del JTextArea
		        String deporte = (String) cbArticleSport.getSelectedItem(); // Valor seleccionado en el JComboBox
		        Date fechaInicioDate = (Date) spinStartDate.getValue(); // Valor del JSpinner (fecha de inicio)
		        Date fechaFinDate = (Date) spinEndDate.getValue(); // Valor del JSpinner (fecha de fin)
		        int distancia = (int) spinDistance.getValue(); // Valor del JSpinner (distancia)
		        int tiempo = (int) spinTime.getValue(); // Valor del JSpinner (tiempo)
		        
		        String nombre_final = reemplazarEspacios(nombre);

		        // Convertir las fechas a String (formato "yyyy-MM-dd")
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        String fechaInicio = sdf.format(fechaInicioDate);
		        String fechaFin = sdf.format(fechaFinDate);

		        // Establecer hora a las 00:00:00 para comparar solo las fechas
		        Calendar calInicio = Calendar.getInstance();
		        calInicio.setTime(fechaInicioDate);
		        calInicio.set(Calendar.HOUR_OF_DAY, 0);
		        calInicio.set(Calendar.MINUTE, 0);
		        calInicio.set(Calendar.SECOND, 0);
		        calInicio.set(Calendar.MILLISECOND, 0);
		        Date fechaInicioSinHora = calInicio.getTime();

		        Calendar calFin = Calendar.getInstance();
		        calFin.setTime(fechaFinDate);
		        calFin.set(Calendar.HOUR_OF_DAY, 0);
		        calFin.set(Calendar.MINUTE, 0);
		        calFin.set(Calendar.SECOND, 0);
		        calFin.set(Calendar.MILLISECOND, 0);
		        Date fechaFinSinHora = calFin.getTime();

		        // Comprobaciones antes de ejecutar CrearReto
		        StringBuilder errores = new StringBuilder();

		        // Verificar que el campo nombre no esté vacío
		        if (nombre.trim().isEmpty()) {
		            errores.append("El nombre del reto no puede estar vacío.\n");
		        }

		        // Verificar que la fecha de inicio sea anterior a la fecha de fin
		        if (fechaInicioSinHora != null && fechaFinSinHora != null) {
		            if (fechaInicioSinHora.after(fechaFinSinHora)) {
		                errores.append("La fecha de inicio debe ser anterior a la fecha de fin.\n");
		            }

		            // Verificar que la fecha de fin sea posterior a la fecha de inicio
		            if (fechaFinSinHora.before(fechaInicioSinHora)) {
		                errores.append("La fecha de fin debe ser posterior a la fecha de inicio.\n");
		            }

		            // Verificar que las fechas de inicio y fin no sean iguales
		            if (fechaInicioSinHora.equals(fechaFinSinHora)) {
		                errores.append("La fecha de inicio no puede ser igual a la fecha de fin.\n");
		            }
		        }

		        // Verificar que los campos distancia y tiempo no sean ambos cero ni ambos con valor
		        if ((distancia == 0 && tiempo == 0) || (distancia != 0 && tiempo != 0)) {
		            errores.append("Solo uno de los campos de distancia o tiempo puede tener valor distinto de cero.\n");
		        }

		        // Si hay errores, mostrar mensaje y no ejecutar CrearReto ni consultarRetos
		        if (errores.length() > 0) {
		            JOptionPane.showMessageDialog(null, errores.toString(), "Errores", JOptionPane.ERROR_MESSAGE);
		        } else {
		            // Llamar al método CrearReto pasándole los valores obtenidos
		            crearReto(nombre_final, deporte, fechaInicio, fechaFin, distancia, tiempo);
		            consultarRetos(null, null);
		        }
		    }
		});


		JPanel jPanelBidButton = new JPanel();
		jPanelBidButton.add(btnBid);
		jPanelArticleDetails.add(jPanelBidButton);

		add(jPanelArticleDetails, BorderLayout.EAST);

		refreshPanels();
	}


	private void panel3() {
		centralPanel.removeAll();
		jPanelArticleDetails.removeAll();
		jPanelArticleDetails.setBorder(null);

		remove(jPanelArticleDetails);

		centralPanel = new JPanel(new BorderLayout());

		// Crear el modelo de la tabla con las columnas adecuadas
		DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "ID", "Nombre", "Deporte", "Fecha Inicio",
				"Fecha Fin", "Distancia", "Tiempo", "Progreso" }, 0);

		// Llamar al método para obtener los retos aceptados y llenar el modelo de la
		// tabla
		try {
			List<RetoAceptado> retosAceptados = controller.consultarRetosAceptados();
			for (RetoAceptado reto : retosAceptados) {
				tableModel.addRow(new Object[] { reto.id(), reto.nombre(), reto.deporte(), reto.fecha_inicio(),
						reto.fecha_fin(), reto.distancia(), reto.tiempo(), reto.progreso() });
			}
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los retos aceptados: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// Crear JTable con el modelo poblado
		jtbleArticles = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Ajustar las columnas si es necesario
		adjustColumnWidths(jtbleArticles);

		// Crear JScrollPane para envolver la tabla
		articleScrollPane = new JScrollPane(jtbleArticles);
		articleScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
		articleScrollPane.setBorder(new TitledBorder("Retos Aceptados"));
		centralPanel.add(articleScrollPane, BorderLayout.CENTER);

		add(centralPanel, BorderLayout.CENTER);

		refreshPanels();

	}

	private void refreshPanels() {
		centralPanel.revalidate();
		centralPanel.repaint();
		jPanelArticleDetails.revalidate();
		jPanelArticleDetails.repaint();
	}

	private void adjustColumnWidths(JTable table) {
		JTableHeader header = table.getTableHeader();
		TableColumnModel columnModel = table.getColumnModel();
		FontMetrics metrics = header.getFontMetrics(header.getFont());

		for (int column = 0; column < columnModel.getColumnCount(); column++) {
			TableColumn tableColumn = columnModel.getColumn(column);
			String headerValue = (String) tableColumn.getHeaderValue();
			int headerWidth = metrics.stringWidth(headerValue) + 20; // Añadimos un margen
			tableColumn.setPreferredWidth(headerWidth);
		}
	}

	private boolean performLogin() {
		JTextField emailField = new JTextField(20);
		emailField.setText(defaultEmail);
		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setText(defaultPassword);

		// Botones personalizados para Login y Registro
		Object[] options = { "Login", "Registrarse", "Cancelar" };

		// Mensaje del cuadro de diálogo
		Object[] message = { new JLabel("Enter Email:"), emailField, new JLabel("Enter Password:"), passwordField };

		int option = JOptionPane.showOptionDialog(this, message, "Login", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.YES_OPTION) {
			try {
				return controller.logIn(emailField.getText(), new String(passwordField.getPassword()));
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				return performLogin();
			}
		} else if (option == JOptionPane.NO_OPTION) {
			performRegistro();
			return performLogin();
		} else {
			return false;
		}
	}

	private void performRegistro() {
		JTextField nombreField = new JTextField(20);
		JTextField emailField = new JTextField(20);
		JComboBox<String> tipoComboBox = new JComboBox<>(new String[] { "GOOGLE", "META" });
		JTextField fechaNacField = new JTextField(20);
		JTextField pesoField = new JTextField(20);
		JTextField alturaField = new JTextField(20);
		JTextField frecCarMaxField = new JTextField(20);
		JTextField frecCarRepField = new JTextField(20);

		// Mensaje del cuadro de diálogo
		Object[] message = { new JLabel("Enter Name:"), nombreField, new JLabel("Enter Email:"), emailField,
				new JLabel("Select Type:"), tipoComboBox, new JLabel("Enter Birth Date (yyyy-MM-dd):"), fechaNacField,
				new JLabel("Enter Weight (kg):"), pesoField, new JLabel("Enter Height (cm):"), alturaField,
				new JLabel("Enter Max Heart Rate (bpm):"), frecCarMaxField,
				new JLabel("Enter Resting Heart Rate (bpm):"), frecCarRepField };

		int option = JOptionPane.showConfirmDialog(this, message, "Register", JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			try {
				String nombre = nombreField.getText();
				String email = emailField.getText();
				String tipo = (String) tipoComboBox.getSelectedItem();
				String fechaNac = fechaNacField.getText();
				float peso = Float.parseFloat(pesoField.getText());
				int altura = Integer.parseInt(alturaField.getText());
				int frecCarMax = Integer.parseInt(frecCarMaxField.getText());
				int frecCarRep = Integer.parseInt(frecCarRepField.getText());

				// Validar si el email coincide con el tipo seleccionado
				if ((tipo.equals("GOOGLE") && !email.endsWith("@gmail.com"))
						|| (tipo.equals("META") && !email.endsWith("@meta.com"))) {
					throw new IllegalArgumentException("Email domain does not match the selected login type.");
				}

				controller.registro(nombre, email, tipo, fechaNac, peso, altura, frecCarMax, frecCarRep);
				JOptionPane.showMessageDialog(this, "Registration successful!");
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid number format. Please check the fields.");
				performRegistro();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				performRegistro();
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				performRegistro();
			}
		} else {
			performLogin(); // Vuelve a la ventana de login si se cancela el registro
		}
	}

	private void performLogout() {
		try {
			controller.logOut();
			JOptionPane.showMessageDialog(this, "Logged out successfully.");
			System.exit(0);
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}


	private void crearReto(String nombre, String deporte, String fecha_inicio, String fecha_fin, Integer distancia,
			Integer tiempo) {

		try {
			controller.crearReto(nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo);

		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public String reemplazarEspacios(String texto) {
	    if (texto == null) {
	        return null;
	    }
	    return texto.replace(" ", "_");
	}

	private void consultarRetos(String deporte, String fecha) {

		try {
			// Llama al controlador para obtener la lista de retos
			List<Reto> retos = controller.consultarRetos(deporte, fecha);

			SwingUtilities.invokeLater(() -> {
				// Limpia la tabla antes de llenarla
				DefaultTableModel model = (DefaultTableModel) jtbleArticles.getModel();
				model.setRowCount(0);

				// Llena la tabla con los datos de los retos
				for (Reto reto : retos) {
					model.addRow(new Object[] { reto.id(), reto.nombre(), reto.deporte(), reto.fecha_inicio(),
							reto.fecha_fin(), reto.distancia(), reto.tiempo() });
				}
			});
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar retos", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SwingClientGUI(new SwingClientController()));
	}
}