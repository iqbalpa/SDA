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
            // System.out.println("===== query ke: " + (i+1));
            if (query.equals("KABUR")){
                int F = in.nextInt();
                int E = in.nextInt();
                // KABUR(F, E);
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
        int temp = dijkstraKabur(N, graf, F-1, E-1);
        out.println(temp);
        // dist[F-1] = 0;
        // out.println(dist[E-1]);
    }
    static void SIMULASI(){
        int K = in.nextInt();
        // int[] exit = new int[K];
        int[] longest = new int[K];
        for (int i=0; i<K; i++){
            int Vi = in.nextInt();
            // exit[i] = Vi-1;
            int[] dist = dijkstraSimulasi(N, graf, Vi-1);
            for (int j=0; j<posisiKurcaci.length; j++){
                if (dist[posisiKurcaci[j]] > longest[i]){
                    longest[i] = dist[posisiKurcaci[j]];
                }
            }
        }
        // find min in array longest
        long min = longest[0];
        for (int i=1; i<longest.length; i++){
            if (longest[i] < min && longest[i] != 0){
                min = longest[i];
            }
        }
        out.println(min);
    }
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
    public static int dijkstraKabur(int V, ArrayList<ArrayList<Node>> graph, int src, int dest){
        int[] sizeTerowongan = new int[V];
        for (int i=0; i<V; i++) sizeTerowongan[i] = Integer.MIN_VALUE;
        sizeTerowongan[src] = (int)0;

        int temp = Integer.MAX_VALUE;
        int counter = 0;
        boolean[] visited = new boolean[V];

        Heap pq = new Heap(N, new Comparator<Node>(){
            @Override
            public int compare(Node v1, Node v2){
                return v2.getSize() - v1.getSize();
            }
        });
        pq.insert(new Node(src, 0, 0));
        // visited[src] = true;

        while (pq.getSize() > 0){
            Node current = pq.poll();
            visited[current.getVertex()] = true;
            if (current.getVertex() == dest) visited[current.getVertex()] = false;

            for (Node n: graph.get(current.getVertex())){
                if (visited[n.getVertex()]) continue;

                if (sizeTerowongan[current.getVertex()] + n.getSize() > sizeTerowongan[n.getVertex()]){
                    sizeTerowongan[n.getVertex()] = sizeTerowongan[current.getVertex()] + n.getSize();

                    if (temp == Integer.MAX_VALUE && n.getVertex() == dest){
                        temp = n.getSize();
                    } else if (temp > n.getSize() && n.getVertex() == dest){
                        temp = n.getSize();
                    }

                    pq.insert(new Node(n.getVertex(), n.getLength(), sizeTerowongan[n.getVertex()]));
                }
            }
            counter++;
        }
        out.println(">>>> jml while loop: " + counter);
        return temp;
    }

    // * QUERY SIMULASI: dijkstra pake length node (ascending)
    public static int[] dijkstraSimulasi(int V, ArrayList<ArrayList<Node>> graph, int src){
        int[] lengthTerowongan = new int[V];
        for (int i=0; i<V; i++) lengthTerowongan[i] = Integer.MAX_VALUE;
        lengthTerowongan[src] = 0;

        Heap pq = new Heap(N, new Comparator<Node>(){
            @Override
            public int compare(Node v1, Node v2){
                return v1.getLength() - v2.getLength();
            }
        });
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
}

// REFERENSI: 
// Geeksforgeeks https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
class Node implements Comparable<Node> {
    int vertex;
    int length, size;
    Node(int v, int l, int s) {
        this.vertex = v;
        this.length = l;
        this.size = s;
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
        return (int)(n.size - this.size);
    }
}

class Heap {
    Node[] heap;
    int tail;
    Heap(int M, Comparator<Node> comparator){
        heap = new Node[M];
        tail = 0;
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
        return tail;
    }
    Node getMin(){
        return heap[0];
    }
    void swap(int i, int j){
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    void percolateUp(int i){
        if (i==0) return;
        if (heap[parent(i)].compareTo(heap[i]) > 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;
        if (left < tail && heap[left].compareTo(heap[smallest]) < 0) smallest = left;
        if (right < tail && heap[right].compareTo(heap[smallest]) < 0) smallest = right;
        if (smallest != i){
            swap(i, smallest);
            percolateDown(smallest);
        }
    }
    void insert(Node n){
        heap[tail] = n;
        percolateUp(tail);
        tail++;
    }
    Node poll(){
        Node min = heap[0];
        heap[0] = heap[tail-1];
        tail--;
        percolateDown(0);
        return min;
    }
}
