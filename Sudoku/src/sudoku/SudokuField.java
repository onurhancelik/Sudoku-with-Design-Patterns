package sudoku;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public class SudokuField implements Serializable,CellInterface{ //İlgili arayüzleri implemente ettik.
    
    private final int blockSize; //Alt bloğun boyutunu tutan değişken.
    private final int fieldSize; //Genel bloğun boyutunu tutan değişken.
    private CellInterface[][] field; //Her hücre CellInteface tipindedir ve matris şeklinde tutulurlar.

    public SudokuField(final int blocks) //Yapıcı fonksiyon.
    {
        blockSize = blocks; //Parametre olarak gelen blocks değişkenini alt blok boyutuna atadık.
        fieldSize = blockSize * blockSize; //Genel bloğun boyutu belirledik ilgili block boyutuna göre.
        field = new CellInterface[fieldSize][fieldSize]; //Nesneyi yarattık.
        for (int i = 0; i < fieldSize; ++i) 
        {
            for (int j = 0; j < fieldSize; ++j) {
                field[i][j] = new SudokuCell(); //Her CellInterface tipindeki hücreyi SudokuCell tipinde yarattık.
            }
        }
    }
    
    //Block boyutunu döndüren fonksiyon.
    public int blockSize() {
        return blockSize;
    }

    //field boyutunu döndüren fonksiyon.
    public int fieldSize() {
        return fieldSize;
    }

    //Alt bloklardaki (satır,sütun,3x3) hücrelerin sayılarını döndüren fonksiyon.
    public int variantsPerCell() {
        return fieldSize;
    }

    //Toplam hücre sayısını döndüren fonksiyon.
    public int numberOfCells() {
        return fieldSize * fieldSize;
    }

    //Parametre olarak gelen row-column değerlerine göre ilgili hücrenin temizlenmesi.
    public void clearOneCell(final int row, final int column) {
        field[row - 1][column - 1].clear();
    }

    //Arayüz sınıfından alınan clear metodunun gerçekleştirimi. Tüm hücreleri temizler.
    @Override
    public void clear()
    {
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                field[i][j].clear();
            }
        }
    }

    //Parametre olarak gelen row-column değerlerine göre ilgili hücreyi resetler.
    public void resetOneCell(final int row, final int column) 
    {
        field[row - 1][column - 1].reset();
    }

    //Arayüz sınıfından alınan reset metodunun gerçekleştirimi. Tüm hücreleri resetler.
    @Override
    public void reset() 
    {
        for (int i = 0; i < fieldSize; ++i) 
        {
            for (int j = 0; j < fieldSize; ++j) 
            {
                field[i][j].reset();
            }
        }
    }

    //Parametre olarak gelen row-column değerlerine göre ilgili hücrenin boş olup olmadığını döndüren fonksiyon.
    public boolean isFilledOneCell(final int row, final int column) 
    {
        return field[row - 1][column - 1].isFilled();
    }

    //Tüm hücreler doluysa True, boş hücre varsa False döndüren fonksiyon.
    @Override
    public boolean isFilled() 
    {
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                if (!((SudokuCell)field[i][j]).isFilled()) {
                    return false;
                }
            }
        }
        return true;
    }

    //Parametre olarak alınan row-column değerine göre ilgili hücredeki sayıyı döndürür.
    public int get(final int row, final int column) 
    {
        return ((SudokuCell)field[row - 1][column - 1]).get();
    }

    //Parametre olarak alınan row-column değerine göre ilgili hücreye yine parametre olarak alınan number değerini atayan fonksiyon.
    public void set(final int number, final int row, final int column) 
    {
        ((SudokuCell)field[row - 1][column - 1]).set(number);
    }

    //Parametrelere göre ilgili hücrede gelen sayının denendiğini kaydeden fonksiyon.
    public void tryNumber(final int number, final int row, final int column) 
    {
        ((SudokuCell)field[row - 1][column - 1]).tryNumber(number);
    }

    //Parametrelere göre ilgili hücreye ilgili sayının daha önceden denenip denenmediğini döndüren fonksiyon.
    public boolean numberHasBeenTried(final int number, final int row, final int column) 
    {
        return ((SudokuCell)field[row - 1][column - 1]).isTried(number);
    }

    //Parametrelere göre ilgili hücreye daha önce toplam kaç sayının denendiğini döndüren fonksiyon
    public int numberOfTriedNumbers(final int row, final int column) 
    {
        return ((SudokuCell)field[row - 1][column - 1]).numberOfTried();
    }

    //Parametrelere göre ilgili hücreye konulan number sayısının hücrenin bulunduğu alt bölgede geçerli olup olmadığını döndüren fonksiyon.
    public boolean checkNumberBox(final int number, final int row, final int column)
    {
        int r = row, c = column;
        if (r % blockSize == 0) { //İlgili hücrenin alt bölgesinin başlangıç ve bitiş satır indexlerini hesaplıyoruz.
            r -= blockSize - 1;
        } else {
            r = (r / blockSize) * blockSize + 1;
        }
        if (c % blockSize == 0) { //İlgili hücrenin alt bölgesinin başlangıç ve bitiş sütun indexlerini hesaplıyoruz.
            c -= blockSize - 1;
        } else {
            c = (c / blockSize) * blockSize + 1;
        }
        //İç içe for döngüleriyle index'leri bulunan alt bölgede parametre olarak gelen sayının 
        //alt bölgede bulunup bulunmadığını kontrol ediyoruz.
        for (int i = r; i < r + blockSize; ++i) {
            for (int j = c; j < c + blockSize; ++j) {
                if (((SudokuCell)field[i - 1][j - 1]).isFilled() && (((SudokuCell)field[i - 1][j - 1]).get() == number)) {
                    return false; //İlgili hücre daha önce doldurulmuşsa ve de doldurulan sayı aranan sayıysa false döndürürüz.
                }
            }
        }
        return true; //Bulunmazsa o sayı ilgili alt bölgede true döndürürüz.
    }

    //Parametre olarak gelen satırda yine parametre olarak gelen sayının bulunup bulunmadığını kontrol ediyoruz.
    public boolean checkNumberRow(final int number, final int row) 
    {
        for (int i = 0; i < fieldSize; ++i) {
            if (((SudokuCell)field[row - 1][i]).isFilled() && ((SudokuCell)field[row - 1][i]).get() == number) {
                return false; //İlgili hücre daha önce doldurulmuşsa ve de doldurulan sayı aranan sayıysa false döndürürüz.
            }
        }
        return true; //Bulunmazsa o sayı ilgili satırda true döndürürüz.
    }

    //Parametre olarak gelen sütunda yine parametre olarak gelen sayının bulunup bulunmadığını kontrol ediyoruz.
    public boolean checkNumberColumn(final int number, final int column) 
    {
        for (int i = 0; i < fieldSize; ++i) {
            if (((SudokuCell)field[i][column - 1]).isFilled() && ((SudokuCell)field[i][column - 1]).get() == number) {
                return false; //İlgili hücre daha önce doldurulmuşsa ve de doldurulan sayı aranan sayıysa false döndürürüz.
            }
        }
        return true; //Bulunmazsa o sayı ilgili sütunda true döndürürüz.
    }

    //İlgili hücrenin bulunduğu satır,sütun ve alt bölgede sayıyı ararız.
    public boolean checkNumberField(final int number, final int row, final int column) 
    {
        return (checkNumberBox(number, row, column)
                && checkNumberRow(number, row)
                && checkNumberColumn(number, column)); //Eğer ilgili satır, sütun ve alt bölgenin hiç birinde yoksa true
                                                        //en az birinde varsa false döndürür.
    }

    //Gelen parametrelere göre ilgili hücreden sonra gelen hücrenin satır ve sütun numarasını hesaplayarak, 
    //Index tipinde değer döndüren fonksiyon
    public Index nextCell(final int row, final int column) 
    {
        int r = row, c = column;
        if (c < fieldSize) { //Eğer son sütuna ulaşılmamışsa sütun sayısı 1 arttırılır, satır sayısı aynı kalır.
            ++c;
        } else { //Son sütuna ulaşılmışsa sütun sayısını 1 yaparız, satır sayısını 1 arttırırız.
            c = 1;
            ++r;
        }
        return new Index(r, c);
    }

    //1-9 arasında random bir sayı üretip döndüren fonksiyon.
    public int getRandomIndex() 
    {
        return (int) (Math.random() * 10) % fieldSize + 1;
    }
    
    //Parametre olarak gelen satır,sütundan başlayarak tüm hücreleri dolduran recursive şekilde çalışan fonksiyon..
    public void generateFullField(final int row, final int column) 
    {
        if (!this.isFilledOneCell(this.fieldSize(), this.fieldSize())) //Gelen hücre dolu değilse;
        {
            while (this.numberOfTriedNumbers(row, column) < this.variantsPerCell()) //İlgili hücrelere denenen toplam sayı 
            {                                                       //field boyutundan küçük olduğu süre while döngüsü döner.
                int candidate = 0;
                do { 
                    candidate = this.getRandomIndex(); //Random bir sayı üretilir.
                } while (this.numberHasBeenTried(candidate, row, column)); //İlgili hücrede denenmeyen bir sayı bulunana kadar döngü döner.
                if (this.checkNumberField(candidate, row, column)) //Bulunan aday sayının olabilme ihtimali kontrol edilir.
                {                                                   
                    this.set(candidate, row, column); //Sayı olabiliyorsa hücreye yazılır.
                    Index nextCell = this.nextCell(row, column); //Sonraki konum üretilir.
                    if (nextCell.getRow() <= this.fieldSize() //Sonraki konum eğer fieldSize'dan taşmıyorsa
                            && nextCell.getColumn() <=this.fieldSize()) {
                        generateFullField(nextCell.getRow(), nextCell.getColumn()); //Fonksiyon yeni parametrelerle kendini çağırır.
                    }
                } else {
                    this.tryNumber(candidate, row, column); //Aday sayı eğer olamıyorsa, sayı ilgili hücrenin denenme kümesine eklenir.
                }
            }
            if (!this.isFilledOneCell(this.fieldSize(), this.fieldSize())) //Eğer en son gelinen hücre boş değilse o hücre reset'lenir.
            {
                this.resetOneCell(row, column);
            }
        }
    }
}
