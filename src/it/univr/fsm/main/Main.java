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

		final long startTime = System.currentTimeMillis();


		Automaton a = Automaton.loadAutomataWithAlternatePattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0013");





		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) + " ms");


		
	}

}