import java.util.*;

/** Stack manipulation.
 * @since 1.8
 * Atakan Delikan
 * A calculator for postfix  expressions using stack.
 */
public class DoubleStack {

   public static void main (String[] argum) {
      System.out.println(DoubleStack.interpret ("2 5 SWAP -"));
      System.out.println(DoubleStack.interpret ("3 DUP *"));
      System.out.println(DoubleStack.interpret ("2 5 9 ROT - +"));
      System.out.println(DoubleStack.interpret ("-3 -5 -7 ROT - SWAP DUP * +"));
   }
   private Stack<Double> stack = new Stack<Double>();

   DoubleStack() {
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      DoubleStack tmp = new DoubleStack();
      tmp.stack = (Stack<Double>)stack.clone();
      return tmp;
   }

   public boolean stEmpty() {
      return stack.isEmpty();
   }

   public void push (double a) {
      //Double dObj = new Double(a);
      stack.push(a);
      
   }

   public double pop() {
      if (stEmpty())
         throw new IndexOutOfBoundsException(" stack underflow");
      return stack.pop();
      
   } // pop

   public void op (String s) {
      double op2 = pop();

      if (s.equals("DUP")){
         push(op2);
         push(op2);
         return;
      }
      double op1 = pop();
      if (s.equals("+"))
         push(op1 + op2);
      else if (s.equals("-"))
         push(op1 - op2);
      else if (s.equals("*"))
         push(op1 * op2);
      else if (s.equals("/"))
         push(op1 / op2);
      else if (s.equals("SWAP")) {
         push(op2);
         push(op1);
      }
      else if (s.equals("ROT")) {
         double op3 = pop();
         push(op1);
         push(op2);
         push(op3);
      }
      else
         throw new IllegalArgumentException("Invalid operation: " + s);
   }
  
   public double tos() {
      if (stEmpty())
         throw new IndexOutOfBoundsException(" stack underflow");
      return stack.peek();
   }

   @Override
   public boolean equals (Object o) {
      return ((DoubleStack)o).stack.equals(this.stack);
   }

   @Override
   public String toString() {
      return stack.toString();
   }

   public static double interpret (String pol) {
      //Reference : https://javarevisited.blogspot.com/2016/10/how-to-split-string-in-java-by-whitespace-or-tabs.html
      String[] arguments = pol.trim().split("\\s+");
      //System.out.println("output string after trim() and split: " + Arrays.toString(arguments) + arguments.length);
      if (arguments[0] == ""){// split always returns at least 1 element
         throw new IllegalArgumentException("Entered an empty expression: " + "\"" + pol + "\"");
      }
      DoubleStack obj = new DoubleStack();
      for (String argument : arguments)
      {
         if (argument.equals("+") ||
                 argument.equals("*") ||
                 argument.equals("-") ||
                 argument.equals("/") ||
                 argument.equals("SWAP"))
         {

            if (obj.stack.size() < 2){
               throw new IllegalArgumentException("Not enough numbers for operation " +
                       "\"" + argument + "\"" + " in expression " + "\"" + pol + "\"");
            }
            obj.op(argument);
            continue;
         }
         if (argument.equals("DUP"))
         {

            if (obj.stack.size() < 1){
               throw new IllegalArgumentException("Not enough numbers for operation " +
                       "\"" + argument + "\"" + " in expression " + "\"" + pol + "\"");
            }
            obj.op(argument);
            continue;
         }
         if (argument.equals("ROT"))
         {

            if (obj.stack.size() < 3){
               throw new IllegalArgumentException("Not enough numbers for operation " +
                       "\"" + argument + "\"" + " in expression " + "\"" + pol + "\"");
            }
            obj.op(argument);
            continue;
         }

         try {
            double val = Double.parseDouble(argument);
            obj.push(val);
         }
         catch(Exception e) {
            throw new IllegalArgumentException("Illegal operand : " + argument +
                    " in expression " + "\"" + pol + "\"");
         }
      }
      if (obj.stack.size() > 1){
         throw new IllegalArgumentException("Too many numbers in expression " + "\"" + pol + "\"");
      }

      return obj.tos(); 
   }

}