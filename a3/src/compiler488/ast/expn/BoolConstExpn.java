package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    /** The value of the constant */
    private boolean value;

    public BoolConstExpn(boolean value) {
        super();
        
        this.setType(new BooleanType());

        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString () {
        return value ? "true" : "false";
    }

}
