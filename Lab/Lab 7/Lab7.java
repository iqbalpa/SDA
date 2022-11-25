import java.io.*;
import java.util.*;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    static Graph graf = new Graph();
    static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        N = in.nextInt();
        int M = in.nextInt();

        for (int i = 1; i <= N; i++) {
            // TODO: Inisialisasi setiap benteng
            Vertex v = graf.getVertex(i);
        }

        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            // TODO: Tandai benteng F sebagai benteng diserang
            Vertex v = graf.getVertex(F);
            v.isAttacked = true;
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graf.addEdge(A, B, W);
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            if (K == 0) out.println("NO");
            else {
                
            }
            // * Biar ga TLE adalah dengan cara jalanin method dijkstra 1x
            // * untuk setiap vertex kita harus save path ke vertex lain
            // * jadi kita bisa cek apakah vertex yang di serang bisa di reach
            // * dengan jarak yang lebih kecil dari K
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
}

// REFERENSI:
// slide Pak Alfan: https://ir.cs.ui.ac.id/alfan/sda/graf/
// untuk Path
class Edge {
    Vertex to;
    int numOfEnemy;
    Edge(Vertex to, int numOfEnemy) {
        this.to = to;
        this.numOfEnemy = numOfEnemy;
    }
}

// Untuk Benteng
class Vertex implements Comparable<Vertex> {
    int id;
    Vertex prev;
    boolean isAttacked;
    List<Edge> adjList;
    int dist;
    int scratch;
    PriorityQueue<Vertex> pq;
    Integer[] distances;

    Vertex(int id) {
        this.id = id;
        this.isAttacked = false;
        this.adjList = new LinkedList<>();
        this.distances = new Integer[Lab7.N];
        this.pq = new PriorityQueue<>();
        reset();
    }
    void reset(){
        this.prev = null;
        this.dist = Graph.INFINITY;
        this.scratch = 0;
    }
    Vertex getFirst(){
        return pq.peek();
    }
    @Override
    public int compareTo(Vertex other) {
        if (this.isAttacked) return -1;
        else if (other.isAttacked) return -1;
        else return Integer.compare(this.dist, other.dist);
    }
}

// untuk Path
class Path implements Comparable<Path> {
    Vertex to;
    int numOfEnemy;
    Path(Vertex to, int numOfEnemy) {
        this.to = to;
        this.numOfEnemy = numOfEnemy;
    }

    @Override
    public int compareTo(Path o) {
        return this.numOfEnemy - o.numOfEnemy;
    }
}

// untuk Graph
class Graph{
    static final Integer INFINITY = Integer.MAX_VALUE;
    Map<Integer, Vertex> vertexMap;
    Graph(){
        vertexMap = new HashMap<>();
    }

    Vertex getVertex(int id) {
        Vertex v = vertexMap.get(id);
        if (v == null) {
            v = new Vertex(id);
            vertexMap.put(id, v);
        }
        return v;
    }
    void printPath(Vertex destination) {
        if (destination.prev != null) {
            printPath(destination.prev);
            System.out.print(" <= ");
        }
        System.out.print(destination.id);
    }
    void addEdge(int from, int to, int numOfEnemy) {
        Vertex v = getVertex(from);
        Vertex w = getVertex(to);
        v.adjList.add(new Edge(w, numOfEnemy));
    }
    void clearAll(){
        for (Vertex v : vertexMap.values()) {
            v.reset();
        }
    }

    // find shortest path to the isAttacked vertex
    void dijksrta(int id) {
        PriorityQueue<Path> pq = new PriorityQueue<>();
        
        Vertex source = getVertex(id);
        if (source == null) {
            System.out.println("Tidak ada benteng dengan id " + id);
            return;
        }
        clearAll();
        pq.add(new Path(source, 0));
        source.dist = 0;

        int visited = 0;
        while (!pq.isEmpty() && visited < vertexMap.size()) {
            Path path = pq.remove();
            Vertex v = path.to;
            if (v.scratch != 0) {
                continue;
            }
            v.scratch = 1;
            visited++;
            for (Edge e : v.adjList) {
                Vertex w = e.to;
                int weight = e.numOfEnemy;
                if (v.dist + weight < w.dist) {
                    w.dist = v.dist + weight;
                    w.prev = v;
                    pq.add(new Path(w, w.dist));
                    v.distances[w.id] = w.dist - v.dist;
                }
            }
        }
    }
}