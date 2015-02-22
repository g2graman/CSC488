package compiler488.ast.type;

import compiler488.ast.ASTVisitor;


/**
 * Used to declare objects that yield integers.
 */
public class IntegerType extends Type {
    @Override
    public String toString() {
        return Type.INT_TYPE;
    }
    
    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
//    	if(!super.accept(visitor)) {
//    		return false;
//    	}
        return visitor.visit(this);
    }
}
