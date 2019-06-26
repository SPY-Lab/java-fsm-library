package it.univr.parsing;

import it.univr.fsm.machine.Automaton;

public class OmegaAssignmentPredicate extends SingleOmegaPredicate {

	private String var;
	private String sign;

	public OmegaAssignmentPredicate(String var, String sign) {
		this.var = var;
		this.sign = sign;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public Automaton evaluate() {
		if (sign.equals("+")) {
			return Automaton.makePositiveAssignment(var);
		} else if (sign.equals("-")) {
			return Automaton.makeNegativeAssignment(var);
		} else {
			return Automaton.makeTopAssignment(var);
		}
	}
	
	@Override
	public String toString() {
		return var + "=" + sign + ";";
	}
}
