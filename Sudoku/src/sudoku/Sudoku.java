package sudoku;
/**
 *
 * @author USER
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { //Oyunu baslatan esas method
        
        SudokuBoard sb = new SudokuBoard(); //Sudoku tahtasi olusturuluyor,
        sb.startGame(); //Ve oyun başlatılıyor.
    }
}
