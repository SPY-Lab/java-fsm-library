package it.univr.fsm.equations;

import java.util.Vector;

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
		if (this.isIndipendent() && !this.getE().isGround()) {
			this.getE().simplify();
			Vector<RegularExpression> parts = getE().inBlockPart();

			if(this.e.contains(this.getLeftSide())){
				RegularExpression result = null;
				for(int i=0 ; i < parts.size(); i++){
					RegularExpression part = parts.get(i);
					if(result == null) {
						if(part instanceof Or) {
							Equation e1 = new Equation(this.getLeftSide(), ((Or) part).getFirst());
							Equation e2 = new Equation(this.getLeftSide(), ((Or) part).getSecond());
							result = new Or(e1.syntetize().getE(), e2.syntetize().getE());
						}else if(part instanceof Comp){

						}else
							result = part;
					}else {
						if (part instanceof Comp ) {
							RegularExpression groundTerm = null;
							if (((Comp) part).getFirst().isGround()) {
								groundTerm = ((Comp) part).getFirst();
							}else {
								groundTerm = ((Comp) part).getSecond();

							}
							result = new Or(result, new Comp(groundTerm,new Star(groundTerm)));

						} else if (part instanceof Or) {
							Equation e = new Equation(this.getLeftSide(), part);
							result = new Or(result, e.syntetize().getE());

						} else if (part instanceof GroundCoeff || part instanceof Var || part instanceof Star) {
							result = new Or(result, part);

						}

					}
				}


				return new Equation(this.getLeftSide(),result);
			}

		}

		return this;
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
