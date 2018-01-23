package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class ConcatTest {

	String path = "src/test/resources/JFLAPautomata_NFA/";
	
    @Test
    public void concatTest1(){

        Automaton a1 = Automaton.makeAutomaton("a");
        Automaton a2 = Automaton.makeAutomaton("b");
        
        Automaton concat = Automaton.concat(a1, a2);
        Automaton expectedResult = Automaton.makeAutomaton("ab");
   
        assertTrue(concat.equals(expectedResult));
    }
    
    @Test
    public void concatTest2(){

        Automaton a1 = Automaton.makeAutomaton("ab");
        Automaton a2 = Automaton.makeAutomaton("ba");
        
        Automaton concat = Automaton.concat(a1, a2);
        Automaton expectedResult = Automaton.makeAutomaton("abba");
   
        assertTrue(concat.equals(expectedResult));
    }
    
    @Test
    public void concatTest3(){

        Automaton a1 = Automaton.makeEmptyString();
        Automaton a2 = Automaton.makeEmptyString();
        
        Automaton concat = Automaton.concat(a1, a2);
        Automaton expectedResult = Automaton.makeEmptyString();
   
        assertTrue(concat.equals(expectedResult));
    }
    
    @Test
    public void concatTest4() {
    	
    	// ab
    	Automaton ab = Automaton.makeAutomaton("ab");
    	
    	// dc
    	Automaton dc = Automaton.makeAutomaton("dc");
    	
    	Automaton concat = Automaton.concat(ab, dc);
    	Automaton expectedResult = Automaton.makeAutomaton("abdc");
    	
    	// abdc
    	assertTrue(concat.equals(expectedResult));
    }
}
