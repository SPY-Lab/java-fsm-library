package it.univr.fsm.equations;

import java.util.Objects;
import java.util.Vector;

import it.univr.fsm.machine.State;

/**
 * Regular expression Class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public abstract class RegularExpression extends Object{
	public abstract RegularExpression replace(State s, RegularExpression e);
//	public abstract RegularExpression replace(RegularExpression e, RegularExpression with);
	public abstract boolean containsOnly(State s);
	public abstract boolean contains(State s);
	public abstract Vector<RegularExpression> getTermsWithState(State s);
	public abstract Vector<RegularExpression> getGroundTerms();
	public abstract boolean isGround();
	public abstract Vector<RegularExpression> inSinglePart();

	public abstract RegularExpression simplify();
	public abstract String getProgram();

	public abstract Vector<RegularExpression> inBlockPart();
	public abstract RegularExpression syntetize(State s);


	public abstract int hashCode();
	public abstract boolean equals(Object other);
}
