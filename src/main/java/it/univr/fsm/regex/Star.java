package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

public class Star extends RegularExpression {

	private RegularExpression op;

	public Star(RegularExpression op) {
		this.op = op;
	}

	@Override
	public int hashCode() {
		return op.hashCode();
	}
	
	@Override
	public String toString() {
		return "(" + op.toString() + ")*";
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Star){
			return op.equals(((Star) other).op);
		}
		return false;
	}

	@Override
	public RegularExpression simplify() {

		RegularExpression result = op.simplify();

		if (op instanceof Atom && ((Atom) op).isEmpty())
			result = new Atom("");
		else if (op instanceof EmptySet)
			result = new Atom("");
		else
			result = new Star(op);
		
		return result;
	}

	@Override
	public Automaton toAutomaton() {
		return Automaton.star(op.toAutomaton());
	}
	
	@Override
	public String getProgram() {
		String program = op.getProgram();
		return "while (top) {" + (Automaton.isJSExecutable(program) ? program : "skip;" ) + "}";
	}
}
