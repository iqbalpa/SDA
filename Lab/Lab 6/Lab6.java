import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

public class Lab6 {
    private static InputReader in;
    static PrintWriter out;
    // static BinaryHeap binaryheap;
    static MyHeap myheap;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak saham
        int N = in.nextInt();
        // binaryheap = new BinaryHeap(N);
        myheap = new MyHeap(N);
        for (int i=1; i<=N; i++) {
            int Ci = in.nextInt();
            Saham newSaham = new Saham(i, Ci);
            myheap.insert(newSaham);
        }
        // banyak query
        int Q = in.nextInt();
        for (int i=0; i<Q; i++){
            String query = in.next();
            if (query.equals("TAMBAH")){
                int C = in.nextInt();
                tambah(C);
            }
            else if (query.equals("UBAH")){
                int X = in.nextInt();
                int C = in.nextInt();
                ubah(X, C);
            }
        }

        out.close();
    }

    static void tambah(int C){
        Saham newSaham = new Saham(myheap.getSize()+1, C);
        myheap.insert(newSaham);
    }
    static void ubah(int X, int C){
        myheap.change(X, C);
    }

    // taken from https://codeforces.com/submissions/Petr
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


class Saham {
    int seri, harga;
    Saham (int seri, int harga){
        this.seri = seri;
        this.harga = harga;
    }
}
// PPT
class MyHeap {
    Vector<Saham> data;
    MyHeap(int N){
        data = new Vector<>(N);
    }
    int parentOf(int i){
        return (i-1)/2;
    }
    int leftChildOf(int i){
        return 2*i+1;
    }
    int rightChildOf(int i){
        return 2*(i+1);
    }
    Saham getMin(){
        return data.get(0);
    }
    void percolateDown(int root){
        int heapSize = data.size();
        Saham value = data.get(root);
        while (root < heapSize){
            int childPos = leftChildOf(root);
            if (childPos < heapSize){
                if ((rightChildOf(root) < heapSize) && (data.get(childPos).harga > data.get(childPos+1).harga)){
                    childPos++;
                }
                if (value.harga > data.get(childPos).harga){
                    data.set(root, data.get(childPos));
                    root = childPos;
                }
                else {
                    data.set(root, value);
                    return;
                }
            }
            else {
                data.set(root, value);
                return;
            }
        }
    }
    void percolateUp(int leaf){
        int parent = parentOf(leaf);
        Saham value = data.get(leaf);
        while (leaf > 0 && value.harga < data.get(parent).harga){
            data.set(leaf, data.get(parent));
            leaf = parent;
            parent = parentOf(leaf);
        }
        data.set(leaf, value);
    }
    boolean isKosong() {
        return data.size() == 0;
    }
    int getSize(){
        return data.size();
    }
    void clear(){
        data.clear();
    }
    public String toString(){
        return data.toString();
    }
    // create heapify method using percolateDown and percolateUp
    void heapify(){
        int heapSize = data.size();
        for (int i=parentOf(heapSize-1); i>=0; i--){
            percolateDown(i);
        }
    }
    void insert(Saham value){
        data.add(value);
        percolateUp(data.size()-1);
    }
    Saham removeMin(){
        Saham min = data.get(0);
        data.set(0, data.get(data.size()-1));
        data.remove(data.size()-1);
        percolateDown(0);
        return min;
    }
    void remove(int i){
        data.set(i, data.get(data.size()-1));
        data.remove(data.size()-1);
        percolateDown(i);
    }
    void change(int X, int C){
        Saham theSaham = getSaham(X);
        theSaham.harga = C;
        percolateDown(X-1);
        percolateUp(X-1);
    }
    Saham getSaham(int seri){
        for (int i=0; i<data.size(); i++){
            if (data.get(i).seri == seri){
                return data.get(i);
            }
        }
        return null; 
    }
    void print(){
        for (int i=0; i<data.size(); i++){
            System.out.print(data.get(i).harga + " ");
        }
        System.out.println();
    }
}
// GFG
// class BinaryHeap {
//     Saham[] heap;
//     int size;
//     int maxsize;
//     BinaryHeap(int maxsize){
//         this.maxsize = maxsize;
//         this.size = 0;
//         heap = new Saham[this.maxsize + 1];
//         heap[0] = new Saham(-999, -999);
//     }
//     int parent(int i){
//         return i/2;
//     }
//     int leftChild(int i){
//         return 2*i;
//     }
//     int rightChild(int i){
//         return 2*i+1;
//     }
//     boolean isLeaf(int i){
//         if (i > (this.size/2)){
//             return true;
//         }
//         return false;
//     }
//     void swap(int i, int j){
//         Saham temp = heap[i];
//         heap[i] = heap[j];
//         heap[j] = temp;
//     }
//     void insert(Saham saham){
//         heap[++size] = saham;
//         int current = size;
//         while (heap[current].harga < heap[parent(current)].harga){
//             swap(current, parent(current));
//             current = parent(current);
//         }
//     }
// }