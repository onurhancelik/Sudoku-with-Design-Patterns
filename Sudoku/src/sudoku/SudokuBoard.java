package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author USER
 */
public class SudokuBoard {

    private int[][] game; //Kullanıcıya sunulacak oyundaki verileri tutar.
    private int[][] solution; //Kullanıcının ulaşmaya çalıştığı çözüm verilerini tutar.
    private JTextField[][] fields; //Görsel şekilde kullanıcının verilerini gireceği her bir kutucuk.
    private JFrame frame; //İlgili frame
    private ButtonController buttonController; //GUI amacıyla ilgili button ve radioButton'ları oluşturup 
                                                //frame'e eklememizi sağlayan ButtonController tipinde değişken

    public SudokuBoard() { //Nesnelerin yaratılmasını yapan ilgili yapıcı fonksiyon
        game = new int[9][9];
        solution = new int[9][9];
        buttonController = new ButtonController(this); //buttonController nesnesini yaratırken içinde bulunulan 
                                                    //SudokuBoard'u parametre olarak yolluyoruz..

    }

    //fields üyesini döndüren metot
    public JTextField[][] getFields() {
        return fields;
    }

    //solution üyesini döndüren metot
    public int[][] getSolution() {
        return solution;
    }

    //game üyesini döndüren metot
    public int[][] getGame() {
        return game;
    }

    //İlgili çözümü ve buna bağla oyunu üreten fonksiyon.
    public void createGame() {
        SudokuField cozum = new SudokuField(3); //SudokuField tipinde yaratılan cozum nesnesi
        cozum.generateFullField(1, 1);      //generateFullField metodunu çağırarak çözüm olusturuluyor.

        for (int m = 1; m < 10; m++) {
            for (int n = 1; n < 10; n++) {
                solution[m - 1][n - 1] = cozum.get(m, n); //Oluşturulan çözümde bulunan sayılar solution üyesine alınıyor.
                game[m - 1][n - 1] = solution[m - 1][n - 1]; //Aynı zamanda oyun yaratılmadan önce game üyesine çözüm atılıyor.
                System.out.print(" " + solution[m - 1][n - 1]);
            }
            System.out.println(" ");
        }
        
        GameStrategy gameStrategy; //Çözüm üzerinden yaratılacak olan oyunun nesnesi oluşturuluyor.
        
        String radioButton = buttonController.getSelectedRadio(); //İlgili radioButton'lardan seçili olan alınıyor.
         if (radioButton.equals("Easy")) { //Eğer EasyRadioButton seçiliyse bu bloğa girilir.
            gameStrategy = new EasyGame(game); //Nesne EasyGame'den yaratılır.
            gameStrategy.generateGame(); //Oyunun üretilmesini sağlan generateGame metodu çağrılır.
            game = gameStrategy.getGame(); //Üretilen oyun game'e atanır.
        }
        if (radioButton.equals("Hard")) { //Eğer HardRadioButton seçiliyse bu bloğa girilir.
            gameStrategy = new HardGame(game);
            gameStrategy.generateGame();
            game = gameStrategy.getGame();
        }
    }

    //İlgili oyunu başlatan metot. Frame'e ilgili control'leri ekler ve kullanıcıya hazır şekilde sunar.
    public void startGame() {
        createGame(); // Kullanıcıya sunulucak oyunu ve oyunun çözümünü oluşturan metot çağrılır.

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }
                frame = new JFrame("Sudoku v1");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Kullanıcı close'a basınca uygulamanın kapatılmasını sağlayan kod parçacığı.
                frame.setLayout(new BorderLayout());
                frame.add(new SubBoard()); //İlgili hücre TextField'lerinin yaratılması ve ayarlanmasını sağlayan 
                                            //SubBoard nested class'ı yaratılır ve frame'e eklenir.
                //İlgili button ve radioButton'ların oluşturulmasını ve frame'e eklenmesini sağlayan createContoller metodu çağrılır.
                frame.add(buttonController.createController(), BorderLayout.AFTER_LINE_ENDS);
                
                frame.pack();
                frame.setVisible(true); //İlgili frame kullanıcıya görünür yapılır.
            }
        });
    }

    //SudokuBoard class'ına ait SubBoard nested class'ı.
    private class SubBoard extends JPanel {

        public SubBoard() {
            setLayout(new GridLayout(3, 3, 2, 2)); //çerçeve boyutu ayarlanır.
            setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 3), new EmptyBorder(4, 4, 4, 4))); //Çerçeve çizgileri ayarlanır.
 
            Font font1 = new Font("SansSerif", Font.BOLD, 20); //İlgili textbox'ların sahip olacağı font'u belirlenir.

            fields = new JTextField[9][9]; //Dizi nesnesi oluşturulur.
            for (int row = 0; row < 9; row++) { //Tüm TextField'ler geziliyor.
                for (int col = 0; col < 9; col++) {
                    fields[row][col] = new JTextField(2); //İlgili TextField oluşturulur.
                    fields[row][col].setFont(font1); //Font'u belirlenir.
                    fields[row][col].setHorizontalAlignment(JTextField.CENTER); //Metnin konumu belirlenir.
                    //TextField'lere değerler atanır game üyesinden. Eğer 0'sa null, 0 değilse değer atanır.
                    fields[row][col].setText(String.valueOf(game[row][col]).equals(String.valueOf(0)) ? "" : String.valueOf(game[row][col]));
                    
                    //TextField'lerin yazılabilirliği ayarlanır. Değeri 0 olmayanlara false, 0 olanlara true atanır.
                    fields[row][col].setEditable(String.valueOf(game[row][col]).equals(String.valueOf(0)) ? true : false);
                    
                    //TextField'lerin arkaplan renkleri ayarlanır. Kullanıcının gireceği TextField'ler beyaz diğerleri gri.
                    fields[row][col].setBackground(game[row][col] == 0 ? Color.WHITE : new Color(-986896));

                    //Kullanıcının TexTField'lere sadece 1-9 aralığında sayı girmesini belirleyen kısım.
                    AbstractDocument d = (AbstractDocument) fields[row][col].getDocument();
                    d.setDocumentFilter(new DocumentFilter() {
                        int max = 1;

                        @Override
                        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                            int documentLength = fb.getDocument().getLength();
                            if (documentLength - length + text.length() <= max && (text.equals("1") || text.equals("2") || text.equals("3")|| text.equals("4") 
                     || text.equals("5")|| text.equals("6")|| text.equals("7")|| text.equals("8")|| text.equals("9") || text.equals(""))) {
                                super.replace(fb, offset, length, text.toUpperCase(), attrs);
                            }
                        }
                    });
                }
            }
                
            //Kullanıcıya sunulan TextField'lerin alt bölgeler halinde görünümünü sağlayan döngü.
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    int startRow = row * 3;
                    int startCol = col * 3;
                    add(new ChildBoard(3, 3, fields, startRow, startCol)); //Her 3x3'lük bölge için ChildBoard nesnesi yaratılır.
                }
            }
        }
    }

    //3x3'lük alanları ayarlayan kısım.
    private class ChildBoard extends JPanel {

        public ChildBoard(int rows, int cols, JTextField[][] fields, int startRow, int startCol) {
            setBorder(new LineBorder(Color.LIGHT_GRAY, 4)); //3x3'lük parçalar arasındaki border'ların ayarlanması.
            setLayout(new GridLayout(rows, cols, 2, 2));
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    add(fields[startRow + row][startCol + col]); //Ve frame'e ilgili TextField'ın eklenmesi.
                }
            }
        }
    }
}
