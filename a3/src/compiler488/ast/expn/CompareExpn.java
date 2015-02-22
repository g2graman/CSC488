package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;


/**
 * Place holder for all ordered comparisons expression where both operands must
 * be integer expressions.  e.g. &lt; , &gt;  etc. comparisons
 */
public class CompareExpn extends BinaryExpn {
    public final static String OP_LESS 			= "<";
    public final static String OP_LESS_EQUAL 	= "<=";
    public final static String OP_GREATER 		= ">";
    public final static String OP_GREATER_EQUAL	= ">=";

    public CompareExpn(String opSymbol, Expn left, Expn right) {
        super(opSymbol, left, right);

        assert ((opSymbol == OP_LESS) ||
                (opSymbol == OP_LESS_EQUAL) ||
                (opSymbol == OP_GREATER) ||
                (opSymbol == OP_GREATER_EQUAL));
    }

    
    @Override
    public Boolean accept(ASTVisitor<Boolean> visitor) {
    	if(!super.accept(visitor)) {
    		return false;
    	}
        
    	if(!visitor.visit(this.left)) {
    		return false;
    	}
        if(!visitor.visit(this.right)) {
        	return false;
        }
    	
        return visitor.visit(this);
    }
}