package GUI;

import javax.swing.table.DefaultTableModel;

public class Tablero extends javax.swing.JFrame {

    private DefaultTableModel model;

    public Tablero() {
        initComponents();
        //creo la referencia al modelo de la tabla para manejar su contenido
        this.model = (DefaultTableModel) this.jTableMolino.getModel();
        //quito los titulos de la tabla
        this.jTableMolino.getTableHeader().setUI(null);
        //ubico el frame en el centro de la pantalla
        this.setLocationRelativeTo(null);
        //pondo al frame para que no le puedan modificar el tamaño
        this.setResizable(false);
        //pinta el tablero de forma como un molino
        pintarTablero();

    }

    private void pintarTablero() {
        Integer[][] valid
                = {
                    {1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1},
                    {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2},
                    {2, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1, 0, 2},
                    {2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2},
                    {2, 0, 2, 0, 1, 2, 1, 2, 1, 0, 2, 0, 2},
                    {2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 2, 0, 2},
                    {1, 2, 1, 2, 1, 0, 0, 0, 1, 2, 1, 2, 1},
                    {2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 2, 0, 2},
                    {2, 0, 2, 0, 1, 2, 1, 2, 1, 0, 2, 0, 2},
                    {2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2},
                    {2, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1, 0, 2},
                    {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2},
                    {1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1}
                };

        for (int i = 0; i < 13; i++) {
            this.jTableMolino.getColumnModel().getColumn(i).setCellRenderer(new CustomRenderer(valid));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMolino = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableMolino.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13"
            }
        ));
        jTableMolino.setRowHeight(40);
        jScrollPane1.setViewportView(jTableMolino);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tablero().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableMolino;
    // End of variables declaration//GEN-END:variables
}
