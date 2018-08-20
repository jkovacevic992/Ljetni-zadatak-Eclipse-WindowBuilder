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
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.mysql.cj.util.StringUtils;

import javax.swing.event.ListSelectionEvent;
import java.awt.Font;

public class Penjalista extends JFrame {

	private JPanel contentPane;
	private Connection veza;
	private PreparedStatement izraz;
	private JTextField txtNaziv;
	private JTextField txtLon;
	private JTextField txtLat;
	private javax.swing.JPanel pnlPodaci;
	private javax.swing.JList<Penjaliste> lstPenjalista;

	public Penjalista() {
		initComponents();
		getContentPane().setBackground(Color.decode("#082F4E"));
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
		lstPenjalista = new javax.swing.JList<>();
		lstPenjalista.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}

				Penjaliste p = lstPenjalista.getSelectedValue();
				if (p == null) {
					return;
				}
				ocistiPolja();

				txtNaziv.setText(p.getNaziv());
				txtLat.setText(String.valueOf(p.getLat()));
				txtLon.setText(String.valueOf(p.getLon()));
			}
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 529, 318);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		getContentPane().setLayout(null);
		contentPane.setLayout(null);

		JPanel pnlPodaci = new JPanel();
		pnlPodaci.setBounds(152, 11, 351, 239);
		getContentPane().add(pnlPodaci);
		pnlPodaci.setBackground(Color.decode("#082F4E"));
		pnlPodaci.setLayout(null);

		JLabel lblNaziv = new JLabel("Naziv");
		lblNaziv.setBounds(10, 11, 46, 14);
		lblNaziv.setFont(new Font("Poppins", Font.PLAIN, 12));
		lblNaziv.setForeground(Color.WHITE);
		pnlPodaci.add(lblNaziv);

		txtNaziv = new JTextField();
		txtNaziv.setBounds(10, 36, 331, 20);
		pnlPodaci.add(txtNaziv);
		txtNaziv.setColumns(10);

		JLabel lblLon = new JLabel("Geografska du\u017Eina");
		lblLon.setBounds(10, 67, 160, 14);
		lblLon.setFont(new Font("Poppins", Font.PLAIN, 12));
		lblLon.setForeground(Color.WHITE);
		pnlPodaci.add(lblLon);

		txtLon = new JTextField();
		txtLon.setBounds(10, 92, 331, 20);
		pnlPodaci.add(txtLon);
		txtLon.setColumns(10);

		JLabel lblLat = new JLabel("Geografska \u0161irina");
		lblLat.setBounds(10, 123, 172, 14);
		lblLat.setFont(new Font("Poppins", Font.PLAIN, 12));
		lblLat.setForeground(Color.WHITE);
		pnlPodaci.add(lblLat);

		txtLat = new JTextField();
		txtLat.setBounds(10, 148, 331, 20);
		pnlPodaci.add(txtLat);
		txtLat.setColumns(10);

		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.setBounds(7, 194, 102, 23);
		btnDodaj.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
			            izraz = veza.prepareStatement("insert into penjaliste (naziv,lat,lon)" + "value (?,?,?)");
			            izraz.setString(1, txtNaziv.getText().substring(0, 1).toUpperCase()+txtNaziv.getText().substring(1).toLowerCase());
			            izraz.setString(2, txtLat.getText());
			            izraz.setString(3, txtLon.getText());
//			            if (!txtNaziv.getText().matches("[a-zA-Z_]+")) {
//			                JOptionPane.showMessageDialog(getRootPane(), "Naziv može sadržavati samo slova.");
//			                return;
//			}

			            if (StringUtils.isNullOrEmpty(txtLat.getText())) {
			                JOptionPane.showMessageDialog(getRootPane(), "Nije unesena geografska širina.");
			                return;
			            } else if (StringUtils.isNullOrEmpty(txtLon.getText())) {
			                JOptionPane.showMessageDialog(getRootPane(), "Nije unesena geografska dužina.");
			                return;
			            }
			            if (izraz.executeUpdate() != 0) {
			                ucitajIzBaze();
			                ocistiPolja();

			            }
			            izraz.close();

			        } catch(StringIndexOutOfBoundsException str){
			            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci");
			        } catch (MysqlDataTruncation er) {
			            JOptionPane.showMessageDialog(getRootPane(), "Geografska širina i/ili dužina pogrešno unesena.");
			        } catch (SQLException ex) {
			            ex.printStackTrace();
			        }
			}
		});
		pnlPodaci.add(btnDodaj);

		JButton btnPromjena = new JButton("Promjena");
		btnPromjena.setBounds(119, 194, 106, 23);
		btnPromjena.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnPromjena.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Penjaliste p = lstPenjalista.getSelectedValue();
		        if (p == null) {
		            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi penjalište.");
		            return;
		        }
		        try {

		            NamedParameterStatement izraz = new NamedParameterStatement(veza, "update penjaliste set naziv=:naziv, "
		                    + " lat=:lat, lon=:lon "
		                    + " where sifra=:sifra");

		            izraz.setString("naziv", txtNaziv.getText().substring(0, 1).toUpperCase()+txtNaziv.getText().substring(1).toLowerCase());
		            izraz.setString("lat", txtLat.getText());
		            izraz.setString("lon", txtLon.getText());
		            izraz.setInt("sifra", p.getSifra());
//		            if (!txtNaziv.getText().matches("[a-zA-Z_]+")) {
//		                JOptionPane.showMessageDialog(getRootPane(), "Naziv može sadržavati samo slova.");
//		                return;
//		}
		             if (StringUtils.isNullOrEmpty(txtLat.getText())) {
		                JOptionPane.showMessageDialog(getRootPane(), "Nije unesena geografska širina.");
		                return;
		            } else if (StringUtils.isNullOrEmpty(txtLon.getText())) {
		                JOptionPane.showMessageDialog(getRootPane(), "Nije unesena geografska dužina.");
		                return;
		            }
		            if (izraz.izvedi() != 0) {
		                ucitajIzBaze();
		                ocistiPolja();

		            }
		        } catch(StringIndexOutOfBoundsException str){
		            JOptionPane.showMessageDialog(getRootPane(), "Nisu upisani svi potrebni podaci");
		        } catch (MysqlDataTruncation er) {
		            JOptionPane.showMessageDialog(getRootPane(), "Geografska širina i/ili dužina pogrešno unesena.");
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
				
			}
		});
		pnlPodaci.add(btnPromjena);

		JButton btnObrisi = new JButton("Obri\u0161i");
		btnObrisi.setBounds(235, 194, 106, 23);
		btnObrisi.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Penjaliste p = lstPenjalista.getSelectedValue();
		        if (p == null) {
		            JOptionPane.showMessageDialog(getRootPane(), "Prvo odaberi penjalište.");
		            return;
		        }

		        try {
		            izraz = veza.prepareStatement("delete from penjaliste where sifra=?");
		            izraz.setInt(1, p.getSifra());

		            if (izraz.executeUpdate() == 0) {
		                JOptionPane.showMessageDialog(getRootPane(), "Nije obrisan nijedan red. ");
		            } else {
		                ucitajIzBaze();
		                ocistiPolja();
		            }
		            izraz.close();

		        } catch (SQLIntegrityConstraintViolationException er) {
		            JOptionPane.showMessageDialog(getRootPane(), "Nemoguæe obrisati.");
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		pnlPodaci.add(btnObrisi);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 121, 226);
		getContentPane().add(scrollPane);

		scrollPane.setViewportView(lstPenjalista);

	}

	private void ocistiPolja() {

		for (Component c : pnlPodaci.getComponents()) {

			if (c instanceof JTextField) {
				((JTextField) c).setText("");
			}
		}
	}

	private void ucitajIzBaze() {
		try {
			izraz = veza.prepareStatement("select * from penjaliste");
			ResultSet rs = izraz.executeQuery();

			List<Penjaliste> lista = new ArrayList<>();
			Penjaliste p;
			while (rs.next()) {
				p = new Penjaliste();
				p.setSifra(rs.getInt("sifra"));
				p.setNaziv(rs.getString("naziv"));
				p.setLon(rs.getDouble("lon"));
				p.setLat(rs.getDouble("lat"));

				lista.add(p);
			}
			rs.close();
			izraz.close();

			Collections.sort(lista, new Comparator<Penjaliste>() {
				Collator col = Collator.getInstance(new Locale("hr", "HR"));

				public int compare(Penjaliste p1, Penjaliste p2) {
					return col.compare(p1.getNaziv(), p2.getNaziv());
				}
			});
			DefaultListModel<Penjaliste> m = new DefaultListModel<>();

			lista.forEach((penjaliste) -> m.addElement(penjaliste));
			lstPenjalista.setModel(m);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
