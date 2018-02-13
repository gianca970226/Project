/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import LogicaJuego.Ficha;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import LogicaJuego.*;

/**
 *
 * @author Ander
 */
public class Panel extends JPanel {

    String Ruta_fondo;
    
    
    public Panel(String Ruta_fondo) {
        this.Ruta_fondo = Ruta_fondo;
        setBackground(new Color(0f, 0f, 0f, 0f));
    }

    public String getRuta_fondo() {
        return Ruta_fondo;
    }

    public void setRuta_fondo(String Ruta_fondo) {
        this.Ruta_fondo = Ruta_fondo;
    }

    public void cambiarJugador(Ficha Player) {

        if (Player == Ficha.JUGADOR_1) {
            this.Ruta_fondo = "../Images/player_1.jpg";
        }
        else if (Player == Ficha.JUGADOR_2) {
            this.Ruta_fondo = "../Images/player_2.jpg";
        }
        else {

            this.Ruta_fondo = "../Images/Desocupado.jpg";
        }
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        ImageIcon Img = new ImageIcon(getClass().getResource(Ruta_fondo));
        g.drawImage(Img.getImage(), 0, 0, null);
    }

}
