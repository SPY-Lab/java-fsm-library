package it.univr.main;

import it.univr.equations.*;
import it.univr.machine.*;

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
