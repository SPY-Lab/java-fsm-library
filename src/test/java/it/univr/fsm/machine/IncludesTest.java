package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class IncludesTest {

    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final int TOPBOOL = -1;

    @Test
    public void includesTest000(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("paguro"));
        set.add(Automaton.makeAutomaton("koala"));

        Automaton a = Automaton.union(set);

        Automaton sub = Automaton.union(Automaton.makeAutomaton("pan"), Automaton.makeAutomaton("koala"));

        Assert.assertEquals(Automaton.includes(a, sub), TOPBOOL);
    }

    @Test
    public void includesTest001() {
        Automaton automaton = Automaton.makeAutomaton("panda?#@");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sansa"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("manga"));

        Automaton search = Automaton.makeAutomaton("an");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest002() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sansa"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("manga"));

        Automaton search = Automaton.makeAutomaton("an");
        search = Automaton.union(search, Automaton.makeAutomaton("p"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest003() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sansa"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("manga"));

        Automaton search = Automaton.makeAutomaton("koala");

        Assert.assertEquals(Automaton.includes(automaton, search), FALSE);
    }

    @Test
    public void includesTest004() {
        Automaton automaton = Automaton.makeAutomaton("panda!mc");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("mc!papanda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("polo!mc!panda"));

        Automaton search = Automaton.makeAutomaton("panda");
        search = Automaton.union(search, Automaton.makeAutomaton("mc"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest005() {
        Automaton automaton = Automaton.makeAutomaton("panda!mc");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("mc!papanda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("polopanda"));

        Automaton search = Automaton.makeAutomaton("panda");
        search = Automaton.union(search, Automaton.makeAutomaton("mc"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest006() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandone"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandina"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandetta"));

        Automaton search = Automaton.makeAutomaton("pa");
        search = Automaton.union(search, Automaton.makeAutomaton("pan"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest007() {
        Automaton automaton = Automaton.makeAutomaton("ronda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("manga"));

        Automaton search = Automaton.makeAutomaton("an");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest008() {
        Automaton automaton = Automaton.makeAutomaton("pandaat");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandamat"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pansarat"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pansasat"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("koladat"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("kolabato"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("kosalata"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("kosanaat"));

        Automaton search = Automaton.makeAutomaton("at");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest009() {
        Automaton automaton = Automaton.makeAutomaton("pan");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandk"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panck"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panrk"));

        Automaton search = Automaton.makeAutomaton("an");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest010() {
        Automaton automaton = Automaton.makeAutomaton("pan");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandk"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panck"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panrk"));

        Automaton search = Automaton.makeAutomaton("k");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest011() {
        Automaton automaton = Automaton.makeAutomaton("pan");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandk"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panck"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panrw"));

        Automaton search = Automaton.makeAutomaton("k");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest012() {
        Automaton automaton = Automaton.makeAutomaton("panda");

        Automaton search = Automaton.makeAutomaton("da");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest013() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("nda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("a"));

        Automaton search = Automaton.makeAutomaton("nda");
        search = Automaton.union(search, Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest014() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("anda"));

        Automaton search = Automaton.makeAutomaton("nda");
        search = Automaton.union(search, Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest015() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("anda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("orda"));

        Automaton search = Automaton.makeAutomaton("nda");
        search = Automaton.union(search, Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest016() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("koala"));

        Automaton search = Automaton.makeAutomaton("nda");
        search = Automaton.union(search, Automaton.makeAutomaton("ala"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest017() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("anda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("nda"));

        Automaton search = Automaton.makeAutomaton("nda");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest018() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pand"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("nd"));

        Automaton search = Automaton.makeAutomaton("panda");
        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest019() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pand"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("nd"));

        Automaton search = Automaton.makeAutomaton("panda");
        search = Automaton.union(search, Automaton.makeAutomaton("anda"));
        search = Automaton.union(search, Automaton.makeAutomaton("da"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest020() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pand"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("nd"));

        Automaton search = Automaton.makeAutomaton("panda");
        search = Automaton.union(search, Automaton.makeAutomaton("pand"));
        search = Automaton.union(search, Automaton.makeAutomaton("nd"));
        search = Automaton.union(search, Automaton.makeAutomaton("d"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest021() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));

        Automaton search = Automaton.makeAutomaton("panda");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest022() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandapanda"));

        Automaton search = Automaton.makeAutomaton("panda");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest023() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandapanda"));

        Automaton search = Automaton.makeAutomaton("pandapanda");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest024() {
        Automaton automaton = Automaton.makeAutomaton("panda");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("pandapanda"));

        Automaton search = Automaton.makeAutomaton("pandaprtretanda");

        Assert.assertEquals(Automaton.includes(automaton, search), FALSE);
    }

    @Test
    public void includesTest025() {
        Automaton automaton = Automaton.makeAutomaton("ordine");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ordine"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ordine"));

        Automaton search = Automaton.makeAutomaton("ine");
        search = Automaton.union(search, Automaton.makeAutomaton("dine"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest026() {
        Automaton automaton = Automaton.makeAutomaton("ordine");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ordine"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sordine"));

        Automaton search = Automaton.makeAutomaton("ine");
        search = Automaton.union(search, Automaton.makeAutomaton("dine"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest027() {
        Automaton automaton = Automaton.makeAutomaton("ordine");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ordine"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sordine"));

        Automaton search = Automaton.makeAutomaton("e");
        search = Automaton.union(search, Automaton.makeAutomaton("e"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest028() {
        Automaton automaton = Automaton.makeAutomaton("ordine");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ordine"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("sordine"));

        Automaton search = Automaton.makeAutomaton("");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest029() {
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        State q0 = new State("q0", true, true);
        states.add(q0);
        delta.add(new Transition(q0, q0, "a"));

        Automaton automaton = new Automaton(delta, states); // Automaton: a*
        Automaton search = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest030() {
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        State q0 = new State("q0", true, true);
        states.add(q0);
        delta.add(new Transition(q0, q0, "a"));

        Automaton automaton = Automaton.makeAutomaton("a");
        Automaton search = new Automaton(delta, states); // Automaton: a*

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest031() {
        Automaton automaton = Automaton.makeAutomaton("");

        Automaton search = Automaton.makeAutomaton("e");

        Assert.assertEquals(Automaton.includes(automaton, search),FALSE);
    }

    @Test
    public void includesTest032() {
        Automaton automaton = Automaton.makeAutomaton("idea");

        Automaton search = Automaton.makeAutomaton("idea");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest033() {
        Automaton automaton = Automaton.makeAutomaton("idea2");

        Automaton search = Automaton.makeAutomaton("idea");

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest034() {
        Automaton automaton = Automaton.makeAutomaton("idea");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("rideva"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("intrinseca"));

        Automaton search = Automaton.makeAutomaton("ea");
        search = Automaton.union(search, Automaton.makeAutomaton("va"));
        search = Automaton.union(search, Automaton.makeAutomaton("ca"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest035() {
        Automaton automaton = Automaton.makeAutomaton("ea");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ea"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("ea"));

        Automaton search = Automaton.makeAutomaton("ea");
        search = Automaton.union(search, Automaton.makeAutomaton("va"));
        search = Automaton.union(search, Automaton.makeAutomaton("ca"));

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest036() {
        Automaton automaton = Automaton.makeAutomaton("pandapanda");

        Automaton search = Automaton.makeAutomaton("panda");
        search = Automaton.union(search, Automaton.makeAutomaton("da"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest037() {
        Automaton automaton = Automaton.makeAutomaton("pandapandapandapanda");

        Automaton search = Automaton.makeAutomaton("pandapanda");
        search = Automaton.union(search, Automaton.makeAutomaton("panda"));

        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }

    @Test
    public void includesTest038() {
        Automaton automaton = Automaton.makeAutomaton("ra");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("bsdifido4v9jsdflsdipa"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("w38ryfw9j8rghwpaksvn8"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("f83n8s8ofrnsd3ruopagg"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("siskehrsnoge8ur30ungr0rfju0jsn39g9rj90sweurf9sn0geruwu4erheotjhfddiogpa9"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("ais8ydbnhiesfnowrnsh98eon3fwhrs9uernhspai8ne4rth9osd9f8fwo98n8hrodsfnoson"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("paksigny43o9shyfroeypangei8tr7g7s8of7os8herpanl8yef8tdn"));

        //AutomatonViewer.show(automaton);

        Automaton search = Automaton.makeAutomaton("pa");

        Assert.assertEquals(Automaton.includes(automaton, search), TOPBOOL);
    }

    @Test
    public void includesTest039() {
        Automaton automaton = Automaton.makeAutomaton("an");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("bsdifido4v9jsdflsdipa"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("w38ryfw9j8rghwpaksvn8"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("f83n8s8ofrnsd3ruopagg"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("siskehrsnoge8ur30ungr0rfju0jsn39g9rj90sweurf9sn0geruwu4erheotjhfddiogpa9"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("ais8ydbnhiesfnowrnsh98eon3fwhrs9uernhspai8ne4rth9osd9f8fwo98n8hrodsfnoson"));
        automaton = Automaton.union(automaton,
                Automaton.makeAutomaton("paksigny43o9shyfroeypangei8tr7g7s8of7os8herpanl8yef8tdn"));

        Automaton search = Automaton.makeAutomaton("tiger99");

        Assert.assertEquals(Automaton.includes(automaton, search), FALSE);
    }
    
    @Test
    public void includesTest040() {
        Automaton automaton = Automaton.makeAutomaton("land");
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("panda"));
        automaton = Automaton.union(automaton, Automaton.makeAutomaton("and"));
        
        Automaton search = Automaton.makeAutomaton("an");
        search = Automaton.union(search, Automaton.makeAutomaton("nd"));
        
        Assert.assertEquals(Automaton.includes(automaton, search), TRUE);
    }
    

    @Test
    public void includesTest041() {
        Automaton automaton = Automaton.star(Automaton.makeAutomaton("land"));
       
        Assert.assertEquals(Automaton.includes(automaton, Automaton.makeAutomaton("c")), FALSE);
    }
}