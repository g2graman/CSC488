package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;


/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn {
    /**
     * The value of this literal.
     */
    private Integer value;

    public IntConstExpn(Integer value) {
        super();

        this.setType(new IntegerType());
        
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString () {
        return value.toString();
    }

}
