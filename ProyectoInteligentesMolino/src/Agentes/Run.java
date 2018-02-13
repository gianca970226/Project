/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;
import LogicaJuego.*;
/**
 *
 * @author Ander
 */
public class Run {

    public static void main(String[] args) {
        Agent_Master Creador = new Agent_Master();
        Creador.initController();
        Object[] Y = new Object[1];
        Object [] Args= new Object[4];
        Args[0]=Ficha.JUGADOR_2;
        Args[1]=Juego.NUMERO_FICHAS_POR_JUGADOR;
        Args[2]=8;
        Args[3]=false;
        Creador.initAgent("Player_1", "Agentes.Agent_Player", Y);
        Creador.initAgent("Player_2", "Agentes.Agent_Minimax", Args);
        Creador.initAgent("manager", "Agentes.Agent_Manager", Y);
    }

}
