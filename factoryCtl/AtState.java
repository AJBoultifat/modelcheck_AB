

package factoryCtl;

import factoryBooleanModel.Nominal;

public class AtState extends StateFormula {

    private QuantifierSubject subject;
    private StateFormula subformula;

    public AtState( String          subjectString,
                    StateFormula    subformula) {
        subjectString = subjectString.substring(1).trim(); // discard '@' and leading spaces
        this.subject = (Character.isDigit(subjectString.charAt(0)) ?
            new Nominal(subjectString) : new Variable(subjectString));
        this.subformula = subformula;
    }
    
    public QuantifierSubject getSubject() {
        return subject;
    }
    
    public StateFormula getSubformula() {
        return subformula;
    }
    
    public int type() {
        return StateFormula.TYPE_AT_STATE;
    }
    
    public String toString() {
        return "@" + subject + ". " + subformula;
    }
}
