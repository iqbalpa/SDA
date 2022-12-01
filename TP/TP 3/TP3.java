import java.io.*;
import java.util.*;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;
    static ArrayList<ArrayList<AdjListNode>> graf = new ArrayList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak pos
        int N = in.nextInt(); 
        for (int i=0; i<N; i++){
            graf.add(new ArrayList<>()); // index start from 0
        }
        // banyak terowongan
        int M = in.nextInt();
        for (int i=0; i<M; i++){
            int Ai = in.nextInt(); // from
            int Bi = in.nextInt(); // to
            int Li = in.nextInt(); // length
            int Si = in.nextInt(); // size
            // undirected graph
            graf.get(Ai-1).add(new AdjListNode(Bi-1, Li, Si));
            graf.get(Bi-1).add(new AdjListNode(Ai-1, Li, Si));
        }
        // banyak kurcaci
        int P = in.nextInt();
        for (int i=0; i<P; i++){
            int Ri = in.nextInt(); // posisi kurcaci
            graf.get(Ri-1).get(Ri-1).jmlKurcaci = 1; // ! masih salah
            // * bisa pake array biasa buat nyimpen jumlah kurcaci di tiap pos
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

    static void KABUR(int F, int E){}
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
    static class AdjListNode {
        int vertex, jmlKurcaci;
        long length, size;
        AdjListNode(int v, long w, long s) {
            this.vertex = v;
            this.length = w;
            this.size = s;
            this.jmlKurcaci = 0;
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
    }
    public static long[] dijkstra(int V, ArrayList<ArrayList<AdjListNode>> graph, int src){
        long[] distance = new long[V];
        for (int i=0; i<V; i++) distance[i] = Long.MAX_VALUE;
        distance[src] = (long)0;

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>((v1,v2) -> (int)(v1.getLength() - v2.getLength()));
        pq.add(new AdjListNode(src, 0, 0));

        while (pq.size() > 0){
            AdjListNode current = pq.poll();
            
            for (AdjListNode n: graph.get(current.getVertex())){
                if (distance[current.getVertex()] + n.getLength() < distance[n.getVertex()]){
                    distance[n.getVertex()] = distance[current.getVertex()] + n.getLength();
                    pq.add(new AdjListNode(n.getVertex(), distance[n.getVertex()], n.getSize()));
                }
            }
        }
        return distance;
    }
}