package it.univr.test;

/**
 * Created by andreaperazzoli on 24/01/17.
 */

import com.sun.tools.internal.xjc.model.AutoClassNameAllocator;
import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

import java.util.HashSet;
public class UnionTests {
    @Test
    public void UnionTest1(){
        Automaton a1 = Automaton.makeAutomaton("''");
        Automaton a2 = Automaton.makeAutomaton("'l'");

        Automaton union = Automaton.union(a1,a2);

        assertEquals("Not equals",union, "'l'");
    }


}
