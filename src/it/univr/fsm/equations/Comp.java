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

	@Override
	public String toString() {
		return first.toString() + second.toString();
	}

	@Override
	public RegularExpression replace(State s, RegularExpression e) {
		return new Comp(first.replace(s, e), second.replace(s, e));
	}

	@Override
	public boolean containsOnly(State s) {
		return  first.containsOnly(s) && second.containsOnly(s);
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
	public RegularExpression simplify() {
		if (this.second.simplify() instanceof Or) {
			return new Or(new Comp(this.first, ((Or)this.second.simplify()).first), new Comp(this.first, ((Or)this.second.simplify()).second));
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
