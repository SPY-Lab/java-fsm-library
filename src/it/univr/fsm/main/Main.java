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

		Automaton a14 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0014");
		Automaton a15 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0015");

		System.out.println(a14);
		System.out.println(a15);

		Automaton a = Automaton.concat(a14,a15);


		System.out.println(a);
		System.out.println(a.toRegex());
		
	}

}