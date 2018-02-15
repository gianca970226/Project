package Agentes;

import GUI.*;
import LogicaJuego.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AgenteJugador extends Agent {

    JugadorM jugadorM;
    ReglasMolino reglasMolino;
    frmJugador frmJugador;
    boolean inicioJuego;

    @Override
    public void setup() {

        System.out.println("CREANDO AGENTE");

        this.reglasMolino = new ReglasMolino();

        Object[] args = getArguments();

        JugadorC jugador = (JugadorC) args[0];

        iniciarTablero(this);

        int nFichasJugador = (int) args[1];
        int level = (int) args[2];
        inicioJuego = (boolean) args[3];
        inicializarJugadorIA(jugador, nFichasJugador, level);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AgenteJugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        addBehaviour(new JugarMolino(inicioJuego));
    }

    private void iniciarTablero(AgenteJugador agente) {
        java.awt.EventQueue.invokeLater(() -> {
            frmJugador = new frmJugador();
            frmJugador.setAgenteJugador(agente);
            frmJugador.setTitle(getLocalName());
            frmJugador.setVisible(true);
        });
    }

    public void inicializarJugadorIA(JugadorC ficha, int numeroDeFichas, int profundidad) {
        try {
            this.jugadorM = new JugadorM(ficha, numeroDeFichas, profundidad);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void setIniciaJuego(boolean inicioJuego) {
        this.inicioJuego = inicioJuego;
    }

    public boolean isInicioJuego() {
        return inicioJuego;
    }

    public ReglasMolino getReglasMolino() {
        return reglasMolino;
    }

    public void setReglasMolino(ReglasMolino reglasMolino) {
        this.reglasMolino = reglasMolino;
    }

    public frmJugador getFrmJugador() {
        return frmJugador;
    }

    public void setFrmJugador(frmJugador Tablero) {
        this.frmJugador = Tablero;
    }

    class JugarMolino extends SimpleBehaviour {

        boolean isGameOver;

        public JugarMolino(boolean inicioJuego) {
            //System.out.println("INICIALIZANDO EL COMPORTAMIENTO  JUEGO MOLINO");
            isGameOver = false;
            if (inicioJuego) {
                hacerMovimientoFaseColocar();
            }
        }

        private void hacerMovimientoFaseColocar() {
            int indiceTablero = jugadorM.getIndiceParaPonerFicha(reglasMolino.getTableroDelJuego());
            try {
                if (reglasMolino.ponerFichaJugador(indiceTablero, jugadorM.getFichaJugador())) {
                    jugadorM.aumentarNumeroFichasEnTablero();
                    if (reglasMolino.hizoUnMolino(indiceTablero, jugadorM.getFichaJugador())) {
                        JugadorC fichaOponente = jugadorM.getFichaJugador() == JugadorC.AGENTE1 ? JugadorC.AGENTE2 : JugadorC.AGENTE1;

                        int indiceEliminar = jugadorM.getIndiceParaQuitarFichaDeOponente(reglasMolino.getTableroDelJuego());
                        reglasMolino.quitarFicha(indiceEliminar, fichaOponente);
                    }
                    frmJugador.ActualizarTableroJugador();
                    enviar();
                }

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        private void moverFicha() {
            int indiceOrigen, indiceDestino;
            Jugada movimiento = null;
            try {
                movimiento = jugadorM.getFichaAMover(reglasMolino.getTableroDelJuego());
                indiceOrigen = movimiento.origen;
                indiceDestino = movimiento.destino;

                int resultado;
                if ((resultado = reglasMolino.moverFichaDe(indiceOrigen, indiceDestino, jugadorM.getFichaJugador())) == ReglasMolino.MOVIMIENTO_VALIDO) {

                    if (reglasMolino.hizoUnMolino(indiceDestino, jugadorM.getFichaJugador())) {
                        JugadorC fichaOponente = jugadorM.getFichaJugador() == JugadorC.AGENTE1 ? JugadorC.AGENTE2 : JugadorC.AGENTE1;

                        int indiceEliminar = jugadorM.getIndiceParaQuitarFichaDeOponente(reglasMolino.getTableroDelJuego());
                        reglasMolino.quitarFicha(indiceEliminar, fichaOponente);

                    }
                    frmJugador.ActualizarTableroJugador();
                    enviar();
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        private void hacerMovimiento() throws Exception {
            if (reglasMolino.getFaseActualDelJuego() == ReglasMolino.ETAPAPONER) {
                hacerMovimientoFaseColocar();
            } else if (!reglasMolino.finalizoElJuego()) {
                moverFicha();
            } else {
                int jugador = reglasMolino.quienGano();
                switch (jugador) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "Empate");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Gano jugador 2");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Gano jugador 1");
                        break;
                }
                isGameOver = true;
                enviar();

            }
        }

        private void enviar() {
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AgenteJugador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                AID idReceptor = new AID();
                if (getLocalName().equals("Jugador1")) {
                    idReceptor.setLocalName("Jugador2");
                } else {
                    idReceptor.setLocalName("Jugador1");
                }
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.setSender(getAID());
                msg.addReceiver(idReceptor);
                msg.setContentObject(reglasMolino);
                send(msg);
                reglasMolino.setMiTurno(false);
            } catch (IOException ex) {
                Logger.getLogger(AgenteJugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void action() {
            ACLMessage mensaje = blockingReceive();
            if (mensaje != null) {
                try {
                    ReglasMolino juegoTemporal = (ReglasMolino) mensaje.getContentObject();

                    juegoTemporal.getTableroDelJuego().imprimirTablero();
                    if (juegoTemporal.isMiTurno()) {

                        setReglasMolino(juegoTemporal);
                        try {
                            getFrmJugador().ActualizarTableroJugador();
                            hacerMovimiento();
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }

                    } else {
                        System.out.println("Activar Juego");
                    }

                } catch (UnreadableException ex) {
                    System.out.println(ex.toString());
                }
            }
        }

        @Override
        public boolean done() {
            return isGameOver;
        }
    }
}
