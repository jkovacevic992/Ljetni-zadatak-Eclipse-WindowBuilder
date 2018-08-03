package kovacevic.ljetnizadatak;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Dimension;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Autori extends JFrame {

	private JPanel contentPane;
	private Connection veza;
    private PreparedStatement izraz;

	
	
	public Autori() {
		initComponents();
		getContentPane().setBackground(Color.decode("#082F4E"));
        pnlPodaci.setBackground(Color.decode("#082F4E"));
		 try {
	            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        try {
	            veza = DriverManager.getConnection("jdbc:mysql://localhost/penjalista?"
	                    + "user=edunova&password=edunova&serverTimezone=CET&useUnicode=true&characterEncoding=utf-8");
	            ucitajIzBaze();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	}
	
	private void initComponents() {
		
		pnlPodaci = new javax.swing.JPanel();
		lstAutori = new javax.swing.JList<>();
		lstAutori.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) {
		            return;
		        }

		        Autor a = lstAutori.getSelectedValue();
		        if (a == null) {
		            return;
		        }
		        ocistiPolja();

		        txtIme.setText(a.getIme());
		        txtPrezime.setText(a.getPrezime());
			}
		});
		
		
		
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 554, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel pnlPodaci = new JPanel();
		pnlPodaci.setBounds(169, 11, 328, 274);
		contentPane.add(pnlPodaci);
		pnlPodaci.setLayout(null);
		pnlPodaci.setBackground(Color.decode("#082F4E"));
		
		JLabel lblIme = new JLabel("Ime");
		lblIme.setForeground(Color.WHITE);
		lblIme.setBounds(10, 11, 46, 14);
		pnlPodaci.add(lblIme);
		
		txtIme = new JTextField();
		txtIme.setBounds(10, 36, 283, 20);
		pnlPodaci.add(txtIme);
		txtIme.setColumns(10);
		
		txtPrezime = new JTextField();
		txtPrezime.setBounds(10, 92, 283, 20);
		pnlPodaci.add(txtPrezime);
		txtPrezime.setColumns(10);
		
		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setForeground(Color.WHITE);
		lblPrezime.setBounds(10, 67, 46, 14);
		pnlPodaci.add(lblPrezime);
		
		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
                     
		             
				        izraz = veza.prepareStatement("insert into autor (ime,prezime)" + "value (?,?)");

				            
				            izraz.setString(1, txtIme.getText().substring(0, 1).toUpperCase()+txtIme.getText().substring(1).toLowerCase());
				            izraz.setString(2, txtPrezime.getText().substring(0, 1).toUpperCase()+txtPrezime.getText().substring(1).toLowerCase());
				            
//				            if (!txtIme.getText().matches("[a-zA-Z_]+") || !txtPrezime.getText().matches("[a-zA-Z_]+")) {
//				                JOptionPane.showMessageDialog(getRootPane(), "Ime i prezime mogu sadržavati samo slova.");
//				                return;
//				}

				            if(izraz.executeUpdate()!=0){
				                ucitajIzBaze();
				                ocistiPolja();
				                
				            }
				            
				           
				            
				            izraz.close();

				        } catch(StringIndexOutOfBoundsException str){
				            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci.");
				        }
				                catch (SQLException ex) {
				            ex.printStackTrace();
				        }
				
			}
		});
		btnDodaj.setBounds(10, 229, 89, 23);
		pnlPodaci.add(btnDodaj);
		
		JButton btnObrisi = new JButton("Obri\u0161i");
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 Autor a = lstAutori.getSelectedValue();
			        if (a == null) {
			            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi autora.");
			            return;
			        }

			        try {
			            izraz = veza.prepareStatement("delete from autor where sifra=?");
			            izraz.setInt(1, a.getSifra());

			            if (izraz.executeUpdate() == 0) {
			                JOptionPane.showMessageDialog(getRootPane(), "Nije obrisan nijedan red. ");
			            } else {
			                ucitajIzBaze();
			                ocistiPolja();
			            }
			            izraz.close();

			        } catch (SQLIntegrityConstraintViolationException ex) {
			            JOptionPane.showMessageDialog(getRootPane(), "Nemoguæe obrisati.");
			        } catch (SQLException ex) {
			            ex.printStackTrace();
			        }
			}
		});
		btnObrisi.setBounds(204, 229, 89, 23);
		pnlPodaci.add(btnObrisi);
		
		JButton btnPromjena = new JButton("Promjena");
		btnPromjena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Autor a = lstAutori.getSelectedValue();
		        if (a == null) {
		            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi autora.");
		            return;
		        }
		        
		        try {

		            NamedParameterStatement izraz = new NamedParameterStatement(veza, "update autor set ime=:ime, "
		                    + " prezime=:prezime "
		                    + " where sifra=:sifra");

		            izraz.setString("ime", txtIme.getText().substring(0, 1).toUpperCase()+txtIme.getText().substring(1).toLowerCase());
		            izraz.setString("prezime", txtPrezime.getText().substring(0, 1).toUpperCase()+txtPrezime.getText().substring(1).toLowerCase());

		            izraz.setInt("sifra", a.getSifra());
//		             if(!txtIme.getText().matches("[a-zA-Z]+") || !txtPrezime.getText().matches("[a-zA-Z]+")){
//		                JOptionPane.showMessageDialog(getRootPane(), "Ime i prezime mogu sadržavati samo slova.");
//		                return;
//		            }
		            if (izraz.izvedi() != 0)  {

		                ocistiPolja();
		                ucitajIzBaze();
		            }
		        } catch(StringIndexOutOfBoundsException str){
		            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci");
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
			}
		});
		btnPromjena.setBounds(105, 229, 89, 23);
		pnlPodaci.add(btnPromjena);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 135, 274);
		contentPane.add(scrollPane);
		
		
		
		
		scrollPane.setViewportView(lstAutori);
		
						

		
		
		
		
	}
	private JTextField txtIme;
	private JTextField txtPrezime;
	private javax.swing.JPanel pnlPodaci; 
	private javax.swing.JList<Autor> lstAutori;
	
	
	
	 private void ocistiPolja() {

	        for (Component c : pnlPodaci.getComponents()) {

	            if (c instanceof JTextField) {
	                ((JTextField) c).setText("");
	            }
	        }
	    }
	
	 private void ucitajIzBaze() {
	        try {
	            izraz = veza.prepareStatement("select * from autor");
	            ResultSet rs = izraz.executeQuery();

	            List<Autor> lista = new ArrayList<>();
	            Autor a;
	            while (rs.next()) {
	                a = new Autor();
	                a.setSifra(rs.getInt("sifra"));
	                a.setIme(rs.getString("ime"));
	                a.setPrezime(rs.getString("prezime"));

	                lista.add(a);
	            }
	            rs.close();
	            izraz.close();

	            Collections.sort(lista, new Comparator<Autor>() {
	                Collator col = Collator.getInstance(new Locale("hr", "HR"));

	                public int compare(Autor a1, Autor a2) {
	                    return col.compare(a1.getPrezime(), a2.getPrezime());
	                }
	            });
	            DefaultListModel<Autor> m = new DefaultListModel<>();

	            lista.forEach((autor) -> m.addElement(autor));
	            lstAutori.setModel(m);

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	    }
}
