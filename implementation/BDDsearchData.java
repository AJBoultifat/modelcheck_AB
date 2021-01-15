package implementation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import factoryManager.ProgramErrorException;
import model.State;
import net.sf.javabdd.BDDFactory;

public class BDDsearchData {

    private int ds = 0;
    private static BDDFactory factory = null;

    private Map<State, Integer> dss = new HashMap<>();

    private Map<State, Integer> ll = new HashMap<>();

    private Stack<State> ss = new Stack<>();

    public void visit(State state) {
        ss.push(state);
        setDfs(state, ds);
        setLowLink(state, ds);
        ds++;
    }

    public boolean isVisited(State state) {
        return dss.containsKey(state);
    }

    public int getDfs(State state) {
        return dss.getOrDefault(state, 0);
    }

    public boolean dfsEqualsLowLink(State state) {
        return getDfs(state) == getLowLink(state);
    }

    public void setLowLink(State state, int lowlink) {
        this.ll.put(state, lowlink);
    }

    public int getLowLink(State state) {
        return ll.getOrDefault(state, 0);
    }

    public boolean isOnStack(State state) {
        return ss.contains(state);
    }

    public State popFromStack() {
        return ss.pop();
    }

    public void removeFromStack(State state) {
        ss.remove(state);
    }

    public int getMaxDfs() {
        return ds;
    }

    public String getStackAsString() {
        return ss.toString();
    }

    private void setDfs(State state, int dfs) {
        this.dss.put(state, dfs);
    }
    
    public static BDDFactory initFactory() {
        if (factory != null) {
            factory.done();
        }
        if (factory == null) {
            throw new ProgramErrorException("There is no BDD implementation available");
        }
        return factory;
    }
}
