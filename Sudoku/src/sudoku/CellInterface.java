package sudoku;

/**
 *
 * @author USER
 */
public interface CellInterface { //Bir hücreye veya tüm hücrelere ait ilgili fonksiyonlar içeren arayüz sınıfı.
    public boolean isFilled();
    public void reset();
    public void clear();
}
