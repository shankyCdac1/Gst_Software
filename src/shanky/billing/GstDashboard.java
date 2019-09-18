package shanky.billing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

public class GstDashboard extends JFrame {

	private JPanel contentPane;
	private JTextField product_quantity;
	private JTextField hsnCode;
	private JTextField manufacturer;
	private JTextField mrpRate;
	private JTextField discount;
	private JComboBox gst;
	private JTextField sellPrice;
	private JTextField quantity;

	private DefaultTableModel model_sale = new DefaultTableModel();
	private JTable table_sale = new JTable();
	private JTextField invoiceNo;
	Connection conn = null;
	private JComboBox batchNo;
	private JTextField mobile_textField;
	private JComboBox customer_Name;
	private JTextArea cus_address;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GstDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GstDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GstDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GstDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GstDashboard frame = new GstDashboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

		 
	  public void ItemNames()
	  {
		  conn = ConnectionManager.getConnection();  
		  try { String query = "select * from itemtable";  
		  		Statement st = conn.createStatement();
		  		ResultSet rs = st.executeQuery(query); 
		  while(rs.next()) { 
			   veg_List.addItem(rs.getString("Veg")); 
			   nonVeg_List.addItem(rs.getString("NonVeg")); } }  
		  catch (Exception e) {
	  
	  } }
	 

	public void setTableColumnSize() {
		table_sale.getColumnModel().getColumn(0).setPreferredWidth(25);
		table_sale.getColumnModel().getColumn(1).setPreferredWidth(180);
		table_sale.getColumnModel().getColumn(2).setPreferredWidth(64);
		table_sale.getColumnModel().getColumn(3).setPreferredWidth(64);
		table_sale.getColumnModel().getColumn(4).setPreferredWidth(80);
		table_sale.getColumnModel().getColumn(5).setPreferredWidth(50);
		table_sale.getColumnModel().getColumn(6).setPreferredWidth(50);
		table_sale.getColumnModel().getColumn(7).setPreferredWidth(100);
		table_sale.getColumnModel().getColumn(8).setPreferredWidth(100);
		table_sale.getColumnModel().getColumn(9).setPreferredWidth(100);
		table_sale.getColumnModel().getColumn(10).setPreferredWidth(100);
		table_sale.getColumnModel().getColumn(11).setPreferredWidth(100);
	}

	public String total;
	private JTextField gross_value;
	private JTextField total_value;
	private JTextField discount_value;
	private JTextField cgst_value;
	private JTextField sgst_value;
	private JTextField igst_value;
	private JComboBox veg_List;
	private JComboBox nonVeg_List;
	private JDateChooser dateChooser1;
	private JTextField total_Products;

	public void commonMethod(String query) throws SQLException {
		conn = ConnectionManager.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (rs.next()) {
			total = rs.getString(1);
		}
	}

	public void getallvaluesafterCalculation() {
		int dis = Integer.parseInt(discount.getText());
		// GROSS Amount
				try {  
					commonMethod("select sum(round(sellPrice*Quantity,2)) from invoicetable where Invoice_No = '" 
							+ invoiceNo.getText() + "'");
					gross_value.setText(total);
				} catch (Exception e) {
				}
		
		if(dis != 0)
		{	
		
		// DISCOUNT Amount
		try {
			commonMethod(
					"select sum(round(((SellPrice*Quantity)/100)*Discount,2)) from invoicetable where Invoice_No = '"
							+ invoiceNo.getText() + "'");
			discount_value.setText(total);
		} catch (Exception e) {
		}

		// TOTAL AMOUNT
		try {
			commonMethod(
					"select sum(round(((SellPrice*Quantity)-((SellPrice*Quantity)/100))*Discount,2)) from invoicetable where Invoice_No = '"
							+ invoiceNo.getText() + "'");
			total_value.setText(total);
		} catch (Exception e) {
		}

		// CGST SGST
		try {
			commonMethod(
					"select sum(round((((((SellPrice*Quantity)-((SellPrice*Quantity)/100))*Discount)*Gst)/(100+Gst))/2,2)) from invoicetable where Invoice_No = '"
							+ invoiceNo.getText() + "'");
			cgst_value.setText(total);
			sgst_value.setText(total); 
		} catch (Exception e) {
		}

		// IGST
		try {
			commonMethod(
					"select sum(round(((SellPrice*Quantity)-((SellPrice*Quantity)/100))*Discount,2)) from invoicetable where Invoice_No = '"
							+ invoiceNo.getText() + "'");
			igst_value.setText(total);
		} catch (Exception e) {
		}
	}
		else
		{
			// TOTAL AMOUNT WITHOUT DISCOUNT
			try {
				commonMethod(
						"select sum(round(SellPrice*Quantity,2)) from invoicetable where Invoice_No = '"
								+ invoiceNo.getText() + "'");
				total_value.setText(total);
			} catch (Exception e) {
			}
			 
			// CGST SGST WITHOUT DISCOUNT
			try {
				commonMethod(
						"select sum(round((((sellPrice*Quantity)*Gst)/(100+Gst))/2,2)) from invoicetable where Invoice_No = '"
								+ invoiceNo.getText() + "'");
				cgst_value.setText(total);
				sgst_value.setText(total);    
			} catch (Exception e) {
			}
			
			// IGST WITHOUT DISCOUNT 
			try { 
				commonMethod(
						"select sum(round(sellPrice*Quantity,2)) from invoicetable where Invoice_No = '"
								+ invoiceNo.getText() + "'");
				igst_value.setText(total);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public GstDashboard() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(210, 100, 1457, 988);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLayeredPane MenuLayeredPane = new JLayeredPane();
		MenuLayeredPane.setBounds(15, 0, 1377, 85);
		contentPane.add(MenuLayeredPane);
		setVisible(true);

		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(GstDashboard.class.getResource("/billing.jpg")));
		lblNewLabel_3.setBounds(0, 0, 172, 85);
		MenuLayeredPane.add(lblNewLabel_3);

		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon(GstDashboard.class.getResource("/chargebee.png")));
		label_3.setBounds(169, 0, 220, 85);
		MenuLayeredPane.add(label_3);

		JDateChooser date_chooser = new JDateChooser();
		date_chooser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		date_chooser.setBounds(907, 19, 163, 35);
		date_chooser.setDateFormatString("yyyy-MM-dd");
		MenuLayeredPane.add(date_chooser);

		JLabel label_4 = new JLabel("Invoice Date");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_4.setBounds(782, 28, 110, 20);
		MenuLayeredPane.add(label_4);

		JLabel label_5 = new JLabel("Invoice Number");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_5.setBounds(1085, 28, 135, 20);
		MenuLayeredPane.add(label_5);

		invoiceNo = new JTextField();
		invoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 20));  
		invoiceNo.setColumns(10);
		invoiceNo.setBounds(1235, 19, 127, 35);
		MenuLayeredPane.add(invoiceNo);

		JLabel lblMobileno = new JLabel("MobileNo");
		lblMobileno.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMobileno.setBounds(522, 28, 82, 20);
		MenuLayeredPane.add(lblMobileno);

		mobile_textField = new JTextField();
		mobile_textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e0) { 
				if (e0.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						String m = mobile_textField.getText();
						conn = ConnectionManager.getConnection();
						System.out.println(m);
						String query = "select * from invoicetable where MobileNo = '"+m+"'";  
						Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery(query);
						while (rs.next()) {  
							hsnCode.setText(rs.getString("hsnCode"));
							manufacturer.setText(rs.getString("Manufacturer"));
							mrpRate.setText(rs.getString("Mrp_Rate")); 
							discount.setText(rs.getString("Discount"));
							sellPrice.setText(rs.getString("SellPrice"));
							quantity.setText(rs.getString("Quantity")); 
							invoiceNo.setText(rs.getString("Invoice_No"));
							gst.setSelectedItem(rs.getString("Gst"));
							cus_address.setText(rs.getString("Address"));
							customer_Name.setSelectedItem(rs.getString("Customer_Name"));
							batchNo.setSelectedItem(rs.getString("batchNo"));  
							BigDecimal a = new BigDecimal(rs.getString("Product_Quantity"));
							BigDecimal b = new BigDecimal(rs.getString("Quantity"));
							BigDecimal c = a.subtract(b);
							total_Products.setText(c.toString());
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
								Date d1 = sdf.parse(rs.getString("Invoice_Date"));
								date_chooser.setDate(d1);
								Date d = sdf.parse(rs.getString("ExpDate"));
								dateChooser1.setDate(d);
							} catch (Exception e) {

							}
						
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							conn.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		mobile_textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mobile_textField.setColumns(10);
		mobile_textField.setBounds(619, 19, 135, 35);
		MenuLayeredPane.add(mobile_textField);

		JLayeredPane WorkSpaceLayeredPane = new JLayeredPane();
		WorkSpaceLayeredPane.setBounds(15, 88, 1393, 800); ///////// /////////////////////////////////////
		contentPane.add(WorkSpaceLayeredPane);
		WorkSpaceLayeredPane.setLayout(new CardLayout(0, 0));

		JPanel panel = new JPanel();
		WorkSpaceLayeredPane.add(panel, "name_9168235717450");
		panel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 417, 1363, 214);
		panel.add(scrollPane);
		scrollPane.setViewportView(table_sale);
		table_sale.setModel(model_sale);
		Font f = new Font("Arial", Font.BOLD, 18);
		JTableHeader header = table_sale.getTableHeader();
		header.setFont(f);
		table_sale.setFont(new Font("Serif", Font.BOLD, 18));
		Object column_Name[] = { "Sno", "Product", "HsnCode", "Batch/UI", "Rate", "Qty", "Disc", "GST", "CGST", "SGST",
				"IGST", "Total" };
		table_sale.setRowHeight(30); 
		
		model_sale.setColumnIdentifiers(column_Name);
		setTableColumnSize();

		JLayeredPane invoiceItemsLayeredPane = new JLayeredPane();
		invoiceItemsLayeredPane.setBounds(15, 16, 1363, 202);
		panel.add(invoiceItemsLayeredPane);
		invoiceItemsLayeredPane.setLayout(new CardLayout(0, 0));

		JPanel ItemFieldsPanel = new JPanel();
		invoiceItemsLayeredPane.add(ItemFieldsPanel, "name_9394615751784");
		ItemFieldsPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Product Quantity");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(15, 128, 154, 20);
		ItemFieldsPanel.add(lblNewLabel);

		JLabel hsnCode_validate = new JLabel("");
		hsnCode_validate.setFont(new Font("Tahoma", Font.BOLD, 17));
		hsnCode_validate.setForeground(Color.RED);
		hsnCode_validate.setBackground(Color.RED);
		hsnCode_validate.setBounds(558, 101, 166, 20);
		ItemFieldsPanel.add(hsnCode_validate);

		product_quantity = new JTextField();
		product_quantity.setHorizontalAlignment(SwingConstants.RIGHT);
		product_quantity.setText("0");
		product_quantity.setFont(new Font("Tahoma", Font.PLAIN, 20));
		product_quantity.setBounds(15, 150, 166, 35);
		ItemFieldsPanel.add(product_quantity);
		product_quantity.setColumns(10);

		hsnCode = new JTextField();
		hsnCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		hsnCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				try {
					String i = hsnCode.getText();
					new BigDecimal(i);
					hsnCode_validate.setText("");
				} catch (NumberFormatException e) {
					hsnCode_validate.setText("Invalid characters!");
				}
			}
		});
		hsnCode.setFont(new Font("Tahoma", Font.PLAIN, 20));
		hsnCode.setBounds(558, 64, 199, 35);
		ItemFieldsPanel.add(hsnCode);
		hsnCode.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("HsnCode");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(558, 40, 104, 20);
		ItemFieldsPanel.add(lblNewLabel_1);

		dateChooser1 = new JDateChooser();
		dateChooser1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dateChooser1.setDateFormatString("yyyy-MM-dd");
		dateChooser1.setBounds(1193, 64, 155, 35);
		ItemFieldsPanel.add(dateChooser1);

		batchNo = new JComboBox();
		
		batchNo.setFont(new Font("Tahoma", Font.BOLD, 18));
		batchNo.setEditable(true);
		batchNo.setBounds(796, 65, 189, 35);
		ItemFieldsPanel.add(batchNo);

		JLabel lblNewLabel_2 = new JLabel("BatchNo ./UnoqueId");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(796, 40, 166, 20);
		ItemFieldsPanel.add(lblNewLabel_2);

		manufacturer = new JTextField();
		manufacturer.setFont(new Font("Tahoma", Font.PLAIN, 20));
		manufacturer.setColumns(10);
		manufacturer.setBounds(1013, 64, 154, 35);
		ItemFieldsPanel.add(manufacturer);

		JLabel lblManufacturer = new JLabel("Manufacturer");
		lblManufacturer.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblManufacturer.setBounds(1013, 40, 126, 20);
		ItemFieldsPanel.add(lblManufacturer);

		JLabel mrpRate_validate = new JLabel("");
		mrpRate_validate.setForeground(Color.RED);
		mrpRate_validate.setFont(new Font("Tahoma", Font.BOLD, 17));
		mrpRate_validate.setBackground(Color.RED);
		mrpRate_validate.setBounds(604, 182, 166, 20);
		ItemFieldsPanel.add(mrpRate_validate);

		/*
		 * JDateChooser date_chooser1 = new JDateChooser(); date_chooser.setFont(new
		 * Font("Tahoma", Font.PLAIN, 20)); date_chooser.setBounds(1194, 64, 154, 35);
		 * date_chooser.setDateFormatString("yyyy-MM-dd");
		 */

		JLabel lblExpdate = new JLabel("ExpDate");
		lblExpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblExpdate.setBounds(1194, 40, 69, 20);
		ItemFieldsPanel.add(lblExpdate);

		mrpRate = new JTextField();
		mrpRate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				try {
					String i = mrpRate.getText();
					new BigDecimal(i);
					mrpRate_validate.setText("");
				} catch (NumberFormatException e) {
					mrpRate_validate.setText("Invalid characters!");
				}
			}
		});
		mrpRate.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mrpRate.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpRate.setText("0");
		mrpRate.setColumns(10);
		mrpRate.setBounds(604, 150, 177, 35);
		ItemFieldsPanel.add(mrpRate);

		JLabel lblMrprate = new JLabel("MrpRate");
		lblMrprate.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMrprate.setBounds(605, 128, 104, 20);
		ItemFieldsPanel.add(lblMrprate);

		discount = new JTextField();
		discount.setFont(new Font("Tahoma", Font.PLAIN, 20));
		discount.setText("0");
		discount.setColumns(10);
		discount.setBounds(796, 150, 81, 35);
		ItemFieldsPanel.add(discount);

		JLabel lblDiscount = new JLabel("Discount%");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDiscount.setBounds(796, 128, 104, 20);
		ItemFieldsPanel.add(lblDiscount);

		gst = new JComboBox();
		gst.setEditable(true);
		gst.setFont(new Font("Tahoma", Font.PLAIN, 20));
		gst.setBounds(900, 150, 85, 35);
		ItemFieldsPanel.add(gst);
		gst.addItem("0");
		gst.addItem("5");
		gst.addItem("12");
		gst.addItem("18");
		gst.addItem("28");

		JLabel lblGst = new JLabel("GST%");
		lblGst.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblGst.setBounds(900, 128, 81, 20);
		ItemFieldsPanel.add(lblGst);

		JLabel lblTotalProducts = new JLabel("Total Products");
		lblTotalProducts.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalProducts.setBounds(208, 128, 119, 20);
		ItemFieldsPanel.add(lblTotalProducts);

		JLabel label_1 = new JLabel("Manufacturer");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_1.setBounds(700, -19, 126, 20);
		ItemFieldsPanel.add(label_1);

		JLabel label_2 = new JLabel("ExpDate");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_2.setBounds(869, -19, 69, 20);
		ItemFieldsPanel.add(label_2);

		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCustomerName.setBounds(15, 12, 154, 20);
		ItemFieldsPanel.add(lblCustomerName);

		cus_address = new JTextArea();
		cus_address.setFont(new Font("Monospaced", Font.BOLD, 20));
		cus_address.setBounds(206, 33, 319, 74);
		ItemFieldsPanel.add(cus_address);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAddress.setBounds(184, 12, 154, 20);
		ItemFieldsPanel.add(lblAddress);

		customer_Name = new JComboBox();
		
		customer_Name.setFont(new Font("Tahoma", Font.BOLD, 18));
		customer_Name.setEditable(true);
		customer_Name.setBounds(15, 37, 176, 41);
		ItemFieldsPanel.add(customer_Name);

		total_value = new JTextField();
		total_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		total_value.setHorizontalAlignment(SwingConstants.RIGHT);
		total_value.setText("0");
		total_value.setColumns(10);
		total_value.setBounds(1188, 749, 177, 35);
		panel.add(total_value);

		JLabel lblGrossValue = new JLabel("Gross Value");
		lblGrossValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblGrossValue.setBounds(1021, 707, 113, 20);
		panel.add(lblGrossValue);

		JLabel lblTotalValue = new JLabel("Total Value");
		lblTotalValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalValue.setBounds(1021, 758, 113, 20);
		panel.add(lblTotalValue);

		discount_value = new JTextField();
		discount_value.setText("0");
		discount_value.setHorizontalAlignment(SwingConstants.RIGHT);
		discount_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		discount_value.setColumns(10);
		discount_value.setBounds(1188, 647, 177, 35);
		panel.add(discount_value);

		JLabel lblDiscountValue = new JLabel("Discount Value");
		lblDiscountValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDiscountValue.setBounds(1021, 656, 128, 20);
		panel.add(lblDiscountValue);

		cgst_value = new JTextField();
		cgst_value.setText("0");
		cgst_value.setHorizontalAlignment(SwingConstants.RIGHT);
		cgst_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cgst_value.setColumns(10);
		cgst_value.setBounds(756, 647, 177, 35);
		panel.add(cgst_value);

		JLabel lblCgstValue = new JLabel("CGST Value");
		lblCgstValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCgstValue.setBounds(604, 656, 113, 20);
		panel.add(lblCgstValue);

		sgst_value = new JTextField();
		sgst_value.setText("0");
		sgst_value.setHorizontalAlignment(SwingConstants.RIGHT);
		sgst_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		sgst_value.setColumns(10);
		sgst_value.setBounds(756, 698, 177, 35);
		panel.add(sgst_value);

		JLabel lblSgstValue = new JLabel("SGST Value");
		lblSgstValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSgstValue.setBounds(604, 707, 113, 20);
		panel.add(lblSgstValue);

		igst_value = new JTextField();
		igst_value.setText("0");
		igst_value.setHorizontalAlignment(SwingConstants.RIGHT);
		igst_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		igst_value.setColumns(10);
		igst_value.setBounds(756, 749, 177, 35);
		panel.add(igst_value);

		JLabel sellPrice_validate = new JLabel("");
		sellPrice_validate.setForeground(Color.RED);
		sellPrice_validate.setFont(new Font("Tahoma", Font.BOLD, 17));
		sellPrice_validate.setBackground(Color.RED);
		sellPrice_validate.setBounds(1013, 182, 166, 20);
		ItemFieldsPanel.add(sellPrice_validate);

		JLabel lblIgstValue = new JLabel("IGST Value");
		lblIgstValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblIgstValue.setBounds(604, 758, 113, 20);
		panel.add(lblIgstValue);

		sellPrice = new JTextField();
		sellPrice.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					String i = sellPrice.getText();
					new BigDecimal(i);
					sellPrice_validate.setText("");
				} catch (NumberFormatException e1) {
					sellPrice_validate.setText("Invalid characters!");
				}
			}
		});

		JLabel quantity_validate = new JLabel("");
		quantity_validate.setForeground(Color.RED);
		quantity_validate.setFont(new Font("Tahoma", Font.BOLD, 17));
		quantity_validate.setBackground(Color.RED);
		quantity_validate.setBounds(1022, 182, 166, 20);
		ItemFieldsPanel.add(quantity_validate);

		sellPrice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		sellPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		sellPrice.setText("0");
		sellPrice.setColumns(10);
		sellPrice.setBounds(1013, 150, 154, 35);
		ItemFieldsPanel.add(sellPrice);

		JLabel lblSellprice = new JLabel("SellPrice");
		lblSellprice.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSellprice.setBounds(1012, 128, 104, 20);
		ItemFieldsPanel.add(lblSellprice);

		quantity = new JTextField();
		quantity.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					/*
					 * try { String i = quantity.getText(); new BigDecimal(i);
					 * quantity_validate.setText(""); }catch(NumberFormatException e) {
					 * quantity_validate.setText("Invalid charatcers!");}
					 */
					conn = ConnectionManager.getConnection();
					try {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String invoiceDate = df.format(date_chooser.getDate());
						String expDate = df.format(dateChooser1.getDate());

						String inputData = "insert into invoicetable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement ps = conn.prepareStatement(inputData);
						
						//ps.setString(1, product_Name.getSelectedItem().toString());
						ps.setString(1, hsnCode.getText());
						ps.setString(2, mrpRate.getText());
						ps.setString(3, discount.getText());
						ps.setString(4, gst.getSelectedItem().toString());
						ps.setString(5, sellPrice.getText());
						ps.setString(6, quantity.getText());
						ps.setString(7, customer_Name.getSelectedItem().toString());
						ps.setString(8, cus_address.getText());
						ps.setString(9, batchNo.getSelectedItem().toString()); 
						ps.setString(10, manufacturer.getText());
						ps.setString(11, expDate);
						ps.setString(12, invoiceDate);
						ps.setString(13, invoiceNo.getText());
						ps.setString(14, product_quantity.getText()); 
						ps.setString(15, mobile_textField.getText());
						
						int insertData = ps.executeUpdate();
						if (insertData > 0) {
							JOptionPane.showMessageDialog(null, "Data entered successfully"); 

							Object row[] = new Object[20];
							getallvaluesafterCalculation();
							row[0] = "1";
							row[1] =  "Pp";//product_Name.getSelectedItem().toString();
							row[2] = hsnCode.getText();
							row[3] = batchNo.getSelectedItem().toString();
							row[4] = mrpRate.getText();
							row[5] = quantity.getText();
							row[6] = discount.getText();
							row[7] = gst.getSelectedItem().toString();
							row[8] = cgst_value.getText();
							row[9] = sgst_value.getText();
							row[10] = igst_value.getText();
							row[11] = total_value.getText();
 
							model_sale.addRow(row);
						}
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							conn.close();
						} catch (Exception e) {
							System.out.println(e);
						}
					}

				}
			}
		});
		quantity.setFont(new Font("Tahoma", Font.PLAIN, 20));
		quantity.setHorizontalAlignment(SwingConstants.RIGHT);
		quantity.setText("0");
		quantity.setColumns(10);
		quantity.setBounds(1194, 150, 154, 35);
		ItemFieldsPanel.add(quantity);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblQuantity.setBounds(1194, 128, 104, 20);
		ItemFieldsPanel.add(lblQuantity);

		JCheckBox ClearValues = new JCheckBox("Clear");
		ClearValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ClearValues.isSelected()) {
					hsnCode.setText("");
					batchNo.setSelectedItem("");
					manufacturer.setText("");
					//product_Name.setSelectedItem("");
					mrpRate.setText("0");
					discount.setText("0");
					gst.setSelectedIndex(0);
					sellPrice.setText("0");
					quantity.setText("0");
					invoiceNo.setText("");
					total_Products.setText("");
					customer_Name.setSelectedItem("");
					cus_address.setText("");
					mobile_textField.setText("");
				}
			}
		});
		ClearValues.setFont(new Font("Tahoma", Font.BOLD, 17));
		ClearValues.setBounds(11, 87, 139, 29);
		ItemFieldsPanel.add(ClearValues);
		
		total_Products = new JTextField();
		total_Products.setText("0");
		total_Products.setHorizontalAlignment(SwingConstants.RIGHT);
		total_Products.setFont(new Font("Tahoma", Font.PLAIN, 20));
		total_Products.setColumns(10);
		total_Products.setBounds(204, 150, 166, 35);
		ItemFieldsPanel.add(total_Products);

		gross_value = new JTextField();
		gross_value.setText("0");
		gross_value.setHorizontalAlignment(SwingConstants.RIGHT);
		gross_value.setFont(new Font("Tahoma", Font.PLAIN, 20));
		gross_value.setColumns(10);
		gross_value.setBounds(1188, 698, 177, 35);
		panel.add(gross_value);

		JButton printButton = new JButton("Print Invoice");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PrinterJob pj = PrinterJob.getPrinterJob();
				pj.setPrintable(new BillPrintable(), getPageFormat(pj)); 
				if (pj.printDialog()) {
					try {
						pj.print();
					} catch (PrinterException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		printButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		printButton.setBounds(401, 647, 153, 42);
		panel.add(printButton);
		
		JButton btnGstValues = new JButton("Save Gst Values");
		btnGstValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conn = ConnectionManager.getConnection();
				String query = "insert into gstTable values(?,?,?,?,?,?,?)";
				try {
					PreparedStatement ps = conn.prepareStatement(query); 
					ps.setString(1, cgst_value.getText());
					ps.setString(2, sgst_value.getText());
					ps.setString(3, igst_value.getText());
					ps.setString(4, discount_value.getText());
					ps.setString(5, gross_value.getText());
					ps.setString(6, total_value.getText()); 
					ps.setString(7, mobile_textField.getText());
						int insertData = ps.executeUpdate();
							if(insertData > 0)
								JOptionPane.showMessageDialog(null, "Data inserted");
							
				} catch (SQLException e) { 
					e.printStackTrace();
				}
				finally {
					try {
						conn.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
					
				}
			}
		});
		btnGstValues.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnGstValues.setBounds(15, 647, 177, 42); 
		panel.add(btnGstValues);
		
		JButton btnGetGstValues = new JButton("Get Gst Values");
		btnGetGstValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conn = ConnectionManager.getConnection();
				String query = "select * from gstTable where MobileNo = '"+mobile_textField.getText()+"'";
				try {
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(query);
					while(rs.next())
					{
						 cgst_value.setText(rs.getString("CGST"));  
						 sgst_value.setText(rs.getString("SGST"));
						 igst_value.setText(rs.getString("IGST"));
						 discount_value.setText(rs.getString("Discount"));
						 gross_value.setText(rs.getString("Gross"));
						 total_value.setText(rs.getString("Total"));
					}
				} catch (Exception e) {
					
				}
			}
		});
		btnGetGstValues.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnGetGstValues.setBounds(209, 647, 177, 42);
		panel.add(btnGetGstValues);
		
		JPanel Item_DishesPanel = new JPanel();
		Item_DishesPanel.setBounds(15, 227, 1363, 186);
		panel.add(Item_DishesPanel); 
		Item_DishesPanel.setLayout(null);
		
		JLabel Veg = new JLabel("Veg");
		Veg.setFont(new Font("Tahoma", Font.BOLD, 16));
		Veg.setBounds(15, 16, 43, 20);
		Item_DishesPanel.add(Veg);
		
		veg_List = new JComboBox();
		veg_List.setFont(new Font("Tahoma", Font.BOLD, 18));
		veg_List.setEditable(true); 
		veg_List.setBounds(15, 39, 164, 35); 
		Item_DishesPanel.add(veg_List);
		veg_List.setSelectedItem(""); 
		
		JLabel lblNonveg = new JLabel("Non-Veg");
		lblNonveg.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNonveg.setBounds(194, 16, 86, 20); 
		Item_DishesPanel.add(lblNonveg);
		
		nonVeg_List = new JComboBox();
		nonVeg_List.setFont(new Font("Tahoma", Font.BOLD, 18));
		nonVeg_List.setEditable(true);
		nonVeg_List.setBounds(194, 39, 177, 35);
		Item_DishesPanel.add(nonVeg_List);
		nonVeg_List.setSelectedItem("");
		
		JLabel lblSalad = new JLabel("Salad");
		lblSalad.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSalad.setBounds(389, 16, 60, 20);
		Item_DishesPanel.add(lblSalad);
		
		JComboBox salad_List = new JComboBox();
		salad_List.setFont(new Font("Tahoma", Font.BOLD, 18));
		salad_List.setEditable(true);
		salad_List.setBounds(386, 39, 137, 35);
		Item_DishesPanel.add(salad_List);
		
		JLabel lblRice = new JLabel("Rice");
		lblRice.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRice.setBounds(537, 16, 51, 20);
		Item_DishesPanel.add(lblRice);
		
		JComboBox rice_List = new JComboBox();
		rice_List.setFont(new Font("Tahoma", Font.BOLD, 18));
		rice_List.setEditable(true);
		rice_List.setBounds(538, 39, 137, 35);
		Item_DishesPanel.add(rice_List);
		
		JButton halfButton = new JButton("Half");
		halfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dis = discount.getText();  
				int disInt = Integer.parseInt(dis);
				String gs  = gst.getSelectedItem().toString();
				String qua = quantity.getText(); 
				String paneerPrice = "200";
				String friedalloPrice = "150";
				String chickenMasala = "250";
				String friedChicken = "350";
				
					if(veg_List.getSelectedIndex() == 0)  
					{	
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(paneerPrice);
						BigDecimal fP =  hP.multiply(pP);      
							if(disInt != 0)    								// if  discount is applied     
							{	 
								BigDecimal d = new BigDecimal(disInt); 
								BigDecimal per = new BigDecimal(100);   
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);   
								BigDecimal afterDiscount = fP1.multiply(d); 
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); } 
							
					}
					else if(veg_List.getSelectedIndex() == 1)
					{	
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(friedalloPrice);  
						BigDecimal fP = hP.multiply(pP);  
							if(disInt != 0)    								// if  discount is applied 
							{	 
								BigDecimal d = new BigDecimal(disInt);
								BigDecimal per = new BigDecimal(100);    
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);   
								BigDecimal afterDiscount = fP1.multiply(d);
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); }
						
					}
					else if(nonVeg_List.getSelectedIndex() == 0)
					{
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(chickenMasala);
						BigDecimal fP =  hP.multiply(pP);      
							if(disInt != 0)    								// if  discount is applied    
							{	 
								BigDecimal d = new BigDecimal(disInt); 
								BigDecimal per = new BigDecimal(100);   
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);    
								BigDecimal afterDiscount = fP1.multiply(d); 
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); } 
					}
					else if(nonVeg_List.getSelectedIndex() == 1)
					{
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(friedChicken);  
						BigDecimal fP = hP.multiply(pP);  
							if(disInt != 0)    								// if  discount is applied 
							{	 
								BigDecimal d = new BigDecimal(disInt);
								BigDecimal per = new BigDecimal(100);    
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);   
								BigDecimal afterDiscount = fP1.multiply(d);
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); }
					}
			}
		});
		halfButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		halfButton.setBounds(15, 90, 164, 35);
		Item_DishesPanel.add(halfButton);
		
		JButton fullButton = new JButton("Full");
		fullButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dis = discount.getText();  
				int disInt = Integer.parseInt(dis);
				String gs  = gst.getSelectedItem().toString(); 
				String qua = quantity.getText(); 
				String paneerPrice = "400";
				String friedalloPrice = "350";
				String chickenMasala = "550";
				String friedChicken = "450";
					if(veg_List.getSelectedIndex() == 0)    
					{	
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(paneerPrice);
						BigDecimal fP =  hP.multiply(pP);      
							if(disInt != 0)    								// if  discount is applied    
							{	  
								BigDecimal d = new BigDecimal(disInt); 
								BigDecimal per = new BigDecimal(100);   
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);    
								BigDecimal afterDiscount = fP1.multiply(d); 
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); } 
							
					}
					else if(veg_List.getSelectedIndex() == 1)
					{	
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(friedalloPrice);  
						BigDecimal fP = hP.multiply(pP);  
							if(disInt != 0)    								// if  discount is applied 
							{	 
								BigDecimal d = new BigDecimal(disInt);
								BigDecimal per = new BigDecimal(100);    
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);   
								BigDecimal afterDiscount = fP1.multiply(d);
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); }
					}
					else if(nonVeg_List.getSelectedIndex() == 0)
					{
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(chickenMasala);
						BigDecimal fP =  hP.multiply(pP);      
							if(disInt != 0)    								// if  discount is applied    
							{	 
								BigDecimal d = new BigDecimal(disInt); 
								BigDecimal per = new BigDecimal(100);   
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);    
								BigDecimal afterDiscount = fP1.multiply(d); 
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); } 
					}
					else if(nonVeg_List.getSelectedIndex() == 1)
					{
						BigDecimal hP = new BigDecimal(qua);
						BigDecimal pP = new BigDecimal(friedChicken);  
						BigDecimal fP = hP.multiply(pP);  
							if(disInt != 0)    								// if  discount is applied 
							{	 
								BigDecimal d = new BigDecimal(disInt);
								BigDecimal per = new BigDecimal(100);    
								BigDecimal fP1 = fP.divide(per,2,RoundingMode.FLOOR);   
								BigDecimal afterDiscount = fP1.multiply(d);
								mrpRate.setText(afterDiscount.toString());
							}
							else
							{ mrpRate.setText(fP.toString()); }
					}
						
				
			}
		});
		fullButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		fullButton.setBounds(194, 90, 177, 35);
		Item_DishesPanel.add(fullButton);

		
		ItemNames(); 
	} 

	public PageFormat getPageFormat(PrinterJob pj) // throws PrinterException
	{
		PageFormat pf = pj.defaultPage();
		Paper paper = pf.getPaper();
		double middleHeight = 8.0;
		double headerHeight = 2.0;
		double footerHeight = 2.0;
		double width = convert_CM_To_PPI(8); // printer know only point per inch.default value is 72ppi
		double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
		paper.setSize(width, height);
		paper.setImageableArea(0, 10, width, height - convert_CM_To_PPI(1)); // define boarder size after that print
																				// area width is about 180 points

		pf.setOrientation(PageFormat.PORTRAIT); // select orientation portrait or landscape but for this time portrait
		pf.setPaper(paper);

		return pf;
	}
 
	protected static double convert_CM_To_PPI(double cm) {
		return toPPI(cm * 0.393600787);
	}

	protected static double toPPI(double inch) {
		return inch * 72d;
	}

	public class BillPrintable implements Printable {

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

			int result = NO_SUCH_PAGE;

			if (pageIndex == 0) {

				Graphics2D g2d = (Graphics2D) graphics;
				double width = pageFormat.getImageableWidth();
				g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

				FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));
				int idLength = metrics.stringWidth("000");
				int amtLength = metrics.stringWidth("000000");
				int qtyLength = metrics.stringWidth("00000");
				int priceLength = metrics.stringWidth("000000");
				int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;
				int productPosition = 0;
				int discountPosition = prodLength + 5;
				int pricePosition = discountPosition + idLength + 10;
				int qtyPosition = pricePosition + priceLength + 4;
				int amtPosition = qtyPosition + qtyLength;

				try {
					/* Draw Header */
					int y = 20;
					int yShift = 10;
					int headerRectHeight = 15;
					int headerRectHeighta = 40;

					// Product names Get
					String pn1a = "Manufacturer";
					String pn2a = "HsnCode";
					String sellP = "SellPrice";
					// Product price Get
					String manu = manufacturer.getText();
					String hsn = hsnCode.getText();
					String sp = sellPrice.getText();

					g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
					g2d.drawString("-------------------------------------", 12, y);y += yShift;
					g2d.drawString("      CHARGEBEE Bill Receipt        ", 12, y);y += yShift;
					g2d.drawString("-------------------------------------", 12, y);y += headerRectHeight;
					g2d.drawString("-------------------------------------", 10, y);y += yShift;
					g2d.drawString(" Food Name                 T.Price   ", 10, y);y += yShift;
					g2d.drawString("-------------------------------------", 10, y);y += headerRectHeight;
					g2d.drawString(" " + pn1a + "                  " + manu + "  ", 10, y);y += yShift;
					g2d.drawString(" " + pn2a + "                  " + hsn + "  ", 10, y);y += yShift;
					g2d.drawString(" " + sellP + "                  " + sp + "  ", 10, y);y += yShift;
					g2d.drawString("-------------------------------------", 10, y);y += yShift;
					g2d.drawString("-------------------------------------", 10, y);y += yShift;
					g2d.drawString("          Free Home Delivery         ", 10, y);y += yShift;
					g2d.drawString("             7080656113              ", 10, y);y += yShift;
					g2d.drawString("*************************************", 10, y);y += yShift; 
					g2d.drawString("    THANKS TO VISIT OUR RESTUARANT   ", 10, y);y += yShift; 
					g2d.drawString("*************************************", 10, y);y += yShift;

				} catch (Exception r) {
					r.printStackTrace();
				}
				result = PAGE_EXISTS; 

			}
			return result;
		}

	}
}
