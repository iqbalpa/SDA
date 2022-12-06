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
        int result = MSTKabur(graf, from, to);
        out.println(result);
    }
    static void SIMULASI(){}
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
    // Geeksforgeeks https://www.geeksforgeeks.org/maximum-spanning-tree-using-prims-algorithm/
    // * QUERY KABUR: Prim's Algorithm Maximum Spanning Tree
    public static int findMaxNode(boolean[] visited, int size[]){
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;
        for (int i=0; i<N; i++){
            if (!visited[i] && size[i] > max){
                max = size[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public static int MSTKabur(ArrayList<ArrayList<Node>> graf, int start, int dest){
        boolean[] visited = new boolean[N];
        int[] parent = new int[N];
        int[] size = new int[N];
        int temp = Integer.MAX_VALUE;
        for (int i=0; i<N; i++) size[i] = Integer.MIN_VALUE;
 
        size[start] = Integer.MAX_VALUE;
        parent[start] = -1;

        for (int i=0; i<N-1; i++){
            int maxNodeIndex = findMaxNode(visited, size);
            visited[maxNodeIndex] = true;
            for (int j=0; j<graf.get(maxNodeIndex).size(); j++){
                int node = graf.get(maxNodeIndex).get(j).vertex;
                int sizeNode = graf.get(maxNodeIndex).get(j).size;
                
                if (!visited[node] && sizeNode > size[node]){
                    parent[node] = maxNodeIndex;
                    size[node] = sizeNode;
                }
                if (temp > size[node] && node == dest) temp = size[node];
                if (node == dest) break;
            }
        }

        return temp;
    }
    // public static int MSTKabur(ArrayList<ArrayList<Node>> graf, int start, int dest){
    //     boolean[] visited = new boolean[N];
    //     int[] parent = new int[N];
    //     int[] size = new int[N];

    //     int temp = Integer.MAX_VALUE;

    //     for (int i=0; i<N; i++){
    //         size[i] = Integer.MIN_VALUE;
    //     }

    //     size[0] = Integer.MAX_VALUE;
    //     parent[0] = -1;

    //     for (int i=0; i<N-1; i++){
    //         int maxNodeIndex = findMaxNode(visited, size);
    //         visited[maxNodeIndex] = true;
    //         for (int j=0; j<graf.get(maxNodeIndex).size(); j++){
    //             int node = graf.get(maxNodeIndex).get(j).vertex;
    //             int sizeNode = graf.get(maxNodeIndex).get(j).size;
    //             if (!visited[node] && sizeNode > size[node]){
    //                 parent[node] = maxNodeIndex;
    //                 size[node] = sizeNode;
    //             }
    //             if (temp > size[node] && node == dest) temp = size[node];
    //             if (node == dest) break;
    //         }
    //     }
    //     return temp;
    // }
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
