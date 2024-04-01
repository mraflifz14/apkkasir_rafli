/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplikasikasir_rafli;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.proteanit.sql.DbUtils;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author user
 */
public class FormPenjualan extends javax.swing.JFrame {
Connection koneksi;
PreparedStatement pst, pst2;
ResultSet rst;
int inputstok, inputstok2, inputharga, inputjumlah, kurangistok, tambahstok;
String harga, idproduk, idprodukpenjualan, iddetail, jam, tanggal, sub_total;
    /**
     * Creates new form FormPenjualan
     */
    public FormPenjualan() {
        initComponents();
        koneksi = Koneksi.koneksiDB();
        this.setExtendedState(MAXIMIZED_BOTH);
        mulai();
        tampilWaktu();
        detail();
        autonumber();
        sum();
    }

    
    public void mulai() {
        txtjam.setEnabled(false);
        txttanggal.setEnabled(false);
        txtidpenjualan.setEnabled(false);
        txttotal.setEnabled(false);
        txtkembalian.setEnabled(false);
    }
    
    
    public void tampilWaktu() {
        Thread clock = new Thread() {
            public void run() {
                for(;;) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat Jam = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat Tanggal = new SimpleDateFormat("yyyy-MM-dd");
                    txtjam.setText(Jam.format(cal.getTime()));
                    txttanggal.setText(Tanggal.format(cal.getTime()));
               try { sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FormPenjualan.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
            }
        };
        clock.start();
    }
    
    
    private void simpan() {
        String jam = txtjam.getText();
        String tgl = txttanggal.getText();
        try {
            String sql="insert into penjualan (PenjualanID,DetailID,TanggalPenjualan,JamPenjualan,TotalHarga) value (?,?,?,?,?)";
            pst=koneksi.prepareStatement(sql);
            pst.setString(1, txtidpenjualan.getText());
            pst.setString(2, iddetail);
            pst.setString(3, tgl);
            pst.setString(4, jam);
            pst.setString(5, txttotal.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Tersimpan");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            }
    }
    
    
    
    private void total() {
        int total, bayar, kembali;
        total = Integer.parseInt(txtbayar.getText());
        bayar = Integer.parseInt(txttotal.getText());
        kembali = total - bayar;
        String Subtotal = String.valueOf(kembali);
        txtkembalian.setText(Subtotal);
    }
    
    public void clear() {
        txtjumlah.setText("");
        
    }
    
    public void cari() {
        try {
        String sql="select * from produk where ProdukID LIKE '%"+txtidproduk.getText()+"%'";
        pst=koneksi.prepareStatement(sql);
        rst=pst.executeQuery();
        tblproduk.setModel(DbUtils.resultSetToTableModel(rst));
       } catch (Exception e){ JOptionPane.showMessageDialog(null, e);} 
    }
    
    public void kurangiStok() {
       int qty;
    qty=Integer.parseInt(txtjumlah.getText());
    kurangistok=inputstok-qty;
    }
    
    private void subTotal() {
        int jumlah, sub;
         jumlah= Integer.parseInt(txtjumlah.getText());
         sub=(jumlah*inputharga);
         sub_total=String.valueOf(sub);     
    }
    
    public void tambahStok() {
        tambahstok=inputjumlah+inputstok2;
        try {
        String update="update produk set Stok='"+tambahstok+"' where ProdukID='"+idproduk+"'";
        pst2=koneksi.prepareStatement(update);
        pst2.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);}
    }
    
    public void ambilStok() {
        try {
    String sql="select * from produk where ProdukID='"+idproduk+"'";
    pst=koneksi.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {    
    String stok=rst.getString(("Stok"));
    inputstok2= Integer.parseInt(stok);
    }
    }catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);}
    }
    
    public void sum() {
        int totalBiaya = 0;
        int subtotal;
        DefaultTableModel dataModel = (DefaultTableModel) tblpenjualan.getModel();
        int jumlah = tblpenjualan.getRowCount();
        for (int i=0; i<jumlah; i++){
        subtotal = Integer.parseInt(dataModel.getValueAt(i, 4).toString());
        totalBiaya += subtotal;
        }
        txttotal.setText(String.valueOf(totalBiaya));
    }
    
    
    public void autonumber() {
        try{
        String sql = "SELECT MAX(RIGHT(PenjualanID,3)) AS NO FROM penjualan";
        pst=koneksi.prepareStatement(sql);
        rst=pst.executeQuery();
        while (rst.next()) {
                if (rst.first() == false) {
                    txtidpenjualan.setText("IDP001");
                } else {
                    rst.last();
                    int auto_id = rst.getInt(1) + 1;
                    String no = String.valueOf(auto_id);
                    int NomorJual = no.length();
                    for (int j = 0; j < 3 - NomorJual; j++) {
                        no = "0" + no;
                    }
                    txtidpenjualan.setText("IDP" + no);
                }
            }
        rst.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);}
    }
    
    public void detail() {
        try {
        String Kode_detail=txtidpenjualan.getText();
        String KD="D"+Kode_detail;
        String sql="select * from detailpenjualan where DetailID='"+KD+"'";
        pst=koneksi.prepareStatement(sql);
        rst=pst.executeQuery();
        tblpenjualan.setModel(DbUtils.resultSetToTableModel(rst));
       } catch (Exception e){ 
           JOptionPane.showMessageDialog(null, e);} 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txttanggal = new javax.swing.JTextField();
        txtjam = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblproduk = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtjumlah = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtbayar = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnbayar = new javax.swing.JButton();
        btnkeluar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblpenjualan = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnhapus = new javax.swing.JButton();
        txtidproduk = new javax.swing.JTextField();
        btncari = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtidpenjualan = new javax.swing.JTextField();
        btntambah = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Transaksi Penjualan");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(36, 26, 175, 22);
        jPanel1.add(txttanggal);
        txttanggal.setBounds(419, 23, 85, 31);
        jPanel1.add(txtjam);
        txtjam.setBounds(537, 23, 76, 31);

        tblproduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblproduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblprodukMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblproduk);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(35, 166, 326, 109);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Jumlah");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(431, 181, 50, 17);
        jPanel1.add(txtjumlah);
        txtjumlah.setBounds(431, 221, 64, 22);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Total");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(35, 539, 34, 17);

        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });
        jPanel1.add(txttotal);
        txttotal.setBounds(129, 532, 197, 30);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Bayar");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(35, 587, 39, 17);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Kembalian");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(35, 634, 73, 17);
        jPanel1.add(txtbayar);
        txtbayar.setBounds(129, 581, 197, 29);

        txtkembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkembalianActionPerformed(evt);
            }
        });
        jPanel1.add(txtkembalian);
        txtkembalian.setBounds(129, 628, 197, 28);

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        btnbayar.setBackground(new java.awt.Color(0, 204, 204));
        btnbayar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnbayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-buy-20.png"))); // NOI18N
        btnbayar.setText("Bayar");
        btnbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbayarActionPerformed(evt);
            }
        });

        btnkeluar.setBackground(new java.awt.Color(0, 204, 204));
        btnkeluar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnkeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-exit-20.png"))); // NOI18N
        btnkeluar.setText("Keluar");
        btnkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeluarActionPerformed(evt);
            }
        });

        tblpenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "kode_detail", "kode_produk", "harga", "jumlah", "diskon", "subtotal"
            }
        ));
        tblpenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblpenjualanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblpenjualan);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Data Produk");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Masukan ID Produk");

        btnhapus.setBackground(new java.awt.Color(0, 204, 204));
        btnhapus.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnhapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-delete-20.png"))); // NOI18N
        btnhapus.setText("Hapus");
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        btncari.setBackground(new java.awt.Color(0, 204, 204));
        btncari.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btncari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-search-20.png"))); // NOI18N
        btncari.setText("Cari");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("ID Penjualan");

        btntambah.setBackground(new java.awt.Color(0, 204, 204));
        btntambah.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btntambah.setText("+");
        btntambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtidproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(50, 50, 50)
                                        .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtidpenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel6))))
                        .addGap(0, 104, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnkeluar))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtidpenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(116, 116, 116)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(10, 60, 640, 620);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 640, 730);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void txtkembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkembalianActionPerformed

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
subTotal();
kurangiStok();  
    try {
            String Kode_detail=txtidpenjualan.getText();
            iddetail="D"+Kode_detail;
            String sql="insert into detailpenjualan (DetailID,ProdukID,Harga,JumlahProduk,Subtotal) value (?,?,?,?,?)";
            String update="update produk set Stok='"+kurangistok+"' where ProdukID='"+idproduk+"'";
            pst=koneksi.prepareStatement(sql);
            pst2=koneksi.prepareStatement(update);
            pst.setString(1, iddetail);
            pst.setString(2, idproduk);
            pst.setString(3, harga);
            pst.setString(4, txtjumlah.getText());
            pst.setString(5, sub_total);
            pst.execute();
            pst2.execute();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            }
        detail();
        sum();
        cari();
        clear();        // TODO add your handling code here:
    }//GEN-LAST:event_btntambahActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
try {
            String sql="delete from detailpenjualan where ProdukID=?";
            pst=koneksi.prepareStatement(sql);
            pst.setString(1, idprodukpenjualan);
            pst.execute();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        detail();
        sum();
        tambahstok();
        cari();        // TODO add your handling code here:
    }//GEN-LAST:event_btnhapusActionPerformed

    private void btnbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbayarActionPerformed
total();
     simpan();
     autonumber();
     detail();
     txttotal.setText("");
     txtbayar.setText("");
     txtkembalian.setText("");
     txtidproduk.setText("");
     cari();        // TODO add your handling code here:
    }//GEN-LAST:event_btnbayarActionPerformed

    private void btnkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeluarActionPerformed
this.dispose();    // TODO add your handling code here:
    }//GEN-LAST:event_btnkeluarActionPerformed

    private void tblprodukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblprodukMouseClicked
try {
    int row=tblproduk.getSelectedRow();
    String tabel_klik=(tblproduk.getModel().getValueAt(row, 0).toString());
    String sql="select * from produk where ProdukID='"+tabel_klik+"'";
    pst=koneksi.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {
    idproduk=rst.getString(("ProdukID"));    
    String stok=rst.getString(("Stok"));
    inputstok= Integer.parseInt(stok);
    harga=rst.getString(("Harga"));
    inputharga= Integer.parseInt(harga);
    }
}catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);
}        // TODO add your handling code here:
    }//GEN-LAST:event_tblprodukMouseClicked

    private void tblpenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblpenjualanMouseClicked
try {
    int row=tblpenjualan.getSelectedRow();
    idprodukpenjualan=(tblpenjualan.getModel().getValueAt(row, 1).toString());
    String sql="select * from detailpenjualan where ProdukID='"+idprodukpenjualan+"'";
    pst=koneksi.prepareStatement(sql);
    rst=pst.executeQuery();
    if (rst.next()) {   
    String jumlah=rst.getString(("JumlahProduk"));
    inputjumlah= Integer.parseInt(jumlah);
    }
}catch (Exception e) {
    JOptionPane.showMessageDialog(null, e);
}
ambilstok();        // TODO add your handling code here:
    }//GEN-LAST:event_tblpenjualanMouseClicked

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
cari();      // TODO add your handling code here:
    }//GEN-LAST:event_btncariActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPenjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbayar;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btnhapus;
    private javax.swing.JButton btnkeluar;
    private javax.swing.JButton btntambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblpenjualan;
    private javax.swing.JTable tblproduk;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txtidpenjualan;
    private javax.swing.JTextField txtidproduk;
    private javax.swing.JTextField txtjam;
    private javax.swing.JTextField txtjumlah;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txttanggal;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables

    private void tambahstok() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void ambilstok() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
