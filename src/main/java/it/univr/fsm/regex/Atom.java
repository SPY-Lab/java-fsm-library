package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

public class Atom extends RegularExpression {

	private String string;

	public Atom(String s) {
		this.string = s;
	}

	public boolean isEmpty() {
		return string.isEmpty();
	}

	public String getString() {
		return string;
	}

	@Override
	public String toString() {
		return string.isEmpty() ? "ε" : string.toString();
	}


	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Atom){
			return string.equals(((Atom) other).string);
		}
		return false;
	}
	
	@Override
	public RegularExpression simplify() {
		return this;
	}

	@Override
	public Automaton toAutomaton() {
		return Automaton.makeAutomaton(string);
	}

	@Override
	public String getProgram() {
		return string;
	}
}
