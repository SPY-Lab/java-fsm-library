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
		Automaton third =Automaton.makeAutomaton("good");
		Automaton fourth =Automaton.makeAutomaton("string");
		Automaton fifth=Automaton.makeAutomaton(" concat");
		Automaton autchar=Automaton.makeAutomaton("'a");
		Automaton epsilon=Automaton.makeAutomaton("");
		
		
		
		System.out.println(epsilon);
		//System.out.println(Automaton.concat(autchar, autchar));
		/*System.out.println(Automaton.concat(first, third));
		System.out.println(Automaton.concat(second, fourth));
		System.out.println(Automaton.concat(first, first));
		System.out.println(Automaton.concat(third, fifth));
		System.out.println(Automaton.concat(third, epsilon));
		*/
		
		//System.out.println(Automaton.complement(autchar));
		
		
	}

}
