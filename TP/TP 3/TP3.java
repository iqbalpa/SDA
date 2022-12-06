import java.io.*;
import java.util.*;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;
    static ArrayList<ArrayList<Node>> graf = new ArrayList<>();
    static int[] posisiKurcaci;
    static int M, N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak pos
        N = in.nextInt(); 
        posisiKurcaci = new int[N];
        for (int i=0; i<N; i++){
            graf.add(new ArrayList<>()); // index start from 0
        }
        // banyak terowongan
        M = in.nextInt();
        for (int i=0; i<M; i++){
            int Ai = in.nextInt(); // from
            int Bi = in.nextInt(); // to
            int Li = in.nextInt(); // length
            int Si = in.nextInt(); // size
            // undirected graph
            graf.get(Ai-1).add(new Node(Bi-1, Li, Si));
            graf.get(Bi-1).add(new Node(Ai-1, Li, Si));
        }
        // banyak kurcaci
        int P = in.nextInt();
        for (int i=0; i<P; i++){
            int Ri = in.nextInt(); // posisi kurcaci
            // inisiasi jumlah kurcaci di post Ri-1
            posisiKurcaci[Ri-1] = 1;
        }

        // banyak query
        int Q = in.nextInt();
        for (int i=0; i<Q; i++){
            String query = in.next();
            if (query.equals("KABUR")){
                int F = in.nextInt();
                int E = in.nextInt();
                KABUR(F, E);
            } 
            else if (query.equals("SIMULASI")){
                // get input
            } 
            else if (query.equals("SUPER")){
                int V1 = in.nextInt();
                int V2 = in.nextInt();
                int V3 = in.nextInt();
                SUPER(V1, V2, V3);
            }
        }

        out.close();
    }

    static void KABUR(int F, int E){
        long[] dist = dijkstraKabur(M, graf, F-1);
        out.println(dist[E-1]);
    }
    static void SIMULASI(int[] K){}
    static void SUPER(int V1, int V2, int V3){}

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    // REFERENSI: 
    // Geeksforgeeks https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
    // * QUERY KABUR: dijkstra pake size node (descending)
    public static long[] dijkstraKabur(int V, ArrayList<ArrayList<Node>> graph, int src){
        long[] sizeTerowongan = new long[V];
        for (int i=0; i<V; i++) sizeTerowongan[i] = Long.MIN_VALUE;
        sizeTerowongan[src] = (long)0;

        Heap pq = new Heap(M, new Comparator<Node>(){
            @Override
            public int compare(Node v1, Node v2){
                return (int)(v2.getSize() - v1.getSize());
            }
        });

        pq.insert(new Node(src, 0, 0));

        while (pq.getSize() > 0){
            Node current = pq.poll();
            
            for (Node n: graph.get(current.getVertex())){
                if (sizeTerowongan[current.getVertex()] + n.getSize() > sizeTerowongan[n.getVertex()]){
                    sizeTerowongan[n.getVertex()] = sizeTerowongan[current.getVertex()] + n.getSize();
                    pq.insert(new Node(n.getVertex(), sizeTerowongan[n.getVertex()], n.getSize()));
                }
            }
        }
        return sizeTerowongan;
    }
}

// REFERENSI: 
// Geeksforgeeks https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
class Node implements Comparable<Node> {
    int vertex;
    long length, size;
    Node(int v, long w, long s) {
        this.vertex = v;
        this.length = w;
        this.size = s;
    }
    int getVertex(){
        return this.vertex;
    }
    long getLength(){
        return this.length;
    }
    long getSize(){
        return this.size;
    }
    @Override
    public int compareTo(Node n){
        return (int)(n.size - this.size);
    }
}

class Heap {
    ArrayList<Node> heap;
    Heap(int M, Comparator<Node> comparator){
        heap = new ArrayList<>();
    }
    int parent(int i){
        return (i-1)/2;
    }
    int leftChild(int i){
        return 2*i+1;
    }
    int rightChild(int i){
        return 2*i+2;
    }
    int getSize(){
        return heap.size();
    }
    boolean isEmpty(){
        return heap.size() == 0;
    }
    Node getMin(){
        return heap.get(0);
    }
    void swap(int i, int j){
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    void percolateUp(int i){
        if (i == 0) return;
        if (heap.get(i).compareTo(heap.get(parent(i))) < 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int min = i;
        int l = leftChild(i);
        if (l < heap.size() && heap.get(l).compareTo(heap.get(min)) < 0){
            min = l;
        }
        int r = rightChild(i);
        if (r < heap.size() && heap.get(r).compareTo(heap.get(min)) < 0){
            min = r;
        }
        if (min != i){
            swap(i, min);
            percolateDown(min);
        }
    }
    void insert(Node n){
        heap.add(n);
        percolateUp(heap.size()-1);
    }
    Node poll(){
        Node min = heap.get(0);
        heap.set(0, heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        percolateDown(0);
        return min;
    }
}
