/** Sorting of balls.
 * @since 1.8
 * Atakan Delikan
 * Sorting an array of enum variables with 3 unique keys using counting sort algorithm.
 */
public class CountSort {

   enum Color {red, green, blue};
   
   public static void main (String[] param) {
      // for debugging
      Color balls[] = new CountSort.Color [100];
      int rCount = 0;
      int gCount = 0;
      int bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = CountSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = CountSort.Color.blue;
            bCount++;
         } else {
            balls[i] = CountSort.Color.green;
            gCount++;
         }
      }

      System.out.println("red count " + rCount);
      System.out.println("green count " + gCount);
      System.out.println("blue count " + bCount);

      reorder(balls);

      for (Color var : balls) {
         if (var == Color.red) {
            System.out.println("red ");
         } else if (var == Color.green) {
            System.out.println("green ");
         } else {
            System.out.println("blue ");
         }
      }
   }
   
   public static void reorder (Color[] balls) {

      if (balls.length < 2) {
         return;
      }
      int red_count = 0;
      int green_count = 0;
      int blue_count = 0;

      for (Color var : balls) {
         if (var.equals(Color.red)) {
            balls[red_count] = Color.red;
            red_count++;
         } else if (var.equals(Color.green)) {
            green_count++;
         } else {
            blue_count++;
         }
      }

      int i = red_count;
      int endpoint = green_count+red_count;
      for (; i < endpoint; i++) {
         balls[i] = Color.green;
      }
      endpoint += blue_count;
      for (; i < endpoint; i++) {
         balls[i] = Color.blue;
      }
}
