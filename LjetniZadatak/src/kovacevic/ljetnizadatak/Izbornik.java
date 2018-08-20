package kovacevic.ljetnizadatak;


import java.awt.Color;
import java.awt.Desktop;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class Izbornik extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Izbornik() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		getContentPane().setBackground(Color.decode("#082F4E"));

		contentPane.setLayout(null);

		JPanel pnlIzbornik = new JPanel();
		pnlIzbornik.setBounds(10, 0, 479, 331);
		contentPane.add(pnlIzbornik);
		pnlIzbornik.setBackground(Color.decode("#082F4E"));
		pnlIzbornik.setLayout(null);

		JButton btnPenjalista = new JButton("Penjali\u0161ta");
		btnPenjalista.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnPenjalista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 Penjalista penjaliste = new Penjalista();
			        penjaliste.setVisible(true);
			}
		});
		btnPenjalista.setBounds(10, 11, 122, 23);
		pnlIzbornik.add(btnPenjalista);

		JButton btnAutori = new JButton("Autori");
		btnAutori.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnAutori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Autori autor = new Autori();
		        autor.setVisible(true);
			}
		});
		btnAutori.setBounds(10, 45, 122, 23);
		pnlIzbornik.add(btnAutori);

		JButton btnPenjaci = new JButton("Penja\u010Di");
		btnPenjaci.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnPenjaci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Penjaci osoba = new Penjaci();
		        osoba.setVisible(true);
			}
		});
		btnPenjaci.setBounds(10, 79, 122, 23);
		pnlIzbornik.add(btnPenjaci);

		JButton btnGit = new JButton("GitHub");
		btnGit.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnGit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URL(
							"https://github.com/jkovacevic992/Ljetni-zadatak-Eclipse-WindowBuilder")
									.toURI());
				} catch (URISyntaxException | IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnGit.setBounds(10, 113, 122, 23);
		pnlIzbornik.add(btnGit);

		JButton btnEra = new JButton("ERA dijagram");
		btnEra.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnEra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Slika s = new Slika();
				s.setVisible(true);
			}
		});
		btnEra.setBounds(10, 147, 122, 23);
		pnlIzbornik.add(btnEra);

		JButton btnZatvori = new JButton("Zatvori");
		btnZatvori.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnZatvori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnZatvori.setBounds(10, 181, 122, 23);
		pnlIzbornik.add(btnZatvori);
		
		JLabel lblSlika = new JLabel("New label");
		lblSlika.setBounds(161, 11, 283, 309);
		pnlIzbornik.add(lblSlika);
		lblSlika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kovacevic/ljetnizadatak/climber1.png")));
		
	
	}
}
