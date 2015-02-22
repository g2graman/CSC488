package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;


/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
    public NotExpn(Expn operand) {
        super(UnaryExpn.OP_NOT, operand);
    }

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
//    	if(!super.accept(visitor)) {
//    		return false;
//    	}
        return visitor.visit(this);
    }
    
}
