package compiler488.semantics;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
import compiler488.ast.BasePrettyPrinter;
import compiler488.ast.PrettyPrintable;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
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
	
	private LinkedList<MajorScope> scopes;
	private boolean isProgramScope = true;

	/** SemanticAnalyzer constructor */
	public Semantics() {
		scopes = new LinkedList<MajorScope>();
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
	
	public void printAllSymbolTables() {
		for(MajorScope scope : scopes) {
			System.out.println(scope);
		}
	}
	
	public SymbolTableEntry lookup(String varname, boolean local) {
		if(local) {
			return scopes.getFirst().lookup(varname);
		}
		
		for(MajorScope scope : scopes) {
			SymbolTableEntry entry = scope.lookup(varname);
			if(entry != null) {
				return entry;
			}
		}
		return null;
	}
	
	public void addEntry(String varname, Type type, SymbolTableEntry.Kind kind, AST node) {
		if(scopes.isEmpty()) {
			return;
		}
		scopes.getFirst().addEntry(varname, type, kind, node);
	}
	
	public void enterScope(MajorScope.ScopeKind kind, RoutineDecl routine) {
		scopes.addFirst(new MajorScope(kind, routine));
	}
	
	public void exitScope() {
		scopes.removeFirst();
	}
	
	public String prettyPrintToString(PrettyPrintable printable) {
		ByteArrayOutputStream printerStream = new ByteArrayOutputStream();
		PrettyPrinter printer = new BasePrettyPrinter(new PrintStream(printerStream));
		printable.prettyPrint(printer);
		
		return printerStream.toString();
	}

	public void outputError(AST ast, String format, Object... inputArgs) {
		List<Object> args = new ArrayList<Object>();
		args.add(ast.getLine());
		args.add(ast.getCol());
		args.addAll(Arrays.asList(inputArgs));
		
		String errorMessage = String.format("ERROR (line: %d, col %d): " + format, args.toArray());
		System.err.println(errorMessage);
	}
	
	public void outputTypeError(Expn expn, Type desiredType) {
		outputError(expn, "%s is not a(n) %s", prettyPrintToString(expn), desiredType);
	}
	
	public void outputAlreadyDeclaredError(AST ast, String name) {
		SymbolTableEntry entry = lookup(name, false);
		
		outputError(ast, "'%s' is already declared on line %d", name, entry.getNode().getLine());
	}
	
	public void outputNotDeclaredError(AST ast, String name, SymbolTableEntry.Kind kind) {
		outputError(ast, "'%s' is not declared as a(n) %s", name, kind);
	}
	
	public void outputNotDeclaredError(AST ast, String name) {
		outputError(ast, "'%s' was not declared", name);
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

	@Override
	public <A extends AST> Boolean visit(ASTList<A> list) {
		Boolean bools = true;
		for(A node : list) {
			// needs to be out to prevent short circuit (if bools is false, node.accept won't be called
			// if it is bools = bools && node.accept
			Boolean z = node.accept(this);
			bools = bools && z;
		}
		return bools;
	}

	public Boolean visit(ArrayDeclPart decl) {
        // S19 S48
        SymbolTableEntry entry = lookup(decl.getName(), false);
        if (entry == null){
            addEntry(decl.getName(), decl.getType(), SymbolTableEntry.Kind.ARRAY, decl);
        } else {
        	outputAlreadyDeclaredError(decl, decl.getName());
            return false;
        }

        // S46
        if (decl.getLowerBoundary1() > decl.getUpperBoundary1()){
            outputError(decl, "Array (%s %s) lower bound (%d) is greater than upper bound (%d)!",
            		decl.getType(),
            		prettyPrintToString(decl),
            		decl.getLowerBoundary1(), decl.getUpperBoundary1());
            return false;
        }
        if (decl.isTwoDimenstional()){
            if (decl.getLowerBoundary2() > decl.getUpperBoundary2()){
                outputError(decl, "Array (%s %s) lower bound (%d) is greater than upper bound (%d)!",
                		decl.getType(),
                		prettyPrintToString(decl),
                		decl.getLowerBoundary2(), decl.getUpperBoundary2());
                return false;
            }
        }

		return true;
	}
  	public Boolean visit(Declaration decl) { return true; }
  	
  	public Boolean visit(MultiDeclarations decl) {
  		ASTList<DeclarationPart> parts = decl.getParts();
  		
  		Boolean declarationsValid = true;
  		for(DeclarationPart part : parts) {
  			if(part instanceof ScalarDeclPart) {
  				ScalarDecl scalarDecl = new ScalarDecl(part.getName(), decl.getType());
  				scalarDecl.setLocation(part.getLine(), part.getCol());
  				
  				Boolean scalar = this.visit(scalarDecl);
  				declarationsValid = declarationsValid && scalar;
  			} else {
  				ArrayDeclPart arrayDecl = (ArrayDeclPart) part;
  				arrayDecl.setType(decl.getType());
  				
  				Boolean array = this.visit(arrayDecl);
  				declarationsValid = declarationsValid && array;
  			}
  		}
  		
        return declarationsValid;
  	}
  	
  	public Boolean visit(RoutineDecl decl) {
  		// S11, S12
    	
        // S17, S18
        if (decl.getType() == null) { 
        	// procedure has no return type
	        if (lookup(decl.getName(), false) == null){
	            addEntry(decl.getName(), decl.getType(), SymbolTableEntry.Kind.PROCEDURE, decl);
	        } else {
	            outputAlreadyDeclaredError(decl, decl.getName());
	            return false;
	        }
        } else { 
        	// function
	        if (lookup(decl.getName(), false) == null){
	            addEntry(decl.getName(), decl.getType(), SymbolTableEntry.Kind.FUNCTION, decl);
	        } else {
	            outputAlreadyDeclaredError(decl, decl.getName());
	            return false;
	        }
	    }
        
  		// S04, S08
  		MajorScope.ScopeKind kind = decl.getType() == null ? MajorScope.ScopeKind.PROCEDURE : MajorScope.ScopeKind.FUNCTION;
  		enterScope(kind, decl);
        
    	// S15, S16
    	ASTList<ScalarDecl> parameters = decl.getParameters();
    	boolean declParameters = true;
    	for(ScalarDecl parameter : parameters) {
      		// S10
      		if ( lookup(parameter.getName(), true) == null) {
      			addEntry(parameter.getName(), parameter.getType(), SymbolTableEntry.Kind.SCALAR, parameter);
      		} else {
                outputAlreadyDeclaredError(parameter, parameter.getName());
      			declParameters = false;
      		}
    	}
    	
    	if(!declParameters) {
    		return false;
    	}
    	
    	// check the body
    	ASTList<Stmt> declBody = decl.getBody().getBody();
    	
    	boolean declAcceptBody = true; //Default to true on empty body
    	if(declBody != null) {
    		declAcceptBody = decl.getBody().accept(this);
    	}

  		// TODO S53
    	
    	// S05, S09
    	exitScope();

  		return declAcceptBody;
  	}
  	public Boolean visit(ScalarDecl decl) {
  		// S10
  		if ( lookup(decl.getName(), false) == null) {
  			addEntry(decl.getName(),decl.getType(), SymbolTableEntry.Kind.SCALAR, decl);
  			return true;
  		}
        outputAlreadyDeclaredError(decl, decl.getName());
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
  		
    	if(!this.visit((BinaryExpn) expn)) {
    		return false;
    	}
  		
  		// S31
    	if (!expn.getLeft().isInteger()) {
    		outputTypeError(expn.getLeft(), new IntegerType());
    		return false;
    	}
    	if (!expn.getRight().isInteger()) {
    		outputTypeError(expn.getRight(), new IntegerType());
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
			outputError(expn, "(%s) and (%s) are not the same type!",
					prettyPrintToString(expn.getLeft()), prettyPrintToString(expn.getRight()));
			return false;
		}
		
		return true;
	}
	
	public Boolean visit(BoolConstExpn expn) {
		// S20
    	expn.setType(new BooleanType());
		return true;
	}
	
  	public Boolean visit(BoolExpn expn) {
  		
    	if(!this.visit((BinaryExpn)expn)) {
    		return false;
    	}
  		
  		// S30
  		if (!expn.getLeft().isBoolean()) {
    		outputTypeError(expn.getLeft(), new BooleanType());
  		}
  		if (!expn.getRight().isBoolean()) {
    		outputTypeError(expn.getRight(), new BooleanType());
  		}
  		// S20
  		expn.setType(new BooleanType());
  		return true;
  	}

	// S31
	@Override
	public Boolean visit(CompareExpn expn) {

    	if(!this.visit((BinaryExpn)expn)) {
    		return false;
    	}
    	
		// S31
    	if (!expn.getLeft().isInteger()) {
    		outputTypeError(expn.getLeft(), new IntegerType());
    		return false;
    	}
    	if (!expn.getRight().isInteger()) {
    		outputTypeError(expn.getRight(), new IntegerType());
    		return false;
    	}
    	
		// S20
		expn.setType(new BooleanType());
		
		return true;
	}
	
	public Boolean visit(ConstExpn expn) { return true; }
	
  	public Boolean visit(EqualsExpn expn) {
		// S32
    	if(!this.visit((BinaryExpn)expn)) {
    		return false;
    	}
    	
  		// S20
  		expn.setType(new BooleanType());
  		return true;
  	}
  
	// S36
	@Override
	public Boolean visit(FunctionCallExpn expn) {
		// S40
        SymbolTableEntry entry = lookup(expn.getIdent(), false);
        if (entry == null) {
            outputNotDeclaredError(expn, expn.getIdent());
            return false;
        }
        
        if (entry.getKind() != SymbolTableEntry.Kind.FUNCTION){
            outputNotDeclaredError(expn, expn.getIdent(), SymbolTableEntry.Kind.FUNCTION);
            return false;
        }
		
		if(!expn.getArguments().accept(this)) {
			return false;
		}
        
        RoutineDecl decl = (RoutineDecl) entry.getNode();
		ASTList<ScalarDecl> parameters = decl.getParameters();
		ASTList<Expn> args = expn.getArguments();
		
		// S42 S43
		if(parameters.size() != args.size()) {
			outputError(expn, "The number of arguments do not match the number of parameters!");
			outputError(args, "%s", prettyPrintToString(args));
			outputError(parameters, "%s %s (%s)", decl.getType(), decl.getName(), parameters);
			return false;
		}
		
		// check that each argument's type matches the parameters type
		int size = parameters.size();
		for(int i = 0; i < size; i++) {
			ScalarDecl parameter = parameters.get(i);
			Expn argument = args.get(i);
			
			if(!argument.isType(parameter.getType())) {
				outputError(argument, "The type of the argument %s does not match the parameter %s %s", 
						prettyPrintToString(argument), parameter.getType(), parameter.getName());
				outputTypeError(argument, parameter.getType());
				return false;
			}
		}

		
		// S28
		expn.setType(decl.getType());
		
		return true;
	}

	// S25,S26
	@Override
	public Boolean visit(IdentExpn expn) {
		SymbolTableEntry entry = lookup(expn.getIdent(), false);
		
		if(entry == null) {
			// TODO error message for S25, S26
			outputNotDeclaredError(expn, expn.getIdent());
			
			return false;
		}
		expn.setType(entry.getType());
		
		return true;
	}
	
	public Boolean visit(IntConstExpn expn) {
		// this is quite annoying in the output :P
//    	if(!expn.parentAccept(this)) {
//    		return false;
//    	}
    	
    	// S21
    	expn.setType(new IntegerType());
		return true;
	}
  	public Boolean visit(NotExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S30
  		if (!expn.getOperand().isBoolean()) {
  			outputError(expn, "%s is not boolean type", prettyPrintToString(expn.getOperand()));
  		}
  		// S20
  		expn.setType(new BooleanType());
  		
  		return true;
  	}
  
  	public Boolean visit(SkipConstExpn expn) { return true; }

	// S38
	@Override
	public Boolean visit(SubsExpn expn) {
		// S38
		// check if it was declared as an array
		SymbolTableEntry entry = lookup(expn.getVariable(), false);
		
		if (entry == null) {
			outputNotDeclaredError(expn, expn.getVariable(), SymbolTableEntry.Kind.ARRAY);
			return false;
		}
		if (entry.getKind() != SymbolTableEntry.Kind.ARRAY) {
			outputNotDeclaredError(expn, expn.getVariable(), SymbolTableEntry.Kind.ARRAY);
			return false;
		}
		
		// check the first subscript
		if(!expn.getSubscript1().accept(this)) {
			return false;
		}
		
		// S31
  		if (!expn.getSubscript1().isInteger()) {
  			outputTypeError(expn.getSubscript1(), new IntegerType());
  		}
		
  		// check the second subscript
		if(expn.getSubscript2() != null) {
			if(!expn.getSubscript2().accept(this)) {
				return false;
			}
			// S31
	  		if (!expn.getSubscript2().isInteger()) {
	  			outputTypeError(expn.getSubscript2(), new IntegerType());
	  		}
	  		
			return false;
		}
		
		
		// S27
		expn.setType(entry.getType());
		
		return true;
	}
	public Boolean visit(TextConstExpn expn) { return true; }
  	public Boolean visit(UnaryExpn expn) {
  		// S30 
    	if(!expn.getOperand().accept(this)) {
    		return false;
    	}
  		return true;
  	}
  	public Boolean visit(UnaryMinusExpn expn) {
  		
    	if(!expn.parentAccept(this)) {
    		return false;
    	}
  		
  		// S31
  		if (!expn.getOperand().isInteger()) {
  			outputError(expn, "%s is not an integer!", prettyPrintToString(expn));
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
  			outputTypeError(stmt.getExpn(), new BooleanType());
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
  			outputTypeError(stmt.getCondition(), new BooleanType());
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
  	public Boolean visit(LoopStmt stmt) { return true; }
  	public Boolean visit(ProcedureCallStmt stmt) {
  		
    	if(!stmt.getArguments().accept(this)) {
    		return false;
    	}
  		
  		// S41
  		SymbolTableEntry result = lookup(stmt.getName(), false);
  		if ( result == null ) {
  			printAllSymbolTables();
  			outputNotDeclaredError(stmt, stmt.getName());
  			return false;
  		} else {
  			if ( result.getKind() != SymbolTableEntry.Kind.PROCEDURE ) {
  				outputNotDeclaredError(stmt, stmt.getName(), SymbolTableEntry.Kind.PROCEDURE);
  				return false;
  			}
  		}

  		// S42 S43
  		ASTList<Expn> arguments = stmt.getArguments();
  		RoutineDecl decl = (RoutineDecl) result.getNode();
  		ASTList<ScalarDecl> parameters = decl.getParameters();
  		if (parameters.size() != arguments.size()) {
  			outputError(stmt, "parameter count mismatch");
  			return false;
  		}
  		
  		return true;
  	}
  	
	public Boolean visit(Program stmt) {
		if(stmt.getBody() == null) {
			return true;
		}
		
		// S00, S01
		return this.visit( (Scope) stmt );
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
		
		if(stmt.getValue() != null) {
			// TODO S35: use the function checked by S51
			Type functionType = new IntegerType();
			Boolean isCorrectType = stmt.getValue().isType(functionType);
			
			if(!isCorrectType) {
				outputTypeError(stmt.getValue(), functionType);
			}
		}
		
		return true;
	}

	public Boolean visit(Scope stmt) {
		// TODO S06, S07

		//Precondition: haven't already created a function 
		// / procedure scope already since RoutineDecl will be visited before this
		
		MajorScope.ScopeKind kind = MajorScope.ScopeKind.NORMAL;
		if(isProgramScope) {
			kind = MajorScope.ScopeKind.PROGRAM;
			isProgramScope = false;
		}
		enterScope(kind, null);
		
		ASTList<Stmt> body = stmt.getBody();
		boolean result = true;
		if(body != null) {
			result = body.accept(this);
		}
		
		exitScope();
		
		return result;
	}
	
	public Boolean visit(Stmt stmt) {return true;}
	public Boolean visit(WhileDoStmt stmt) {
    	
    	if(!this.visit((LoopingStmt)stmt)) {
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
    		outputTypeError(stmt.getExpn(), new BooleanType());
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
