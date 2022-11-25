import java.io.*;
import java.util.*;

// IDEAS BY:
// 1. Fresty Tania S
// 2. Monica Oktaviona

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    static List<Integer> attackedBenteng = new ArrayList<>();
    static long[] distances;
    static ArrayList<ArrayList<AdjListNode>> graf = new ArrayList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(), M = in.nextInt();
        distances = new long[N];

        for (int i = 0; i < N; i++) {
            // TODO: Inisialisasi setiap benteng
            graf.add(new ArrayList<>());
            distances[i] = Long.MAX_VALUE;
        }

        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            // TODO: Tandai benteng F sebagai benteng diserang
            attackedBenteng.add(F-1);
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graf.get(B-1).add(new AdjListNode(A-1, W));
        }

        // mencari jarak terdekat dari suatu benteng ke benteng yang sedang diserang,
        // dengan cara memanggil method dijkstra untuk benteng yang sedang diserang untuk
        // mengetahui shortest path
        for (int i = 0; i < attackedBenteng.size(); i++) {
            long[] temp = dijkstra(N, graf, attackedBenteng.get(i));
            for (int j = 0; j < temp.length; j++) {
                if (distances[j] > temp[j]) {
                    distances[j] = temp[j];
                }
            }
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            // jika berhasil menyelamatkan
            if (distances[S-1] < K){
                out.println("YES");
            }
            // jika gagal
            else {
                out.println("NO");
            }
        }
        out.close();
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
    static class AdjListNode {
        int vertex;
        long weight;
        AdjListNode(int v, long w){
            this.vertex = v;
            this.weight = w;
        }
        int getVertex(){
            return this.vertex;
        }
        long getWeight(){
            return this.weight;
        }
    }
    public static long[] dijkstra(int V, ArrayList<ArrayList<AdjListNode>> graph, int src){
        long[] distance = new long[V];
        for (int i=0; i<V; i++) distance[i] = Long.MAX_VALUE;
        distance[src] = (long)0;

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>((v1,v2) -> (int)(v1.getWeight() - v2.getWeight()));
        pq.add(new AdjListNode(src, 0));

        while (pq.size() > 0){
            AdjListNode current = pq.poll();
            
            for (AdjListNode n: graph.get(current.getVertex())){
                if (distance[current.getVertex()] + n.getWeight() < distance[n.getVertex()]){
                    distance[n.getVertex()] = distance[current.getVertex()] + n.getWeight();
                    pq.add(new AdjListNode(n.getVertex(), distance[n.getVertex()]));
                }
            }
        }
        return distance;
    }
}