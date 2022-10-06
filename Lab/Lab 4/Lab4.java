import java.io.*;
import java.util.StringTokenizer;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;

    static Lab4Character denji = new Lab4Character();
    static Lab4Character iblis = new Lab4Character();
    static LinkedList<Gedung> listGedung = new LinkedList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int B = in.nextInt();
        for (int i=0; i<B; i++){
            String Ai = in.next();      // nama gedung
            int Fi = in.nextInt();      // jumlah lantai gedung
            Gedung newGedung = new Gedung(Ai, Fi);
            listGedung.add(newGedung);
        }

        String AD = in.next();          // nama gedung yang tempat pertama denji
        int FD = in.nextInt();          // lantai tempat pertama denji
        denji.diGedung = AD;
        denji.diLantai = FD;
        denji.isTurun = false;

        String AS = in.next();          // nama gedung yang tempat pertama iblis
        int FS = in.nextInt();          // lantai tempat pertama iblis
        iblis.diGedung = AS;
        iblis.diLantai = FS;
        iblis.isTurun = true;

        int Q = in.nextInt();           // banyak query
        for (int i=0; i<Q; i++){
            String query = in.next();
            if (query.equals("GERAK")){
                gerak();
            } else if (query.equals("HANCUR")){
                hancur();
            } else if (query.equals("TAMBAH")){
                tambah();
            } else if (query.equals("PINDAH")){
                pindah();
            }
        }   

        // Tutup OutputStream
        out.close();
    }

    public static void gerak(){}
    public static void hancur(){}
    public static void tambah(){}
    public static void pindah(){}

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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}

class Gedung {
    String nama;
    int jumlahLantai;
    boolean isIblisHere;
    boolean isDenjiHere;
    Gedung next;
    public Gedung(String nama, int jumlahLantai) {
        this.nama = nama;
        this.jumlahLantai = jumlahLantai;
        this.isDenjiHere = false;
        this.isIblisHere = false;
        this.next = null;
    }
}
class Lab4Character {
    int diLantai;
    String diGedung;
    boolean isTurun;
}
class ListGedung {
    Gedung first;
    Gedung last;

    public ListGedung() {
        this.first = null;
        this.last = null;
    }
    public void add(Gedung newGedung){
        if (first == null){
            first = newGedung;
            last = newGedung;
        } else {
            last.next = newGedung;
            last = newGedung;
        }
    }
}