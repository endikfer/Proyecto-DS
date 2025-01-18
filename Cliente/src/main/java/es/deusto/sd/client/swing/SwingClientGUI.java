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
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;

/**
 * SwingClientGUI class is a Swing-based client that demonstrates the usage of the
 * AuctionsService. It is implemented using a classic Controller pattern.
 */
public class SwingClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	// Controller instance
	private final SwingClientController controller;

	// Default login credentials
	private String defaultEmail = "support@gmail.com";
	private String defaultPassword = "456";

	private JLabel logoutLabel;
	private JComboBox<String> currencyComboBox;
	private JList<String> categoryList;
	private JTable jtbleArticles;
	private JLabel lblArticleTitle;
	private JLabel lblArticlePrice;
	private JLabel lblArticleBids;
	private JSpinner spinBidAmount;
	private JButton btnBid;
	private JScrollPane articleScrollPane;
	private JPanel jPanelArticleDetails;

	private static final String[] CURRENCIES = { "EUR", "USD", "GBP", "JPY" };

	public SwingClientGUI(SwingClientController controller) {
		this.controller = controller;
		
		articleScrollPane = new JScrollPane();
		jPanelArticleDetails = new JPanel();

		if (!performLogin()) {
			System.exit(0);
		}

		setTitle("Auctions Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());

		// Currency ComboBox
		currencyComboBox = new JComboBox<>(CURRENCIES);
		currencyComboBox.setSelectedItem("EUR"); // Default currency
		currencyComboBox.addActionListener(e -> {
			loadArticleDetails();
			//loadArticlesForCategory();
		});
		topPanel.add(currencyComboBox, BorderLayout.WEST);

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
		categoryList = new JList<>(new String[]{"Sesiones", "Retos", "Retos Aceptados"});
		
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
	        case "Sesiones" -> Panel1();
	        case "Retos" -> Panel2();
	        case "Retos Aceptados" -> Panel3();
	    }
	}
	
	private void Panel1() {
        // Update Central Panel
		if (articleScrollPane != null) {
	        articleScrollPane.removeAll();
	    }
		jtbleArticles = new JTable(new DefaultTableModel(new Object[] { "ID", "Title", "Current Price", "Bids" }, 0)) {
		private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
		};
		jtbleArticles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtbleArticles.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				loadArticleDetails();
			}
		});
//		jtbleArticles.getColumnModel().getColumn(0).setMaxWidth(40);
//		jtbleArticles.getColumnModel().getColumn(1).setPreferredWidth(200);
//		jtbleArticles.getColumnModel().getColumn(3).setMaxWidth(40);
		
		adjustColumnWidths(jtbleArticles);

		articleScrollPane = new JScrollPane(jtbleArticles);
		articleScrollPane.setPreferredSize(new Dimension(600, getHeight()));
		articleScrollPane.setBorder(new TitledBorder("Articles of the selected Category"));
		add(articleScrollPane, BorderLayout.CENTER);
		
		

        // Update East Panel
		if (jPanelArticleDetails != null) {
			jPanelArticleDetails.removeAll();
	    }
        jPanelArticleDetails = new JPanel(new GridLayout(5, 2, 10, 10));
		jPanelArticleDetails.setBorder(new TitledBorder("Article Details"));
		jPanelArticleDetails.setPreferredSize(new Dimension(300, getHeight())); // Remaining width

		jPanelArticleDetails.add(new JLabel("Title:"));
		lblArticleTitle = new JLabel();
		jPanelArticleDetails.add(lblArticleTitle);

		jPanelArticleDetails.add(new JLabel("Current Price:"));
		lblArticlePrice = new JLabel();
		jPanelArticleDetails.add(lblArticlePrice);

		jPanelArticleDetails.add(new JLabel("Bids:"));
		lblArticleBids = new JLabel();
		jPanelArticleDetails.add(lblArticleBids);

		jPanelArticleDetails.add(new JLabel("Bid Amount:"));
		spinBidAmount = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		jPanelArticleDetails.add(spinBidAmount);

		btnBid = new JButton("Place Bid");
		btnBid.setEnabled(false);
		btnBid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placeBid();
			}
		});

		JPanel jPanelBidButton = new JPanel();
		jPanelBidButton.add(btnBid);
		jPanelArticleDetails.add(jPanelBidButton);

		add(jPanelArticleDetails, BorderLayout.EAST);

        refreshPanels();
    }

    private void Panel2() {
    	// Update Central Panel
    			articleScrollPane.removeAll();
    			jtbleArticles = new JTable(new DefaultTableModel(new Object[] { "ID", "Nombre", "Deporte", "Fecha inicio", "Fecha fin", "Distancia", "Tiempo" }, 0)) {
    			private static final long serialVersionUID = 1L;

    	        @Override
    	        public boolean isCellEditable(int row, int column) {
    	            return false;
    	            }
    			};
    			jtbleArticles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			jtbleArticles.getSelectionModel().addListSelectionListener(e -> {
    				if (!e.getValueIsAdjusting()) {
    					loadArticleDetails();
    				}
    			});
    			
    			adjustColumnWidths(jtbleArticles);

    			articleScrollPane = new JScrollPane(jtbleArticles);
    			articleScrollPane.setPreferredSize(new Dimension(600, getHeight()));
    			articleScrollPane.setBorder(new TitledBorder("Retos creados"));
    			add(articleScrollPane, BorderLayout.CENTER);
    			
    			

    	        // Update East Panel
    	        jPanelArticleDetails.removeAll();
    	        jPanelArticleDetails = new JPanel(new GridLayout(5, 2, 10, 10));
    			jPanelArticleDetails.setBorder(new TitledBorder("Crear retos"));
    			jPanelArticleDetails.setPreferredSize(new Dimension(300, getHeight())); // Remaining width

    			jPanelArticleDetails.add(new JLabel("Title:"));
    			lblArticleTitle = new JLabel();
    			jPanelArticleDetails.add(lblArticleTitle);

    			jPanelArticleDetails.add(new JLabel("Current Price:"));
    			lblArticlePrice = new JLabel();
    			jPanelArticleDetails.add(lblArticlePrice);

    			jPanelArticleDetails.add(new JLabel("Bids:"));
    			lblArticleBids = new JLabel();
    			jPanelArticleDetails.add(lblArticleBids);

    			jPanelArticleDetails.add(new JLabel("Bid Amount:"));
    			spinBidAmount = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    			jPanelArticleDetails.add(spinBidAmount);

    			btnBid = new JButton("Place Bid");
    			btnBid.setEnabled(false);
    			btnBid.addActionListener(new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					placeBid();
    				}
    			});

    			JPanel jPanelBidButton = new JPanel();
    			jPanelBidButton.add(btnBid);
    			jPanelArticleDetails.add(jPanelBidButton);

    			add(jPanelArticleDetails, BorderLayout.EAST);

    	        refreshPanels();
    }

    private void Panel3() {
    	// Update Central Panel
    			articleScrollPane.removeAll();
    			jtbleArticles = new JTable(new DefaultTableModel(new Object[] { "ID", "Title", "Current Price", "Bids" }, 0)) {
    			private static final long serialVersionUID = 1L;

    	        @Override
    	        public boolean isCellEditable(int row, int column) {
    	            return false;
    	            }
    			};
    			jtbleArticles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			jtbleArticles.getSelectionModel().addListSelectionListener(e -> {
    				if (!e.getValueIsAdjusting()) {
    					loadArticleDetails();
    				}
    			});
//    			jtbleArticles.getColumnModel().getColumn(0).setMaxWidth(40);
//    			jtbleArticles.getColumnModel().getColumn(1).setPreferredWidth(200);
//    			jtbleArticles.getColumnModel().getColumn(3).setMaxWidth(40);
    			
    			adjustColumnWidths(jtbleArticles);

    			articleScrollPane = new JScrollPane(jtbleArticles);
    			articleScrollPane.setPreferredSize(new Dimension(600, getHeight()));
    			articleScrollPane.setBorder(new TitledBorder("Retos Aceptados"));
    			add(articleScrollPane, BorderLayout.CENTER);
    			
    			

    	        // Update East Panel
    	        jPanelArticleDetails.removeAll();
    	        jPanelArticleDetails = new JPanel(new GridLayout(5, 2, 10, 10));
    			jPanelArticleDetails.setBorder(new TitledBorder("Aceptar Retos"));
    			jPanelArticleDetails.setPreferredSize(new Dimension(300, getHeight())); // Remaining width

    			jPanelArticleDetails.add(new JLabel("Title:"));
    			lblArticleTitle = new JLabel();
    			jPanelArticleDetails.add(lblArticleTitle);

    			jPanelArticleDetails.add(new JLabel("Current Price:"));
    			lblArticlePrice = new JLabel();
    			jPanelArticleDetails.add(lblArticlePrice);

    			jPanelArticleDetails.add(new JLabel("Bids:"));
    			lblArticleBids = new JLabel();
    			jPanelArticleDetails.add(lblArticleBids);

    			jPanelArticleDetails.add(new JLabel("Bid Amount:"));
    			spinBidAmount = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    			jPanelArticleDetails.add(spinBidAmount);

    			btnBid = new JButton("Place Bid");
    			btnBid.setEnabled(false);
    			btnBid.addActionListener(new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					placeBid();
    				}
    			});

    			JPanel jPanelBidButton = new JPanel();
    			jPanelBidButton.add(btnBid);
    			jPanelArticleDetails.add(jPanelBidButton);

    			add(jPanelArticleDetails, BorderLayout.EAST);

    	        refreshPanels();
    }

    private void refreshPanels() {
    	articleScrollPane.revalidate();
    	articleScrollPane.repaint();
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
	    Object[] message = { 
	        new JLabel("Enter Email:"), emailField, 
	        new JLabel("Enter Password:"), passwordField 
	    };

	    int option = JOptionPane.showOptionDialog(
	        this, message, "Login", 
	        JOptionPane.YES_NO_CANCEL_OPTION, 
	        JOptionPane.PLAIN_MESSAGE, null, 
	        options, options[0]
	    );

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
	    Object[] message = { 
	        new JLabel("Enter Name:"), nombreField, 
	        new JLabel("Enter Email:"), emailField, 
	        new JLabel("Select Type:"), tipoComboBox, 
	        new JLabel("Enter Birth Date (yyyy-MM-dd):"), fechaNacField, 
	        new JLabel("Enter Weight (kg):"), pesoField, 
	        new JLabel("Enter Height (cm):"), alturaField, 
	        new JLabel("Enter Max Heart Rate (bpm):"), frecCarMaxField, 
	        new JLabel("Enter Resting Heart Rate (bpm):"), frecCarRepField 
	    };

	    int option = JOptionPane.showConfirmDialog(
	        this, message, "Register", 
	        JOptionPane.OK_CANCEL_OPTION
	    );

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
	            if ((tipo.equals("GOOGLE") && !email.endsWith("@gmail.com")) || 
	                (tipo.equals("META") && !email.endsWith("@meta.com"))) {
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
			controller.LogOut();
			JOptionPane.showMessageDialog(this, "Logged out successfully.");
			System.exit(0);
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

//	private void loadCategories() {
//		try {
//			List<Category> categories = controller.getCategories();
//
//			SwingUtilities.invokeLater(() -> {
//				categoryList.setListData(categories.toArray(new Category[0]));
//			});
//		} catch (RuntimeException e) {
//			JOptionPane.showMessageDialog(this, e.getMessage());
//		}
//	}

//	private void loadArticlesForCategory() {
//		Category selectedCategory = categoryList.getSelectedValue();
//		String currency = (String) currencyComboBox.getSelectedItem();
//
//		if (selectedCategory != null) {
//			try {
//				List<Article> articles = controller.getArticlesByCategory(selectedCategory.name(), currency);
//
//				SwingUtilities.invokeLater(() -> {
//					DefaultTableModel model = (DefaultTableModel) jtbleArticles.getModel();
//					model.setRowCount(0);
//
//					for (Article article : articles) {
//						model.addRow(new Object[] { article.id(), article.title(),
//								formatPrice(article.currentPrice(), currency), article.bids() });
//					}
//				});
//			} catch (RuntimeException e) {
//				JOptionPane.showMessageDialog(this, e.getMessage());
//			}
//		}
//	}

	private void loadArticleDetails() {
		int selectedRow = jtbleArticles.getSelectedRow();
		String currency = (String) currencyComboBox.getSelectedItem();

		if (selectedRow != -1) {
			Long articleId = (Long) jtbleArticles.getValueAt(selectedRow, 0);

			try {
				Article article = controller.getArticleDetails(articleId, currency);

				SwingUtilities.invokeLater(() -> {
					lblArticleTitle.setText(article.title());
					lblArticlePrice.setText(formatPrice(article.currentPrice(), currency));
					lblArticleBids.setText(String.valueOf(article.bids()));
					spinBidAmount.setValue((int) Math.ceil(article.currentPrice()) + 1);
					btnBid.setEnabled(true);
				});
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}

	private void placeBid() {
		int selectedRow = jtbleArticles.getSelectedRow();
		String currency = (String) currencyComboBox.getSelectedItem();

		if (selectedRow != -1) {
			Long articleId = (Long) jtbleArticles.getValueAt(selectedRow, 0);
			Float bidAmount = ((Integer) spinBidAmount.getValue()).floatValue();

			try {
				controller.placeBid(articleId, bidAmount, currency);

				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, "Bid placed successfully!");
				});

				loadArticleDetails();
				//loadArticlesForCategory();

				SwingUtilities.invokeLater(() -> {
					jtbleArticles.setRowSelectionInterval(selectedRow, selectedRow);
				});
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}

	private String formatPrice(float price, String currency) {
		return switch (currency) {
				case "USD" -> String.format("$ %.2f", price);
				case "GBP" -> String.format("%.2f £", price);
				case "JPY" -> String.format("¥ %.2f", price);
				default -> String.format("%.2f €", price);
		};
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SwingClientGUI(new SwingClientController()));
	}
}