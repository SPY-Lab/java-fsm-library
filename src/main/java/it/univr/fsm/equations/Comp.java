package it.univr.fsm.equations;

import java.util.Vector;

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
	public RegularExpression remove(RegularExpression e) {
		first = first.remove(e);
		second = second.remove(e);


		if(second instanceof GroundCoeff && ((GroundCoeff) second).getString().equals("")){
			return first;
		}else if(first instanceof GroundCoeff && ((GroundCoeff) first).getString().equals("")){
			return second;
		}

		return this;
	}

	@Override
	public RegularExpression factorize(RegularExpression e) {
		return first.factorize(e) != null ? first.factorize(e) :
			second.factorize(e) != null ? second.factorize(e) :
				e.factorize(first) != null ? e.factorize(first) :
					e.factorize(second) != null ? e.factorize(second) : null;
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
		v.add(this);
		return v;
	}

	/*@Override
	public RegularExpression replace(RegularExpression e, RegularExpression with) {
		return new Comp(first.replace(e,with),second.replace(e,with));
	}*/

	@Override
	public RegularExpression simplify() {

		/**
		 * This fixes the "unsoundness problem"
		 */
		if (this.second instanceof Or) {
			return new Or(new Comp(this.first, ((Or)this.second.simplify()).first), new Comp(this.first, ((Or)this.second.simplify()).second));
		}



		// Comp with an only term isn't a Comp, but a GroundCoeff
		if(second instanceof GroundCoeff && ((GroundCoeff) second).getString().equals("")){
			return first.simplify();
		}else if (first instanceof GroundCoeff && ((GroundCoeff) first).getString().equals("")){
			return second.simplify();
		}

		// TODO: sicuro?
		// (a + b)(a + b)* = (a + b)*
		if(first instanceof Or && second instanceof Star){
			if(new Star(first).equals(second)){
				return second.simplify();
			}
		}else if(first instanceof Star && second instanceof Or){
			if(new Star(second).equals(first)){
				return first.simplify();
			}
		}


		if (first instanceof GroundCoeff && second instanceof Star && ((Star) second).getOperand() instanceof Or) {

			if (((Or) ((Star) second).getOperand()).getFirst() instanceof GroundCoeff 
					&& ((Or) ((Star) second).getOperand()).getSecond() instanceof GroundCoeff) {

				Or newOr = new Or(getFirst(), new Or(new GroundCoeff(getFirst().toString() + ((Or) ((Star) second).getOperand()).getFirst().toString()), 
						new GroundCoeff(getFirst().toString() + ((Or) ((Star) second).getOperand()).getSecond().toString())));

				return new Comp(newOr, second);
			}

		}


		return new Comp(first.simplify(),second.simplify());
	}
}
