package it.univr.fsm.machine;


import java.util.HashSet;

/**
 * Created by andreaperazzoli on 21/03/17.
 */
public class MacroState {
    private State macrostate;
    private HashSet<State> states;

    public MacroState(State macrostate, HashSet<State> composedby){
        this.macrostate = macrostate;
        this.states = composedby;
    }

    public void setMacrostate(State macrostate) {
        this.macrostate = macrostate;
    }

    public void setStates(HashSet<State> states) {
        this.states = states;
    }

    public State getMacrostate() {
        return macrostate;
    }

    public HashSet<State> getStates() {
        return states;
    }

    @Override
    public int hashCode() {
        return macrostate.hashCode() + states.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MacroState){
            return macrostate.equals(((MacroState) obj).macrostate) && states.equals(((MacroState) obj).states);
        }
        return false;
    }

    @Override
    public String toString() {
        return states.toString();
    }
}
