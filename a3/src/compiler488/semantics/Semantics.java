package compiler488.semantics;

import java.io.*;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
import compiler488.ast.BasePrettyPrinter;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.expn.AnonFuncExpn;
import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.BoolExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.type.IntegerType;
import compiler488.ast.type.Type;
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.UnaryExpn;
import compiler488.ast.expn.UnaryMinusExpn;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.GetStmt;
import compiler488.ast.stmt.IfStmt;
import compiler488.ast.stmt.LoopStmt;
import compiler488.ast.stmt.LoopingStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.stmt.Program;
import compiler488.ast.stmt.PutStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.type.BooleanType;
import compiler488.symbol.SymbolTableEntry;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.MajorScope;

/** Implement semantic analysis for compiler 488
 *  @author  <B> Put your names here </B>
 */
public class Semantics implements ASTVisitor<Boolean> {

	/** flag for tracing semantic analysis */
	private boolean traceSemantics = false;
	/** file sink for semantic analysis trace */
	private String traceFile = new String();
	public FileWriter Tracer;
	public File f;
	
	public PrettyPrinter printer;
	private MajorScope scope = new MajorScope();

	/** SemanticAnalyzer constructor */
	public Semantics() {
		printer = new BasePrettyPrinter(System.err);
	}

	/** semanticsInitialize - called once by the parser at the */
	/* start of compilation */
	void Initialize() {

		/* Initialize the symbol table */

		// Symbol.Initialize();

		/*********************************************/
		/* Additional initialization code for the */
		/* semantic analysis module */
		/* GOES HERE */
		/*********************************************/

	}

	/** semanticsFinalize - called by the parser once at the */
	/* end of compilation */
	void Finalize() {

		/* Finalize the symbol table */

		// Symbol.Finalize();

		/*********************************************/
		/* Additional finalization code for the */
		/* semantics analysis module */
		/* GOES here. */
		/**********************************************/

	}

	/**
	 * Perform one semantic analysis action
	 * 
	 * @param actionNumber
	 *            semantic analysis action number
	 */
	void semanticAction(int actionNumber) {

		if (traceSemantics) {
			if (traceFile.length() > 0) {
				// output trace to the file represented by traceFile
				try {
					// open the file for writing and append to it
					File f = new File(traceFile);
					Tracer = new FileWriter(traceFile, true);

					Tracer.write("Sematics: S" + actionNumber + "\n");
					// always be sure to close the file
					Tracer.close();
				} catch (IOException e) {
					System.out.println(traceFile
							+ " could be opened/created.  It may be in use.");
				}
			} else {
				// output the trace to standard out.
				System.out.println("Sematics: S" + actionNumber);
			}

		}

		/*************************************************************/
		/* Code to implement each semantic action GOES HERE */
		/* This stub semantic analyzer just prints the actionNumber */
		/*                                                           */
		/* FEEL FREE TO ignore or replace this procedure */
		/*************************************************************/

		System.out.println("Semantic Action: S" + actionNumber);
		return;

	}

	// ADDITIONAL FUNCTIONS TO IMPLEMENT SEMANTIC ANALYSIS GO HERE

	public void outputError(Object type, String msg) {
		System.err.println(type.getClass().toString() + " ERROR: " + msg);
	}
	
	// NOTE: Semantic actions not required to implement here
	// TODO: double check these
	// all of these are done by the AST / parser
	// S03 
	// S13 
	// S14 
	// S16 
	// S23
	// S37, S39
	// S44, S45
	
	@Override
	public Boolean visit(AST node) {
		System.out.println("Hit ASTNode: " + node);
		return true;
	}

	public Boolean visit(ArrayDeclPart decl) {
		
		// TODO S19
		// TODO S46, S48
		return true;
	}
  	public Boolean visit(Declaration decl) { return true; }
  	public Boolean visit(MultiDeclarations decl) {
  		
    	if(!decl.getParts().accept(this)) {
    		return false;
    	}
  		
  		// TODO S10, S19
  		// TODO S46, S47,S48
  		return true;
  	}
  	public Boolean visit(RoutineDecl decl) {
  		
    	if(!decl.getParameters().accept(this)) {
    		return false;
    	}
    	if(decl.getBody() != null && !decl.getBody().accept(this)) {
    		return false;
    	}
  		
  		// TODO S04,S05, S08,S09
  		// TODO S11, S12
  		// TODO S15, S17, S18
  		// TODO S53
  		return true;
  	}
  	public Boolean visit(ScalarDecl decl) {
  		// S10
  		SymbolTable mostLocalTable = scope.getMostLocalScope();
  		if ( mostLocalTable.lookup(decl.getName()) == null) {
  			mostLocalTable.addEntry(decl.getName(),decl.getType(), SymbolTableEntry.Kind.SCALAR, decl, null);
  			return true;
  		}
  		return false;
  	}

  	public Boolean visit(AnonFuncExpn expn) {
  		
    	if(!expn.getBody().accept(this)) {
    		return false;
    	}
    	if(!expn.getExpn().accept(this)) {
    		return false;
    	}
  		
  		// S24
    	expn.setType(expn.getExpn().getType());
  		return true;
  	}
  	public Boolean visit(ArithExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S31
    	if (!expn.getLeft().isInteger()) {
    		outputError(expn, "left operand was not an integer");
    		return false;
    	}
    	if (!expn.getRight().isInteger()) {
    		outputError(expn, "right operand was not an integer");
    		return false;
    	}
 
  		// S21
    	expn.setType(new IntegerType());
  		return true;
  	}

	@Override
	public Boolean visit(BinaryExpn expn) {
		
    	if(!expn.getLeft().accept(this)) {
    		return false;
    	}
        if(!expn.getRight().accept(this)) {
        	return false;
        }
		
		// S32
		if (!(expn.getLeft().isType(expn.getRight().getType()))) {
			expn.getLeft().prettyPrint(printer);
			expn.getRight().prettyPrint(printer);
			outputError(expn, "not of the same types");
			return false;
		}
		
		
		
		return true;
	}
	public Boolean visit(BoolConstExpn expn) {
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
    	expn.setType(new BooleanType());
		return true;
	}
  	public Boolean visit(BoolExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S30
  		if (!expn.getLeft().isBoolean()) {
  			outputError(expn, "left operand not boolean type");
  		}
  		if (!expn.getRight().isBoolean()) {
  			outputError(expn, "right operand not boolean type");
  		}
  		// S20
  		expn.setType(new BooleanType());
  		return true;
  	}

	// S31
	@Override
	public Boolean visit(CompareExpn expn) {
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
		// S31
		if (!expn.getLeft().isInteger()) {
			outputError(expn, "left are not integers");
			return false;
		}
		if (!expn.getRight().isInteger()) {
			outputError(expn, "right are not integers");
			return false;
		}
		// S20
		expn.setType(new BooleanType());
		
		return true;
	}
	
	public Boolean visit(ConstExpn expn) {return true;}
  	public Boolean visit(EqualsExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
    	
		// S32
		if (!(expn.getLeft().isType(expn.getRight().getType()))) {
			expn.getLeft().prettyPrint(printer);
			expn.getRight().prettyPrint(printer);
			outputError(expn, "not of the same types");
			return false;
		}
    	
  		// S20
  		expn.setType(new BooleanType());
  		return true;
  	}
  
	// S36
	@Override
	public Boolean visit(FunctionCallExpn expn) {
		
		if(!expn.getArguments().accept(this)) {
			return false;
		}
		
		
		// TODO S40
		
		ASTList<ScalarDecl> parameters = new ASTList<ScalarDecl>();
		ASTList<Expn> args = expn.getArguments();

		// TODO S42
		
		// S43
		if(parameters.size() != args.size()) {
			// TODO create an error for S43
			return false;
		}
		
		// check that each argument's type matches the parameters type
		int size = parameters.size();
		for(int i = 0; i < size; i++) {
			ScalarDecl parameter = parameters.get(i);
			Expn argument = args.get(i);
			
			if(!argument.isType(parameter.getType())) {
				// TODO create an error for S36
				outputError(expn, "type of argument does nto match type of corresponding formal parameter");
				return false;
			}
		}
		
		// TODO S42: look up the entry
		SymbolTableEntry fn = new SymbolTableEntry(null, null, SymbolTableEntry.Kind.FUNCTION, null);
		
		// TODO S42: check that fn is declared as a function
		
		// S28
		expn.setType(fn.getType());
		
		return true;
	}

	// S25,S26
	@Override
	public Boolean visit(IdentExpn expn) {
		// TODO S25,S26: look up the entry
		SymbolTableEntry entry = new SymbolTableEntry(null, null, null, null);
		
		expn.setType(entry.getType());
		
		return true;
	}
	
	public Boolean visit(IntConstExpn expn) {
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
    	
    	// S21
    	expn.setType(new IntegerType());
		return true;
	}
  	public Boolean visit(NotExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S30
  		if (!expn.isBoolean()) {
  			outputError(expn, "not boolean type");
  		}
  		// S20
  		expn.setType(new BooleanType());
  		
  		return true;
  	}
  
  	public Boolean visit(SkipConstExpn expn) {
  	
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		return true;
  	}

	// S38
	@Override
	public Boolean visit(SubsExpn expn) {
		
		if(!expn.getSubscript1().accept(this)) {
			return false;
		}
		if(expn.getSubscript2() != null && !expn.getSubscript2().accept(this)) {
			return false;
		}
		
		// S31
  		if (!expn.isInteger()) {
  			outputError(expn, "not integer type");
  		}
		// TODO S38: look up the entry
		SymbolTableEntry entry = new SymbolTableEntry(null, null, SymbolTableEntry.Kind.SCALAR, null);
		
		// S38
		if (entry.getKind() != SymbolTableEntry.Kind.ARRAY) {
			outputError(expn, "The identifier " + expn.getVariable() + " was not declared as an array!");
			return false;
		}
		
		// S27
		expn.setType(entry.getType());
		
		return true;
	}
	public Boolean visit(TextConstExpn expn) {
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
		
		return true;
	}
  	public Boolean visit(UnaryExpn expn) {
  		// S30 
    	if(!expn.getOperand().accept(this)) {
    		return false;
    	}
  		// S20
  		expn.setType(new BooleanType());
  		return true;
  	}
  	public Boolean visit(UnaryMinusExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S31
  		if (!expn.isInteger()) {
  			outputError(expn, "not integer type");
  		}
  		
  		// S21
  		expn.setType(new IntegerType());
  		return true;
  	}
  	
  	public Boolean visit(AssignStmt stmt) {
    	if(!stmt.getLval().accept(this)) {
    		return false;
    	}
    	if(!stmt.getRval().accept(this)) {
    		return false;
    	}
  		
  		// S34
    	if (stmt.getLval() == null || stmt.getRval() == null ) {
    		return false;
    	}
    	if (!stmt.getLval().isType(stmt.getRval().getType())) {
    		return false;
    	}
    	
  		return true;
  	}
  	public Boolean visit(ExitStmt stmt) {
  		
    	if(stmt.getExpn() != null && !stmt.getExpn().accept(this)) {
    		return false;
    	}
  		
  		// S30	
  		if (stmt.getExpn() != null && !stmt.getExpn().isBoolean()) {
  			outputError(stmt, "statment is not a boolean type");
  			return false;
  		}
    	
    	// TODO S50
  		return true;
  	}
  	public Boolean visit(IfStmt stmt) {
  		
    	if(stmt.getCondition() != null && !stmt.getCondition().accept(this)) {
    		return false;
    	}
    	if(stmt.getWhenTrue() != null && !stmt.getWhenTrue().accept(this)) {
    		return false;
    	}
    	if(stmt.getWhenFalse() != null && !stmt.getWhenFalse().accept(this)) {
    		return false;
    	}
    	
    	// S30
    	if (!stmt.getCondition().isBoolean()) {
    		return false;
    	}
    	
  		return true;
  	}
  	public Boolean visit(LoopingStmt stmt) {
    	if(stmt.getExpn() != null && !stmt.getExpn().accept(this)) {
    		return false;
    	}
    	if(!stmt.getBody().accept(this)) {
    		return false;
    	}
  		
  		return true;
  	}
  	public Boolean visit(LoopStmt stmt) {
    	if(!stmt.parentAccept(this)) {
    		return false;
    	}
  		
  		return true;
  	}
  	public Boolean visit(ProcedureCallStmt stmt) {
  		
    	if(!stmt.getArguments().accept(this)) {
    		return false;
    	}
  		
  		// TODO S41
		// TODO S42
  		// TODO S43
  		
  		return true;
  	}
	public Boolean visit(Program stmt) {
		// TODO S00, S01
		return true;
	}
  	public Boolean visit(PutStmt stmt) {
  		
    	if(!stmt.getOutputs().accept(this)) {
    		return false;
    	}

  		return true;
  	}

	// S35
	@Override
	public Boolean visit(ReturnStmt stmt) {
		
		if(stmt.getValue() != null && !stmt.getValue().accept(this)) {
			return false;
		}
		
		// TODO S51
		
		// TODO S52
		
		// TODO S35: use the function checked by S51
		Type functionType = new IntegerType();
		
		return stmt.getValue().isType(functionType);
	}

	public Boolean visit(Scope stmt) {
		// TODO S06, S07
		// NOTE: you have to make sure that you haven't already created a function 
		// / procedure scope already since RoutineDecl will be visited before
		// this
		return true;
	}
	public Boolean visit(Stmt stmt) {return true;}
	public Boolean visit(WhileDoStmt stmt) {
    	
		if(!stmt.parentAccept(this)) {
    		return false;
    	}
		if (stmt.getExpn() != null && !stmt.getExpn().accept(this)) {
			return false;
		}
		if (stmt.getExpn() != null && !stmt.getBody().accept(this)) {
			return false;
		}
		
    	// S30
    	if (!stmt.getExpn().isBoolean()) {
    		return false;
    	}
		
		return true;
	}
  
	public Boolean visit(BooleanType type) {return true;}
	public Boolean visit(IntegerType type) {return true;}
	public Boolean visit(GetStmt stmt) {
		
		return stmt.getInputs().accept(this);
	}

  
}
