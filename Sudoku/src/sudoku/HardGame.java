package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author USER
 */
public class HardGame implements GameStrategy { //Bu class GameStrategy interface'inden implemente edilmektedir.

    private int[][] game; //Oyunu tutmak icin matrisin taninlanmasi

    public HardGame(int[][] game) { //Yapici method
        this.game = game;
    }

    @Override
    public int[][] getGame() { //Olusturulan oyunu donduren method
        return game;
    }

    //Oyun üretmek icin kullanilacak olan method
    @Override
    public void generateGame() {
        List<Integer> positions = new ArrayList(); //9x9 dan 81 farklı konumu tutacak olan arraylist 

        for (int i = 0; i < 80; i++) {
            positions.add(i); //Sira ile 81 farklı konum arraylist e ekleniyor
        }

        Collections.shuffle(positions); //Sirali olan 81 konumun yerleri birbirleri ile rastgele olarak degistiriliyor
        generateGame(positions); //Yerleri degistirilmis olan arraylist generateGame methoduna gonderiliyor
    }

    private void generateGame(List<Integer> positions) { //Sırali olmayan sekilde 81 farkli konumu tutan arraylisti parametre olarak alan ve bu pozisyonlara
        // kullaniciya verilecek olan sayilari belirleyen method
        while (positions.size() > 0) { //Arraylistteki butun elemanlar kullanilip, arraylistte eleman kalmayincaya kadar kullanilan method
            int position = positions.remove(0); //İlk eleman aliniyor

             //9 tane kare oldugu icin konum belirlemesi yapiliyor 
            int x = position / 9;
            int y = position % 9;

            int temp = game[x][y]; //Belirlenen konumdaki deger gecici bir degiskene atiliyor
            game[x][y] = 0; //Belirlenen konumdaki deger sifirlaniyor yani cozumden 1 hucrelik deger atiliyor
            if (!isValid()) { //Atilan degerden sonra oyun cozulebilecek mi diye bakilsin diye isValid method u kullaniliyor
                game[x][y] = temp; //Cozulemeyecek durumda ise sifirlanan son konumdaki deger aynı yere geri atiliyor
            }
        }
    }

    private boolean isValid() { //Oyunun verilen degerlerle cozulup cozulemeyecegine karar veren method
        return isValid(0, new int[]{0});
    }

    private boolean isValid(int index, int[] numberOfSolutions) { //O anki oyun durumuna göre oyunun kaç farklı şekilde
                                                                  //çözülebileceğini bulup 1 ise true değilse false döndüren recursive şekilde çalışan method 
        if (index > 80) {  //index 80'e ulaşmışsa                                   
            return ++numberOfSolutions[0] == 1;  //çözüm 1 ise true, 1'den farklıysa false döner
        }

        int x = index / 9; //Konumlar ayarlanır
        int y = index % 9; 

        if (game[x][y] == 0) { //Eğer o konumdaki değer 0'sa
            List<Integer> numbers = new ArrayList();
            for (int i = 1; i <= 9; i++) {
                numbers.add(i); //Aday sayıların olduğu arraylist oluşturulur.
            }

            while (numbers.size() > 0) { //Aday sahalarda veri oldukça döner.
                int number = getNextPossibleNumber(x, y, numbers); //İlgili aday sayıları ve konumu gönderip uygun sayıyı alırız.
                if (number == -1) { //Uygun sayı -1'se yani çözülemiyorsa bitiririz döngüyü
                    break; 
                }
                game[x][y] = number; //Uygun sayıyı bulduysak o sahaya sayıyı yazarız.

                if (!isValid(index + 1, numberOfSolutions)) { //Tekrardan çözülme durumunu kontrol amaçla isValid'i yeni değerlerle çağırırız.
                    game[x][y] = 0;
                    return false;
                }
                game[x][y] = 0;
            }
        } else if (!isValid(index + 1, numberOfSolutions)) {
            return false;
        }

        return true;
    }

    private int getNextPossibleNumber(int x, int y, List<Integer> numbers) { //Satir, sutun ve block kontrolune gore
        //olabilecek muhtemel degeri bulan method
        while (numbers.size() > 0) {
            int number = numbers.remove(0);
            if (isPossibleX(x, number)
                    && isPossibleY(y, number)
                    && isPossibleBlock(x, y, number)) {
                return number;
            }
        }
        return -1;
    }

    private boolean isPossibleX(int row, int number) { //istenilen sutunda istenilen degeri ariyan method
        for (int i = 0; i < 9; ++i) { //Satır boyunca donuyor
            if (game[row][i] == number) { //Eger satırda aranan deger varsa bu degerin kullanilamayacagi anlasiliyor
                return false;
            }
        }
        return true; //Boyle bir deger bulunamazsa bu degerin kullanilabilecegini belirtmek icin true donduruluyor
    }

    private boolean isPossibleY(int column, int number) { //istenilen satirda istenilen degeri ariyan method
        for (int i = 0; i < 9; ++i) { //Sütun boyunca donuyor
            if (game[i][column] == number) { //Eger sütun aranan deger varsa bu degerin kullanilamayacagi anlasiliyor
                return false;
            }
        }
        return true; //Boyle bir deger bulunamazsa bu degerin kullanilabilecegini belirtmek icin true donduruluyor
    }

    private boolean isPossibleBlock(int row, int column, int number) { //9 blocktan , verilen satir ve sutun degerlerine gore
        //block u bulup o blockun kendi icindeki tutarliligini anlayan method 
        int[] locationPoints = getLocation(row, column);  //Satir ve sutun lokasyonuna gore gerekli block seciliyor

        for (int m = locationPoints[0]; m < locationPoints[0] + 3; m++) { //9 hucrelik karede geziliyor
            for (int n = locationPoints[1]; n < locationPoints[1] + 3; n++) { //Eger bu deger olusturulacak oyunda varsa
                if (game[m][n] == number) {
                    return false; //False donduruluyor
                }
            }
        }
        return true;  //Deger bu blockta yoksa true donduruluyor
    }

    private int[] getLocation(int x, int y) { //Satir ve sutun lokasyonunu alip bu lokasyonu 9 kareden hangisinde oldugunu belirleyen method
        int[] locationPoints = new int[2];
        if (x == 0 || x == 1 || x == 2) { //Satira gore konumu belirliyor
            locationPoints[0] = 0;
        } else if (x == 3 || x == 4 || x == 5) {
            locationPoints[0] = 3;
        } else {
            locationPoints[0] = 6;
        }

        if (y == 0 || y == 1 || y == 2) { //Sutuna gore konumu belirliyor
            locationPoints[1] = 0;
        } else if (y == 3 || y == 4 || y == 5) {
            locationPoints[1] = 3;
        } else {
            locationPoints[1] = 6;
        }
 
        return (locationPoints); //Bulunan konum geri donduruluyor
    }
}


