package it.univr.fsm.main;

import it.univr.fsm.equations.*;
import it.univr.fsm.machine.*;

/**
 * Main class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 0.1
 * @since 19-11-2016
 */
public class Main {

	public static void main(String[] args) {
		System.out.println(Automaton.makeAutomaton("'helloworld'"));
	}

}
