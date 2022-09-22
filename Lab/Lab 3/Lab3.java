import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    
    static Map<Integer, Integer> map = new HashMap<>();
    public static int[] list = new int[1010];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // inisiasi list
        for (int i=0; i<1010; i++) {
            list[i] = -99;
        }

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

        if (start == end){
            return A[start] == 'R' ? 1 : 0;
        } else if (start > end) {
            return 0;
        } else if (list[start] != -99) {
            return list[start];
        }

        // int temp = 0;
        for (int i=start; i<=end; i++){
            if (A[i] == 'R'){
                red++;
            } else {
                blue++;
            }

            if (red > blue){
                redVotes = red + blue;
            } else {
                redVotes = 0;
            }

            // if (map.containsKey(start)){
            //     temp = map.get(start);
            // } else {
            //     temp = redVotes + getMaxRedVotes(i+1, end);
            //     map.put(start, temp);
            // }
            max = Math.max(max, redVotes + getMaxRedVotes(i+1, end));
        }
        list[start] = max;
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