package GUI;

import LogicaJuego.Ficha;
import LogicaJuego.Juego;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerPanelPlayer implements MouseListener {

    PhysicalBoardPlayer tablero;
    PanelPlayer panelPlayer;

    public ListenerPanelPlayer(PhysicalBoardPlayer tablero, PanelPlayer panelPlayer) {
        this.tablero = tablero;
        this.panelPlayer = panelPlayer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            if (tablero.getPlayer().getJuego().isMiTurno()) {
                
                switch (tablero.getPlayer().getJuego().getFaseJuego()) {
                    case Juego.FASE_COLOCAR:
                        System.out.println("FASE COLOCAR:" +tablero.Player.getLocalName());
                        this.panelPlayer.Seleccionar(tablero.getPlayer().getLocalName());
                        tablero.capturarmovimiento(0, this.panelPlayer.getIdentificador(), 0, Juego.FASE_COLOCAR);
                        break;
                    case Juego.FASE_MOVIMIENTO:
                    case Juego.FASE_VUELO:
                        System.out.println("FASE MOVIMIENTO:" +tablero.Player.getLocalName());
                        if (tablero.getFrom() < 0) {
                            if (tablero.getPlayer().getLocalName().equals("Player_1")) {
                                if (tablero.getPlayer().getJuego().posicionTieneFichaDeJugador(this.panelPlayer.getIdentificador(), Ficha.JUGADOR_1)) {
                                    this.panelPlayer.Seleccionar(tablero.getPlayer().getLocalName());
                                    System.out.println("POSICION ORIGEN A HACER MOVIMIENTO:"+this.panelPlayer.getIdentificador());
                                    tablero.setFrom(this.panelPlayer.getIdentificador());
                                }
                            }
                        } else if (tablero.getFrom() > -1) {
                            if ((tablero.getFrom() != this.panelPlayer.getIdentificador() && tablero.getPlayer().getJuego().movimientoValido(tablero.getFrom(), this.panelPlayer.getIdentificador()))
                                   || (this.tablero.Player.getJuego().getTableroDelJuego().getNumeroFichasJugador(Ficha.JUGADOR_1) == Juego.NUMERO_MINIMO_FICHAS + 1 )) {
                                 System.out.println("POSICION DESTINO A HACER MOVIMIENTO:" + this.panelPlayer.getIdentificador());
                                tablero.capturarmovimiento(tablero.getFrom(), this.panelPlayer.getIdentificador(), 0, Juego.FASE_MOVIMIENTO);
                                System.out.println("ACTUALICE LA INTERFAZ GRAFICA");
                                tablero.ActualizarTablero();
                            } else {
                                System.out.println("MOVIMIENTO INVALIDO: " + tablero.getFrom() + " -> " + this.panelPlayer.getIdentificador());
                                tablero.setFrom(-1);
                                tablero.setTo(-1);
                                tablero.ActualizarTablero();
                            }
                        }   break;
                    case Juego.FASE_ELIMINAR:
                        System.out.println("FASE  ELIMINAR:" +tablero.Player.getLocalName());
                        System.out.println("VOY A ELIMINAR LA FICHA DE LA POSICION:"+this.panelPlayer.getIdentificador());
                        tablero.capturarmovimiento(0, 0, this.panelPlayer.getIdentificador(), Juego.FASE_ELIMINAR);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception ex) {
            System.out.println("NO ES MI TURNO!!");
        }
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

}
