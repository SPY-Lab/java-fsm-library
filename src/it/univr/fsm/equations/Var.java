package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.machine.State;

public class Var extends RegularExpression {

	private State variable;

	public Var(State s) {
		this.variable = s;
	}


	public State getVariable() {
		return variable;
	}


	public void setVariable(State variable) {
		this.variable = variable;
	}

	@Override
	public String toString() {
		return getVariable().toString();
	}


	@Override
	public RegularExpression replace(State s, RegularExpression e) {
		if (s.equals(variable))
			return e;
		return this;
	}


	@Override
	public boolean containsOnly(State s) {
		if (this.variable.toString().equals(s.toString()))
			return true;
		return false;
	}

	@Override
	public boolean contains(State s) {
		return containsOnly(s);
	}

	@Override
	public Vector<RegularExpression> getTermsWithState(State s) {
		Vector<RegularExpression> v = new Vector<RegularExpression>();

		if (variable.equals(s)) 
			v.add(this);

		return v;
	}


	@Override
	public Vector<RegularExpression> getGroundTerms() {
		return new Vector<RegularExpression>();
	}


	@Override
	public boolean isGround() {
		return false;
	}


	@Override
	public Vector<RegularExpression> inSinglePart() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		v.add(this);
		return v;
	}


	@Override
	public Vector<RegularExpression> inBlockPart(){
		return inSinglePart();
	}

	@Override
	public RegularExpression simplify() {
		return this;
	}


	@Override
	public String getProgram() {
		return "";
	}
}
