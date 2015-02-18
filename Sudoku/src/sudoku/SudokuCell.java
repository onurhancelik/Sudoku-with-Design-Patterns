package sudoku;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author USER
 */
public class SudokuCell implements Serializable,CellInterface{ //Gerek duyulan arayüz sınıflarını implemente ettik.
    
    private int value; //Bir hücrenin gerçek değerini tutar.
    private boolean filled; //Bir hücrenin gerçek değeri varsa bu değişken True'dur.
    private HashSet<Integer> tried; //Hücreye daha önce denenen sayıları tutar.

    public SudokuCell() { //Yapıcı fonksiyon, nesne oluşturumunu ve ilklemeleri yaptık.
        filled = false;
        tried = new HashSet();
    }

    @Override //Arayüzden alınan metotun gerçekleştirimi.
    public boolean isFilled() {
        return filled;
    }
    
    //Hücre verisini value değerini döndürür.
    public int get() {
        return value;
    }

    //Hücreye değer atanır ve aynı zamanda ilgili diğer üyelerde de işlemler yapılır.
    public void set(final int number) {
        filled = true; //Hücreye gerçek değeri atandığı için filled True oldu.
        value = number; 
        tried.add(number);
    }

    @Override //Arayüzden alınan clear metodunun gerçekleştirimi. Hücrenin değerini(value) ve doluluğunu(filled) temizler.
    public void clear() {
        value = 0;
        filled = false;
    }

    @Override //Arayüzden alınan reset metotunun gerçekleştirimi. 
    public void reset() { //clear() metoduna artı olarak daha önce denenen sayıların tutulduğu tried kümesini de sıfırlar.
        clear();
        tried.clear();
    }

    //Verilen sayının daha önce denenip denenmediğini döndüren fonksiyon.
    public boolean isTried(final int number) {
        return tried.contains(number);
    }

    //Denenen sayıyı tried kümesine atan fonksiyon.
    public void tryNumber(final int number) {
        tried.add(number);
    }
    
    //Daha önce toplam kaç sayının denendiğini döndüren fonksiyon.
    public int numberOfTried() {
        return tried.size();
    }
}
