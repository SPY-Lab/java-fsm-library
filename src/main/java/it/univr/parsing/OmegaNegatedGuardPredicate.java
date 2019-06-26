package it.univr.parsing;

import it.univr.fsm.machine.Automaton;

public class OmegaNegatedGuardPredicate extends SingleOmegaPredicate {
	private String left;
	private String op;
	private String right;

	public OmegaNegatedGuardPredicate(String left, String op, String right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	@Override
	public Automaton evaluate() {
		Automaton result = null;
		if (toString().contains("*")) {
			//TODO
		} else {
			result = Automaton.makeRealAutomaton("!(" + left + op + right + ")");
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "!(" + left + " "+ op + " " + right + ")";
	}
}
