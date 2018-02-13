package Agentes;

import jade.core.behaviours.SimpleBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import GUI.*;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import LogicaJuego.*;
import javax.swing.JOptionPane;

public class Agent_Player extends GuiAgent {

    PhysicalBoardPlayer tablero;
    Juego Juego;
    CompEnvAct Mensajero;
    boolean Esmiturno = true;

    @Override
    protected void setup() {
        this.Juego = new Juego();
        iniciarTablero(this);
        this.Mensajero = new CompEnvAct(this);
        this.addBehaviour(Mensajero);
    }

    public void Event(GuiEvent g) {
        onGuiEvent(g);
    }

    private void iniciarTablero(Agent_Player agente) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tablero = new PhysicalBoardPlayer();
                tablero.setPlayer(agente);
                tablero.setTitle(getLocalName());
                tablero.setVisible(true);
            }
        });
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        if (ge.getType() == 1) {
            Mensajero.Enviar(Juego);
        }
    }

    public Juego getJuego() {
        return Juego;
    }

    public void setJuego(Juego Juego) {

        this.Juego = Juego;
    }

    public PhysicalBoardPlayer getTablero() {
        return tablero;
    }

    public void setTablero(PhysicalBoardPlayer Tablero) {
        this.tablero = Tablero;
    }

    public boolean isEsmiturno() {
        return Esmiturno;
    }

    public void setEsmiturno(boolean Esmiturno) {
        this.Esmiturno = Esmiturno;
    }

}

class CompEnvAct extends SimpleBehaviour {

    boolean isGameOver = false;
    Agent_Player Player;

    public CompEnvAct(Agent_Player Player) {
        this.Player = Player;
    }

    void Enviar(Juego Object) {
        try {
            AID idReceptor = new AID();
            if (Player.getLocalName().equals("Player_1")) {
                idReceptor.setLocalName("Player_2");
            } else {
                idReceptor.setLocalName("Player_1");
            }
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(idReceptor);
            msg.setContentObject(Object);
            getAgent().send(msg);

        } catch (Exception e) {
        }
    }

    @Override
    public void action() {
        try {
            ACLMessage mensaje = getAgent().blockingReceive();
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (mensaje != null) {

                Juego Object = (Juego) mensaje.getContentObject();

                System.out.println("RECIBI TURNO DE:" + mensaje.getSender().getLocalName());
                Object.getTableroDelJuego().imprimirTablero();
                AID idReceptor = new AID();

                System.out.println("JUEGO TERMINADO?:" + Object.finalizoElJuego());

                Player.setJuego(Object);
                Player.getTablero().ActualizarTablero();
                if (!Player.Juego.finalizoElJuego()) {
                    System.out.println("TURNO:" + Player.getLocalName());
                } else {
                    JOptionPane.showMessageDialog(null, "EL JUEGO FINALIZO!!");
                    int jugador = Player.Juego.quienGano();
                    if (jugador == 0) {
                        JOptionPane.showMessageDialog(null, "JUEGO TERMINO!!, ES UN EMPATE");
                    } else if (jugador == 1) {
                        JOptionPane.showMessageDialog(null, "JUEGO TERMINO!!, GANADOR JUGADOR 2");
                    } else {
                        JOptionPane.showMessageDialog(null, "JUEGO TERMINO!!, GANADOR JUGADOR 1");
                    }
                    isGameOver = true;
                    Enviar(Player.Juego);
                    
                }

            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean done() {
        return isGameOver;
    }

}
