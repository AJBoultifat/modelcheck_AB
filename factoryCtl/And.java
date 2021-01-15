

package factoryCtl;

public class And extends StateFormula {

    private StateFormula leftSubformula;
    private StateFormula rightSubformula;

    public And( StateFormula    leftSubformula,
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
        return StateFormula.TYPE_AND;
    }
    
    public String toString() {
        return "(" + leftSubformula + " & " + rightSubformula + ")";
    }
}
