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
    Juego juego;
    frmJugador frmJugador;
    boolean inicioJuego;

    @Override
    public void setup() {

        System.out.println("CREANDO AGENTE");

        this.juego = new Juego();

        Object[] args = getArguments();

        Ficha jugador = (Ficha) args[0];

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

    public void inicializarJugadorIA(Ficha ficha, int numeroDeFichas, int profundidad) {
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

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
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
            int indiceTablero = jugadorM.getIndiceParaPonerFicha(juego.getTableroDelJuego());
            try {
                if (juego.ponerFichaJugador(indiceTablero, jugadorM.getFichaJugador())) {
                    jugadorM.aumentarNumeroFichasEnTablero();
                    if (juego.hizoUnMolino(indiceTablero, jugadorM.getFichaJugador())) {
                        Ficha fichaOponente = jugadorM.getFichaJugador() == Ficha.JUGADOR_1 ? Ficha.JUGADOR_2 : Ficha.JUGADOR_1;

                        int indiceEliminar = jugadorM.getIndiceParaQuitarFichaDeOponente(juego.getTableroDelJuego());
                       juego.quitarFicha(indiceEliminar, fichaOponente);
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
            Movimiento movimiento = null;
            try {
                movimiento = jugadorM.getFichaAMover(juego.getTableroDelJuego());
                indiceOrigen = movimiento.indiceOrigen;
                indiceDestino = movimiento.indiceDestino;

                int resultado;
                if ((resultado = juego.moverFichaDe(indiceOrigen, indiceDestino, jugadorM.getFichaJugador())) == Juego.MOVIMIENTO_VALIDO) {

                    if (juego.hizoUnMolino(indiceDestino, jugadorM.getFichaJugador())) {
                        Ficha fichaOponente = jugadorM.getFichaJugador() == Ficha.JUGADOR_1 ? Ficha.JUGADOR_2 : Ficha.JUGADOR_1;

                        int indiceEliminar = jugadorM.getIndiceParaQuitarFichaDeOponente(juego.getTableroDelJuego());
                        juego.quitarFicha(indiceEliminar, fichaOponente);
                  
                    }
                    frmJugador.ActualizarTableroJugador();
                    enviar();
                } 
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        private void hacerMovimiento() throws Exception {
            if (juego.getFaseActualDelJuego() == Juego.FASE_COLOCAR) {
                hacerMovimientoFaseColocar();
            } else if (!juego.finalizoElJuego()) {
                moverFicha();
            } else {
                int jugador=juego.quienGano();
                switch (jugador) {
                    case 0:
                        JOptionPane.showMessageDialog(null,"Empate");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null,"Gano jugador 2");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"Gano jugador 1");
                        break;
                }
                isGameOver = true;
                enviar();

            }
        }

        private void enviar() {
                    try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AgenteJugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            try {
                AID idReceptor = new AID();
                if (getLocalName().equals("Jugador1")) {
                    idReceptor.setLocalName("Jugador2");
                } else {
                    idReceptor.setLocalName("Jugador1");
                }
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.setSender(getAID());
                msg.addReceiver(idReceptor);
                msg.setContentObject(juego);
                send(msg);
                juego.setMiTurno(false);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        @Override
        public void action() {
            ACLMessage mensaje = blockingReceive();
            if (mensaje != null) {
                try {
                    Juego juegoTemporal = (Juego) mensaje.getContentObject();
            
                    juegoTemporal.getTableroDelJuego().imprimirTablero();
                    if (juegoTemporal.isMiTurno()) {

                        setJuego(juegoTemporal);
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
