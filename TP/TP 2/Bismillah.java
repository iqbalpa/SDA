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
            MesinPermainan newMesin = new MesinPermainan(i);

            // banyak skor awal pada mesin ke-i
            int Mi = in.nextInt();
            for (int j=1; j<=Mi; j++){
                int Zj = in.nextInt();
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
            else if (query.equals("EVALUASI")){
                evaluasi();
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

    // query EVALUASI
    static void evaluasi(){}

    
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

// class untuk node skor di dalam mesin permainan
class Skor {
    int skor, jumlahSkor;
    int height, count;
    Skor left, right;
    Skor (int skor) {
        this.skor = skor;
        this.jumlahSkor = 1;
        this.left = null;
        this.right = null;
        this.height = 1;
        this.count = 1;
    }
}

// class untuk mesin permainan
// mesin permainan sebagai AVL tree
class MesinPermainan {
    int id;
    Skor root;
    int currentCount;
    MesinPermainan next, prev;
    MesinPermainan(int id){
        this.id = id; 
    }

    // method-method untuk operasi pada AVL tree    
    int getHeight(Skor node){
        if (node == null) return 0;
        return node.height;
    }
    int getBalance(Skor node){
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    int getCount(Skor node){
        if (node == null) return 0;
        return node.count + (node.jumlahSkor - 1);
    }
    Skor rightRotate(Skor y) {
        Skor x = y.left;
        Skor z = x.right;
        x.right = y;
        y.left = z;
        // update height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        // update size
        y.count = getCount(y) + getCount(z);
        y.count = getCount(y) - getCount(x) + getCount(z);
        x.count = getCount(x.left) + getCount(y);
        x.count = getCount(x) - getCount(z) + getCount(y);
        return x;
    }
    Skor leftRotate(Skor x) {
        // TODO: implement left rotate
        Skor y = x.right;
        Skor z = y.left;
        y.left = x;
        x.right = z;
        // update height
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        // update size
        x.count = getCount(x) + getCount(z);
        x.count = getCount(x) - getCount(y) + getCount(z);
        y.count = getCount(y.right) + getCount(x);
        y.count = getCount(y) - getCount(z) + getCount(x);
        return y;
    }
    // !ganti metode buat menghitung jumlah child
    Skor insertSkor(Skor node, int newSkor){
        if (node == null){
            return new Skor(newSkor);
        }
        if (node.skor == newSkor) {
            if (node.left != null) {
                currentCount += getCount(node.left);
            }
            node.jumlahSkor++;
            return node;
        }
        if (node.skor > newSkor) {
            node.count++;
            node.left = insertSkor(node.left, newSkor);
        }
        else {
            if (node.left != null){
                currentCount += getCount(node.left);
            }
            currentCount += node.jumlahSkor;
            node.count++;
            node.right = insertSkor(node.right, newSkor);
        }

        // balancing
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balanceFactor = getBalance(node);
        if (balanceFactor > 1) {
            if (newSkor < node.left.skor) {
                return rightRotate(node);
            } else if (newSkor > node.left.skor) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balanceFactor < -1) {
            if (newSkor > node.right.skor) {
                return leftRotate(node);
            } else if (newSkor < node.right.skor){
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }
    Skor lowerBound(Skor node, int batasBawah){
        if (node == null) return null;
        if (node.skor == batasBawah) return node;
        if (node.skor < batasBawah) return lowerBound(node.right, batasBawah);
        Skor temp = lowerBound(node.left, batasBawah);
        return (temp.skor >= batasBawah) ? temp : node;
    }
    Skor upperBound(Skor node, int batasAtas){
        if (node == null) return null;
        if (node.skor == batasAtas) return node;
        if (node.skor > batasAtas) return upperBound(node.left, batasAtas);
        Skor temp = upperBound(node.right, batasAtas);
        return (temp.skor <= batasAtas) ? temp : node;
    }
}

// FunZone sebagai kumpulan MesinPermainan as LinkedList class
class FunZone {
    MesinPermainan first, last, current;
    FunZone(){
        this.first = null;
        this.last = null;
        this.current = null; // buat posisi Budi
    }
    void addMesinPermainan(MesinPermainan newMesin){
        if (first == null){
            first = newMesin;
            last = first;
        } else {
            newMesin.prev = last;
            last = newMesin;
            last.next = first;
            first.prev = last;
        }
    }
}
