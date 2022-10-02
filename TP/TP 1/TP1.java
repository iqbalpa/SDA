import java.util.*;
import java.io.*;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;

    static Menu[] arrMenu;
    static Koki[] arrKoki;
    static Pelanggan[] arrPelanggan;
    static LinkedList<Pelanggan> sedangMakan = new LinkedList<>();
    static LinkedList<Integer> blackList = new LinkedList<>();
    static Queue<Pesanan> pesanan = new LinkedList<>();
    static Queue<Integer> ruangLapar = new LinkedList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak menu
        int M = in.nextInt();
        arrMenu = new Menu[M+1];
        for (int i = 1; i <= M; i++) {
            int harga = in.nextInt();
            String tipe = in.next();
            Menu newMenu = new Menu(harga, tipe);
            arrMenu[i] = newMenu;
        }
        
        // banyak koki
        int V = in.nextInt();
        arrKoki = new Koki[V+1];
        for (int i = 1; i <= V; i++) {
            String spesialisasi =  in.next();
            Koki newKoki = new Koki(spesialisasi, i);
            arrKoki[i] = newKoki;
        }

        // banyak pelanggan
        int P = in.nextInt();
        // banyak kursi
        int N = in.nextInt();
        // jumlah hari restoran beroperasi
        int Y = in.nextInt();

        // array pelanggan
        arrPelanggan = new Pelanggan[P+1];
        // jumlah pelanggan per hari
        int[] pelangganHariKe = new int[Y];
        // jumlah pelayanan per hari
        int[] pelayananHariKe = new int[Y];

        for (int i = 0; i < Y; i++) {
            pelangganHariKe[i] = in.nextInt();
            int ruangMakan = 0;
            for (int j=0; j<pelangganHariKe[i]; j++){
                int I = in.nextInt();
                char K = in.next().charAt(0);
                int U = in.nextInt();

                if (blackList.contains(I)){
                    out.print(3);
                } else {
                    Pelanggan newPelanggan;
                    if (K == '?'){
                        int R = in.nextInt();
                        // advance scanning
                        int negatif = 0;
                        int positif = 0;
                        for (int k=I-R; k>=I-1; k--){
                            if (arrPelanggan[k].K == '-'){
                                negatif++;
                            } else {
                                positif++;
                            }
                        }
                        K = negatif < positif ? '+' : '-';
                    } 

                    if (K == '+'){
                        out.print(0);
                    } else {
                        newPelanggan = new Pelanggan(I, K, U);
                        arrPelanggan[I] = newPelanggan;
    
                        if (ruangMakan < N){
                            sedangMakan.add(newPelanggan);
                            out.print(1);
                            ruangMakan++;
                        } else {
                            ruangLapar.add(I);
                            out.print(2);
                        }
                    }
                }
                if (j != pelangganHariKe[i]-1){
                    out.print(" ");
                }
            }
            out.println();

            pelayananHariKe[i] = in.nextInt();
            // fungsi pelayanan
            for (int j=0; j<pelayananHariKe[i]; j++){
                int satu;
                int dua;
                int tiga;
                char pelayanan = in.next().charAt(0);
                if (pelayanan == 'P'){
                    satu = in.nextInt();
                    dua = in.nextInt();
                    P(satu, dua);
                } else if (pelayanan == 'L'){
                    L();
                } else if (pelayanan == 'B'){
                    satu = in.nextInt();
                    B(satu);
                } else if (pelayanan == 'C'){
                    satu = in.nextInt();
                    C(satu);
                } else if (pelayanan == 'D'){
                    satu = in.nextInt();
                    dua = in.nextInt();
                    tiga = in.nextInt();
                }
            }
        }

        out.close();
    }

    public static void P(int idPelanggan, int indexMakanan){
        Koki theKoki = arrKoki[1];
        Menu theMenu = arrMenu[indexMakanan];
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        for (int i=2; i<=arrKoki.length-1; i++){
            if (arrKoki[i].spesialisasi.equals(theMenu.tipe)){
                if (!theKoki.spesialisasi.equals(theMenu.tipe)){
                    theKoki = arrKoki[i];
                } else {
                    if (theKoki.jumlahMelayani > arrKoki[i].jumlahMelayani){
                        theKoki = arrKoki[i];
                    } 
                    else if (theKoki.jumlahMelayani == arrKoki[i].jumlahMelayani && i > theKoki.id) {
                        theKoki = arrKoki[theKoki.id];
                    }
                }
            }
        }
        thePelanggan.tagihan += theMenu.harga;

        Pesanan newPesanan = new Pesanan(theMenu, theKoki, thePelanggan);
        pesanan.add(newPesanan);
        out.println(theKoki.id);
    }
    public static void L(){
        for (Pesanan p: pesanan){
            p.koki.jumlahMelayani++;
            out.println(p.pelanggan.I);
            pesanan.poll();
        }
    }
    public static void B(int idPelanggan){
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        if (thePelanggan.U < thePelanggan.tagihan){
            out.println(0);
            blackList.add(thePelanggan.I);
        } else {
            out.println(1);
            thePelanggan.U -= thePelanggan.tagihan;
        }
        sedangMakan.remove(thePelanggan);
        if (ruangLapar.size() != 0){
            int idNewPelanggan = ruangLapar.poll();
            sedangMakan.add(arrPelanggan[idNewPelanggan]);
        }
    }
    public static void C(int Q){
        Arrays.sort(arrKoki, 1, arrKoki.length);
        for (int i=1; i<=Q; i++){
            out.print(arrKoki[i].id);
            if (i != Q){
                out.print(" ");
            }
        }
    }
    public static void D(int A, int G, int S){

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

class Menu {
    int harga;
    String tipe;

    public Menu(int harga, String tipe) {
        this.harga = harga;
        this.tipe = tipe;
    }
}

class Pesanan {
    Menu menu;
    Koki koki;
    Pelanggan pelanggan;

    public Pesanan(Menu menu, Koki koki, Pelanggan pelanggan){
        this.menu = menu;
        this.koki = koki;
        this.pelanggan = pelanggan;
    }
}

class Pelanggan {
    int I; // id pelanggan
    char K; // status kesehatan
    int U; // jumlah uang
    int tagihan;
  
    public Pelanggan(int I, char K, int U) {
        this.I = I;
        this.K = K;
        this.U = U;
        this.tagihan = 0;
    }
}

class Koki implements Comparable<Koki> {
    String spesialisasi;
    int jumlahPelayanan;
    int jumlahMelayani;
    int id;

    public Koki(String spesialisasi, int id){
        this.spesialisasi = spesialisasi;
        this.jumlahPelayanan = 0;
        this.jumlahMelayani = 0;
        this.id = id;
    }

    // hasnt done yet
    public int compareTo(Koki k){
        if (this.jumlahMelayani > k.jumlahMelayani){
            return 1;
        } else if (this.jumlahMelayani < k.jumlahMelayani){
            return -1;
        } else {
            if (this.spesialisasi.equals(k.spesialisasi)){
                return this.id > k.id ? 1 : -1;
            } else if (this.spesialisasi.equals("Airfood")){
                return 1;
            } else if (this.spesialisasi.equals("Groundfood") && k.spesialisasi.equals("Seafood")){
                return 1;
            } else if (this.spesialisasi.equals("Groundfood") && k.spesialisasi.equals("Airfood")){
                return -1;
            } else {
                return -1;
            }
        }
    }
}