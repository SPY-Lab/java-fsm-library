package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.machine.State;

public class GroundCoeff extends RegularExpression {

	private String string;

	public GroundCoeff(String s) {
		this.string = s;
	}

	@Override
	public String toString() {
		return string.toString();
	}

	@Override
	public RegularExpression replace(State s, RegularExpression e) {
		return new GroundCoeff(string);
	}

	@Override
	public boolean containsOnly(State s) {
		return true;
	}

	@Override
	public boolean contains(State s) {
		return true;
	}

	@Override
	public Vector<RegularExpression> getTermsWithState(State s) {
		return new Vector<RegularExpression>();
	}

	@Override
	public Vector<RegularExpression> getGroundTerms() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		v.add(new GroundCoeff(string));
		return v;
	}

	@Override
	public boolean isGround() {
		return true;
	}

	@Override
	public Vector<RegularExpression> inSinglePart() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		v.add(this);
		return v;
	}

	@Override
	public Vector<RegularExpression> inBlockPart() {
		return inSinglePart();
	}

	@Override
	public RegularExpression simplify() {
		return this;
	}

	@Override
	public String getProgram() {
		if (this.string.equals("$"))
			return "";
		return this.string;
	}


}
