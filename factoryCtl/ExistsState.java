

package factoryCtl;

public class ExistsState extends StateFormula {

    private String variable;
    private StateFormula subformula;

    public ExistsState( String variable,
                        StateFormula    subformula) {
        this.variable = variable;
        this.subformula = subformula;
    }
    
    public String getVariable() {
        return variable;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public int type() {
        return StateFormula.TYPE_EXISTS_STATE;
    }
    
    public String toString() {
        return "]" + variable + ". " + subformula;
    }
}
