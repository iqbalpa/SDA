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
        posisiKurcaci = new int[P];
        for (int i=0; i<P; i++){
            int Ri = in.nextInt(); // posisi kurcaci
            posisiKurcaci[i] = Ri-1;
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
                SIMULASI();
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
        int from = F-1;
        int to = E-1;
        int result = dijkstraKabur(N, graf, from, to);
        out.println(result);
    }
    static void SIMULASI(){
        graf.add(new ArrayList<>());
        int K = in.nextInt();
        for (int i=0; i<K; i++){
            int Vi = in.nextInt();
            graf.get(graf.size()-1).add(new Node(Vi-1, 0, 0));
        }
        int[] dist = dijkstraSimulasi(N+1, graf, graf.size()-1);
        int time = Integer.MIN_VALUE;
        for (int i=0; i<posisiKurcaci.length; i++){
            if (time < dist[posisiKurcaci[i]]){
                time = dist[posisiKurcaci[i]];
            }
        }
        graf.remove(graf.size()-1);
        out.println(time);
    }
    static void SUPER(int V1, int V2, int V3){
        int[][] dist1 = dijkstraSuper(N, graf, V1-1);
        int[][] dist2 = dijkstraSuper(N, graf, V2-1);

        // System.out.println("============= dist1 ==============");
        // for (int i=0; i<2; i++) {
        //     for (int j=0; j<N; j++){
        //         System.out.printf("%d   ", dist1[i][j]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("============= dist2 ==============");
        // for (int i=0; i<2; i++) {
        //     for (int j=0; j<N; j++){
        //         System.out.printf("%d   ", dist2[i][j]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("==================================");

        int result1_0 = dist1[0][V2-1];
        int result1_1 = dist1[1][V2-1];
        int result2_0 = dist2[0][V3-1];
        int result2_1 = dist2[1][V3-1];

        if (result1_0 + result2_1 <= result1_1 + result2_0) {
            out.println(result1_0 + result2_1);
        } else {
            out.println(result1_1 + result2_0);
        }
    }

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
    public static int dijkstraKabur(int V, ArrayList<ArrayList<Node>> graph, int src, int dest){
        int[] sizeTerowongan = new int[V];
        boolean[] visited = new boolean[V];
        for (int i=0; i<V; i++) sizeTerowongan[i] = Integer.MAX_VALUE;
        sizeTerowongan[src] = 0;

        int temp = Integer.MAX_VALUE;

        Heap pq = new Heap();
        pq.insert(new Node(src, 0, 0));

        while (!pq.isEmpty()){
            Node current = pq.poll();

            visited[current.getVertex()] = true;
            if (current.getVertex() != src && temp > current.getSize()) temp = current.getSize();
            if (current.getVertex() == dest) break;
            
            for (Node n: graph.get(current.getVertex())){
                if (visited[n.getVertex()]) continue;
                pq.insert(new Node(n.getVertex(), 0, n.getSize()));
            }
        }
        return temp;
    }
    // * QUERY SIMULASI: dijkstra pake length node (ascending)
    public static int[] dijkstraSimulasi(int V, ArrayList<ArrayList<Node>> graph, int src){
        int[] lengthTerowongan = new int[V];
        for (int i=0; i<V; i++) lengthTerowongan[i] = Integer.MAX_VALUE;
        lengthTerowongan[src] = 0;

        Heap pq = new Heap();
        pq.insert(new Node(src, 0, 0));

        while (pq.getSize() > 0){
            Node current = pq.poll();

            for (Node n: graph.get(current.getVertex())){
                if (lengthTerowongan[current.getVertex()] + n.getLength() < lengthTerowongan[n.getVertex()]){
                    lengthTerowongan[n.getVertex()] = lengthTerowongan[current.getVertex()] + n.getLength();
                    pq.insert(new Node(n.getVertex(), lengthTerowongan[n.getVertex()], n.getSize()));
                }
            }
        }
        return lengthTerowongan;
    }
    // * QUERY SUPER: dijkstra pake length node (ascending) dan ada 2 state (non super dan super)
    public static int[][] dijkstraSuper(int V, ArrayList<ArrayList<Node>> graph, int src){
        int[][] distance = new int[2][V];
        for (int i=0; i<2; i++) for (int j=0; j<N; j++) distance[i][j] = Integer.MAX_VALUE;
        distance[0][src] = 0;
        distance[1][src] = 0;

        Heap pq = new Heap();
        pq.insert(new Node(src, 0, 0));

        while (!pq.isEmpty()){
            Node current = pq.poll();

            for (Node n: graph.get(current.getVertex())){
                // * state 1
                // saat pake SUPER
                if (current.useSuper == 0){
                    if (distance[1][n.getVertex()] > distance[0][current.getVertex()]){
                        // tambah path lama (gausah nambahin length yg di-SUPER)
                        distance[1][n.getVertex()] = distance[0][current.getVertex()];
                        // add node ke pq
                        Node node1 = new Node(n.getVertex(), distance[1][n.getVertex()], 0);
                        node1.useSuper = 1;
                        pq.insert(node1);
                    }
                }
                if (distance[current.useSuper][current.getVertex()] + n.getLength() < distance[current.useSuper][n.getVertex()]){
                    // * state 0
                    // * kalo udah make SUPER, maka akan terus di state 1
                    // update shortest path biasa
                    distance[current.useSuper][n.getVertex()] = distance[current.useSuper][current.getVertex()] + n.getLength();
                    // add node ke pq
                    Node node = new Node(n.getVertex(), distance[current.useSuper][n.getVertex()], 0);
                    node.useSuper = current.useSuper;
                    pq.insert(node);
                }
            }
        }
        return distance;
    }
}

// REFERENSI: 
// Geeksforgeeks https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
class Node implements Comparable<Node> {
    int vertex;
    int length, size;
    int useSuper;
    Node(int v, int l, int s) {
        this.vertex = v;
        this.length = l;
        this.size = s;
        this.useSuper = 0;
    }
    int getVertex(){
        return this.vertex;
    }
    int getLength(){
        return this.length;
    }
    int getSize(){
        return this.size;
    }
    @Override
    public int compareTo(Node n){
        if (this.length < n.getLength()) return -1;
        else if (this.length > n.getLength()) return 1;
        else return n.getSize() - this.size;
    }
}

class Heap {
    ArrayList<Node> heap;
    Heap(){
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
