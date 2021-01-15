

package factoryBooleanModel;

import java.math.BigInteger;

import factoryCtl.QuantifierSubject;
import factoryCtl.StateFormula;
import factoryManager.NominalCounter;

public class Nominal extends StateFormula implements QuantifierSubject {

    private String stringRepresentation;
    private BigInteger value;
    private NominalCounter counter;

    public Nominal(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
        String tmp = stringRepresentation.toLowerCase();
        if ( tmp.startsWith("0x")) {
            value = new BigInteger(tmp.substring(2), 16);
        } else if ( tmp.startsWith("0b")) {
            value = new BigInteger(tmp.substring(2), 2);
        } else {
            value = new BigInteger(tmp, 10);
        }
        counter = new NominalCounter(value);
    }

    public Nominal(BigInteger value) {
        this.value = value;
        this.stringRepresentation = value.toString();
        counter = new NominalCounter(value);
    }

    public Nominal(NominalCounter counter) {
        this.counter = counter;
    }
    
    public boolean testBit(int n) {
        if (value == null) return counter.testBit(n);
        else  return value.testBit(n);
    }

    public int type() {
        return StateFormula.TYPE_NOMINAL;
    }
    
    public String toString() {
        if (stringRepresentation == null) return counter.toString();
        return stringRepresentation;
    }
}
