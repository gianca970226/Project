package GUI;

import LogicaJuego.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PanelJugador extends JPanel {

    String fondo;
    frmJugador frmJugador;
    int id = -1;

    public PanelJugador(String fondo, frmJugador frmJugador, int id) {
        this.fondo = fondo;
        this.frmJugador = frmJugador;
        this.id = id;
        setBackground(new Color(0f, 0f, 0f, 0f));
       // addMouseListener(new ListenerPanelPlayer(this.frmJugador,this));
    }

 

    public int getId(){
        return this.id;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public void cambiarJugador(Ficha Player) {
        if (null == Player) {
            this.fondo = "../Images/Libre.jpg";
        }
        else switch (Player) {
            case JUGADOR_1:
                this.fondo = "../Images/Jugador1.jpg";
                break;
            case JUGADOR_2:
                this.fondo = "../Images/Jugador2.jpg";
                break;
            default:
                this.fondo = "../Images/Libre.jpg";
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0f, 0f, 0f, 0f));
        ImageIcon Img = new ImageIcon(getClass().getResource(fondo));
        g.drawImage(Img.getImage(), 0, 0, null);
    }
    
}
