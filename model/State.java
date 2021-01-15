package model;

import java.util.HashSet;
import java.util.Set;
import ctlFormula.Atom;

public class State {
    private String name;
    private Set<Atom> truePredicates = new HashSet<>();

    public State(String name, String... atomicPredicates) {
        this.name = name;
        for (String atomicPredicate : atomicPredicates) {
            truePredicates.add(new Atom(atomicPredicate));
        }
    }

    public boolean satisfies(Atom predicate) {
        return truePredicates.contains(predicate);
    }

    public String toString() {
        return name;
    }
}
