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
		
		
		Automaton first = Automaton.makeAutomaton("'hello'");
		Automaton second = Automaton.makeAutomaton("'world'");
		
		
		//System.out.println(Automaton.union(first,second));
		System.out.println(Automaton.concat(first, second));
	}

}
