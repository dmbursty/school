// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
// JAVAC:
/**
 * Parser/weeder:
 * - Break statement not allowed.
 */
public class Je_1_NonJoosConstructs_Break {

    public Je_1_NonJoosConstructs_Break() {}

    public static int test() {
	int x = 117;
	while (x>0) {
	    x=x-1;
	}
	return 123;
    }
}

// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
/**
 * Parser/weeder:
 * - Continue statements not allowed.
 */
public class Je_1_NonJoosConstructs_Continue {

    public Je_1_NonJoosConstructs_Continue() {}

    public static int test() {
	int x = 117;
	while (x>0) {
		x = x - 1;
	}
	return 123;
    }
}


// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
// JAVAC:UNKNOWN
/**
 * Parser/weeder:
 * - Expression sequencing not allowed.
 */
public class Je_1_NonJoosConstructs_ExpressionSequence {

    public Je_1_NonJoosConstructs_ExpressionSequence() {}

    public static int test() {
	int i = 0;
	int j= (i = i + 1, i); 
	return 123;
    }

}
// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
/**
 * Parser/weeder:
 * - Multiple types pr. file not allowed.
 */
public class Je_1_NonJoosConstructs_MultipleTypesPrFile {

    public Je_1_NonJoosConstructs_MultipleTypesPrFile(){}
    
    public static int test() {
    	return 123;
    }
}

class A {

    public A(){}
}
// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
/**
 * Parser/weeder:
 * - Nested types not allowed.
 */
public class Je_1_NonJoosConstructs_NestedTypes {

    public Je_1_NonJoosConstructs_NestedTypes() {}

    public class A {
	
		public A(){}
    }
    
    public static int test() {
    	return 123;
    }
}
public class Je_1_NonJoosConstructs_PrivateFields {

    public int x;

    public Je_1_NonJoosConstructs_PrivateFields() {}

    public static int test() {
	return 123;
    }
}
public class Je_1_NonJoosConstructs_PrivateMethods {

    public Je_1_NonJoosConstructs_PrivateMethods () {}

    public int m() {
	return 42;
    }

    public static int test() {
	return 123;
    }
}
// JOOS1:PARSER_WEEDER,PARSER_EXCEPTION
// JOOS2:PARSER_WEEDER,PARSER_EXCEPTION
// JAVAC:
/**
 * Parser/weeder:
 * - Static initializers not allowed in joos
 */
public class Je_1_NonJoosConstructs_StaticInitializers {

    public Je_1_NonJoosConstructs_StaticInitializers() {}

    static { 
	Je_1_NonJoosConstructs_StaticInitializers.test(); 
    }

    public static int test() {
	return 123;
    }
}

