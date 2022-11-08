import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Bismillah {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // TODO: get the input from user

        // banyak mesin pada FunZone
        int N = in.nextInt();
        for (int i=1; i<=N; i++){
            MesinPermainan newMesin = new MesinPermainan(i, N+1);

            // banyak skor awal pada mesin ke-i
            int Mi = in.nextInt();
            for (int j=1; j<=Mi; j++){
                int Zj = in.nextInt();
                newMesin.daftarSkor[j] = Zj;
            }
        }

        // banyak query
        int Q = in.nextInt();
        for (int i=1; i<=Q; i++){
            String query = in.next();

            // cek jenis query
            if (query.equals("MAIN")){
                // skor Budi
                int Y = in.nextInt();
                mainmain(Y);
            }
            else if (query.equals("GERAK")){
                // arah gerak Budi
                String arahGerak = in.next();
                gerak(arahGerak);
            }
            else if (query.equals("HAPUS")) {
                // banyak skor yg mau dihapus
                int X = in.nextInt();
                hapus(X);
            }
            else if (query.equals("LIHAT")) {
                // batas bawah skor
                int L = in.nextInt();
                // batas atas skor
                int H = in.nextInt();
                lihat(L, H);
            }
        }
        
        out.close();
    }

    // method untuk handle masing-masing query
    // query MAIN
    static void mainmain(int skorBudi){}

    // query GERAK
    static void gerak(String arahGerak){}

    // query HAPUS
    static void hapus(int banyakSkor){}

    // query LIHAT
    static void lihat(int batasBawah, int batasAtas){}

    
    // referensi: template Lab SDA biasanya
    // untuk input output
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

// class untuk mesin permainan
class MesinPermainan {
    int id;
    Integer[] daftarSkor;
    MesinPermainan(int id, int banyakSkor){
        this.id = id;
        daftarSkor = new Integer[banyakSkor];
        daftarSkor[0] = -999; //unused
    }
}