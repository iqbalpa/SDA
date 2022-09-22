import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

// collaborators and ideas by 
// 1. Aushaaf Fadhilah A
// 2. Laela Putri Salsa Biella
// 3. Daffa Ilham R

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    
    // map untuk memoization
    static Map<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)
        int max = 0;
        int red = 0;
        int blue = 0;
        int redVotes = 0;
        
        // base case
        // return 1 jika R dan 0 jika B
        if (start == end){
            return A[start] == 'R' ? 1 : 0;
        } 
        // jika index start dan end udah crossing, return 0
        else if (start > end) {
            return 0;
        } 
        // jika di map udah ada, maka akan langsung return
        else if (map.containsKey(start)){
            return map.get(start);
        }

        for (int i=start; i<=end; i++){

            // jika vote adalah R
            if (A[i] == 'R'){
                red++;
            } 
            // jika vote adalah B
            else {
                blue++;
            }

            // jika jumlah votes red > votes blue
            if (red > blue){
                redVotes = red + blue;
            } 
            // jika jumlah votes red < votes blue
            else {
                redVotes = 0;
            }

            // cari max dengan recursive
            max = Math.max(max, redVotes + getMaxRedVotes(i+1, end));
        }

        // masukkan value max ke map dengan key start
        map.put(start, max);
        return max;
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}