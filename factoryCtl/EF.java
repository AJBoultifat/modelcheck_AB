

package factoryCtl;

public class EF extends StateFormula {

    private StateFormula subformula;

    public EF(StateFormula    subformula) {
        this.subformula = subformula;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public int type() {
        return StateFormula.TYPE_EF;
    }
    
    public String toString() {
        return "EF(" + subformula + ")";
    }
}
