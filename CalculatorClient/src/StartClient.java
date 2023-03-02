/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhaowei
 */
import Calculator.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.*;
 
public class StartClient {
    private static Calc calcObj;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // create and initialize the ORB
	    ORB orb = ORB.init(args, null);
            
            // get the root naming context
	    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
            // Use NamingContextExt instead of NamingContext. This is 
            // part of the Interoperable naming Service. 
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // resolve the Object Reference in Naming
	    calcObj = (Calc) CalcHelper.narrow(ncRef.resolve_str("Calculator"));

            while(true) {
                // asking for input and read it
                System.out.println("\n------------------------------------------\n");
                System.out.println("For help with the operands aviable type 'Operands_av'\n");
                Scanner c=new Scanner(System.in);
		        String input = c.nextLine();
                
                // if the command is exit, request the server to shutdown
                if (input.toLowerCase().equals("exit")) {
                    calcObj.exit();
                    break;
                }
                
                // test the input
                String[] inputParams = input.split(" ");
                if (inputParams.length > 3) {

                    System.out.println("Client Exception 1: Wrong number of parameters . Try again...");
                    
                    continue;
                }
                    

                if(inputParams[0].equals("Operands_av")){
                    System.out.println("\nList of operands: add + | substract - |  multiply * | divide / | square root -> sqrt |"
                        +"\nIn case of square root only uses the first operator [operator][sp][operand1]-> sqrt 2"); 
                        continue;                   
                }
                else if(inputParams[0].equals("sqrt")){
                    if(inputParams.length != 2){
                        System.out.println("Client Exception 2: Wrong number of parameters for sqrt. Try again...");
                        continue;
                    }
                }
                //initialize in case theres no input so code keeps running----------------------
                int operatorCode=1;
                int operand1=1;
                int operand2=1;
                
                // set calculation type
                if (inputParams[0].equals("+")) {
                    operatorCode = 1;
                }
                else if (inputParams[0].equals("-")) {
                    operatorCode = 2;
                }
                else if (inputParams[0].equals("*")) {
                    operatorCode = 3;
                }
                else if (inputParams[0].equals("/")) {
                    operatorCode = 4;
                }else if (inputParams[0].equals("sqrt")) {
                    operatorCode = 5;
                }else if (inputParams[0].equals("Operands_av")){
                    operatorCode = 6;
                }else {
                    System.out.println("Client Exception: Un-recognized operation code. Try again...");
                    continue;
                }
                
                // test input operands are integers
                try {
                    //check for not out of range error---------------------------------
                    if(operatorCode < 5){
                        operand1 = Integer.parseInt(inputParams[1]);
                        operand2 = Integer.parseInt(inputParams[2]);
                    }else if(operatorCode == 5){
                        operand1 = Integer.parseInt(inputParams[1]);
                    }
                    
                }
                catch (NumberFormatException e) {
                    System.out.println("Client Exception 3: Wrong number format. Try again...");
                    continue;
                }
                
                // check if it is divided by zero
                if (operatorCode == 4 && operand2 == 0) {
                    System.out.println("Client Exception 4: Can't be divided by zero. Try again...");
                    continue;
                }
                
                // do the calculation and return result---------------------------------------------------
		        float result = calcObj.calculate(operatorCode, operand1, operand2);
                if (result == Integer.MAX_VALUE) {
                    System.out.println ("There might be an Integer Overflow. Please try again...");
                }
                else if (result == Integer.MIN_VALUE) {
                    System.out.println ("There might be an Integer Underflow. Please try again...");
                }
                
		        System.out.println("The result is: " + result);
            }
        }
        catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
