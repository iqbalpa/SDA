import java.io.*;
import java.util.*;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak pos
        int N = in.nextInt(); 
        for (int i=1; i<=N; i++){
            Vertex newVertex = new Vertex(i);
        }
        // banyak terowongan
        int M = in.nextInt();
        for (int i=0; i<M; i++){
            int Ai = in.nextInt();
            int Bi = in.nextInt();
            int Li = in.nextInt();
            int Si = in.nextInt();
        }
        // banyak kurcaci
        int P = in.nextInt();
        for (int i=0; i<P; i++){
            int Ri = in.nextInt();
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
}

// Edge as Terowongan
class Edge {
    Vertex dest;
    int size; //ukuran terowongan
    int length; //panjang terowongan
    public Edge(Vertex d, int s, int l){
        dest = d;
        size = s;
        length = l;
    }
}
// Vertex as Pos
class Vertex {
    int id;
    List<Edge> adj;
    int dist;
    Vertex prev;
    int scratch;
    public Vertex(int i){
        id = i;
        adj = new LinkedList<Edge>();
        reset();
    }
    public void reset(){
        dist = Graph.INFINITY;
        prev = null;
        scratch = 0;
    }
}
// Path
class Path implements Comparable<Path>{
    int size;
    int length;
    public Path(int s, int l){
        size = s;
        length = l;
    }
    public int compareTo(Path rhs){
        if (size != rhs.size) return size - rhs.size;
        else return length - rhs.length;
    }
}
// Graph
class Graph {
    static final int INFINITY = Integer.MAX_VALUE;
    Map<Integer, Vertex> vertexMap = new HashMap<>();

    Vertex getVertex(int id){
        Vertex v = vertexMap.get(id);
        if (v == null){
            v = new Vertex(id);
            vertexMap.put(id, v);
        }
        return v;
    }
    void clearAll(){
        for (Vertex v : vertexMap.values()){
            v.reset();
        }
    }
    void addEdge(int sourceId, int destId, int size, int length){
        Vertex v = getVertex(sourceId);
        Vertex w = getVertex(destId);
        v.adj.add(new Edge(w, size, length));
    }
    void dijkstra(int startId){
        Queue<Path> pq = new PriorityQueue<>();
        Vertex start = vertexMap.get(startId);
        pq.add(new Path(0, 0));
        start.dist = 0;
        int nodesSeen = 0;
        while (!pq.isEmpty() && nodesSeen < vertexMap.size()){
            Path vrec = pq.remove();
            Vertex v = vertexMap.get(vrec.size);
            if (v.scratch != 0) continue;
            v.scratch = 1;
            nodesSeen++;
            for (Edge e : v.adj){
                Vertex w = e.dest;
                int cvw = e.size;
                if (cvw < 0) continue;
                if (w.dist > v.dist + cvw){
                    w.dist = v.dist + cvw;
                    w.prev = v;
                    pq.add(new Path(w.dist, w.id));
                }
            }
        }
    }
}