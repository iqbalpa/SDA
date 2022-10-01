import java.util.*;

public class TP1 {
    static Menu[] arrMenu;
    static Koki[] arrKoki;
    // static List<Koki> arrKoki = new ArrayList<>();
    static Pelanggan[] arrPelanggan;
    // static LinkedList<Integer> sedangMakan = new LinkedList<>();
    static LinkedList<Pelanggan> sedangMakan = new LinkedList<>();
    static LinkedList<Integer> blackList = new LinkedList<>();
    static Queue<Pesanan> pesanan = new LinkedList<>();
    static Queue<Integer> ruangLapar = new LinkedList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // sedangMakan.add(-1);
        // blackList.add(-1);

        // banyak menu
        int M = input.nextInt();
        arrMenu = new Menu[M+1];
        for (int i = 1; i <= M; i++) {
            int harga = input.nextInt();
            String tipe = input.next();
            Menu newMenu = new Menu(harga, tipe);
            arrMenu[i] = newMenu;
        }
        
        // banyak koki
        int V = input.nextInt();
        arrKoki = new Koki[V+1];
        for (int i = 1; i <= V; i++) {
            String spesialisasi =  input.next();
            Koki newKoki = new Koki(spesialisasi, i);
            arrKoki[i] = newKoki;
            // arrKoki.add(newKoki);
        }

        // banyak pelanggan
        int P = input.nextInt();
        // banyak kursi
        int N = input.nextInt();
        // jumlah hari restoran beroperasi
        int Y = input.nextInt();

        // array pelanggan
        arrPelanggan = new Pelanggan[P+1];
        // jumlah pelanggan per hari
        int[] pelangganHariKe = new int[Y];
        // jumlah pelayanan per hari
        int[] pelayananHariKe = new int[Y];

        for (int i = 0; i < Y; i++) {
            pelangganHariKe[i] = input.nextInt();
            for (int j=0; j<pelangganHariKe[i]; j++){
                int I = input.nextInt();
                char K = input.next().charAt(0);
                int U = input.nextInt();

                if (blackList.contains(I)){
                    // System.out.print("===" + 3 + " blacklist");
                    System.out.print(3);
                } else {
                    Pelanggan newPelanggan;
                    if (K == '?'){
                        int R = input.nextInt();
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
                        // System.out.println("===" + 0 + " positif");
                        System.out.print(0);
                    } else {
                        newPelanggan = new Pelanggan(I, K, U);
                        arrPelanggan[I] = newPelanggan;
    
                        if (j <= N){
                            // sedangMakan.add(I);
                            sedangMakan.add(newPelanggan);
                            System.out.print(1);
                            // System.out.println("===" + 1 + " tidak masalah");
                        } else {
                            ruangLapar.add(I);
                            System.out.print(2);
                            // System.out.println("===" + 2 + " masuk ruang lapar");
                        }
                    }
                }
                System.out.print(" ");
            }
            System.out.println();

            pelayananHariKe[i] = input.nextInt();
            // fungsi pelayanan
            for (int j=0; j<pelayananHariKe[i]; j++){
                int satu;
                int dua;
                int tiga;
                char pelayanan = input.next().charAt(0);
                if (pelayanan == 'P'){
                    satu = input.nextInt();
                    dua = input.nextInt();
                    P(satu, dua);
                } else if (pelayanan == 'L'){
                    L();
                } else if (pelayanan == 'B'){
                    satu = input.nextInt();
                    B(satu);
                } else if (pelayanan == 'C'){
                    satu = input.nextInt();
                    C(satu);
                } else if (pelayanan == 'D'){
                    satu = input.nextInt();
                    dua = input.nextInt();
                    tiga = input.nextInt();
                }
            }
        }

        input.close();
    }

    public static void P(int idPelanggan, int indexMakanan){
        Koki theKoki = arrKoki[1];
        // Koki theKoki = arrKoki.get(0);
        Menu theMenu = arrMenu[indexMakanan];
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        // int indexKoki = 1;
        for (int i=2; i<=arrKoki.length-1; i++){
            // System.out.printf("i: %d || id: %d || spesialisasi: %s%n", i, arrKoki[i].id, arrKoki[i].spesialisasi);
            if (arrKoki[i].spesialisasi.equals(theMenu.tipe)){
                if (!theKoki.spesialisasi.equals(theMenu.tipe)){
                    // System.out.println(i + "satu");
                    theKoki = arrKoki[i];
                    // indexKoki = i;
                } else {
                    if (theKoki.jumlahMelayani > arrKoki[i].jumlahMelayani){
                    // if (theKoki.jumlahPelayanan > arrKoki[i].jumlahPelayanan){
                        // System.out.println(i + "dua");
                        theKoki = arrKoki[i];
                        // indexKoki = i;
                    } 
                    else if (theKoki.jumlahMelayani == arrKoki[i].jumlahMelayani && i > theKoki.id) {
                    // else if (theKoki.jumlahPelayanan == arrKoki[i].jumlahPelayanan && i > theKoki.id){
                        // System.out.println(i + "tiga"); 
                        theKoki = arrKoki[theKoki.id];
                    }
                }
            }
        }
        thePelanggan.tagihan += theMenu.harga;

        Pesanan newPesanan = new Pesanan(theMenu, theKoki, thePelanggan);
        pesanan.add(newPesanan);
        // theKoki.jumlahMelayani++;
        // System.out.println("===" + theKoki.id + " index koki");
        System.out.println(theKoki.id);
    }
    public static void L(){
        for (Pesanan p: pesanan){
            // p.koki.jumlahMelayani--;
            // p.koki.jumlahPelayanan++;
            p.koki.jumlahMelayani++;
            // System.out.println("==="+ p.pelanggan.I + " id pemesan");
            System.out.println(p.pelanggan.I);
            // pesanan.remove();
            pesanan.poll();
        }
    }
    public static void B(int idPelanggan){
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        if (thePelanggan.U < thePelanggan.tagihan){
            // System.out.println("===" + 0 + " uang tidak cukup");
            System.out.println(0);
            blackList.add(thePelanggan.I);
        } else {
            // System.out.println("===" + 1 + " uang cukup");
            System.out.println(1);
            thePelanggan.U -= thePelanggan.tagihan;
        }
        // System.out.println("size sedang makan " + sedangMakan.size());
        // System.out.println("size ruang lapar " + ruangLapar.size());
        sedangMakan.remove(thePelanggan);
        if (ruangLapar.size() != 0){
            int idNewPelanggan = ruangLapar.remove();
            sedangMakan.add(arrPelanggan[idNewPelanggan]);
        }
    }
    public static void C(int Q){
        Arrays.sort(arrKoki, 1, arrKoki.length-1);
        for (int i=1; i<=Q; i++){
            System.out.print(arrKoki[i].id + " ");
        }
    }
    public static void D(int A, int G, int S){}
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
                return 1;
            }
        }
    }
}