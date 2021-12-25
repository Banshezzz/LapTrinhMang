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
                
                //kiem tra xem du lieu tu Client phai la khoa hay khong
                if ( Check(chuoi)== true) {
                    chuoi = chuoi + Khoa();//trao doi khoa vơi server
                    key = Integer.parseInt(chuoi);
                    System.out.println("Khoa sau khi trao doi: "+ chuoi);
                    if (!chuoi.equals("")) {
                        send(chuoi, host, port);
                        
                    }
                } else { 
                    Giaima(chuoi); //giai ma ban ma tu client gui len
                    DemKyTu(Giaima(chuoi));
                    System.out.println("Van ban sau khi giai ma: "+ Giaima(chuoi));
                    if (!kytu.equals("")) {
                        send(kytu, host, port);
                        kytu = "";//reset lai biến kytu ve null
                        key = 0;//reset lai biến key ve 0
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
     
     //ham kiem tra xem co phai so kieu so nguyen khong khong
    private boolean Check(String key){
        boolean isCheck = true; //khoi tao co
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(key);
        if (!m.find()) {
            isCheck = false;
        }
        
        return isCheck;
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new DoAn_LTM().action();
    }
    
}
