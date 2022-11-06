/** Word puzzle.
 * @since 1.8
 * Atakan Delikan
 * A brute-force approach to solve a world puzzle.
 * An example of the puzzle:
 * SEND + MORE = MONEY resolves to S=9, E=5, N=6, D=7, M=1, O=0, R=8, Y=2 (9567 + 1085 = 10652)
 */
public class Puzzle {

   /** Solve the word puzzle.
    * @param args three words (addend1 addend2 sum)
    */
   public static void main (String[] args) {
      Puzzle p = new Puzzle();

      StringBuilder uniqueLetters = new StringBuilder("");
      for (String arg : args)
      {
         for (int i = 0; i < arg.length(); i++) {
            if (uniqueLetters.toString().indexOf(arg.charAt(i)) < 0){
               uniqueLetters.append(arg.charAt(i));
            }
         }
      }

      p.addend1_indexes = new int[args[0].length()];
      for (int i = 0; i < args[0].length(); i++) {
         p.addend1_indexes[i] = uniqueLetters.toString().indexOf(args[0].charAt(i));
      }
      p.addend2_indexes = new int[args[1].length()];
      for (int i = 0; i < args[1].length(); i++) {
         p.addend2_indexes[i] = uniqueLetters.toString().indexOf(args[1].charAt(i));
      }
      p.sum_indexes = new int[args[2].length()];
      for (int i = 0; i < args[2].length(); i++) {
         p.sum_indexes[i] = uniqueLetters.toString().indexOf(args[2].charAt(i));
      }

      p.uniqueLetterCount = uniqueLetters.length();
      p.addend1 = args[0];
      p.addend2 = args[1];
      p.sum = args[2];
      p.run();
   }

   public void run() {
      System.out.println("--------------------------");
      System.out.print(addend1);
      System.out.print(" + ");
      System.out.print(addend2);
      System.out.print(" = ");
      System.out.println(sum);
      int[] arr = new int[]{1,2,3,4,5,6,7,8,9,0};
      int[] data = new int[uniqueLetterCount];
      combination(arr, data, 0, 9, 0, uniqueLetterCount);
      System.out.print("solution count is: ");
      System.out.println(solutionCount);
   }

   int uniqueLetterCount;
   private String addend1;
   private String addend2;
   private String sum;
   private int[] addend1_indexes;
   private int[] addend2_indexes;
   private int[] sum_indexes;
   private int solutionCount = 0;

   //https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
   public void combination(int[] arr, int[] data, int start, int end, int index, int r)
   {
      // Current combination is ready to be printed, print it
      if (index == r)
      {
         permute(data, 0);
         return;
      }

      for (int i=start; i<=end && end-i+1 >= r-index; i++)
      {
         data[index] = arr[i];
         combination(arr, data, i+1, end, index+1, r);
      }
   }

   //https://stackoverflow.com/questions/2920315/permutation-of-array
   public void permute(int[] arr, int k){
      for(int i = k; i < arr.length; i++){
         swap(arr, i, k);
         permute(arr, k+1);
         swap(arr, k, i);
      }
      if (k == arr.length -1){
         isSolution(arr);
      }
   }

   static void swap(int[] arr, int i , int k){
      int temp = arr[i];
      arr[i] = arr[k];
      arr[k] = temp;
   }

   public void isSolution(int[] arr){
      if (arr[addend1_indexes[0]]==0 ||
              arr[addend2_indexes[0]]==0 ||
              arr[sum_indexes[0]]==0){
         return;
      }

      long addend1 = 0;
      int z = addend1_indexes.length - 1;
      for (int index: addend1_indexes) {
         addend1 += arr[index]*(Math.pow(10,z));
         z--;
      }
      long addend2 = 0;
      z = addend2_indexes.length - 1;
      for (int index: addend2_indexes) {
         addend2 += arr[index]*(Math.pow(10,z));
         z--;
      }

      long sum = 0;
      z = sum_indexes.length - 1;
      for (int index: sum_indexes) {
         sum += arr[index]*(Math.pow(10,z));
         z--;
      }

      if (addend1 + addend2 == sum){
         System.out.print(addend1);
         System.out.print(" + ");
         System.out.print(addend2);
         System.out.print(" = ");
         System.out.print(sum);
         System.out.println("");
         solutionCount++;
      }
   }
}

