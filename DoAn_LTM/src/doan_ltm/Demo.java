/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_ltm;

/**
 *
 * @author hongs
 */
public class Demo {
    
  
    public static void main(String[] args) {
        String kytu = "";
        char[] kyty = {'A','B','C','D','E','F','G',
                        'H','I','J','K','L','M','N','O','P',
                        'Q','R','S','T','U','V','X','Y','Z'};
        
        String str = "ANH YEU OI OI";
        str = str.replaceAll(" ", "");//xoa tat ca khoang trong
        char[] charArray = str.toCharArray();
        int len = kyty.length;
        int len2 = charArray.length;
        for (int i = 0; i < len; i++) {
            int d =0;
            for (int j = 0; j < len2; j++) {
                if (kyty[i] == charArray[j]) {
                    d++;
                }
            }
            if (d >0){ 
                 System.out.println(kyty[i]+" "+ d);
            } 
        }
      
    }
}
