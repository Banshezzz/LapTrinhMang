/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_ltm;
//khai bao thu vien can dung
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hongs
 */
public class DoAn_LTM {
    
    static final int Port = 1234;//khai bao port su dung
    private DatagramSocket socket = null;//khai bao datagramsocket de luu ket noi
    private String chuoi = "";//khai bao bien de luu chuoi du lieu
    private int key = 0;//khai bao bien de luu chuoi du lieu
    private static String kytu = ""; //luu tru so lan xuat hien cua cac ky tu trong mang
    
    //ham khoi tao socket
    public DoAn_LTM() {
        try {
            socket = new DatagramSocket(Port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //phan nhan va trao doi khoa giua Client va Server
    //sau khi trao doi xong gưi dư lieu ve lại Cilent
    public void action(){
        InetAddress host = null;
        int port;
        try {
            System.out.println("Server đang chờ kết nối !!!");
            while (true) {                
                DatagramPacket packet = recevice();//nhan khoa tu Client truyen len
                host = packet.getAddress();
                port = packet.getPort();//lay thong tin port cua Client
                chuoi = new String(packet.getData()).trim();//lay du lieu cua may Client
                //tach chuoi kiem tra co la gi
                String[] s_array = chuoi.split("@");//chuyen item kieu string sang mang kieu string
                int check = Integer.parseInt(s_array[0]);
                
                //kiem tra neu co tra ve la 1 thì trao doi khoa
                if ( check == 1) {
                    String khoa = s_array[1] + Khoa();//trao doi khoa vơi server
                    //chuyen khoa kieu string sang int de kiem tra 
                    key = Integer.parseInt(khoa);
                    System.out.println("Khoa sau khi trao doi: "+ key);
                    //neu khoa khác rỗng thi gửi lại client
                    if (!khoa.equals("")) {
                        send(khoa, host, port);
                        key = 0;//reset gia tri cua bien key
                    }
                }  
                //kiem tra neu co tra ve 2 thi giai ma ban ma, dem so lan xuat hien cua tung ky tu
                if (check == 2) {
                    key = Integer.parseInt(s_array[1]);//chuyen gia tri key trong mang thanh int
                    System.out.println("Khoa co 2: "+ key);
                    String data = s_array[2];//lay gia tri trong mang
                    //giai ma ban ma tu client gui len
                    //dem so lan xuat hien cua tung ky tu
                    DemKyTu(Giaima(data));
                    //xuat ra man hinh kiem tra ket qua
                    System.out.println("Van ban sau khi giai ma: "+ Giaima(data));
                    //kiem tra bien giu thong tin so lan xuat hien cua cac chu cai, meu khac rong gui laij client
                    if (!kytu.equals("")) {
                        send(kytu, host, port);
                        kytu = "";//reset lai bien kytu
                    }
                }
                //kiem tra neu co bang 3 thi giai ma ban ma va tim so lan xuat hien cua ky tu gui len
                if (check == 3) {
                    key = Integer.parseInt(s_array[1]);//chuyen gia tri key trong mang thanh int
                    System.out.println("Khoa co 3: "+ key);
                    String kt = s_array[2];//ky tu nhan tu client
                    kt = kt.toUpperCase();
                    System.out.println("Ky tu: "+ kt);
                    String data = s_array[3];//lay gia tri trong mang
                    //giai ma ban ma tu client gui len
                    //dem so lan xuat hien cua tung ky tu
                    DemKT(Giaima(data),kt);
                    //xuat ra man hinh kiem tra ket qua
                    System.out.println("Van ban sau khi giai ma: "+ Giaima(data));
                    //kiem tra bien giu thong tin so lan xuat hien cua cac chu cai, meu khac rong gui laij client
                    if (!kytu.equals("")) {
                        send(kytu, host, port);
                        kytu = "";//reset lai bien kytu
                    }
                }
                
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            socket.close();
        }
    }
    
    //ham tra du lieu ve cho Client
    public void send(String chuoiString, InetAddress hostAddress, int port) throws IOException{
        byte[] buffer = chuoiString.getBytes();//chuyen du lieu thanh dang byte
        //sau do dua chuoi truyen vao goi tin
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, hostAddress, port);
        socket.send(packet);
    }
    //ham nhan du lieu tu client gưi len
    private DatagramPacket recevice() throws IOException{
        byte[] buffer = new byte[65507]; //khai bao mang byte nhan
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        return packet;
    }
     //ham sinh mot so ngau nhien de trao doi khoa
    private String Khoa(){
        //khoi tao ham random
        Random rand = new Random();
        //ran dom mot so tu nhien
        int ranNum = rand.nextInt(99);
        
        return String.valueOf(ranNum);//tra ve mot kieu string
    }
    
    //phan nhan khoa va ban ma tu client
    //giai ma ban ma va kiem tra xem có bao nhieu ky tu xuat hien
     //ham giai ma thuat toan caser
    char mahoakt(char c,int k){
        if(!Character.isLetter(c)) return c;
        return (char) ((((Character.toUpperCase(c) - 'A') + k) % 26 + 26) % 26 + 'A');
    }
    private String mahoa(String br,int k){
        String kq="";
        int n=br.length();
        for(int i=0;i<n;i++)
            kq+=mahoakt(br.charAt(i),k);
        return kq;
    }
    
    //ham giai ma ban ma client gui len
     private String Giaima( String c){
        //chuyen doi khoa kieu string sang int
        //tra ve chuoi sau khi ma hoa
        return mahoa(c,-key);
    }
      
    //ham dem so lan xuat hien cua cac ky tu trong mang
    static void DemKyTu(String str)
    {
        //khoi tao mang chu cai tieng anh
        char[] MangKT = {'A','B','C','D','E','F','G',
                        'H','I','J','K','L','M','N','O','P',
                        'Q','R','S','T','U','V','X','Y','Z'};
        str = str.replaceAll(" ", "");//xoa tat ca khoang trong chuoi du lieu tu client gui len
        char[] charArray = str.toCharArray();//chuyen chuoi thanh mang char 
        int lenchar = MangKT.length;//do dai mang ky tu ban dau
        int lenchuoi = charArray.length;//do dai mang du liu tu client
        
        for (int i = 0; i < lenchar; i++) {//vong lap mang ky tu ban dau
            int d =0;//khoi tao bien dem bang 0
            for (int j = 0; j < lenchuoi; j++) {//vong lap mang du lieu client
                //kiem tra ky tu trong mang du lieu client gui len
                if (MangKT[i] == charArray[j]) {
                    d++;//neu ton tai tang gia tri bien dem len 1 don vi
                }
            }
            if (d >0){
                 kytu = kytu + MangKT[i]+ "-" + d + "@" ;//luu lai gia tri dem duoc vao chuoi
                 System.out.println(MangKT[i]+" "+ d);//xuat ra man hinh kiem tra
            } 
        }
    }
    //ham diem ký tu do client gui len
    static void DemKT(String str, String kt){
        char chu = kt.charAt(0);
         System.out.println("Ky tu: "+chu);//xuat ra man hinh kiem tra
        int dem =0;//khoi tao bien dem
        str = str.replaceAll(" ", "");//xoa tat ca khoang trang trong mang
        char[] charArray = str.toCharArray();//chuyen chuoi thanh mang char
        int lenchuoi = charArray.length;//do dai mang du liu tu client
        //vong lap dem ky tu
        for (int i = 0; i < lenchuoi; i++) {
            if (chu == charArray[i]) {
                dem++;
            }
        }
        kytu =kytu + dem;//tra ve so lan dem duoc
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new DoAn_LTM().action();
    }
    
}
