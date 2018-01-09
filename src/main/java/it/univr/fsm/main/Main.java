package it.univr.fsm.main;

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

		Automaton a1 = Automaton.makeAutomaton("a");
		Automaton a2 = Automaton.makeAutomaton("b");

		Automaton a2complement = Automaton.complement(a2);
		Automaton intersection = Automaton.intersection(a1,a2complement);
		boolean result = Automaton.isEmptyLanguageAccepted(intersection);


		System.out.println(a2complement);
		System.out.println(intersection);
		System.out.println(result);

	}

}