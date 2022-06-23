import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.proteanit.sql.DbUtils;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JavaCrud {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtedition;
	private JTextField txtprice;
	private JTable table;
	private JTextField txtbid;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaCrud window = new JavaCrud();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JavaCrud() {
		initialize();
		Connect();
		table_load();
	}

	Connection con;
	PreparedStatement pst;
	ResultSet rs;

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/javacrud", "root", "");
		} catch (ClassNotFoundException ex) {

		} catch (SQLException ex) {

		}
	}

	public void table_load() {
		try {
			pst = con.prepareStatement("select * from book");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 930, 551);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Book Shop");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(380, 0, 285, 86);
		frame.getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Registation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 78, 447, 255);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(20, 26, 89, 34);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Edition");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(20, 70, 89, 34);
		panel.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("Price");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(20, 114, 89, 34);
		panel.add(lblNewLabel_1_1_1);

		txtbname = new JTextField();
		txtbname.setBounds(216, 41, 194, 19);
		panel.add(txtbname);
		txtbname.setColumns(10);

		txtedition = new JTextField();
		txtedition.setColumns(10);
		txtedition.setBounds(216, 80, 194, 19);
		panel.add(txtedition);

		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(216, 124, 194, 19);
		panel.add(txtprice);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname, edition, price;
				bname = txtbname.getText();
				edition = txtedition.getText();
				price = txtprice.getText();

				try {
					pst = con.prepareStatement("insert into book(bname,edition,price)values(?,?,?)");
					pst.setString(1, bname);
					pst.setString(2, edition);
					pst.setString(3, price);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Addedd.");
					table_load();
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				} catch (SQLException el) {
					el.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(20, 343, 113, 59);
		frame.getContentPane().add(btnNewButton);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(168, 343, 113, 59);
		frame.getContentPane().add(btnExit);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtbname.setText("");
				txtedition.setText("");
				txtprice.setText("");
				txtbname.requestFocus();
			}
		});
		btnClear.setBounds(322, 343, 113, 59);
		frame.getContentPane().add(btnClear);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(466, 78, 440, 316);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 425, 448, 79);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_1_1_2 = new JLabel("Book ID");
		lblNewLabel_1_1_2.setBounds(21, 34, 83, 17);
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblNewLabel_1_1_2);

		txtbid = new JTextField();
		txtbid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String id = txtbid.getText();

					pst = con.prepareStatement("Select name,edition,price from book where id = ?");
					pst.setString(1, id);
					ResultSet rs = pst.executeQuery();

					if (rs.next() == true) {
						String name = rs.getString(1);
						String edition = rs.getString(2);
						String price = rs.getString(3);

						txtbname.setText(name);
						txtedition.setText(edition);
						txtprice.setText(price);
					} else {
						txtbname.setText("");
						txtedition.setText("");
						txtprice.setText("");
					}
				} catch (SQLException ex) {

				}
			}
		});
		txtbid.setBounds(128, 35, 217, 20);
		txtbid.setColumns(10);
		panel_1.add(txtbid);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname, edition, price, bid;
				bname = txtbname.getText();
				edition = txtbname.getText();
				price = txtbname.getText();
				bid = txtbid.getText();

				try {
					pst = con.prepareStatement("update book set name = ?, edition = ?, price = ? where id = ?");
					pst.setString(1, bname);
					pst.setString(2, edition);
					pst.setString(3, price);
					pst.setString(4, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Addedd.");
					table_load();

					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				} catch (SQLException el) {
					el.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(522, 421, 113, 59);
		frame.getContentPane().add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bid;
				bid = txtbid.getText();

				try {
					pst = con.prepareStatement("delete from book where id = ?");
					pst.setString(1, bid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Delete.");
					table_load();

					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				} catch (SQLException el) {
					el.printStackTrace();
				}
			}
		});
		btnDelete.setBounds(645, 421, 113, 59);
		frame.getContentPane().add(btnDelete);
	}
}