package GUI;

import Agentes.*;

import LogicaJuego.*;
import jade.gui.GuiEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class PhysicalBoardPlayer extends javax.swing.JFrame {

    Agent_Player Player;
    PanelPlayer[] boardPositions;
    Agent_Minimax PlayerM;
    PanelPlayer panel;
    int from = -1;
    int to = -1;
    int faseAnterior;

    JTextField consola2;

    public PhysicalBoardPlayer() {
        initComponents();
        faseAnterior = 1;

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        IniciarTablero();
        setSize(423, 500);
        consola2= new JTextField("");
        consola2.setSize(423, 50);
        consola2.setLocation(0,423);
        consola2.setBackground(Color.BLACK);
        consola2.setForeground(Color.WHITE);
        consola2.setEditable(false);
        add(consola2);


    }

    public Agent_Minimax getPlayerM() {
        return PlayerM;
    }

    public void setPlayerM(Agent_Minimax PlayerM) {
        this.PlayerM = PlayerM;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void repintar() {
        for (int i = 0; i < boardPositions.length && boardPositions[i] != null; i++) {
            boardPositions[i].repaint();
        }
    }

    public void ActualizarTablero() throws JuegoException {
        for (int i = 0; i < boardPositions.length; i++) {
            boardPositions[i].cambiarJugador(Player.getJuego().getTableroDelJuego().getPosicionesDelTablero()[i].getJugadorOcupandola());
        }
        repintar();
    }

    public void ActualizarTableroMinimax() throws JuegoException {
        for (int i = 0; i < boardPositions.length; i++) {
            boardPositions[i].cambiarJugador(PlayerM.getJuego().getTableroDelJuego().getPosicionesDelTablero()[i].getJugadorOcupandola());
        }
        repintar();
    }

    public void IniciarTablero() {
        int[][] Valid
                = {
                    {1, 0, 0, 1, 0, 0, 1},
                    {0, 1, 0, 1, 0, 1, 0},
                    {0, 0, 1, 1, 1, 0, 0},
                    {1, 1, 1, 0, 1, 1, 1},
                    {0, 0, 1, 1, 1, 0, 0},
                    {0, 1, 0, 1, 0, 1, 0},
                    {1, 0, 0, 1, 0, 0, 1}};
        int x = 0;
        int y = 0;
        int pos = 0;
        boardPositions = new PanelPlayer[24];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (Valid[i][j] == 1) {
                    boardPositions[pos] = new PanelPlayer("../Images/Desocupado.jpg", this, pos);
                    boardPositions[pos].setSize(30, 30);
                    boardPositions[pos].setLocation(x, y);
                    this.add(boardPositions[pos]);
                    boardPositions[pos].repaint();
                    pos++;

                }
                x += 65;
            }
            x = 0;
            y += 65;

        }
        panel = new PanelPlayer("../Images/tablero.png", this, 99);
        panel.setSize(423, 430);
        panel.setLocation(0, 0);
        this.add(panel);
        panel.setVisible(true);

    }

    public void capturarmovimiento(int src, int dest, int remove, int type) throws JuegoException {
        switch (type) {
            case Juego.FASE_COLOCAR:
                boolean movimientoValido = true;
                if (Player.getLocalName().equals("Player_1")) {
                    movimientoValido = Player.getJuego().ponerFichaJugador(dest, Ficha.JUGADOR_1);
                    if (movimientoValido) {
                        if (Player.getJuego().hizoUnMolino(dest, Ficha.JUGADOR_1)) {
                             consola2.setText("");
                            consola2.setText("HIZO MOLINO!!, ELIMINA UNA FICHA ENEMIGA");
                            this.faseAnterior = Player.getJuego().getFaseActualDelJuego();
                            Player.getJuego().setFaseJuego(Juego.FASE_ELIMINAR);
                        } else {
                            System.out.println("ENVIANDO JUEGO AL OPONENTE!!");
                            GuiEvent a = new GuiEvent(this, 1);
                            Player.Event(a);
                            Player.getJuego().setMiTurno(false);
                        }
                    } else {
                        consola2.setText("");
                        consola2.setText("MOVIMIENTO INVALIDO!!");
                    }
                }
                break;
            case Juego.FASE_MOVIMIENTO:
            case Juego.FASE_VUELO:
                if (Player.getLocalName().equals("Player_1")) {
                    int esValido = -1;
                    esValido = Player.getJuego().moverFichaDe(src, dest, Ficha.JUGADOR_1);
                    if (esValido == 0) {
                        if (Player.getJuego().hizoUnMolino(dest, Ficha.JUGADOR_1)) {
                            
                            consola2.setText("");
                            consola2.setText("HIZO MOLINO!!, ELIMINA UNA FICHA ENEMIGA");
                            this.faseAnterior = Player.getJuego().getFaseActualDelJuego();
                            Player.getJuego().setFaseJuego(Juego.FASE_ELIMINAR);
                        } else {
                            GuiEvent a = new GuiEvent(this, 1);
                            Player.Event(a);
                            Player.getJuego().setMiTurno(false);
                            this.from = -1;
                            this.to = -1;
                        }
                    } else {
                        consola2.setText("");
                        consola2.setText("MOVIMIENTO INVALIDO!!");
                        this.from = -1;
                        this.to = -1;
                    }
                }
                break;
            case Juego.FASE_ELIMINAR:
                System.out.println("FASE ELIMINAR");
                boolean eliminoFicha = true;
                if (Player.getLocalName().equals("Player_1")) {
                    eliminoFicha = Player.getJuego().quitarFicha(remove, Ficha.JUGADOR_2);
                }
                if (eliminoFicha) {
                    consola2.setText("");
                    consola2.setText("SE ELIMINO LA FICHA:" + remove);
                    GuiEvent a = new GuiEvent(this, 1);
                    Player.getJuego().setFaseJuego(this.faseAnterior);
                    Player.Event(a);
                    Player.getJuego().setMiTurno(false);
                } else {
                    consola2.setText("");
                    consola2.setText("ELIMINE OTRA FICHA!!");
                }
                break;
            default:
                break;
        }
        Player.getJuego().imprimirTableroDeJuego();
        ActualizarTablero();
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public Agent_Player getPlayer() {
        return Player;
    }

    public void setPlayer(Agent_Player Player) {
        this.Player = Player;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(PhysicalBoardPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PhysicalBoardPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PhysicalBoardPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PhysicalBoardPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new PhysicalBoardPlayer().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
