
package graf;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import peer.P2PConnection;
import peer.Peer;
import common.FileInfo;
import common.PeerActionObserver;

import peer.Peer;


import common.FileInfo;
import common.PeerActionObserver;

/**
 *
 * @author Klaudu�
 */
public class Frame3 extends javax.swing.JFrame implements PeerActionObserver{

	/** Creates new form Frame3 */
	public Frame3() {
		initComponents();
		//  FileInfo fi = new FileInfo();
		///  name = fi.getName();
		// type = fi.getType();
	}

	DefaultTableModel tmcontekst = new DefaultTableModel(null, new String[]{"id", "nazwa", "typ", "rozmiar"} );
	List<Contekst> conteksty;
	ListSelectionModel lsmContekt;

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		dowload = new javax.swing.JButton();
		search = new javax.swing.JButton();
		namefile = new javax.swing.JTextField();
		exit = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		table = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		dowload.setText("DOWLOAD");
		dowload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dowloadActionPerformed(evt);
			}
		});

		search.setText("SEARCH");
		search.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchActionPerformed(evt);
			}
		});

		namefile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				namefileActionPerformed(evt);
			}
		});

		exit.setText("EXIT");
		exit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitActionPerformed(evt);
			}
		});

		jLabel1.setText("NAZWA PLIKU");

		jTable1.setModel(tmcontekst);
		table.setViewportView(jTable1);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(115, Short.MAX_VALUE))
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel1)
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(namefile, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(18, 18, 18)
														.addComponent(search))
														.addGroup(jPanel1Layout.createSequentialGroup()
																.addGap(472, 472, 472)
																.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																		.addComponent(exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(dowload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
																		.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(207, Short.MAX_VALUE)
						.addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGap(76, 76, 76)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(namefile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(search))
												.addGap(18, 18, 18)
												.addComponent(dowload)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(exit)
												.addContainerGap(291, Short.MAX_VALUE)))
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(119, Short.MAX_VALUE))
		);

		pack();
	}// </editor-fold>

	private void searchActionPerformed(java.awt.event.ActionEvent evt) {
		namefile1 =this.namefile.getText();
		
		s6= new Searching6(); 
		//	s6.activePeer = this.activePeer;
		s6.setVisible(true);
		//dispose();

		this.activePeer.addPeerActionObserver(this);
		this.activePeer.searchForFile(namefile1);


		
		//  activePeer.searchForFile(s);
		//miesjce w kt�rym b�d� �ci�ga� plik
		// getPeer().searchForFile(namefile1);


		//  activePeer.searchForFile(s);
		//miesjce w kt�rym b�d� �ci�ga� plik
		// getPeer().searchForFile(namefile1);



	}




	private void namefileActionPerformed(java.awt.event.ActionEvent evt) {

	}

	private void exitActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void dowloadActionPerformed(java.awt.event.ActionEvent evt) {

		r5 = new Receiving5();
		r5.setVisible(true);
		this.activePeer.addPeerActionObserver(this);
		this.activePeer.downloadFile(this.file);
	}

	private void uzupTabel(FileInfo file){
		if(file == null){
			JOptionPane.showMessageDialog(null, "Nie znaleziono pliku");
			}
		else{
			for (int i= 0; i<tmcontekst.getRowCount(); i++ )
			{
				tmcontekst.removeRow(i);
			}
			String[] linia = new String[]{1 + " ", file.name ,file.type, file.size + " "};
				tmcontekst.addRow(linia);				
			/*	tmcontekst.setValueAt(0, 1, 0);
				tmcontekst.setValueAt(file.name, 0, 1);
				tmcontekst.setValueAt(file.type, 0, 2);
				tmcontekst.setValueAt(file.size, 0, 3);
			*/
			}



		}



	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Frame3().setVisible(true);
			}
		});
	}

	private String namefile1;

	// Variables declaration - do not modify
	private javax.swing.JButton dowload;
	private javax.swing.JButton exit;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JTable jTable1;
	private javax.swing.JTextField namefile;
	private javax.swing.JButton search;
	private javax.swing.JScrollPane table;
	// End of variables declaration
	public Peer activePeer;






	@Override
	public void fileActionPerformed(FileInfo file, String actionType) {
		if(actionType.equalsIgnoreCase(PeerActionObserver.FILE_FOUND));
		{
			s6.dispose();
			uzupTabel(file);
			//f3.activePeer = this.activePeer;
			if(file != null)
				this.file = file;

			//f3.setVisible(true);
			//this.frame2.dispose();


		}

		if (actionType.equalsIgnoreCase(PeerActionObserver.FILE_DOWNLOADED  ))
		{
			//poinformuj uzytkownika
				
			r5.dispose();
			//this.r5 = new Receiving5();
			//r5.activePeer = this.activePeer;
			//r5.setVisible(true);
			//dispose();
			if(file == null)
				JOptionPane.showMessageDialog(null, "Nie udalo sie pobrac pliku");
			else
				JOptionPane.showMessageDialog(null, "Pobrano plik: ");
		}
	}



	@Override
	public void peerActionPerformed(String action) {
		// TODO Auto-generated method stub

	}

	//Peer activePeer;
	Searching6 s6;
	Receiving5 r5;
	private String name;
	private String type;
	FileInfo file;
}



