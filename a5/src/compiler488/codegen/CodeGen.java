package compiler488.codegen;

import java.util.*;

import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.runtime.ExecutionException ;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.AST;
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
import compiler488.symbol.MajorScope.ScopeKind;
import compiler488.symbol.SymbolTableEntry;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTableEntry.SymbolKind;
import compiler488.symbol.MajorScope;
import compiler488.ast.Printable;

/**      CodeGenerator.java 
 *<pre>
 *  Code Generation Conventions
 *
 *  To simplify the course project, this code generator is
 *  designed to compile directly to pseudo machine memory
 *  which is available as the private array memory[]
 *
 *  It is assumed that the code generator places instructions
 *  in memory in locations
 *
 *      memory[ 0 .. startMSP - 1 ]
 *
 *  The code generator may also place instructions and/or
 *  constants in high memory at locations (though this may
 *  not be necessary)
 *      memory[ startMLP .. Machine.memorySize - 1 ]
 *
 *  During program exection the memory area
 *      memory[ startMSP .. startMLP - 1 ]
 *  is used as a dynamic stack for storing activation records
 *  and temporaries used during expression evaluation.
 *  A hardware exception (stack overflow) occurs if the pointer
 *  for this stack reaches the memory limit register (mlp).
 *
 *  The code generator is responsible for setting the global
 *  variables:
 *      startPC         initial value for program counter
 *      startMSP        initial value for msp
 *      startMLP        initial value for mlp
 * </pre>
 * @author  <B> PUT YOUR NAMES HERE </B>
 */

public class CodeGen implements ASTVisitor<Boolean>
    {

    /** initial value for memory stack pointer */
    private short startMSP;
    /** initial value for program counter */
    private short startPC;
    /** initial value for memory limit pointer */
    private short startMLP;

    /** flag for tracing code generation */
    private boolean traceCodeGen = Main.traceCodeGen ;

    private ArrayList<String> instructionCounter;
    private int programCounter;
    private LinkedList<MajorScope> scopes;
    private int lexicalLevel;
    private HashMap<String, Integer> hash; // keep track where start of procedures and functions
    private int num_var; //number of variables in a scope, for POPN use
    private int num_par; // number of parameters for a function

    private boolean is_routine;

    private String anonFcnName = "";

    /*
    * A 2D array which keeps track of addresses of exit statements
    * The first dimension will indicate the current loop we are in
    * The second dimension will contain addresses that need to be patched
    * When we enter a loop, push a new ArrayList<Integer> on
    * Then we evaluate the statements. If an exit is come across, it will go to the
    * latest ArrayList<Integer> in loopExits and push the address that needs to be patched on
    * Then the statements in a loop are evaluated, the last ArrayList<Integer> should be popped off,
    * and those addresses will be evaluated (if any)
     */
    private ArrayList<ArrayList<Integer>> loopExits;


    /**  
     *  Constructor to initialize code generation
     */
    public CodeGen()
    {
    // YOUR CONSTRUCTOR GOES HERE.
    }

    // Utility procedures used for code generation GO HERE.

    /** 
     *  Additional intialization for gode generation.
     *  Called once at the start of code generation.
     *  May be unnecesary if constructor does everything.
     */

   /** Additional initialization for Code Generation (if required) */
   public void Initialize()
    {
    /********************************************************/
    /* Initialization code for the code generator GOES HERE */
    /* This procedure is called once before codeGeneration  */      
    /*                                                      */
    /********************************************************/
    instructionCounter = new ArrayList<String>();
    programCounter = 0;
    scopes = new LinkedList<MajorScope>();
    hash = new HashMap<String, Integer>();
    num_var = 0;
    num_par = 0;
    lexicalLevel = 0;
    loopExits = new ArrayList<ArrayList<Integer>>();
    is_routine = false;
    return;
    }

    
    /**  
     *  Perform any requred cleanup at the end of code generation.
     *  Called once at the end of code generation.
     *  @throws MemoryAddressException  from Machine.writeMemory
     *  @throws ExecutionException      from Machine.writeMemory
     */
    public void Finalize()
        throws MemoryAddressException, ExecutionException     // from Machine.writeMemory 
    {
    /********************************************************/
    /* Finalization code for the code generator GOES HERE.  */      
    /*                                                      */
    /* This procedure is called once at the end of code     */
    /* generation                                           */
    /********************************************************/

    //  REPLACE THIS CODE WITH YOUR OWN CODE
    //  THIS CODE generates a single HALT instruction 
        //  as an example.
    Machine.setPC( (short) 0 ) ;        /* where code to be executed begins */
    Machine.setMSP((short)  programCounter );       /* where memory stack begins */
    Machine.setMLP((short) ( Machine.memorySize -1 ) ); 
                    /* limit of stack */
    return;
    }

    /** Procedure to implement code generation based on code generation
     *  action number
     * @param actionNumber  code generation action to perform
     */
    void generateCode( int actionNumber )
    {
    if( traceCodeGen )
        {
        //output the standard trace stream
        Main.traceStream.println("CodeGen: C" +  actionNumber );
        }

    /****************************************************************/
    /*  Code to implement the code generation actions GOES HERE     */
    /*  This dummy code generator just prints the actionNumber      */
    /*  In Assignment 5, you'll implement something more interesting */
        /*                                                               */
        /*  FEEL FREE TO ignore or replace this procedure                */
    /****************************************************************/

        System.out.println("Codegen: C" + actionNumber ); 
    return;
    }

     //  ADDITIONAL FUNCTIONS TO IMPLEMENT CODE GENERATION GO HERE
    public void emitInstructions( String instruction ) {
        String splitstring[] = instruction.split("\\s+");
        for (String token : splitstring) {
            try {
                switch(token) {
                    case "ADDR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ADDR );
                        break;
                    case "LOAD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.LOAD );
                        break;
                    case "STORE":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.STORE );
                        break;
                    case "PUSH":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PUSH );
                        break;
                    case "PUSHMT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PUSHMT );
                        break;
                    case "SETD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SETD );
                        break;
                    case "POPN":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.POPN );
                        break;
                    case "POP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.POP );
                        break;
                    case "DUPN":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DUPN );
                        break;
                    case "DUP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DUP );
                        break;
                    case "BR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.BR );
                        break;
                    case "BF":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.BF );
                        break;
                    case "NEG":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.NEG );
                        break;
                    case "ADD":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ADD );
                        break;
                    case "SUB":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SUB );
                        break;
                    case "MUL":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.MUL);
                        break;
                    case "DIV":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.DIV );
                        break;
                    case "EQ":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.EQ );
                        break;
                    case "LT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.LT );
                        break;
                    case "OR":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.OR );
                        break;
                    case "SWAP":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.SWAP );
                        break;
                    case "READC":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.READC );
                        break;
                    case "PRINTC":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PRINTC );
                        break;
                    case "READI":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.READI );
                        break;
                    case "PRINTI":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.PRINTI );
                        break;
                    case "HALT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.HALT );
                        break;
                    case "TRON":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.TRON );
                        break;
                    case "TROFF":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.TROFF );
                        break;
                    case "ILIMIT":
                        Machine.writeMemory( (short)instructionCounter.size() , Machine.ILIMIT );
                        break;
                    case "UNDEFINED":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.UNDEFINED );
                        break;
                    case "true":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.MACHINE_TRUE );
                        break;
                    case "false":
                        Machine.writeMemory( (short)instructionCounter.size(), Machine.MACHINE_FALSE );
                        break;
                    default:
                        Machine.writeMemory( (short)instructionCounter.size() , Short.parseShort(token) );
                        break;

                }
                instructionCounter.add(token);
            } catch (MemoryAddressException e) {
                System.out.println(e);
            } catch (ExecutionException e) {
                System.out.println(e);
            }
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

    public int lookup_offset(String varname, boolean local) {
        if(local) {
            return scopes.getFirst().lookup(varname).getOff();
        }

        for(MajorScope scope : scopes) {
            SymbolTableEntry entry = scope.lookup(varname);
            if(entry != null) {
                return entry.getOff();
            }
        }
        return 0;
    }

    public int lookup_lex(String varname, boolean local) {
        if(local) {
            return scopes.getFirst().lookup(varname).getLex();
        }

        for(MajorScope scope : scopes) {
            SymbolTableEntry entry = scope.lookup(varname);
            if(entry != null) {
                return entry.getLex();
            }
        }
        return 0;
    }

    public void addEntry(String varname, Type type, SymbolKind kind, AST node, int off, int lex) {
        if(scopes.isEmpty()) {
            return;
        }
        scopes.getFirst().addEntry(varname, type, kind, node, off, lex);
    }

    public void enterScope(ScopeKind kind, RoutineDecl routine) {
        scopes.addFirst(new MajorScope(kind, routine));
    }

    public void exitScope() {
        scopes.removeFirst();
    }
    
    public MajorScope getFirstRoutine() {
        for(MajorScope scope : scopes) {
            if(scope.getKind() == ScopeKind.FUNCTION ||
                scope.getKind() == ScopeKind.PROCEDURE) {
                return scope;
            }
        }
        
        return null;
    }

    public Boolean visit(Program stmt) {
        emitInstructions("PUSHMT");
        emitInstructions("SETD 0");

        enterScope(ScopeKind.PROGRAM, null);
        this.visit((Scope) stmt);
        exitScope();   
        // pop all local variables
        emitInstructions("PUSH "+num_var);
        emitInstructions("POPN");
        emitInstructions("HALT");
        return true;
    }
    public Boolean visit(AST node) {return true;};
    public <A extends AST> Boolean visit(ASTList<A> list) {
        Boolean bools = true;
        for(A node : list) {
            Boolean z = node.accept(this);
            bools = bools && z;
        }
        return bools;
    }
 
    public Boolean visit(ArrayDeclPart decl) {
        System.out.println("ArrayDeclPart");
        if ( lookup(decl.getName(), true) == null) {
            addEntry(decl.getName(),decl.getType(), SymbolKind.SCALAR, decl, num_var, lexicalLevel);
        }
        for (int i=0; i<decl.getSize(); i++) {
            emitInstructions("PUSH UNDEFINED");
            num_var++;
        }

        return true;
    }
    public Boolean visit(Declaration decl) {
        System.out.println("Declaration");
        return true;
    }
    public Boolean visit(MultiDeclarations decl) {
        System.out.println("MultiDeclarations");
        ASTList<DeclarationPart> parts = decl.getParts();

        for(DeclarationPart part : parts) {
            if(part instanceof ScalarDeclPart) {
                ScalarDecl scalarDecl = new ScalarDecl(part.getName(), decl.getType());
                scalarDecl.setLocation(part.getLine() - 1, part.getCol());

                Boolean scalar = this.visit(scalarDecl);
            } else {
                ArrayDeclPart arrayDecl = (ArrayDeclPart) part;
                arrayDecl.setType(decl.getType());

                Boolean array = this.visit(arrayDecl);
            }
        }

        return true;
    }
    public Boolean visit(RoutineDecl decl) {
        System.out.println("RoutineDecl");
        int pc = instructionCounter.size()+1;
        emitInstructions("PUSH 0");
        emitInstructions("BR");

        ScopeKind routineKind = decl.getType() == null ? ScopeKind.PROCEDURE : ScopeKind.FUNCTION;

        if ( lookup(decl.getName(), true) == null ) {
            if (decl.getType() == null) {
                addEntry(decl.getName(), decl.getType(), SymbolKind.PROCEDURE, decl, 0, 0);
            } else {
                addEntry(decl.getName(), decl.getType(), SymbolKind.FUNCTION, decl, 0, 0);
            }
        }

        // store the start location of the routine so it can be called alter
        hash.put(decl.getName(), instructionCounter.size());

        // store the current number of local variables
        int temp = num_var;
        // reset the number of local variable for the new scope
        num_var = 0;
        enterScope(routineKind, decl);

        emitInstructions("PUSHMT");
        lexicalLevel++;
        emitInstructions("SETD "+lexicalLevel);

        int temp_par = num_par;
        ASTList<ScalarDecl> parameters = decl.getParameters();
        int i = 0;
        for (ScalarDecl s : parameters) {
            if ( lookup(s.getName(), true) == null) {
                addEntry(s.getName(), s.getType(), SymbolKind.SCALAR, s, (-decl.getParameters().size()+i), lexicalLevel);
                i++;
            }
        }
        num_par = i;

        this.visit(decl.getBody());
        exitScope();

        // pop all local variables
        emitInstructions("PUSH "+num_var);
        emitInstructions("POPN");

        // restore number of local variables
        num_var = temp;
        // restore number of parameters
        num_par = temp_par;

        // pop all parameters
        emitInstructions("PUSH "+i);
        emitInstructions("POPN");

        lexicalLevel--;
        emitInstructions("BR");

        try {
            Machine.writeMemory( (short)pc, (short)instructionCounter.size());
        } catch (MemoryAddressException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }

        return true;
    }
    public Boolean visit(ScalarDecl decl) {
        System.out.println("ScalarDecl");
        if ( lookup(decl.getName(), true) == null) {
            addEntry(decl.getName(),decl.getType(), SymbolKind.SCALAR, decl, num_var, lexicalLevel);
            num_var++;
        }
        emitInstructions("PUSH UNDEFINED");
        return true;
    }

    public Boolean visit(AnonFuncExpn expn) {
        System.out.println("AnonFuncExpn");
        RoutineDecl anonFun = new RoutineDecl(anonFcnName, expn.getExpn().getType(), new Scope(expn.getBody()));

        // store the current number of local variables
        int temp = num_var;
        // reset the number of local variable for the new scope
        num_var = 0;
        enterScope(ScopeKind.FUNCTION, anonFun);

        emitInstructions("PUSHMT");
        lexicalLevel++;
        emitInstructions("SETD "+lexicalLevel);

        this.visit(anonFun.getBody());
        expn.getExpn().accept(this);

        exitScope();

        // pop all local variables
        emitInstructions("PUSH "+num_var);
        emitInstructions("POPN");

        // restore number of local variables
        num_var = temp;

        lexicalLevel--;
        return true;
    }
    public Boolean visit(ArithExpn expn) {
        System.out.println("ArithExpn");
        this.visit((BinaryExpn) expn);
        if (expn.getOpSymbol().equals(ArithExpn.OP_PLUS)){
            emitInstructions("ADD");
        } else if (expn.getOpSymbol().equals(ArithExpn.OP_MINUS)){
            emitInstructions("SUB");
        } else if (expn.getOpSymbol().equals(ArithExpn.OP_TIMES)){
            emitInstructions("MUL");
        } else if (expn.getOpSymbol().equals(ArithExpn.OP_DIVIDE)){
            emitInstructions("DIV");
        }

        return true;
    }
    public Boolean visit(BinaryExpn expn) {
        System.out.println("BinaryExpn");
        expn.getLeft().accept(this);
        if ((expn.getLeft() instanceof IdentExpn) && (hash.get(expn.getLeft().toString()) == null)) {
            emitInstructions("LOAD");
        }
        expn.getRight().accept(this);
        if ((expn.getRight() instanceof IdentExpn) && (hash.get(expn.getRight().toString()) == null)) {
            emitInstructions("LOAD");
        }
        return true;
    }
    public Boolean visit(BoolConstExpn expn) {
        System.out.println("BoolConstExpn");
        System.out.println("PUSH "+expn.toString());
        emitInstructions("PUSH "+expn.toString());
        return true;
    }
    public Boolean visit(BoolExpn expn) {
        System.out.println("BoolExpn");

        Expn left = expn.getLeft();
        left.accept(this); // Execute left operand
        if ((hash.get(left.toString()) == null) && (left instanceof IdentExpn)) {
            emitInstructions("LOAD");
        }


        Expn right = expn.getRight();

        int pc = instructionCounter.size();
        if(expn.getOpSymbol().equals(BoolExpn.OP_OR)) {
            int pc1 = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BF"); // Branch to OR_RIGHT if false
            emitInstructions("PUSH " + Machine.MACHINE_TRUE);
            int pc2 = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BR"); // Branch to OR_END with true atop stack, short-circuit
            try {
                Machine.writeMemory((short) pc1, (short) (instructionCounter.size()));
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            right.accept(this); // the value of the right side will dictate the value of overall expression since left is false
            if ((hash.get(right.toString()) == null) && (right instanceof IdentExpn)) {
                emitInstructions("LOAD");
            }

            try {
                Machine.writeMemory((short) pc2, (short) (instructionCounter.size()));
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else if (expn.getOpSymbol().equals(BoolExpn.OP_AND)) {
            int pc1 = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BF"); // Branch to end if false

            right.accept(this);
            if ((hash.get(right.toString()) == null) &&  (right instanceof IdentExpn)) {
                emitInstructions("LOAD");
            }


            int pc2 = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BF");
            emitInstructions("PUSH " + Machine.MACHINE_TRUE);


            int pc3 = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BR");

            try {
                Machine.writeMemory((short) pc1, (short) (instructionCounter.size()));
                Machine.writeMemory((short) pc2, (short) (instructionCounter.size()));
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            emitInstructions("PUSH "+ Machine.MACHINE_FALSE); // AND_END

            try {
                Machine.writeMemory((short) pc3, (short) (instructionCounter.size()));
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }



        } else {
            //TODO: error?
        }
        return true;
    }
    public Boolean visit(CompareExpn expn) {
        System.out.println("CompareExpn");
        this.visit((BinaryExpn)expn);

        if (expn.getOpSymbol().equals(CompareExpn.OP_LESS)){
            emitInstructions("LT");
        } else if (expn.getOpSymbol().equals(CompareExpn.OP_LESS_EQUAL)){
            // Use a <= b = not(b < a), and avoid arithmetic
            CompareExpn flipped = 
                    new CompareExpn(CompareExpn.OP_LESS, 
                            expn.getRight(), 
                            expn.getLeft());
            NotExpn negated = (NotExpn) new NotExpn(flipped);
            this.visit(flipped);
            this.visit(negated);
        } else if (expn.getOpSymbol().equals(CompareExpn.OP_GREATER)){
            // Use a > b = b < a (switch order and use LT)
            
            emitInstructions("SWAP");
            emitInstructions("LT");
        } else if (expn.getOpSymbol().equals(CompareExpn.OP_GREATER_EQUAL)){
            // Use a >= b = not(a < b)
            CompareExpn changeOp = 
                    new CompareExpn(CompareExpn.OP_LESS, 
                            expn.getLeft(), 
                            expn.getRight());
            NotExpn negated = (NotExpn) new NotExpn(changeOp);
            this.visit(changeOp);
            this.visit(negated);
        } else {
            // TODO: error?
        }

        return true;
    }
    public Boolean visit(ConstExpn expn) {
        System.out.println("ConstExpn");
        // TODO: Eric
        return true;
    }
    public Boolean visit(EqualsExpn expn) {
        System.out.println("EqualsExpn");
        this.visit((BinaryExpn)expn);

        if (expn.getOpSymbol().equals(EqualsExpn.OP_EQUAL)){
            emitInstructions("EQ");
        } else if (expn.getOpSymbol().equals(EqualsExpn.OP_NOT_EQUAL)){
            emitInstructions("EQ");
            
            //Emit negation
            NotExpn negated = (NotExpn) new NotExpn((Expn) expn);
            this.visit(negated);
        }

        return true;
    }
    public Boolean visit(FunctionCallExpn expn) {
        System.out.println("FunctionCallExpn");
        // return value
        emitInstructions("PUSH 0");
        int pc = instructionCounter.size() + 1;
        int c = instructionCounter.size() + 5;
        emitInstructions("PUSH "+0);

        this.visit(expn.getArguments());
        for (int i = expn.getArguments().size(); i > 0; i --) {
            if (expn.getArguments().get(expn.getArguments().size()-i) instanceof IdentExpn) {
                emitInstructions("PUSHMT");
                emitInstructions("PUSH "+i);
                emitInstructions("SUB");
                emitInstructions("DUP");
                emitInstructions("LOAD");
                emitInstructions("LOAD");
                emitInstructions("STORE");
                c = c + 11;
            } else {
                c = c + 2;
            }
        }
        try {
            Machine.writeMemory( (short)pc, (short)c);
        } catch (MemoryAddressException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }

        emitInstructions("PUSH "+hash.get(expn.getIdent()));
        emitInstructions("BR");
        
        return true;
    }
    public Boolean visit(IdentExpn expn) {
        System.out.println("IdentExpn");

        // check if this is a function by checking if it is in hash
        if (hash.get(expn.toString()) == null){
            emitInstructions("ADDR "+lookup_lex(expn.toString(), false)+" "+lookup_offset(expn.toString(), false));
        } else {
            FunctionCallExpn fnCallExpn = new FunctionCallExpn(expn.toString(), new ASTList<Expn>());
            fnCallExpn.accept(this);
        }


        // System.out.println("display " + lexicalLevel);
        // System.out.println("display " + lookup_offset(expn.toString(), false));

        return true;
    }
    public Boolean visit(IntConstExpn expn) {
        System.out.println("IntConstExpn");
        emitInstructions("PUSH "+expn.toString());

        return true;
    }
    public Boolean visit(NotExpn expn) {
        System.out.println("NotExpn");

        expn.getOperand().accept(this);
        if ((hash.get(expn.getOperand().toString()) == null) && (expn.getOperand() instanceof IdentExpn)) {
            emitInstructions("LOAD");
        }
        int pc1 = instructionCounter.size() + 1;
        emitInstructions("PUSH 0");
        emitInstructions("BF");
        emitInstructions("PUSH " + Machine.MACHINE_FALSE);
        int pc2 = instructionCounter.size() + 1;
        emitInstructions("PUSH 0");
        emitInstructions("BR");
        try {
            Machine.writeMemory((short) pc1, (short) instructionCounter.size());
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        emitInstructions("PUSH " + Machine.MACHINE_TRUE);
        try {
            Machine.writeMemory((short) pc2, (short) instructionCounter.size());
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return true;
    }


    /*
    * The only time skip is used is within a put. So it is okay to emit the PRINTC here
     */
    public Boolean visit(SkipConstExpn expn){
        System.out.println("SkipConstExpn");
        emitInstructions("PUSH " + (int) '\n');
        emitInstructions("PRINTC");
        return true;
    }
    public Boolean visit(SubsExpn expn){
        System.out.println("SubsExpn");

        SymbolTableEntry array = lookup(expn.getVariable(), true);
        if (array.getNode() instanceof ArrayDeclPart) {
            expn.getSubscript1().accept(this);
            if ((expn.getSubscript1() instanceof IdentExpn)) {
                if (hash.get(expn.getSubscript1().toString()) == null) {
                    emitInstructions("LOAD");
                }
            }
            int lb1 = ((ArrayDeclPart)array.getNode()).getLowerBoundary1();
            emitInstructions("PUSH "+lb1);
            emitInstructions("SUB");

            if (((ArrayDeclPart)array.getNode()).isTwoDimensional() == true) {
                // off = (ub2-lb2+1)*(sub1-lb1)+(sub2-lb2)
                int ub2 = ((ArrayDeclPart)array.getNode()).getUpperBoundary2();
                emitInstructions("PUSH "+ub2);
                int lb2 = ((ArrayDeclPart)array.getNode()).getLowerBoundary2();
                emitInstructions("PUSH "+lb2);
                emitInstructions("SUB");
                emitInstructions("PUSH 1");
                emitInstructions("ADD");
                emitInstructions("MUL");

                expn.getSubscript2().accept(this);
                if ((expn.getSubscript2() instanceof IdentExpn)) {
                    if (hash.get(expn.getSubscript2().toString()) == null) {
                        emitInstructions("LOAD");
                    }
                }
                emitInstructions("PUSH "+lb2);
                emitInstructions("SUB");
                emitInstructions("ADD");
                emitInstructions("ADDR "+lookup_lex(expn.getVariable(), false)+" "+lookup_offset(expn.getVariable(), false));
                emitInstructions("ADD");

            } else {

                // off = sub1 - lb1;

                emitInstructions("ADDR "+lookup_lex(expn.getVariable(), false)+" "+lookup_offset(expn.getVariable(), false));
                emitInstructions("ADD");
            }
        }
        return true;
    }

    /*
     * The only time TextConstExpn is within a Put. So it is okay to emit the print here.
     */
    public Boolean visit(TextConstExpn expn) {
        System.out.println("TextConstExpn");
        String out = expn.getValue();
        for (char c : out.toCharArray()) {
            emitInstructions("PUSH " + (int) c);
            emitInstructions("PRINTC");
        }
        return true;
    }
    public Boolean visit(UnaryExpn expn){
        System.out.println("UnaryExpn");
        // TODO: Frans
        return true;
    }
    public Boolean visit(UnaryMinusExpn expn){
        System.out.println("UnaryMinusExpn");
        UnaryExpn e = (UnaryExpn) expn;
        this.visit(e);
        emitInstructions("NEG");
        return true;
    }

    public Boolean visit(AssignStmt stmt){

        System.out.println("AssignStmt");
        stmt.getLval().accept(this);
        stmt.getRval().accept(this);
        if (stmt.getRval() instanceof IdentExpn) {
            if (hash.get(stmt.getRval().toString()) == null) {
                emitInstructions("LOAD");
            }
        }
        emitInstructions("STORE");

        return true;
    }
    public Boolean visit(ExitStmt stmt){
        System.out.println("ExitStmt");
        int patchAddr;
        if (stmt.getExpn() != null){ // exit when statement
            NotExpn notExpn = new NotExpn(stmt.getExpn()); // emit negated version of the expression
            notExpn.accept(this);
            patchAddr = instructionCounter.size() + 1;
            emitInstructions("PUSH 0"); // patch later
            emitInstructions("BF");
        } else { // regular exit statement
            patchAddr = instructionCounter.size() + 1;
            emitInstructions("PUSH 0");
            emitInstructions("BR");
        }
        loopExits.get(loopExits.size() - 1).add(patchAddr); // add this patch address to latest loop
        return true;
    }
    public Boolean visit(GetStmt stmt){
        System.out.println("GetStmt");
        for (Expn e : stmt.getInputs()){
            e.accept(this);
            emitInstructions("READI");
            emitInstructions("STORE");
        }

        return true;
    }
    public Boolean visit(IfStmt stmt){
        System.out.println("IfStmt");
        // first emit code for the condition
        stmt.getCondition().accept(this);
        if (stmt.getCondition() instanceof IdentExpn){
            emitInstructions("LOAD");
        }
        int startOfIf = instructionCounter.size()+1; // address of instruction to branch to end of true branch
        emitInstructions("PUSH 0");
        emitInstructions("BF");
        stmt.getWhenTrue().accept(this);
        if (stmt.getWhenFalse() != null){ // else clause exists
            int startOfElse = instructionCounter.size()+1; // address of instruction branch to end of else clause
            emitInstructions("PUSH 0");
            emitInstructions("BR");
            int startOfElseBranch = instructionCounter.size();
            stmt.getWhenFalse().accept(this);
            try {
                Machine.writeMemory((short) startOfIf, (short) startOfElseBranch);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int endOfConditional = instructionCounter.size();
            try {
                Machine.writeMemory((short) startOfElse, (short) endOfConditional);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else {
            int endOfConditional = instructionCounter.size();
            try {
                Machine.writeMemory((short) startOfIf, (short) endOfConditional);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }


        return true;
    }
    public Boolean visit(LoopingStmt stmt){
        if (stmt instanceof LoopStmt){
            this.visit((LoopStmt) stmt);
        } else if (stmt instanceof WhileDoStmt){
            this.visit((WhileDoStmt) stmt);
        } else {
            //TODO: error?
        }
        return true;
    }
    public Boolean visit(LoopStmt stmt){
        System.out.println("LoopStmt");
        int loopBegin = instructionCounter.size();
        loopExits.add(new ArrayList<Integer>());
        stmt.getBody().accept(this);
        emitInstructions("PUSH " + loopBegin);
        emitInstructions("BR");
        // patch exit statements
        int currInstr = instructionCounter.size();
        ArrayList<Integer> patchAddresses = loopExits.remove(loopExits.size() - 1);
        for (Integer i: patchAddresses){
            try {
                Machine.writeMemory(i.shortValue(), (short) currInstr);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public Boolean visit(ProcedureCallStmt stmt){
        System.out.println("ProcedureCallStmt");

        int pc = instructionCounter.size() + 1;
        int c = instructionCounter.size() + 5;
        emitInstructions("PUSH "+0);

        this.visit(stmt.getArguments());
        for (int i = stmt.getArguments().size(); i > 0; i --) {
            if (stmt.getArguments().get(stmt.getArguments().size()-i) instanceof IdentExpn) {
                emitInstructions("PUSHMT");
                emitInstructions("PUSH "+i);
                emitInstructions("SUB");
                emitInstructions("DUP");
                emitInstructions("LOAD");
                emitInstructions("LOAD");
                emitInstructions("STORE");
                c = c + 11;
            } else {
                c = c + 2;
            }
        }

        try {
            Machine.writeMemory( (short)pc, (short)c);
        } catch (MemoryAddressException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }

        emitInstructions("PUSH "+hash.get(stmt.getName()));
        emitInstructions("BR");

        return true;
    }
    public Boolean visit(PutStmt stmt){

        System.out.println("PutStmt");

        ASTList<Printable> out = stmt.getOutputs();
        for (Printable p: out){
            p.accept(this);

            if (p instanceof BinaryExpn){
                emitInstructions("PRINTI");
            } else if (p instanceof IntConstExpn){
                emitInstructions("PRINTI");
            } else if (p instanceof IdentExpn){
                emitInstructions("LOAD");
                emitInstructions("PRINTI");
            } else if (p instanceof FunctionCallExpn){
                emitInstructions("PRINTI");
            } else if (p instanceof UnaryExpn){
                emitInstructions("PRINTI");
            } else if (p instanceof SubsExpn){
               emitInstructions("LOAD");
               emitInstructions("PRINTI");
            }
        }

        return true;
    }
    public Boolean visit(ReturnStmt stmt){
        System.out.println("ReturnStmt");

        if (stmt.getValue() != null) {
            emitInstructions("ADDR "+lexicalLevel+" "+(-(num_par+2)));
            stmt.getValue().accept(this);
            if (stmt.getValue() instanceof IdentExpn) {
                emitInstructions("LOAD");
            }
            emitInstructions("STORE");
        }

        return true;
    }
    public Boolean visit(Scope stmt){
        System.out.println("Scope");
        
        ASTList<Stmt> body = stmt.getBody();
        System.out.println("Body length before finding declarations: " + body.size());
        findDeclarations(body);
        System.out.println("Body length after finding declarations: " + body.size());
        body.accept(this);
//        if (body != null ) {
//            for (Stmt s : body){
//                if (s instanceof MultiDeclarations) {
//                    s.accept(this);
//                }
//            }
//
//            for (Stmt s : body){
//                if (!(s instanceof MultiDeclarations)) {
//                    s.accept(this);
//                }
//            }
//        }
        return true;
    }

    private void findDeclarations(ASTList<Stmt> body){
        if (body == null){
            return;
        }
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < body.size(); i++){
            Stmt s = body.get(i);
            if (s instanceof MultiDeclarations){
                s.accept(this);
                toRemove.add(i);
            } else if (s instanceof LoopingStmt){
                findDeclarations(((LoopingStmt) s).getBody());
            } else if (s instanceof IfStmt){

                findDeclarations(((IfStmt) s).getWhenTrue());
                findDeclarations(((IfStmt) s).getWhenFalse());
            }
        }

        // remove in reverse order
        for (int j = toRemove.size() - 1; j >= 0; j--){
            body.remove(toRemove.get(j).intValue());
        }
    }

    public Boolean visit(Stmt stmt){
        System.out.println("Stmt");
        return true;
    }
    public Boolean visit(WhileDoStmt stmt){
        System.out.println("WhileDoStmt");

        loopExits.add(new ArrayList<Integer>());

        int expnBegin = instructionCounter.size();
        stmt.getExpn().accept(this);
        if (stmt.getExpn() instanceof IdentExpn){
            emitInstructions("LOAD");
        }
        int branchToEndAddr = instructionCounter.size() + 1;
        emitInstructions("PUSH 0");
        emitInstructions("BF");
        stmt.getBody().accept(this);
        emitInstructions("PUSH " + expnBegin); // loop back to beginning
        emitInstructions("BR");
        int loopEndAddr = instructionCounter.size();
        try {
            Machine.writeMemory((short) branchToEndAddr, (short) loopEndAddr);
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> patchAddresses = loopExits.remove(loopExits.size() - 1);
        for (Integer i: patchAddresses){
            try {
                Machine.writeMemory(i.shortValue(), (short) loopEndAddr);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Boolean visit(BooleanType type){
        System.out.println("BooleanType");
        return true;
    }
    public Boolean visit(IntegerType type){
        System.out.println("IntegerType");
        return true;
    }

}