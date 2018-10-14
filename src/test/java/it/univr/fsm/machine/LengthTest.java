package it.univr.fsm.machine;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

public class LengthTest {
	
    @Test
    public void lengthTest001(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("marco"));
        Automaton a = Automaton.union(set);

        int result = Automaton.length(a);

        Assert.assertEquals(result, 5);
    }  
    
    @Test
    public void lengthTest002(){
    	Automaton a = Automaton.star(Automaton.makeAutomaton("a"));
        int result = Automaton.length(a);

        Assert.assertEquals(result, -1);
    }
    
    @Test
    public void lengthTest003(){
    	Automaton a = Automaton.star(Automaton.makeAutomaton("panda"));
        int result = Automaton.length(a);

        Assert.assertEquals(result, -1);
    }

    @Test
    public void lengthTest004(){
    	Automaton a = Automaton.union(Automaton.makeAutomaton("panda"), Automaton.makeAutomaton("abc"));
        int result = Automaton.length(a);

        Assert.assertEquals(result, -1);
    }
}
