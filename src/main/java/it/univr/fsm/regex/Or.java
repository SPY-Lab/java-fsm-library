package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

public class Or extends RegularExpression {
	
	public RegularExpression first;
	public RegularExpression second;

	public Or(RegularExpression first, RegularExpression second) {
		this.first = first;
		this.second = second;
	}

	public RegularExpression getSecond() {
		return second;
	}

	public RegularExpression getFirst() {
		return first;
	}

	@Override
	public String toString() {
		return "(" + first.toString() + " + " + second.toString() + ")";
	}

	public boolean isAtomic() {
		return first instanceof Atom && second instanceof Atom;
	}


	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Or) {
			return (first.equals(((Or) other).first) && second.equals(((Or) other).second))
					|| (first.equals(((Or) other).second) && second.equals(((Or) other).first));
		}
		return false;

	}

	@Override
	public RegularExpression simplify() {

		first = first.simplify();
		second = second.simplify();
		RegularExpression result = new Or(first, second);

		if (first instanceof EmptySet)
			result = second;
		else if (second instanceof EmptySet)
			result = first;
		else if (first instanceof Atom && ((Atom) first).isEmpty() && second instanceof Atom && ((Atom) second).isEmpty())
			result = new Atom("");
		else if (first instanceof Atom && ((Atom) first).isEmpty() && second instanceof Star)
			result = second;
		else if (second instanceof Atom && ((Atom) second).isEmpty() && first instanceof Star)
			result = first;
		else
			result = new Or(first, second);

		return result;
	}

	@Override
	public Automaton toAutomaton() {		
		return Automaton.union(first.toAutomaton(), second.toAutomaton());
	}
	
	@Override
	public String getProgram() {
	
		String firstProgram = first.getProgram();
		String secondProgram = second.getProgram();

		return "if (top) {" + (Automaton.isJSExecutable(firstProgram) ? firstProgram : "skip;") + "} else { } if (top) {" + (Automaton.isJSExecutable(secondProgram) ? secondProgram : "skip;") + "} else {}";
	}
}
