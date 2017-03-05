package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.config.Config;
import it.univr.fsm.machine.State;

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
	public RegularExpression replace(State s, RegularExpression e) {
		return new Comp(first.replace(s, e), second.replace(s, e));
	}

	@Override
	public RegularExpression syntetize(State s) {
		if(first instanceof Var && first.containsOnly(s)){
			return new Star(second);
		}
		else if(second instanceof Var && second.containsOnly(s))
			return new Star(first);
		else
			return new Comp(first.syntetize(s), second.syntetize(s));
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
	public boolean containsOnly(State s) {
		return  first.containsOnly(s) && second.containsOnly(s);
	}

	@Override
	public boolean contains(State s) {
		return  first.contains(s) || second.contains(s);
	}

	@Override
	public Vector<RegularExpression> getTermsWithState(State s) {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		
		if (this.containsOnly(s))
			v.add(this);
		return v;
	}
	
	@Override
	public Vector<RegularExpression> getGroundTerms() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();

		if (first.isGround() && second.isGround())
			v.add(this);
		return v;
	}
	
	@Override
	public boolean isGround() {
		return first.isGround() && second.isGround();
	}
	
	@Override
	public Vector<RegularExpression> inSinglePart() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		
		for (int i = 0; i < first.inSinglePart().size(); ++i)
			v.add(first.inSinglePart().get(i));
		
		for (int i = 0; i < second.inSinglePart().size(); ++i)
			v.add(second.inSinglePart().get(i));
		
		return v;
	}

	@Override
	public Vector<RegularExpression> inBlockPart() {
		Vector<RegularExpression> v = new Vector<>();
		v.add(first);
		v.add(second);
		return v;
	}

	/*@Override
	public RegularExpression replace(RegularExpression e, RegularExpression with) {
		return new Comp(first.replace(e,with),second.replace(e,with));
	}*/

	@Override
	public RegularExpression simplify() {
		/*if (this.second instanceof Or) {
			return new Or(new Comp(this.first, ((Or)this.second.simplify()).first), new Comp(this.first, ((Or)this.second.simplify()).second));
		}*/


		first = first.simplify();
		second = second.simplify();

		if(second instanceof GroundCoeff && ((GroundCoeff) second).getString().equals("")){
			return first;
		}


		return this;
	}

	@Override
	public String getProgram() {
		
		String a = first.getProgram();
		String b = second.getProgram();
		
		return a + b;
	}
}
