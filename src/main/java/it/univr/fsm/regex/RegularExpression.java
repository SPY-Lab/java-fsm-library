package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

/**
 * Regular expression class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public abstract class RegularExpression extends Object{

	public abstract RegularExpression simplify();
	public abstract int hashCode();
	public abstract boolean equals(Object other);
	public abstract Automaton toAutomaton();
	public abstract String getProgram();
}
