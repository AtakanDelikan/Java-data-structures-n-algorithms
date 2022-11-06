import static org.junit.Assert.*;
import org.junit.Test;

public class CountSortTest {

   /** Number of milliseconds allowed to spend for sorting 1 million elements */
   public static int threshold = 25;

   /** Test data */
   static CountSort.Color[] balls = null;

   static int rCount = 0;
   static int gCount = 0;
   static int bCount = 0;

   /** Correctness check for the result */
   static boolean check (CountSort.Color[] balls, int r, int g, int b) {
      if (balls == null)
         return false;
      int len = balls.length;
      if (r<0 || g<0 || b<0 || len != r+g+b)
         return false; 
      if (len == 0)
         return true;
      for (int i=0; i < r; i++)
         if (balls[i] != CountSort.Color.red)
            return false;
      for (int i=r; i < r+g; i++)
         if (balls[i] != CountSort.Color.green)
            return false;
      for (int i=r+g; i < len; i++)
         if (balls[i] != CountSort.Color.blue)
            return false;
      return true;
   } // check

    @Test (timeout=1000)
    public void testFunctionality() {
      balls = new CountSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
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
      CountSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testShort() {
      balls = new CountSort.Color [1];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      double rnd = Math.random();
      if (rnd < 1./3.) {
         balls[0] = CountSort.Color.red;
         rCount++;
      } else  if (rnd > 2./3.) {
         balls[0] = CountSort.Color.blue;
         bCount++;
      } else {
         balls[0] = CountSort.Color.green;
         gCount++;
      }
      CountSort.reorder (balls);
      assertTrue ("One element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new CountSort.Color [0];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      CountSort.reorder (balls);
      assertTrue ("Zero element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new CountSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         rnd = Math.random();
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
      CountSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllRed() {
      balls = new CountSort.Color [100000];
      int len = balls.length;
      rCount = len;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = CountSort.Color.red;
      }
      CountSort.reorder (balls);
      assertTrue ("Result incorrect for all red", 
         check (balls, rCount, gCount, bCount));
      balls = new CountSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
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
      CountSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllGreen() {
      balls = new CountSort.Color [100000];
      int len = balls.length;
      rCount = 0;
      gCount = len;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = CountSort.Color.green;
      }
      CountSort.reorder (balls);
      assertTrue ("Result incorrect for all green", 
         check (balls, rCount, gCount, bCount));
      balls = new CountSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
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
      CountSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllBlue() {
      balls = new CountSort.Color [100000];
      int len = balls.length;
      rCount = 0;
      gCount = 0;
      bCount = len;
      for (int i=0; i < len; i++) {
            balls[i] = CountSort.Color.blue;
      }
      CountSort.reorder (balls);
      assertTrue ("Result incorrect for all blue",
         check (balls, rCount, gCount, bCount));
      balls = new CountSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
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
      CountSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testSpeed() {
      balls = new CountSort.Color [1000000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
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
      long t0 = System.currentTimeMillis();
      CountSort.reorder (balls);
      long t1 = System.currentTimeMillis();
      int delta = (int)(t1-t0);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
      assertTrue ("Too slow: "+ delta, delta < threshold);
      System.out.println ("Time spent: " + delta + " ms");
    }

/*
   @Test (expected=RuntimeException.class)
   public void testNullArray() {
      CountSort.reorder (null);
   }
*/
}

