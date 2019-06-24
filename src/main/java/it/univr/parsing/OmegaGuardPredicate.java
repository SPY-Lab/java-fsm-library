package it.univr.parsing;

import it.univr.fsm.machine.Automaton;

public class OmegaGuardPredicate extends SingleOmegaPredicate {
	private String left;
	private String right;

	public OmegaGuardPredicate(String left, String right) {
		this.left = left;
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
		return null;
	}
}
