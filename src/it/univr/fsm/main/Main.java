package it.univr.fsm.main;

import java.util.Collection;
import java.util.HashSet;

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
				
		//System.out.println(epsilon);
		//System.out.println(Automaton.concat(autchar, autchar));
		/*System.out.println(Automaton.concat(first, third));
		System.out.println(Automaton.concat(second, fourth));
		System.out.println(Automaton.concat(first, first));
		System.out.println(Automaton.concat(third, fifth));
		System.out.println(Automaton.concat(third, epsilon));
		*/
		
		//System.out.println(Automaton.complement(autchar));
		Automaton a1= Automaton.loadAutomaton("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0001" );
		Automaton a2= Automaton.loadAutomaton("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0002" );
		
		Collection<Automaton> coll= new HashSet<Automaton>(2);
		coll.add(a1);
		coll.add(a2);
		
		System.out.println("Intersection: " + Automaton.intersection(coll));
		System.out.println(Automaton.intersection(coll).equals(Automaton.intersection(a1, a2)));
		
		System.out.println("Minus: " + Automaton.minus(coll));
		System.out.println(Automaton.minus(coll).equals(Automaton.minus(a1, a2)));
		
		
		Automaton b= Automaton.concat(a1, a2);
		Automaton a= Automaton.concat(coll);
		System.out.println("Concat: " + a);
		System.out.println(a.equals(b));
		
	}

}