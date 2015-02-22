package compiler488.semantics;

import java.io.*;

import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.CompareExpn;

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
  
  public Boolean visit(CompareExpn expn) {
//	  System.out.println(expn.getLeft());
      if(!expn.getLeft().isInteger()) {
      	System.out.println("left and right are not integers");
      	return false;
      }
      
      return true;
  }
  
//  public void visit() {
//	  
//  }
  
  public Boolean visit(BinaryExpn expn) {
      if(!(expn.getLeft().isType(expn.getRight().getType()))) {
    	  System.out.println("not the same types");
    	  return false;
      }
      return true;
//      System.out.println(expn.getOpSymbol());
  }
}
