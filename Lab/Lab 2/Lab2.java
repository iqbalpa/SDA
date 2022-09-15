import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;
    static Deque<Stack<Integer>> conveyorBelt = new LinkedList<>();
    static int JUMLAH_TOPLES = 0;

    static int geserKanan() {
        // TODO : Implementasi fitur geser kanan conveyor belt
        Stack<Integer> kanan = conveyorBelt.removeFirst();
        conveyorBelt.addLast(kanan);
        if (kanan.empty()) {
            return -1;
        } else {
            return kanan.peek();
        }
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
        int counter = 0;
        if (conveyorBelt.getLast().peek() == rasa) {
            conveyorBelt.getLast().pop();
            return 0;
        }
        counter++;
        Stack<Integer> kiri = conveyorBelt.removeLast();
        conveyorBelt.addFirst(kiri);

        while (conveyorBelt.getLast().peek() != rasa && counter != JUMLAH_TOPLES-1) {
            counter++;
            kiri = conveyorBelt.removeLast();
            conveyorBelt.addFirst(kiri);
        }

        if (counter == JUMLAH_TOPLES-1) {
            return -1;
        } else if (conveyorBelt.getLast().size() == 1) {
            conveyorBelt.getLast().pop();
            return -1;
        }

        conveyorBelt.getLast().pop();
        return counter;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();
        JUMLAH_TOPLES = N;

        for (int i = 0; i < N; ++i) {

            // TODO: Inisiasi toples ke-i
            Stack<Integer> toples = new Stack<>();

            for (int j = 0; j < X; j++) {

                int rasaKeJ = in.nextInt();

                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                toples.push(rasaKeJ);
            }
            conveyorBelt.add(toples);
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
        out.close();
    }
    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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