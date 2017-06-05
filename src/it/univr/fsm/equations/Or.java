package it.univr.fsm.equations;

import java.util.Vector;

import it.univr.fsm.config.Config;
import it.univr.fsm.machine.Automaton;
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
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Or) {
			return (first.equals(((Or) other).first) && second.equals(((Or) other).second))
					|| (first.equals(((Or) other).second) && second.equals(((Or) other).first));
		}
		return false;

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

	/*@Override
	public RegularExpression replace(RegularExpression e, RegularExpression with) {
		return new Or(first.replace(e, with), second.replace(e, with));
	}*/

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
		return first.factorize(e) != null && second.factorize(e) != null ? first.factorize(e) :
			e.factorize(first) != null && e.factorize(second) != null ? e.factorize(first) : null;
	}

	@Override
	public RegularExpression simplify() {

		//		// common factor: (ab + ac) = a(b+c)
		//		Vector<RegularExpression> first_part = first.inBlockPart();
		//		Vector<RegularExpression> second_part = second.inBlockPart();
		//		Vector<RegularExpression> inCommon = new Vector<>();
		//		RegularExpression result = null;
		//		RegularExpression factor = null;
		//
		//		for(int i = 0; i < first_part.size(); i++)
		//			for(int j = 0; j < second_part.size(); j++){
		//				if( (factor = first_part.get(i).factorize(second_part.get(j))) != null){
		//					inCommon.add(new Comp(factor,new Or(first_part.get(i).remove(factor),second_part.get(j).remove(factor))));
		//					first_part.remove(i);
		//					second_part.remove(j);
		//				}
		//			}
		//			if(inCommon.size() > 0) {
		//				first_part.addAll(second_part);
		//				first_part.addAll(inCommon);
		//
		//				for(RegularExpression e : first_part){
		//					result = result == null ? e : new Or(result,e);
		//				}
		//
		//				return result;
		//			}
		//
		//

		return new Or(first.simplify(), second.simplify());
	}


	public static String G = "g";

	@Override
	public String getProgram() {
		int curr = Config.GEN;
		Config.GEN++;
		
		String randomVar = G + curr;

		//String result = "g" + curr + ":=rand(); if g" + curr  + " = 1 {" + (this.first.getProgram().equals("") ? "skip;" : this.first.getProgram()) + "}; if g" + curr  + " = 2 {" + (this.second.getProgram().equals("") ? "skip;" : this.second.getProgram()) + "};";

		//		if (first.getProgram().trim().endsWith(";}") && second.getProgram().trim().endsWith(";}")) {
		//
		//			String newFirst = null;
		//			String newSecond = null;
		//			try {
		//				newFirst = first.getProgram().substring(0, first.getProgram().length() - 1);
		//				newSecond = second.getProgram().substring(0, second.getProgram().length() - 1);
		//			} catch (Exception e) {
		//				System.err.println(first.getProgram().trim());
		//				System.err.println(second.getProgram().trim());
		//			}
		//
		//			return "var g" + curr + "=rand(); if (g" + curr  + " == 1) {" + (Automaton.isJSExecutable(newFirst) ? newFirst : ";") + "} if (g" + curr  + " == 2) {" + (Automaton.isJSExecutable(newSecond) ? newSecond : ";") + "}}";
		//		} else 

		String firstProgram = first.getProgram();
		String secondProgram = second.getProgram();

		if (firstProgram.trim().startsWith(";}"))
			return firstProgram.trim().substring(2) + " var "+ randomVar + "=rand(); if (" + randomVar  + " == 1) {" + (Automaton.isJSExecutable(secondProgram) ? secondProgram : ";") + "}";
		else if (secondProgram.trim().startsWith(";}"))
			return secondProgram.trim().substring(2) +  " var " + randomVar + "=rand(); if (" + randomVar  + " == 1) {" + (Automaton.isJSExecutable(firstProgram) ? firstProgram : ";") + "}";

		return "var " + randomVar + "=rand(); if (" + randomVar  + " == 1) {" + (Automaton.isJSExecutable(firstProgram) ? firstProgram : ";") + "} if (" + randomVar  + " == 2) {" + (Automaton.isJSExecutable(secondProgram) ? secondProgram : ";") + "}";
	}

}
