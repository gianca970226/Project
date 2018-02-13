/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import LogicaJuego.*;
import GUI.PhysicalBoard;
import LogicaJuego.Juego;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ander
 */
public class Agent_Manager extends GuiAgent {

    Juego Juego;
    PhysicalBoard Tablero;
    String Siguiente = "Player_1";
    ComportamientoActualizar Mensajero;

    @Override
    protected void setup() {
        this.Juego = new Juego();
        this.Tablero = new PhysicalBoard();
        this.Tablero.setManager(this);
        this.Tablero.setTitle(this.getLocalName());
    //    this.Tablero.setVisible(true);
        this.Mensajero = new ComportamientoActualizar(this);
        addBehaviour(Mensajero);

    }

    public void Event(GuiEvent ge) {
        this.onGuiEvent(ge);
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        if (ge.getType() == 1) {
            Run();
        }
    }

    public void Run() {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            AID idReceptor = new AID();
            String Destino = "Player_1";
            idReceptor.setLocalName(Destino);
            msg.setSender(getAID());
            msg.addReceiver(idReceptor);
            //this.Juego.setDescripcion("Player_1");
            msg.setContentObject(this.Juego);
            send(msg);
        } catch (Exception e) {
        }

    }

    public void Reiniciar() {
        this.Juego = new Juego();
    }

    public void ActualizarUI(Juego Juego) throws JuegoException {
        this.Juego = Juego;
        Tablero.ActualizarTablero();
    }

    public Juego getJuego() {
        return Juego;
    }

    public void setJuego(Juego Juego) {
        this.Juego = Juego;
    }

}

class ComportamientoActualizar extends SimpleBehaviour {

    Agent_Manager Manager;

    public ComportamientoActualizar(Agent_Manager Manager) {
        this.Manager = Manager;
    }

    @Override
    public void action() {
        try {
            ACLMessage mensaje = getAgent().receive();
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (mensaje != null) {
                Juego Object = (Juego) mensaje.getContentObject();
                AID idReceptor = new AID();
//                String Player = Object.getDescripcion();
                String Destino;
//                if (Player.equals("Player_1")) {
//                    Destino = "Player_2";
//                }
//                else {
//                    Destino = "Player_1";
//                }
                if (Object.finalizoElJuego()) {
                    //Object.setDescripcion("Game Over");
                }
                else {
                    //Object.setDescripcion("Continue");
                }
//                idReceptor.setLocalName(Destino);
                Manager.ActualizarUI(Object);
                msg.setSender(getAgent().getAID());
                msg.addReceiver(idReceptor);
                msg.setContentObject(Object);
                getAgent().send(msg);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
