package compiler488.ast.type;


/**
 * The type of things that may be true or false.
 */
public class BooleanType extends Type {
    @Override
    public String toString() {
        return Type.BOOL_TYPE;
    }
}