package factoryCtl;

public class Or extends StateFormula {

    private StateFormula leftSubformula;
    private StateFormula rightSubformula;

    public Or(  StateFormula    leftSubformula,
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
        return StateFormula.TYPE_OR;
    }
    
    public String toString() {
        return "(" + leftSubformula + " | " + rightSubformula + ")";
    }
}
