/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import LogicaJuego.JugadorC;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Ander
 */
public class Panel extends JPanel {

    String fondo;
    
    
    public Panel(String fondo) {
        this.fondo = fondo;
        setBackground(new Color(0f, 0f, 0f, 0f));
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public void cambiarJugador(JugadorC Player) {

        if (null == Player) {
            
            this.fondo = "../Images/Libre.jpg";
        }
        else switch (Player) {
            case AGENTE1:
                this.fondo = "../Images/Jugador1.jpg";
                break;
            case AGENTE2:
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
        ImageIcon Img = new ImageIcon(getClass().getResource(fondo));
        g.drawImage(Img.getImage(), 0, 0, null);
    }
}
