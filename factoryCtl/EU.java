

package factoryCtl;

public class EU extends StateFormula {

    private StateFormula leftSubformula;
    private StateFormula rightSubformula;

    public EU(  StateFormula    leftSubformula,
                StateFormula    rightSubformula) {
        this.leftSubformula  = leftSubformula;
        this.rightSubformula = rightSubformula;
    }
    
    public StateFormula getLeftSubformula() {
        return leftSubformula;
    }
    
    public StateFormula getRightSubformula() {
        return rightSubformula;
    }
    
    public int type() {
        return StateFormula.TYPE_EU;
    }
    
    public String toString() {
        return "E[" + leftSubformula + " U " + rightSubformula + "]";
    }
}
