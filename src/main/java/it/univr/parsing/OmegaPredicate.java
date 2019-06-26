package it.univr.parsing;

import java.util.HashSet;

import it.univr.fsm.machine.Automaton;

public class OmegaPredicate extends HashSet<SingleOmegaPredicate> {
	
	public OmegaPredicate() {
		super();
	}
	
	public OmegaPredicate(HashSet<SingleOmegaPredicate> predicates ) {
		for (SingleOmegaPredicate s : predicates)
			this.add(s);
	}
	
	
	public OmegaPredicate lub(OmegaPredicate other) {
		// TODO Auto-generated method stub
		return null;
	}

	public Automaton evaluate() {
		// TODO Auto-generated method stub
		return null;
	}
}
