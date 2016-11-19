package it.univr.equations;

import java.util.Vector;

import it.univr.machine.State;

/**
 * Regular expression Class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public abstract class RegularExpression {
	public abstract RegularExpression replace(State s, RegularExpression e);
	public abstract boolean containsOnly(State s);
	public abstract Vector<RegularExpression> getTermsWithState(State s);
	public abstract Vector<RegularExpression> getGroundTerms();
	public abstract boolean isGround();
	public abstract Vector<RegularExpression> inSinglePart();
	public abstract RegularExpression simplify();
	public abstract String getProgram();
}
