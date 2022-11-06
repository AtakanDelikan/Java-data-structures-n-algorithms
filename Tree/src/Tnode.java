import javax.swing.tree.TreeNode;
import java.util.*;

/** Tree with two pointers.
 * @since 1.8
 * Atakan Delikan
 * An RPN calculator using Tree structure.
 * Creates tree from an RPN expression, and evaluates it.
 */
public class Tnode {

   private String name;
   private Tnode firstChild;
   private Tnode nextSibling;

   @Override
   public String toString() {
      StringBuffer b = new StringBuffer();
      if(this.getName()!=null) {
         b.append(getName());
      }
      if(this.firstChild != null)
      {
         b.append("(").append(this.firstChild.getNextSibling()).
                 append(",").append(this.getFirstChild()).append(")");
      }
      return b.toString();
   }

//   @Override
//   public Object clone() throws CloneNotSupportedException
//   {
//      Tnode firstChild = getFirstChild();
//      Tnode nextSibling = getNextSibling();
//      return new Tnode(getName());
//              //firstChild == null? null: (Tnode) firstChild.clone(),
//              //nextSibling == null? null: (Tnode) nextSibling.clone());
//   }
   @Override
   public Object clone() throws CloneNotSupportedException
   {
      Tnode cloned = new Tnode();
      cloned.name = this.name;
      cloneTree(this, cloned);
      return cloned;
   }

   public static void cloneTree (Tnode root, Tnode cloned) {

      if (root.firstChild != null) {
         cloned.firstChild = new Tnode();
         cloned.firstChild.name = root.firstChild.name;
         cloneTree(root.firstChild, cloned.firstChild);
      }
      if (root.nextSibling != null) {
         cloned.nextSibling = new Tnode();
         cloned.nextSibling.name = root.nextSibling.name;
         cloneTree(root.nextSibling, cloned.nextSibling);
      }
   }

   Tnode (String s, Tnode p, Tnode a)
   {
      setName(s);
      setFirstChild(p);
      p.setNextSibling(a);
   }

   Tnode(String s)
   {
      setName(s);
   }

   Tnode()
   {
   }

   private void setName(String s)
   {
      this.name = s;
   }

   private String getName()
   {
      return name;
   }

   private void setNextSibling(Tnode p)
   {
      this.nextSibling = p;
   }

   private Tnode getNextSibling()
   {
      return nextSibling;
   }

   private void setFirstChild(Tnode a)
   {
      this.firstChild = a;
   }

   private Tnode getFirstChild()
   {
      return firstChild;
   }

   public static Tnode buildFromRPN (String pol) {
      Tnode root = null;
      if(pol.isEmpty())
      {
         throw new RuntimeException(pol + "Input string is empty");
      }
      String[] convert = pol.trim().split("\\s+");
      String illegalCharacters = "!@#$%&()',:;<=>?[]^_`{|}x";
      String operators = "+-*/";
      String SWAP = "SWAP";
      String DUP = "DUP";
      String ROT = "ROT";
      Tnode x, y, z;
      String more_operators ="SWAP DUP ROT";
      for(int i = 0; i < pol.trim().length();i++) {
         char symbol = pol.trim().charAt(i);

         if (illegalCharacters.contains(Character.toString(symbol))) {
            throw new RuntimeException(pol.trim()
                    + " expression has illegal symbol " + symbol);
         }
      }
      Stack <Tnode> numSave = new Stack<>();
      int operandCount=0;
      int operatorCount=0;
      for(String t : convert)
      {
         if(!operators.contains(t) && !more_operators.contains(t))
         {
            root = new Tnode(t);
            operandCount++;
            numSave.push(root);
         }
         else{
            if(t.equals(SWAP))
            {
               if(numSave.size() < 2)
               {
                  throw new RuntimeException("Expression " + pol.trim() +  " does not have" +
                          " 2 subtress");
               }
               x = numSave.pop();
               y = numSave.pop();
               numSave.push(x);
               numSave.push(y);
            }
            else if(t.equals(DUP))
            {
               if(root == null)
               {
                  throw new RuntimeException("Tree does not exist - " + pol.trim());
               }
               x = numSave.peek();
               try {
                  x.clone();
               } catch (CloneNotSupportedException e) {
                  e.printStackTrace();
               }
               numSave.push(x);
               operandCount++;
            }
            else if(t.equals(ROT))
            {
               if(numSave.size() < 3)
               {
                  throw new RuntimeException("Not enough subtrees - " + pol.trim());
               }
               x = numSave.pop();
               y = numSave.pop();
               z = numSave.pop();
               numSave.push(y);
               numSave.push(x);
               numSave.push(z);
            }
            else {
               if (numSave.size() < 2) {
                  throw new IndexOutOfBoundsException("Expression " + pol.trim()
                          + " does not have enough operands to build " + t + " node");
               }
               root = new Tnode(t, numSave.pop(), numSave.pop());
               numSave.push(root);
               operatorCount++;
            }
         }
         //numSave.push(root);
      }
      if(operandCount - operatorCount != 1)
      {
         throw new RuntimeException("Expression " + pol.trim()
                 +" must have one less operator than operands, you have: operands - "
                 + operandCount + " operators - " + operatorCount);
      }
      return root;
   }

   public static void main (String[] param) {
      String rpn = "3 DUP 4 ROT / /";
      System.out.println ("RPN: " + rpn);
      Tnode res = buildFromRPN (rpn);
      System.out.println ("Tree: " + res);
//      String rpn = "5 1 - 7 * 6 3 / + ";
//      System.out.println ("RPN: " + rpn);
//      Tnode res = buildFromRPN (rpn);
//      System.out.println ("Tree: " + res);
//      String rpn = "7 3 6 5 + - / ";
//      System.out.println ("RPN: " + rpn);
//      Tnode res = buildFromRPN (rpn);
//      System.out.println ("Tree: " + res);

   }
}

