package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {
    public UnaryMinusExpn(Expn operand) {
        super(UnaryExpn.OP_MINUS, operand);
    }

    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
//    	if(!super.accept(visitor)) {
//    		return false;
//    	}
        return visitor.visit(this);
    }
}
