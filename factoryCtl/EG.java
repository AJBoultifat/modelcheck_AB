

package factoryCtl;

public class EG extends StateFormula {

    private StateFormula subformula;

    public EG(StateFormula    subformula) {
        this.subformula = subformula;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public int type() {
        return StateFormula.TYPE_EG;
    }
    
    public String toString() {
        return "EG(" + subformula + ")";
    }
}
