/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaJuego;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public class JuegoException  extends Exception implements Serializable{
    	private static final long serialVersionUID = -4036588148354448335L;

	public JuegoException(String string) {
		super(string);
	}
}
