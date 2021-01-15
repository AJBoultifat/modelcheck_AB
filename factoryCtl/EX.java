

package factoryCtl;

public class EX extends StateFormula {

    private StateFormula subformula;

    public EX(StateFormula    subformula) {
        this.subformula = subformula;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public int type() {
        return StateFormula.TYPE_EX;
    }
    
    public String toString() {
        return "EX(" + subformula + ")";
    }
}
