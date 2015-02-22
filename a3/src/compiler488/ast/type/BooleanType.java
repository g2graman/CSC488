package compiler488.ast.type;

import compiler488.ast.ASTVisitor;


/**
 * The type of things that may be true or false.
 */
public class BooleanType extends Type {
    @Override
    public String toString() {
        return Type.BOOL_TYPE;
    }
    
    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
//    	if(!super.accept(visitor)) {
//    		return false;
//    	}
        return visitor.visit(this);
    }
}
