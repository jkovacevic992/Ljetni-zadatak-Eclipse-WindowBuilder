package kovacevic.ljetnizadatak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.cj.util.StringUtils;

import javax.swing.event.ListSelectionEvent;

public class Penjaci extends JFrame {

	private JPanel contentPane;
	private Connection veza;
	private PreparedStatement izraz;

	public Penjaci() {
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
		lstPenjaci = new javax.swing.JList<>();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		getContentPane().setLayout(null);

		JPanel pnlPodaci = new JPanel();
		pnlPodaci.setBounds(123, 11, 301, 220);
		getContentPane().add(pnlPodaci);
		pnlPodaci.setLayout(null);
		pnlPodaci.setBackground(Color.decode("#082F4E"));

		JLabel lblIme = new JLabel("Ime");
		lblIme.setForeground(Color.WHITE);
		lblIme.setBounds(10, 11, 46, 14);
		pnlPodaci.add(lblIme);

		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setForeground(Color.WHITE);
		lblPrezime.setBounds(10, 71, 70, 14);
		pnlPodaci.add(lblPrezime);

		JLabel lblRezultat = new JLabel("Rezultat");
		lblRezultat.setForeground(Color.WHITE);
		lblRezultat.setBounds(10, 119, 70, 14);
		pnlPodaci.add(lblRezultat);

		txtIme = new JTextField();
		txtIme.setBounds(10, 36, 281, 20);
		pnlPodaci.add(txtIme);
		txtIme.setColumns(10);

		txtPrezime = new JTextField();
		txtPrezime.setBounds(10, 96, 281, 20);
		pnlPodaci.add(txtPrezime);
		txtPrezime.setColumns(10);

		txtRezultat = new JTextField();
		txtRezultat.setBounds(10, 144, 281, 20);
		pnlPodaci.add(txtRezultat);
		txtRezultat.setColumns(10);

		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
		            izraz = veza.prepareStatement("insert into penjac (ime,prezime,rezultat)" + "value (?,?,?)");
		            izraz.setString(1, txtIme.getText().substring(0, 1).toUpperCase()+txtIme.getText().substring(1).toLowerCase());
		            izraz.setString(2, txtPrezime.getText().substring(0, 1).toUpperCase()+txtPrezime.getText().substring(1).toLowerCase());
		            izraz.setString(3, txtRezultat.getText());

		            
		             if(StringUtils.isNullOrEmpty(txtRezultat.getText())) {
		                JOptionPane.showMessageDialog(getRootPane(), "Nije unesen rezultat.");
		                return;}
//		             if (!txtIme.getText().matches("[a-zA-Z_]+") || !txtPrezime.getText().matches("[a-zA-Z_]+")) {
//		                JOptionPane.showMessageDialog(getRootPane(), "Ime i prezime mogu sadržavati samo slova.");
//		                return;
//		}
		            if (izraz.executeUpdate()!= 0) {
		                ucitajIzBaze();
		                ocistiPolja();
		                
		            }
		            izraz.close();

		        } catch(StringIndexOutOfBoundsException str){
		            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci");
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		btnDodaj.setBounds(7, 186, 89, 23);
		pnlPodaci.add(btnDodaj);

		JButton btnPromjena = new JButton("Promjena");
		btnPromjena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Penjac p = lstPenjaci.getSelectedValue();
		        if (p == null) {
		            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi penjaèa.");
		            return;
		        }
		        try {

		            NamedParameterStatement izraz = new NamedParameterStatement(veza, "update penjac set ime=:ime, "
		                    + " prezime=:prezime, rezultat=:rezultat "
		                    + " where sifra=:sifra");

		            izraz.setString("ime", txtIme.getText().substring(0, 1).toUpperCase()+txtIme.getText().substring(1).toLowerCase());
		            izraz.setString("prezime", txtPrezime.getText().substring(0, 1).toUpperCase()+txtPrezime.getText().substring(1).toLowerCase());
		            izraz.setString("rezultat", txtRezultat.getText());
		            izraz.setInt("sifra", p.getSifra());
//		             if (!txtIme.getText().matches("[a-zA-Z_]+") || !txtPrezime.getText().matches("[a-zA-Z_]+")) {
//		                JOptionPane.showMessageDialog(getRootPane(), "Ime i prezime mogu sadržavati samo slova.");
//		                return;
//		}
		            if (izraz.izvedi() != 0)  {

		                ocistiPolja();
		                ucitajIzBaze();
		            }
		        }catch(StringIndexOutOfBoundsException str){
		            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci");
		        } 
		        catch (Exception ex) {
		            ex.printStackTrace();
		        }
			}
		});
		btnPromjena.setBounds(106, 186, 89, 23);
		pnlPodaci.add(btnPromjena);

		JButton btnObrisi = new JButton("Obri\u0161i");
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Penjac p = lstPenjaci.getSelectedValue();
		        if (p == null) {
		            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi penjaèa.");
		            return;
		        }

		        try {
		            izraz = veza.prepareStatement("delete from penjac where sifra=?");
		            izraz.setInt(1, p.getSifra());

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
		btnObrisi.setBounds(202, 186, 89, 23);
		pnlPodaci.add(btnObrisi);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 103, 210);
		getContentPane().add(scrollPane);

		
		lstPenjaci.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) {
					return;
				}

				Penjac p = lstPenjaci.getSelectedValue();
				if (p == null) {
					return;
				}
				ocistiPolja();

				txtIme.setText(p.getIme());
				txtPrezime.setText(p.getPrezime());
				txtRezultat.setText(p.getRezultat());

			}
		});
		scrollPane.setViewportView(lstPenjaci);
		
		

	}

	private JTextField txtIme;
	private JTextField txtPrezime;
	private JTextField txtRezultat;
	private javax.swing.JPanel pnlPodaci;
	private javax.swing.JList<Penjac> lstPenjaci;

	private void ocistiPolja() {

		for (Component c : pnlPodaci.getComponents()) {

			if (c instanceof JTextField) {
				((JTextField) c).setText("");
			}
		}
	}

	private void ucitajIzBaze() {
		try {
			izraz = veza.prepareStatement("select * from penjac");
			ResultSet rs = izraz.executeQuery();

			List<Penjac> lista = new ArrayList<>();
			Penjac p;
			while (rs.next()) {
				p = new Penjac();
				p.setSifra(rs.getInt("sifra"));
				p.setIme(rs.getString("ime"));
				p.setPrezime(rs.getString("prezime"));
				p.setRezultat(rs.getString("rezultat"));

				lista.add(p);
			}
			rs.close();
			izraz.close();

			Collections.sort(lista, new Comparator<Penjac>() {
				Collator col = Collator.getInstance(new Locale("hr", "HR"));

				public int compare(Penjac a1, Penjac a2) {
					return col.compare(a1.getPrezime(), a2.getPrezime());
				}
			});
			DefaultListModel<Penjac> m = new DefaultListModel<>();

			lista.forEach((penjac) -> m.addElement(penjac));
			lstPenjaci.setModel(m);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
