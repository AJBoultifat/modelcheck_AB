
package factoryBooleanModel;

public class BooleanConstant extends Formula {

    private boolean value;

    public BooleanConstant(boolean     value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public String toString() {
        return "" + value;
    }
}
