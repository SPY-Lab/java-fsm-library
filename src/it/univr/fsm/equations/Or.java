package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.config.Config;
import it.univr.fsm.machine.State;

public class Or extends RegularExpression {
	public RegularExpression first;
	public RegularExpression second;

	public Or(RegularExpression first, RegularExpression second) {
		this.first = first;
		this.second = second;
	}

	public RegularExpression getSecond() {
		return second;
	}

	public RegularExpression getFirst() {
		return first;
	}

	@Override
	public String toString() {
		return "(" + first.toString() + " + " + second.toString() + ")";
	}

	@Override
	public RegularExpression replace(State s, RegularExpression e) {
		return new Or(first.replace(s, e), second.replace(s, e));
	}


	@Override
	public RegularExpression syntetize(State s) {
		return new Or(first.syntetize(s), second.syntetize(s));
	}

	@Override
	public boolean containsOnly(State s) {
		return first.containsOnly(s) && second.containsOnly(s);
	}

	@Override
	public boolean contains(State s) {
		return first.contains(s) || second.contains(s);
	}

	@Override
	public Vector<RegularExpression> getTermsWithState(State s) {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		
		for (int i = 0; i < first.getTermsWithState(s).size(); ++i) {
			if (first.getTermsWithState(s).get(i).isGround())
				continue;
			v.add(first.getTermsWithState(s).get(i));
		}
		
		for (int i = 0; i < second.getTermsWithState(s).size(); ++i) {
			if (second.getTermsWithState(s).get(i).isGround())
				continue;
			v.addElement(second.getTermsWithState(s).get(i));
		}
		
		return v;
	}
	
	@Override
	public Vector<RegularExpression> getGroundTerms() {
		Vector<RegularExpression> v = new Vector<RegularExpression>();
		
		for (int i = 0; i < first.getGroundTerms().size(); ++i)
			v.add(first.getGroundTerms().get(i));
		
		for (int i = 0; i < second.getGroundTerms().size(); ++i)
			v.add(second.getGroundTerms().get(i));
		
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
	public Vector<RegularExpression> inBlockPart(){
		Vector<RegularExpression> v = new Vector<>();
		v.add(first);
		v.add(second);
		return v;
	}

	@Override
	public RegularExpression simplify() {
		return new Or(this.first.simplify(), this.second.simplify());
	}

	@Override
	public String getProgram() {
		int curr = Config.GEN;
		Config.GEN++;
		//String result = "g" + curr + ":=rand(); if g" + curr  + " = 1 {" + (this.first.getProgram().equals("") ? "skip;" : this.first.getProgram()) + "}; if g" + curr  + " = 2 {" + (this.second.getProgram().equals("") ? "skip;" : this.second.getProgram()) + "};";
		String result = "g" + curr + "=rand(); if (g" + curr  + " == 1) {" + (this.first.getProgram().equals("") ? ";" : this.first.getProgram()) + "} if (g" + curr  + " == 2) {" + (this.second.getProgram().equals("") ? ";" : this.second.getProgram()) + "}";

		//Config.GEN++;
		return result;
	}
}
