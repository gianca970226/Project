package GUI;

import LogicaJuego.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PanelPlayer extends JPanel {

    String Ruta_fondo;
    PhysicalBoardPlayer Tablero;
    int identificador = -1;

    public PanelPlayer(String Ruta_fondo, PhysicalBoardPlayer Tablero, int identificador) {
        this.Ruta_fondo = Ruta_fondo;
        this.Tablero = Tablero;
        this.identificador = identificador;
        setBackground(new Color(0f, 0f, 0f, 0f));
        addMouseListener(new ListenerPanelPlayer(this.Tablero,this));
    }

    public void Seleccionar(String Player) {
        if (Player.equals("Player_1")) {
            this.Ruta_fondo = "../Images/splayer_1.jpg";
        }
        if (Player.equals("Player_2")) {
            this.Ruta_fondo = "../Images/splayer_2.jpg";
        }
        repaint();
    }

    public int getIdentificador(){
        return this.identificador;
    }

    public String getRuta_fondo() {
        return Ruta_fondo;
    }

    public void setRuta_fondo(String Ruta_fondo) {
        this.Ruta_fondo = Ruta_fondo;
    }

    public void cambiarJugador(Ficha Player) {
        if (null == Player) {
            this.Ruta_fondo = "../Images/Desocupado.jpg";
        }
        else switch (Player) {
            case JUGADOR_1:
                this.Ruta_fondo = "../Images/player_1.jpg";
                break;
            case JUGADOR_2:
                this.Ruta_fondo = "../Images/player_2.jpg";
                break;
            default:
                this.Ruta_fondo = "../Images/Desocupado.jpg";
                break;
        }
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0f, 0f, 0f, 0f));
        ImageIcon Img = new ImageIcon(getClass().getResource(Ruta_fondo));
        g.drawImage(Img.getImage(), 0, 0, null);
    }
    
}
