package Agentes;

import jade.core.behaviours.SimpleBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import GUI.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import LogicaJuego.*;
import jade.lang.acl.UnreadableException;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.swing.JOptionPane;

public class AgenteGui extends GuiAgent {

    frmJugador frmJugador;
    ReglasMolino Juego;
    CompEnvAct ComportamientoAgente;
    boolean turnoAsginado = true;

    @Override
    protected void setup() {
        this.Juego = new ReglasMolino();
        crearTablero(this);
        this.ComportamientoAgente = new CompEnvAct(this);
        this.addBehaviour(ComportamientoAgente);
    }

    public void Event(GuiEvent g) {
        onGuiEvent(g);
    }

    private void crearTablero(AgenteGui agente) {
        java.awt.EventQueue.invokeLater(() -> {
            frmJugador = new frmJugador();
            frmJugador.setJugador(agente);
            frmJugador.setTitle(getLocalName());
            frmJugador.setVisible(true);
        });
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        if (ge.getType() == 1) {
            ComportamientoAgente.Enviar(Juego);
        }
    }

    public ReglasMolino getJuego() {
        return Juego;
    }

    public void setJuego(ReglasMolino Juego) {

        this.Juego = Juego;
    }

    public frmJugador getFrmJugador() {
        return frmJugador;
    }

    public void setFrmJugador(frmJugador Tablero) {
        this.frmJugador = Tablero;
    }

    public boolean isTurnoAsginado() {
        return turnoAsginado;
    }

    public void setTurnoAsginado(boolean turnoAsginado) {
        this.turnoAsginado = turnoAsginado;
    }

}

class CompEnvAct extends SimpleBehaviour {

    boolean perdio = false;
    AgenteGui Jugador;

    public CompEnvAct(AgenteGui Player) {
        this.Jugador = Player;
    }

    void Enviar(ReglasMolino Juego) {
        try {
            AID idReceptor = new AID();
            if (Jugador.getLocalName().equals("Jugador1")) {
                idReceptor.setLocalName("Jugador2");
            } else {
                idReceptor.setLocalName("Jugador1");
            }
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(idReceptor);
            msg.setContentObject(Juego);
            getAgent().send(msg);

        } catch (IOException e) {
        }
    }

    @Override
    public void action() {
        try {
            ACLMessage mensaje = getAgent().blockingReceive();
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (mensaje != null) {

                ReglasMolino Object = (ReglasMolino) mensaje.getContentObject();

               System.out.println("Turno eviado de:" + mensaje.getSender().getLocalName());
                Object.getTableroDelJuego().imprimirTablero();
                AID idReceptor = new AID();

               // System.out.println("JUEGO TERMINADO?:" + Object.finalizoElJuego());

                Jugador.setJuego(Object);
                Jugador.getFrmJugador().ActualizarTablero();
                if (!Jugador.Juego.finalizoElJuego()) {
                    System.out.println("Turno:" + Jugador.getLocalName());
                } else {
                    JOptionPane.showMessageDialog(null, "Juego terminado");
                    int jugador = Jugador.Juego.quienGano();
                    switch (jugador) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "Juego Terminado quedaron empate");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "Juego Terminado gano jugador 1");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Juego Terminado gano jugador 2");
                            break;
                    }
                    perdio = true;
                    Enviar(Jugador.Juego);
                    
                }

            }
        } catch (UnreadableException | HeadlessException e) {
        }
    }

    @Override
    public boolean done() {
        return perdio;
    }

}
