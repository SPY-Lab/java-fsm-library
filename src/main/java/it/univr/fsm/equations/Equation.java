package it.univr.fsm.equations;

import it.univr.fsm.machine.State;

public class Equation {
	private State leftSide;
	private RegularExpression e;

	public Equation(State leftSide, RegularExpression e) {
		this.leftSide = leftSide;
		this.e = e;
	}

	public State getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(State leftSide) {
		this.leftSide = leftSide;
	}

	public RegularExpression getE() {
		return e;
	}

	public void setE(RegularExpression e) {
		this.e = e;
	}


	/*
	public Equation syntetize() {
		if (this.isIndipendent() && !this.getE().isGround()) {
			Vector<RegularExpression> st = this.getE().getTermsWithState(this.getLeftSide());
			Vector<RegularExpression> ground = this.getE().getGroundTerms();

			RegularExpression groundsum = null;

			for (int i = 0; i< ground.size(); ++i) {
				if (groundsum == null)
					groundsum = ground.get(i);
				else
					groundsum = new Or(groundsum, ground.get(i));
			}

			if (st.isEmpty() || st == null) {
				if (ground.isEmpty() || ground == null)
					return this;
				else
					return new Equation(this.getLeftSide() ,groundsum);
			} else {
				if (ground.isEmpty() || ground == null)
					return new Equation(this.getLeftSide(), new Star(st, this.getLeftSide()));
				else {				
					return new Equation(this.getLeftSide(), new Comp(new Star(st, this.getLeftSide()), groundsum));
				}
			}
		}
		return this;
	}
	*/



	public Equation syntetize(){
		return new Equation(this.getLeftSide(), this.getE().syntetize(this.getLeftSide()));
	}


	/*
	public boolean isIndipendent() {
		if (this.getE().containsOnly(this.getLeftSide()))
			return true;
		return false;
	}
	*/

	public boolean isIndipendent() {
		if (this.getE().contains(this.getLeftSide()))
			return true;
		return false;
	}



	@Override
	public String toString() {
		return getLeftSide().toString() + "=" + e.toString();
	}
}
