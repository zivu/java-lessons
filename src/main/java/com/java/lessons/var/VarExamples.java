package com.java.lessons.var;

//import lombok.var;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;

/**
 * This lesson includes local variable type inference from jdk 10
 * and local variable syntax for lambda parameters (jdk 11).
 * Official documentation: https://openjdk.java.net/jeps/286
 * Style guidelines: https://openjdk.java.net/projects/amber/LVTIstyle.html
 */
public class VarExamples {

    public static void main(String[] args) {
        varIsAllowedInCastingLambdaExpressionsToIntersectionTypes();
    }

    /**
     * Reserved type name 'var' was introduced to be used instead of the explicit type declaration,
     * where type can be deduced.
     */

    /*------------------------------------ HOW TO USE 'var' ------------------------------------*/

    public static void jdk10VarValidUseCases() {
//  (1) local variable declarations with initializer;
        var users = new ArrayList<String>();
        users.add("Tom");
        users.add("Jim");

//  (2) enhanced for-loop
        for (var user : users) {
            System.out.println(user);
        }

//  (3) traditional for-loop
        for (var i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }

//  (4) try with resources
        try (var scanner = new Scanner(new File("src/main/resources/example.txt"))) {
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//  (5) with anonymous classes
        var x = new Runnable() {
            @Override
            public void run() {
                System.out.println("var is allowed with anonymous classes");
            }
        };
        x.run();
    }

    public interface LogSystem {
        void print();
    }

//  (6) casting lambda expressions to intersection types
    public static void varIsAllowedInCastingLambdaExpressionsToIntersectionTypes() {
        var x = (LogSystem & Serializable) () -> System.out.println("implementation");
        x.print();
    }

    /*------------------------------------ HOW TO NOT USE 'var' ------------------------------------*/

//  (1) var cannot be used in catch clauses
//        catch (var e) {
//            e.printStackTrace();
//        }

// (2) public static void varNotAllowedForMethodArgumentTypeDeclaration(var illegalVarDeclaration) {
//        System.out.println(illegalVarDeclaration);
//    }

// (3) var cannot be used to define constructor arguments type
//    public VarExamples(var x) {
//        this.x = x;
//    }

// (4) public var varCannotBeUsedAsMethodReturnType() {
//        return "example";
//    }

// (5) private var varNotAllowedToBeUsedAsFieldType;

    public void prohibitedCasesWithVar() {
//  (6) var not allowed as a target type of lambda expression
//      var serialNumber = (x, y) -> x + "&" + y;

//  (7) null cannot be assigned to var
//      var x = null;

//  (8) var cannot be used with array initializer
//      var x = {"one", "two"};
    }

    /*------------------------------------ 'var' IS NOT A KEYWORD, IT IS RESERVED TYPE NAME, HENCE ------------------------------------*/

//  (1) you can use var as a variable name
    private final String var ="t";
//  you cannot do it with keywords
//  private final String class = "";

//  (2) you can use it as a package name;

//  (3) you can use it as a method name
    public void var() {}

//  (4) we cannot create a class name with a name var
//  public static class var {}

//  (5) we cannot name interface as 'var'
//  public interface var {}

    /*------------------------------------ JAVA 11 ------------------------------------*/

    /**
     * Java 11 allows using var in lambda expressions.
     * 1. (var x, var y) -> x + "-" + y ... is the same as ... (x, y) -> x + "-" + y
     * 2. This change is a further enhancement of var usage in java,
     *    that was introduced in jdk 10.
     * 3. var allows us usage of annotations (@NotNull for example).
     * 4. var cannot be mixed with no var.
     * 5. var cannot be mixed with explicitly typed parameters (e.i. int)
     * 6. var should be used for all lambda formal parameters not be used at all.
     */
    public static void combineSerialNumberAndPrint(String classification, String no) {
        BiFunction<String, String, String> serialNumber = (var x, var y) -> x + "-" + y;
        System.out.println(serialNumber.apply(classification, no));
    }

    /*------------------------------------ LOMBOK ALTERNATIVE: 'var' and 'val' ------------------------------------*/

    /*
     * Explicit import required:
     * import lombok.var;
     */

    /*------------------------------------ PITFALLS ------------------------------------*/

    /**
     * Be careful using var with diamonds <>.
     * Type inference for generic methods relies on the target type (what on the left of '=').
     * For 'var inferredQueue' inferred type is PriorityQueue<Object> might be not what we might expect.
     * Can be mitigated by providing constructors with parameters.
     */
    public void varPitfallsWithDiamonds() {
        PriorityQueue<String> queue = new PriorityQueue<>();
        System.out.println(queue);
        var inferredQueue = new PriorityQueue<>();
        System.out.println(inferredQueue);
        Comparator<String> comparator = (o1, o2) -> 0;
        var correctInferredQueue = new PriorityQueue<>(comparator);
        System.out.println(correctInferredQueue);
    }

    /**
     * It is a common practice in java to construct a concrete instance and assign it to an interface type.
     * If 'var' is used, the inferred type is a concrete instance, instead of an interface.
     */
    public void varPitfallsWithList() {
        List<String> users = new ArrayList<>();
        System.out.println(users);
        var workers = new ArrayList<>();
        System.out.println(workers);
    }

    /**
     * With a numeric type on the left, a type might be widened or narrowed to types other than int.
     * With 'var' type is always int.
     */
    public void pitfallWithNumericLiterals() {
        byte point = 0;
        short hash = 0x77f;
        long id = 2;
        var x = 34;
    }

}
