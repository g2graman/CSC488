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
		System.out.println(node);
		return true;
	}

	public Boolean visit(ArrayDeclPart decl) {
		// TODO S19
		// TODO S46, S48
		return true;
	}
  	public Boolean visit(Declaration decl) { return true; }
  	public Boolean visit(MultiDeclarations decl) {
  		// TODO S10, S19
  		// TODO S46, S47,S48
  		return true;
  	}
  	public Boolean visit(RoutineDecl decl) {
  		// TODO S04,S05, S08,S09
  		// TODO S11, S12
  		// TODO S15, S17, S18
  		// TODO S53
  		return true;
  	}
  	public Boolean visit(ScalarDecl decl) {
  		// TODO S10
  		return true;
  	}

  	public Boolean visit(AnonFuncExpn expn) {
  		// TODO S24
  		return true;
  	}
  	public Boolean visit(ArithExpn expn) {
  		// TODO S31
  		// TODO S21
  		return true;
  	}

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
  	public Boolean visit(BoolExpn expn) {
  		// TODO S30
  		// TODO S20
  		return true;
  	}

	// S31
	@Override
	public Boolean visit(CompareExpn expn) {
		// S32 is done before this node is visited
		if (!expn.getLeft().isInteger()) {
			// TODO create an error for S31
			System.err.println("left and right are not integers");
			return false;
		}
		
		// TODO S20

		return true;
	}
	
	public Boolean visit(ConstExpn expn) {return true;}
  	public Boolean visit(EqualsExpn expn) {
  		// TODO S20
  		return true;
  	}
  
	// S36
	@Override
	public Boolean visit(FunctionCallExpn expn) {
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
  	public Boolean visit(NotExpn expn) {
  		// TODO S30
  		// TODO S20
  		return true;
  	}
  
  	public Boolean visit(SkipConstExpn expn) {return true;}

	// S38
	@Override
	public Boolean visit(SubsExpn expn) {
		// TODO S31
		
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
  	public Boolean visit(UnaryMinusExpn expn) {
  		// TODO S31
  		return true;
  	}
  
  	public Boolean visit(AssignStmt stmt) {
  		// TODO S34
  		return true;
  	}
  	public Boolean visit(ExitStmt stmt) {
  		// TODO S30
  		// TODO S50
  		return true;
  	}
  	public Boolean visit(GetStmt stmt) {
  		// TODO S31
  		return true;
  	}
  	public Boolean visit(IfStmt stmt) {
  		// TODO S30
  		return true;
  	}
  	public Boolean visit(LoopingStmt stmt) {return true;}
  	public Boolean visit(LoopStmt stmt) {return true;}
  	public Boolean visit(ProcedureCallStmt stmt) {
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
  		// TODO S31
  		return true;
  	}

	// S35
	@Override
	public Boolean visit(ReturnStmt stmt) {
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
		// TODO S30
		return true;
	}
  
	public Boolean visit(BooleanType type) {return true;}
	public Boolean visit(IntegerType type) {return true;}

  
}
