package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.config.Config;
import it.univr.fsm.machine.State;

public class Star extends RegularExpression {
	private RegularExpression op;

	public Star(RegularExpression op) {
		this.op = op;
	}
	public Star(Vector<RegularExpression> opv, State s) {

		opv = opv.get(0).inSinglePart();
		RegularExpression result = opv.get(0);


		for (int i = 1; i < opv.size(); ++i) {
			if (opv.get(i) instanceof Var)
				if (((Var) opv.get(i)).getVariable().equals(s)) 
					continue;

			result = new Comp(result, opv.get(i));
		}

		this.op = result;
	}

	@Override
	public RegularExpression replace(State s, RegularExpression e) {
			return this;
	}

	@Override
	public RegularExpression syntetize(State s) {
		return this;
	}

	@Override
	public int hashCode() {
		return op.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Star){
			return op.equals(((Star) other).op);
		}
		return false;
	}

	@Override
	public boolean containsOnly(State s) {
		return op.containsOnly(s);
	}

	@Override
	public boolean contains(State s) {
		return op.contains(s);
	}

	@Override
	public Vector<RegularExpression> getTermsWithState(State s) {
		return op.getTermsWithState(s);
	}

	@Override
	public Vector<RegularExpression> getGroundTerms() {
		return op.getGroundTerms();
	}

	@Override
	public String toString() {
		return "("  + this.op.toString() + ")*";
	}

	@Override
	public boolean isGround() {
		return op.isGround();
	}
	@Override
	public Vector<RegularExpression> inSinglePart() {
		return inBlockPart();
	}

	@Override
	public Vector<RegularExpression> inBlockPart() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		v.add(this);
		return v;
	}
/*
	@Override
	public RegularExpression replace(RegularExpression e, RegularExpression with) {

		return new Star(op.replace(e,with));
	}
*/
	@Override
	public RegularExpression simplify() {
		if(op.toString().equals(""))
			return new GroundCoeff("");

		return new Star(this.op.simplify());
	}
	@Override
	public String getProgram() {
		int curr = Config.GEN;
		Config.GEN++;
		String result = "g" + curr + ":=rand(); while g" + curr  + " = 1 {" + this.op.getProgram() + " g" + curr + ":=rand(); };";
		Config.GEN++;
		return result;
	}
}
