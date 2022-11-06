/** This class represents fractions of form n/d where n and d are long integer
 * numbers. Basic operations and arithmetics for fractions are provided.
 * Atakan Delikan
 */
public class Lfraction implements Comparable<Lfraction> {

   /** Main method. Different tests. */
   public static void main (String[] param) {
      Lfraction f1 = Lfraction.toLfraction (Math.PI, 7);
      System.out.println(f1);
   }


   private final long numerator;
   private final long denominator;

   /** Private method to calculate greatest common divisor.
    * @return GCD
    */
   private static long gcd(long a, long b) {
      long gcd = 1;
      while (b != 0) {
         gcd = b;
         b = a % b;
         a = gcd;
      }
      return Math.abs(gcd);
   }

   /** Constructor.
    * @param a numerator
    * @param b denominator > 0
    */
   public Lfraction (long a, long b) {
      if (b < 1)
         throw new IllegalArgumentException("denominator should be a positive integer");
      long gcd = gcd(a, b);
      this.numerator = a/gcd;
      this.denominator = b/gcd;
   }

   /** Public method to access the numerator field.
    * @return numerator
    */
   public long getNumerator() {
      return this.numerator;
   }

   /** Public method to access the denominator field.
    * @return denominator
    */
   public long getDenominator() {
      return this.denominator;
   }

   /** Conversion to string.
    * @return string representation of the fraction
    */
   @Override
   public String toString() {
      String n=String.valueOf(numerator);
      String d=String.valueOf(denominator);
      return n+'/'+d;
   }

   /** Equality test.
    * @param m second fraction
    * @return true if fractions this and m are equal
    */
   @Override
   public boolean equals (Object m) {
      if (getClass() != m.getClass())
         return false;
      if ( ((Lfraction)m).numerator == this.numerator &&
              ((Lfraction)m).denominator == this.denominator)
         return true;
      return false;
   }

   /** Hashcode has to be the same for equal fractions and in general, different
    * for different fractions.
    * @return hashcode
    */
   @Override
   public int hashCode() {
      return (int)(numerator>>16) + (int)denominator;  //custom way to create hash
      //return Objects.hash(numerator, denominator);   //built-in java hash generator
   }

   /** Sum of fractions.
    * @param m second addend
    * @return this+m
    */
   public Lfraction plus (Lfraction m) {
      long gdc = gcd(this.denominator, m.denominator);// to reduce risk of integer overflow
      long numerator = (m.denominator/gdc)*this.numerator +
              (this.denominator/gdc)*m.numerator;
      long denominator = (m.denominator/gdc)*this.denominator;
      return new Lfraction (numerator, denominator);
   }

   /** Multiplication of fractions.
    * @param m second factor
    * @return this*m
    */
   public Lfraction times (Lfraction m) {
      long gdc1 = gcd(this.denominator, m.numerator);
      long gdc2 = gcd(this.numerator, m.denominator);
      long numerator = (m.numerator/gdc1)*(this.numerator/gdc2);
      long denominator = (m.denominator/gdc2)*(this.denominator/gdc1);
      return new Lfraction (numerator, denominator);
   }

   /** Inverse of the fraction. n/d becomes d/n.
    * @return inverse of this fraction: 1/this
    */
   public Lfraction inverse() {
      if (numerator == 0)
         throw new IllegalArgumentException("fraction " + this +
                 "has numerator 0. Thus cannot be inversed.");
      if (numerator < 0)
         return new Lfraction (-denominator, -numerator);
      return new Lfraction (denominator, numerator);
   }

   /** Power of fractions.
    * @parm n
    * @return m ** n
    */
   public Lfraction pow (int n) {
      if (n == 0) {
         return new Lfraction(1,1);
      } else if (n == 1) {
         return new Lfraction(this.numerator, this.denominator);
      } else if (n == -1) {
         return this.inverse();
      } else if (n > 1) {
         return this.times(this.pow(n - 1));
      }
      return this.pow(-n).inverse();
   }

   /** Opposite of the fraction. n/d becomes -n/d.
    * @return opposite of this fraction: -this
    */
   public Lfraction opposite() {

      return new Lfraction (-numerator, denominator);
   }

   /** Difference of fractions.
    * @param m subtrahend
    * @return this-m
    */
   public Lfraction minus (Lfraction m) {

      return plus(m.opposite());
   }

   /** Quotient of fractions.
    * @param m divisor
    * @return this/m
    */
   public Lfraction divideBy (Lfraction m) {
      if (m.numerator == 0)
         throw new IllegalArgumentException("fraction " + this +
                 "cannot be divided by fraction zero: " + m);
      return times(m.inverse());
   }

   /** Comparision of fractions.
    * @param m second fraction
    * @return -1 if this < m; 0 if this==m; 1 if this > m
    */
   @Override
   public int compareTo (Lfraction m) {
      if (this.equals(m))
         return 0;
      long gdc = gcd(this.denominator, m.denominator);
      if ((this.denominator/gdc)*m.numerator < (m.denominator/gdc)*this.numerator)
         return 1;
      return -1;
   }

   /** Clone of the fraction.
    * @return new fraction equal to this
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Lfraction (numerator, denominator);
   }

   /** Integer part of the (improper) fraction.
    * @return integer part of this fraction
    */
   public long integerPart() {

      return numerator/denominator;
   }

   /** Extract fraction part of the (improper) fraction
    * (a proper fraction without the integer part).
    * @return fraction part of this fraction
    */
   public Lfraction fractionPart() {

      return new Lfraction (numerator%denominator, denominator);
   }

   /** Approximate value of the fraction.
    * @return real value of this fraction
    */
   public double toDouble() {

      return (double) numerator/denominator;
   }

   /** Double value f presented as a fraction with denominator d > 0.
    * @param f real number
    * @param d positive denominator for the result
    * @return f as an approximate fraction of form n/d
    */
   public static Lfraction toLfraction (double f, long d) {

      return new Lfraction (Math.round(f*d), d);
   }

   /** Conversion from string to the fraction. Accepts strings of form
    * that is defined by the toString method.
    * @param s string form (as produced by toString) of the fraction
    * @return fraction represented by s
    */
   public static Lfraction valueOf (String s) {
      String[] numbers = s.split("\\/", -1);
      if (numbers.length != 2) // if not two elements created or if elements are not integers
         throw new IllegalArgumentException("string " + s + "cannot be represented as fraction");
      long numerator;
      long denominator;
      try {
         numerator = Long.parseLong(numbers[0]);
         denominator = Long.parseLong(numbers[1]);
      }
      catch(Exception e) {
         throw new IllegalArgumentException("illegal character in string " + s);
      }
      return new Lfraction (numerator, denominator);
   }
}

