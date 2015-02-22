package compiler488.semantics;

import java.io.*;

import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
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
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.SubsExpn;
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
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;

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



     /** SemanticAnalyzer constructor */
	public Semantics (){

	}

	/**  semanticsInitialize - called once by the parser at the      */
	/*                        start of  compilation                 */
	void Initialize() {

	   /*   Initialize the symbol table             */

	   // Symbol.Initialize();

	   /*********************************************/
	   /*  Additional initialization code for the   */
	   /*  semantic analysis module                 */
	   /*  GOES HERE                                */
	   /*********************************************/

	}

	/**  semanticsFinalize - called by the parser once at the        */
	/*                      end of compilation                      */
	void Finalize(){

	  /*  Finalize the symbol table                 */

	  // Symbol.Finalize();

	   /*********************************************/
	  /*  Additional finalization code for the      */
	  /*  semantics analysis module                 */
	  /*  GOES here.                                */
	  /**********************************************/

	}

	/**
	 *  Perform one semantic analysis action
         *  @param  actionNumber  semantic analysis action number
         */
	void semanticAction( int actionNumber ) {

	if( traceSemantics ){
		if(traceFile.length() > 0 ){
	 		//output trace to the file represented by traceFile
	 		try{
	 			//open the file for writing and append to it
	 			File f = new File(traceFile);
	 		    Tracer = new FileWriter(traceFile, true);

	 		    Tracer.write("Sematics: S" + actionNumber + "\n");
	 		    //always be sure to close the file
	 		    Tracer.close();
	 		}
	 		catch (IOException e) {
	 		  System.out.println(traceFile +
				" could be opened/created.  It may be in use.");
	 	  	}
	 	}
	 	else{
	 		//output the trace to standard out.
	 		System.out.println("Sematics: S" + actionNumber );
	 	}

	}

	   /*************************************************************/
	   /*  Code to implement each semantic action GOES HERE         */
	   /*  This stub semantic analyzer just prints the actionNumber */
	   /*                                                           */
           /*  FEEL FREE TO ignore or replace this procedure            */
	   /*************************************************************/

	   System.out.println("Semantic Action: S" + actionNumber  );
	   return ;

	}

	// ADDITIONAL FUNCTIONS TO IMPLEMENT SEMANTIC ANALYSIS GO HERE
	

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
  public Boolean visit(BinaryExpn expn) {
      if(!(expn.getLeft().isType(expn.getRight().getType()))) {
    	  System.out.println("not the same types");
    	  return false;
      }
      return true;
//      System.out.println(expn.getOpSymbol());
  }
  public Boolean visit(BoolConstExpn expn) {return true;}
  public Boolean visit(BoolExpn expn) {return true;}
  public Boolean visit(CompareExpn expn) {

//	  System.out.println(expn.getLeft());
      if(!expn.getLeft().isInteger()) {
      	System.out.println("left and right are not integers");
      	return false;
      }
      
      return true;
  }
  public Boolean visit(ConstExpn expn) {return true;}
  public Boolean visit(EqualsExpn expn) {return true;}
  public Boolean visit(FunctionCallExpn expn) {return true;}
  public Boolean visit(IdentExpn expn) {return true;}
  public Boolean visit(IntConstExpn expn) {return true;}
  public Boolean visit(NotExpn expn) {return true;}
  
  public Boolean visit(SkipConstExpn expn) {return true;}
  public Boolean visit(SubsExpn expn) {return true;}
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
  public Boolean visit(ReturnStmt stmt) {return true;}
  public Boolean visit(Scope stmt) {return true;}
  public Boolean visit(Stmt stmt) {return true;}
  public Boolean visit(WhileDoStmt stmt) {return true;}
  
  public Boolean visit(BooleanType type) {return true;}
  public Boolean visit(IntegerType type) {return true;}
  
  
}
