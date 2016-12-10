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
				
	
		/*
		Automaton a1= Automaton.loadAutomaton("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0001" );
		Automaton a2= Automaton.loadAutomaton("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0002" );
		
		Collection<Automaton> coll = new HashSet<Automaton>(2);
		coll.add(a1);
		coll.add(a2);
		
		System.out.println("Intersection: " + Automaton.intersection(coll));
		System.out.println(Automaton.intersection(coll).equals(Automaton.intersection(a1, a2)));
		
		System.out.println("Minus: " + Automaton.minus(coll));
		System.out.println(Automaton.minus(coll).equals(Automaton.minus(a1, a2)));
		
		
		Automaton b = Automaton.concat(a1, a2);
		Automaton a = Automaton.concat(coll);
		System.out.println("Concat: " + a);
		System.out.println(a.equals(b));
		
		System.out.println("a1 isEmptyLanguage accepted: " + Automaton.isEmptyLanguageAccepted(a1));
		System.out.println("a2 isEmptyLanguage accepted: " + Automaton.isEmptyLanguageAccepted(a2));
		
		System.out.println(Automaton.intersection(a1, Automaton.complement(a2)));
		System.out.println("(a1 intersect !a2) isEmptyLanguage accepted: " + Automaton.isEmptyLanguageAccepted(Automaton.intersection(a1, Automaton.complement(a2))));
		System.out.println("isContained: " + Automaton.isContained(a1, a2));
		
		System.out.println(Automaton.intersection(first, Automaton.complement(second)));
		System.out.println("(first intersect !second) isEmptyLanguage accepted: " + Automaton.isEmptyLanguageAccepted(Automaton.intersection(first, Automaton.complement(second))));
		System.out.println("isContained: " + Automaton.isContained(first, second));
		
		System.out.println("a1 is deterministic? " + Automaton.isDeterministic(a1));
		System.out.println("a2 is deterministic? " + Automaton.isDeterministic(a2));
		
		System.out.println("a2:");
		System.out.println(a2);
		a2.hopcroftMinimize();
		System.out.println(a2);
		
		System.out.println("a:");
		System.out.println(a);
		a.hopcroftMinimize();
		System.out.println(a);
		*/
		
		Automaton a3 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0003");
		System.out.println("a3:");
		System.out.println(a3);
		
		Automaton a4 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0004");
		System.out.println("a4:");
		System.out.println(a4);
		
		Automaton a2 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0002");
		System.out.println("a2:");
		System.out.println(a2);
		
		Automaton a1 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0001");
		System.out.println("a1:");
		System.out.println(a1);
		
		
		Automaton a5 = Automaton.makeAutomaton("'y:=1;$'");
		System.out.println("a5:");
		System.out.println(a5);
		a5.hopcroftMinimize();
		System.out.println(a5);
		
		Automaton a5bis = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0005");
		System.out.println("a5bis:");
		System.out.println(a5bis);
		
		
		
	}

}