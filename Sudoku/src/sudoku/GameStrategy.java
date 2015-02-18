package sudoku;

/**
 *
 * @author USER
 */
public interface GameStrategy { //Burada Strategy pattern'ini olusturmak icin interface olusturuyoruz.
    public void generateGame(); //interface icinde tanimlanmis, oyun üretmek icin kullanilacak olan method
    public int[][] getGame(); //interface icinde tanimlanmis ve olusturulan oyunu donduren oyun
}
