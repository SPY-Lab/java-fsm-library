package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

public class EmptySet extends RegularExpression {

	@Override
	public RegularExpression simplify() {
		return new EmptySet();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof EmptySet;
	}

	@Override
	public String toString() {
		return "∅";
	}

	@Override
	public Automaton toAutomaton() {
		return Automaton.makeEmptyLanguage();
	}

	@Override
	public String getProgram() {
		return "";
	}

}
