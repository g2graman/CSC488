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
import compiler488.symbol.SymbolTableEntry;
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

/** Implement semantic analysis for compiler 488
 *  @author  <B> Put your names here </B>
>>>>>>> a5c6e0db5afb6b2be3084771ae2509dc2ad0a4c8
 */
public class Semantics implements ASTVisitor<Boolean> {

	/** flag for tracing semantic analysis */
	private boolean traceSemantics = false;
	/** file sink for semantic analysis trace */
	private String traceFile = new String();
	public FileWriter Tracer;
	public File f;
	
	public PrettyPrinter printer;

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

	// NOTE: S37 and S39 I don't think we need to do.
	
	@Override
	public Boolean visit(AST node) {
		System.out.println(node);
		return true;
	}

	public Boolean visit(ArrayDeclPart decl) {return true;}
  	public Boolean visit(Declaration decl) {return true;}
  	public Boolean visit(MultiDeclarations decl) {return true;}
  	public Boolean visit(RoutineDecl decl) {return true;}
  	public Boolean visit(ScalarDecl decl) {return true;}

  	public Boolean visit(AnonFuncExpn expn) {return true;}
  	public Boolean visit(ArithExpn expn) {return true;}

	// S32
	@Override
	public Boolean visit(BinaryExpn expn) {
		// 
		if (!(expn.getLeft().isType(expn.getRight().getType()))) {
			// TODO create an error for S32
			expn.getLeft().prettyPrint(printer);
			System.err.print(" and ");
			expn.getRight().prettyPrint(printer);
			System.err.println(" are not the same types");
			return false;
		}
		return true;
	}
	public Boolean visit(BoolConstExpn expn) {return true;}
  	public Boolean visit(BoolExpn expn) {return true;}

	// S31
	@Override
	public Boolean visit(CompareExpn expn) {
		if (!expn.getLeft().isInteger()) {
			// TODO create an error for S31
			System.err.println("left and right are not integers");
			return false;
		}

		return true;
	}
	
	public Boolean visit(ConstExpn expn) {return true;}
  	public Boolean visit(EqualsExpn expn) {return true;}
  
	// S36
	@Override
	public Boolean visit(FunctionCallExpn expn) {
		ASTList<ScalarDecl> parameters = new ASTList<ScalarDecl>();
		ASTList<Expn> args = expn.getArguments();
		
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
				System.err.println("");
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
	
	public Boolean visit(IntConstExpn expn) {return true;}
  	public Boolean visit(NotExpn expn) {return true;}
  
  	public Boolean visit(SkipConstExpn expn) {return true;}

	// S38
	@Override
	public Boolean visit(SubsExpn expn) {
		// TODO S38: look up the entry
		SymbolTableEntry entry = new SymbolTableEntry(null, null, SymbolTableEntry.Kind.SCALAR, null);
		
		if (entry.getKind() != SymbolTableEntry.Kind.ARRAY) {
			// TODO create an error for S38
			System.err.println("The identifier " + expn.getVariable() + 
					" was not declared as an array!");
			return false;
		}
		
		// S27
		expn.setType(entry.getType());
		
		return true;
	}
	
	public Boolean visit(TextConstExpn expn) {return true;}
  	public Boolean visit(UnaryExpn expn) {return true;}
  	public Boolean visit(UnaryMinusExpn expn) {return true;}
  
  	public Boolean visit(AssignStmt stmt) {return true;}
  	public Boolean visit(ExitStmt stmt) {return true;}
  	public Boolean visit(GetStmt stmt) {return true;}
  	public Boolean visit(IfStmt stmt) {return true;}
  	public Boolean visit(LoopingStmt stmt) {return true;}
  	public Boolean visit(LoopStmt stmt) {return true;}
  	public Boolean visit(ProcedureCallStmt stmt) {return true;}
  	public Boolean visit(PutStmt stmt) {return true;}

	// S35
	@Override
	public Boolean visit(ReturnStmt stmt) {
		// TODO S35: use the function associated with this return statement
		Type functionType = new IntegerType();
		
		return stmt.getValue().isType(functionType);
	}

	public Boolean visit(Scope stmt) {return true;}
	public Boolean visit(Stmt stmt) {return true;}
	public Boolean visit(WhileDoStmt stmt) {return true;}
  
	public Boolean visit(BooleanType type) {return true;}
	public Boolean visit(IntegerType type) {return true;}
  
}
