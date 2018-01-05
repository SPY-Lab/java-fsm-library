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
	public RegularExpression syntetize(State s) {
		return this;
	}

	@Override
	public int hashCode() {
		return variable.getState().hashCode();
	}

	/*@Override
	public RegularExpression replace(RegularExpression e, RegularExpression with) {
		return this;
	}*/

	@Override
	public RegularExpression remove(RegularExpression e) {
		if(this.equals(e)){
			return new GroundCoeff("");
		}
		return this;
	}

	@Override
	public RegularExpression factorize(RegularExpression e) {
		return this.equals(e) ? this : null;
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Var){
			return variable.getState().equals(((Var) other).variable.getState());
		}
		return false;
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
	public StringBuilder getProgram() {
		return new StringBuilder("");
	}
	
	@Override
	public RegularExpression adjust() {
		return simplify();
	}
}
