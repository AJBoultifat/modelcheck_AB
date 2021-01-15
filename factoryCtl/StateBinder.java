
package factoryCtl;

public class StateBinder extends StateFormula {

    private StateFormula subformula;
    private String       variable;

    public StateBinder( String          variable,
                        StateFormula    subformula) {
        this.subformula = subformula;
        this.variable = variable;
    }
    
    public String getVariable() {
        return variable;
    }
    
    public int type() {
        return StateFormula.TYPE_STATE_BINDER;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public String toString() {
        return "!" + variable + "." + subformula;
    }
}
