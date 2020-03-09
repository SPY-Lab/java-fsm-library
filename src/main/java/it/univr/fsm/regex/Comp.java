package it.univr.fsm.regex;

import it.univr.fsm.machine.Automaton;

public class Comp extends RegularExpression {
	
	private RegularExpression first;
	private RegularExpression second;

	public Comp(RegularExpression first, RegularExpression second) {
		this.first = first;
		this.second = second;
	}

	public RegularExpression getFirst() {
		return first;
	}

	public RegularExpression getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return first.toString() + second.toString();
	}

	@Override
	public int hashCode() {
		return first.hashCode() * second.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Comp) {
			return first.equals(((Comp) other).first) && second.equals(((Comp) other).second);
		}
		return false;
	}

	@Override
	public RegularExpression simplify() {
		
		first = first.simplify();
		second = second.simplify();
		
		RegularExpression result = new Comp(first, second);
		
		if (first instanceof EmptySet || second instanceof EmptySet)
			result = new EmptySet();
		else if (second instanceof Or)
			result =  new Or(new Comp(this.first, ((Or)this.second).first), new Comp(this.first, ((Or)this.second).second));
		else if (first instanceof Atom && second instanceof Or && ((Or) second).isAtomic())
			result = new Or(new Atom(first.toString() + ((Or) this.second).first.toString()), new Atom(this.first.toString() + ((Or) this.second).second.toString()));
		else if(second instanceof Atom && ((Atom) second).isEmpty()) 
			result = first;
		else if (first instanceof Atom && ((Atom) first).isEmpty())
			result = second;
	
		return result;
	}

	@Override
	public Automaton toAutomaton() {
		return Automaton.concat(first.toAutomaton(), second.toAutomaton());
	}
	
	@Override
	public String getProgram() {
		StringBuilder a = new StringBuilder(first.getProgram());
		StringBuilder b = new StringBuilder(second.getProgram());
		return a.append(b).toString();
	}
}
