import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 * @since 1.8
 * Atakan Delikan
 * Application of Edmonds-Karp Algorithm to find max flow between any 2 nodes in a graph.
 * The graph is a custom abstract data structure created for this task.
 * Arcs(nodes) holds source and destination arcs, a pointer to next arc from same vertex, as well as unique ID. They are directional.
 * Vertices holds a pointer to another vertex in the graph and first arc from that vertex.
 */
public class GraphTask {

   /** Main method. */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /** Actual main method to run examples and everything. */
   public void run() {
      Graph g = new Graph ("G");
      g.createTestGraph1();
      g.createTestGraph2();
      g.createTestGraph3();
      g.createTestGraph4();
      //g.createTestGraph5();

   }

   /** Finding max flow from source vertex to sink vertex on a directed graph
    * using Edmonds-Karp Algorithm.
    */
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private Vertex leading;
      private int info = 0;

      private int lastVisit;
      // You can add more fields, if needed

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }


      /**
       * Mark the vertex with the last iteration of bfs to indicate
       * it was visited on that iteration
       * @param bfsIteration breadth first search iteration
       */
      public void visitVertex(int bfsIteration){
         this.lastVisit = bfsIteration;
      }

      /**
       * Check the vertex-field lastVisit to understand if it was
       * marked(visited) on the current iteration of bfs
       * @param bfsIteration breadth first search iteration
       * @return true if visited, false otherwise
       */
      public boolean isVisited(int bfsIteration){
         return this.lastVisit == bfsIteration;
      }


   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private final int limit;
      private int flow = 0;
      private int info = 0;
      // You can add more fields, if needed

      Arc (String s, Vertex v, Arc a, int limit) {
         id = s;
         target = v;
         next = a;
         this.limit = limit;
      }

      Arc (String s) {
         this (s, null, null, 0);
      }

      @Override
      public String toString() {
         return id;
      }


      /**
       * Calculate and return remaining capacity of the arc from its limit current flow through it.
       * @return remaining capacity of the arc
       */
      public int capacity(){
         return this.limit - this.flow;
      }
   }


   /** This header represents a graph.
    */
   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      // You can add more fields, if needed
      private int bfsIteration = 0;

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append ("| A:");
               sb.append (a.toString());
               sb.append (", l:");
               sb.append (a.limit);
               sb.append (", f:");
               sb.append (a.flow);
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (") ");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_"
                       + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                       + varray [vnr].toString(), varray [i], varray [vnr]);
            } else {}
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException
                    ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0)
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }


      /**
       * Find max flow from source vertex to sink vertex
       * @param s source vertex
       * @param t sink vertex
       * @return max flow from s to t
       */
      public int maxFlow (Vertex s, Vertex t){

         Graph g = new Graph("residual graph", s);
         System.out.println("--------------------------------");
         System.out.println("A: Arc ID.");
         System.out.println("l: flow limit of the arc.");
         System.out.println("f: current flow through the arc.(negative if in opposite direction of the arc)");
         System.out.println("initial graph: ");
         System.out.println(g);

         bfsIteration = 0;
         int maxFlow = 0;
         ArrayList<Arc> path = new ArrayList<>();
         ArrayList<Arc> residualPath = new ArrayList<>();

         Vertex tempV1;
         Vertex tempV2;
         s.leading = null;

         while (bfs(s, t)){
            path.clear();
            residualPath.clear();

            int pathCapacity = Integer.MAX_VALUE;
            tempV1 =t;
            tempV2 =t.leading;
            while (tempV2 != null){
               path.add(findArc(tempV2, tempV1));
               residualPath.add(findArc(tempV1, tempV2));

               int tempCap = path.get(path.size() - 1).capacity();
               if (tempCap < pathCapacity){
                  pathCapacity = tempCap;
               }

               tempV1 = tempV2;
               tempV2 = tempV1.leading;
            }
            for (Arc arc : path) {
               arc.flow += pathCapacity;
            }
            for (Arc arc : residualPath) {
               arc.flow -= pathCapacity;
            }
            maxFlow += pathCapacity;

            System.out.println(g);
            System.out.println("augmenting path capacity: "+ pathCapacity);
            System.out.println("found augmenting path: " + path);
         }

         System.out.println("max flow: " + maxFlow);
         return maxFlow;
      }

      /**
       * Find arc from one given vertex to another given vertex
       * @param v1 source vertex
       * @param v2 target vertex
       * @return found arc from v1 to v2
       */
      public Arc findArc(Vertex v1, Vertex v2){
         Arc tempArc = v1.first;

         while (tempArc != null){
            if (tempArc.target == v2){return tempArc;}
            tempArc = tempArc.next;
         }
         throw new IllegalArgumentException("There is no arc from: "+ v1 +" to: "+ v2);
      }

      /**
       * Breadth first search to get the shortest augmentation path from source to sink.
       * Side effect: corrupts "leading" fields of vertexes in the graph
       * @param s source vertex
       * @param t sink vertex
       * @return true if sink vertex is reached, false if cannot be reached.
       */
      public boolean bfs(Vertex s, Vertex t){
         Queue<Vertex> queue = new ArrayDeque<>();
         queue.add(s);
         Vertex currV = s;
         bfsIteration++;//make all unvisited
         currV.visitVertex(bfsIteration);
         while (queue.peek() != null){
            currV = queue.poll();

            Arc currA = currV.first;
            while (currA != null){
               if (currA.capacity() > 0 && !currA.target.isVisited(bfsIteration)) {
                  currA.target.visitVertex(bfsIteration);
                  queue.add(currA.target);
                  currA.target.leading = currV;

                  if (currA.target == t){return true;}
               }
               currA = currA.next;
            }

         }
         return false;
      }

      public void createTestGraph1 () {

         Vertex v5 = new Vertex("5");
         Vertex v4 = new Vertex("4", v5, null);
         Vertex v3 = new Vertex("3", v4, null);
         Vertex v2 = new Vertex("2", v3, null);
         Vertex v1 = new Vertex("1", v2, null);
         Vertex v0 = new Vertex("0", v1, null);

         Arc a0 = new Arc("0", v1, null, 16);
         Arc a1 = new Arc("1", v2, a0, 13);
         v0.first = a1;

         Arc a2 = new Arc("2", v0, null, 0);
         Arc a3 = new Arc("3", v2, a2, 10);
         Arc a4 = new Arc("4", v3, a3, 12);
         v1.first = a4;

         Arc a5 = new Arc("5", v0, null, 0);
         Arc a6 = new Arc("6", v1, a5, 4);
         Arc a7 = new Arc("7", v3, a6, 0);
         Arc a8 = new Arc("8", v4, a7, 14);
         v2.first = a8;

         Arc a9 = new Arc("9", v1, null, 0);
         Arc a10 = new Arc("10",v2 , a9, 9);
         Arc a11 = new Arc("11", v4, a10, 0);
         Arc a12 = new Arc("12", v5, a11, 20);
         v3.first = a12;

         Arc a13 = new Arc("13", v2, null, 0);
         Arc a14 = new Arc("14", v3 , a13, 7);
         Arc a15 = new Arc("15", v5 , a14, 4);
         v4.first = a15;

         Arc a16 = new Arc("16", v3, null, 0);
         Arc a17 = new Arc("17", v4, a16, 0);
         v5.first = a17;

         maxFlow (v0, v5);

      }

      public void createTestGraph2 () {

         Vertex v7 = new Vertex("7");
         Vertex v6 = new Vertex("6", v7, null);
         Vertex v5 = new Vertex("5", v6, null);
         Vertex v4 = new Vertex("4", v5, null);
         Vertex v3 = new Vertex("3", v4, null);
         Vertex v2 = new Vertex("2", v3, null);
         Vertex v1 = new Vertex("1", v2, null);
         Vertex v0 = new Vertex("0", v1, null);

         Arc a0 = new Arc("0", v1, null, 21);
         Arc a1 = new Arc("1", v3, a0, 15);
         v0.first = a1;

         Arc a2 = new Arc("2", v5, null, 11);
         Arc a3 = new Arc("3", v0, a2, 0);
         v1.first = a3;

         Arc a4 = new Arc("4", v4, null, 12);
         Arc a5 = new Arc("5", v5, a4, 3);
         Arc a6 = new Arc("6", v3, a5, 5);
         v2.first = a6;

         Arc a7 = new Arc("7", v2, null, 7);
         Arc a8 = new Arc("8", v6, a7, 16);
         Arc a9 = new Arc("9", v0, a8, 0);
         v3.first = a9;

         Arc a10 = new Arc("10", v7, null, 18);
         Arc a11 = new Arc("11", v2, a10, 4);
         v4.first = a11;

         Arc a12 = new Arc("12", v7, null, 25);
         Arc a13 = new Arc("13", v2, a12, 0);
         Arc a14 = new Arc("14", v1, a13, 11);
         v5.first = a14;

         Arc a15 = new Arc("15", v7, null, 13);
         Arc a16 = new Arc("16", v3, a15, 0);
         v6.first = a16;

         Arc a17 = new Arc("17", v6, null, 0);
         Arc a18 = new Arc("18", v5, a17, 0);
         Arc a19 = new Arc("19", v4, a18, 0);
         v7.first = a19;

         maxFlow (v0, v7);

      }

      public void createTestGraph3 () {

         Vertex v5 = new Vertex("5");
         Vertex v4 = new Vertex("4", v5, null);
         Vertex v3 = new Vertex("3", v4, null);
         Vertex v2 = new Vertex("2", v3, null);
         Vertex v1 = new Vertex("1", v2, null);
         Vertex v0 = new Vertex("0", v1, null);

         Arc a0 = new Arc("0", v1, null, 10);
         Arc a1 = new Arc("1", v2, a0, 10);
         v0.first = a1;

         Arc a2 = new Arc("2", v3, null, 25);
         Arc a3 = new Arc("3", v4, a2, 0);
         Arc a4 = new Arc("4", v0, a3, 0);
         v1.first = a4;

         Arc a5 = new Arc("5", v4, null, 15);
         Arc a6 = new Arc("6", v0, a5, 0);
         v2.first = a6;

         Arc a7 = new Arc("7", v5, null, 10);
         Arc a8 = new Arc("8", v1, a7, 0);
         v3.first = a8;

         Arc a9 = new Arc("9", v5, null, 10);
         Arc a10 = new Arc("10",v2 , a9, 0);
         Arc a11 = new Arc("11", v1, a10, 6);
         v4.first = a11;

         Arc a12 = new Arc("12", v4, null, 0);
         Arc a13 = new Arc("13", v3, a12, 0);
         v5.first = a13;

         maxFlow (v0, v5);

      }

      public void createTestGraph4 () {

         Vertex v10 = new Vertex("10");
         Vertex v9 = new Vertex("9", v10, null);
         Vertex v8 = new Vertex("8", v9, null);
         Vertex v7 = new Vertex("7", v8, null);
         Vertex v6 = new Vertex("6", v7, null);
         Vertex v5 = new Vertex("5", v6, null);
         Vertex v4 = new Vertex("4", v5, null);
         Vertex v3 = new Vertex("3", v4, null);
         Vertex v2 = new Vertex("2", v3, null);
         Vertex v1 = new Vertex("1", v2, null);
         Vertex v0 = new Vertex("0", v1, null);

         Arc a0 = new Arc("0", v1, null, 5);
         Arc a1 = new Arc("1", v2, a0, 10);
         Arc a2 = new Arc("2", v3, a1, 5);
         v0.first = a2;

         Arc a3 = new Arc("3", v4, null, 10);
         Arc a4 = new Arc("4", v2, a3, 0);
         Arc a5 = new Arc("5", v0, a4, 0);
         v1.first = a5;

         Arc a6 = new Arc("6", v1, null, 15);
         Arc a7 = new Arc("7", v5, a6, 20);
         Arc a8 = new Arc("8", v0, a7, 0);
         v2.first = a8;

         Arc a9 = new Arc("9", v5, null, 0);
         Arc a10 = new Arc("10", v6, a9, 10);
         Arc a11 = new Arc("11", v0, a10, 0);
         v3.first = a11;

         Arc a12 = new Arc("12", v7, null, 10);
         Arc a13 = new Arc("13", v8, a12, 0);
         Arc a14 = new Arc("14", v5, a13, 25);
         Arc a15 = new Arc("15", v1, a14, 0);
         v4.first = a15;

         Arc a16 = new Arc("16", v4, null, 0);
         Arc a17 = new Arc("17", v8, a16, 30);
         Arc a18 = new Arc("18", v3, a17, 5);
         Arc a19 = new Arc("19", v2, a18, 0);
         v5.first = a19;

         Arc a20 = new Arc("20", v8, null, 5);
         Arc a21 = new Arc("21", v9, a20, 10);
         Arc a22 = new Arc("22", v3, a21, 0);
         v6.first = a22;

         Arc a23 = new Arc("23", v10, null, 5);
         Arc a24 = new Arc("24", v4, a23, 0);
         v7.first = a24;

         Arc a25 = new Arc("25", v10, null, 15);
         Arc a26 = new Arc("26", v9, a25, 5);
         Arc a27 = new Arc("27", v6, a26, 0);
         Arc a28 = new Arc("28", v5, a27, 0);
         Arc a29 = new Arc("29", v4, a28, 15);
         v8.first = a29;

         Arc a30 = new Arc("30", v8, null, 0);
         Arc a31 = new Arc("31", v10, a30, 10);
         Arc a32 = new Arc("32", v6, a31, 0);
         v9.first = a32;

         Arc a33 = new Arc("33", v9, null, 0);
         Arc a34 = new Arc("34", v8, a33, 0);
         Arc a35 = new Arc("35", v7, a34, 0);
         v10.first = a35;

         maxFlow (v0, v10);

      }

      public void createTestGraph5 () {
         Vertex vFirst = new Vertex("0");
         Graph g = new Graph("g5", vFirst);

         Vertex tempV1 = vFirst;
         Vertex tempV2;

         int n = 25;//rows
         int m = 100;//columns

         int arcIndex = 0;
         for (int i = 1; i < m*n; i++) {
            tempV2 = new Vertex(String.valueOf(i));
            tempV1.next = tempV2;
            if (i % n != 0){
               arcIndex = makeRandomCapArc (tempV1, tempV2, arcIndex);
            }
            tempV1 = tempV2;
         }
         Vertex vLast = tempV1;

         tempV1 = vFirst;
         tempV2 = vFirst;
         for (int i = 0; i < n; i++) {
            tempV2 = tempV2.next;
         }

         while (tempV2 != null){
            arcIndex = makeRandomCapArc (tempV1, tempV2, arcIndex);
            tempV1 = tempV1.next;
            tempV2 = tempV2.next;
         }

         maxFlow (vFirst, vLast);
      }

      /**
       * Create two random capacity arcs between given vertexes.
       * @param v1 vertex 1
       * @param v2 vertex 2
       * @param arcIndex index of last created arc
       * @return index of last created arc after creating 2 more
       */
      public int makeRandomCapArc (Vertex v1, Vertex v2, int arcIndex) {

         Arc tempA = v1.first;

         Arc a1 = new Arc(String.valueOf(arcIndex++), v2, tempA, (int)(Math.random()*995 + 5));
         v1.first = a1;

         tempA = v2.first;
         a1 = new Arc(String.valueOf(arcIndex++), v1, tempA, (int)(Math.random()*995 + 5));
         v2.first = a1;

         return arcIndex;
      }
   }
}

