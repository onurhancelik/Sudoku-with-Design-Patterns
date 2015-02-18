package sudoku;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USER
 */
public class ButtonController extends JPanel { //Grafik arayuzlerinin kullanilacagi class

    // check , hint , new ve rest icin butonlar tanimlaniyor
    private JButton btnCheck;
    private JButton btnHint;
    private JButton btnNew;
    private JButton btnReset;
    // kolay ve zorluk secimi icin radio buttonlar tanimlaniyor
    private JRadioButton rBtnEasy;
    private JRadioButton rBtnHard;
    
    private ButtonGroup btnGroup;// radio buttonların stateleri birbirlerine gore onem kazanacagi icin gruplanmak icin button grup olusturuluyor
    
    private SudokuBoard sudokuBoard; //Oyun tahtasi olusturuluyor
    private GridBagConstraints gbc; //Butonların konumunu belirtmeye yaran üye

    public ButtonController(SudokuBoard sudokuBoard) { //Yapici method olusturuluyor , buttonların nesneleri olusturuluyor
        this.sudokuBoard = sudokuBoard;
        gbc = new GridBagConstraints();

        btnCheck = new JButton("Check");
        btnHint = new JButton("Hint");
        btnNew = new JButton("New");
        btnReset = new JButton("Reset");
        setRadioButtons(); //Radio buttonlar ayarlanıyor
    }

    public String getSelectedRadio() { //Radio button'lardan seçili olanı döndüren metot
        Enumeration<AbstractButton> allRadioButton = btnGroup.getElements();
        while (allRadioButton.hasMoreElements()) { //Başka eleman kalmayana kadar
            JRadioButton temp = (JRadioButton) allRadioButton.nextElement(); //RadioButton'a cast edilip, geçiciye atılıyor
            if (temp.isSelected()) { //Eğer seçiliyse o buton
                return temp.getText();
            }
        }
        return null;
    }

    private void setMenu() {  //Menu kisminin grafiksel ayarlarinin yapildigi method
        setBorder(new EmptyBorder(4, 4, 4, 4));
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    private void checkButtonAction() { //Check button'ının eventlerinin olusturulduğu method
        btnCheck.setFocusable(false);
        
        btnCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { //Check'e basildiginda yapilan islemlerin oldugu method
                boolean flag = true; //Doğru çözüm olup olmadığını tutan değişken
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) { //Sudokuboard'daki cozum ile hucrelere girilen degerler kontrol ediliyor
                        if (!sudokuBoard.getFields()[i][j].getText().equals(String.valueOf(sudokuBoard.getSolution()[i][j]))) {
                            flag = false; //Eğer eşleşmeyen bir saha olursa flag false yapılır.
                            sudokuBoard.getFields()[i][j].setBackground(Color.RED); //Hucre eslesmezse rengi kirmizi yapiliyor
                        } else { //Hucre eslesirse rengi yesil yapiliyor
                            if (sudokuBoard.getFields()[i][j].getBackground().getRGB() != -986896) {
                                sudokuBoard.getFields()[i][j].setBackground(Color.GREEN);
                            }
                        }
                    }
                }
                if (!flag) { //Yanlis cozuldugunde yazdirilacak mesaj
                    JOptionPane.showMessageDialog(null, "Yanlış çözdünüz..", "Wrong Solution", JOptionPane.ERROR_MESSAGE);
                } else { //Dogru cozumde yazdirilacak mesaj
                    JOptionPane.showMessageDialog(null, "Tebrikler, doğru çözdünüz..", "True Solution", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        add(btnCheck, gbc); //Frame'e ekleniyor 
        gbc.gridy++; //gbc komutu artırılıyor.
    }

    private void hintButtonAction() { //Hint button'ının eventlerinin olusturulduğu method
        btnHint.setFocusable(false);
        btnHint.addActionListener(new ActionListener() { 

            @Override
            public void actionPerformed(ActionEvent e) { //Hint'e basildiginda yapilan islemlerin oldugu method
                boolean flag = false;
                List<Integer> positions = new ArrayList();
                for (int i = 0; i < 81; i++) {
                    positions.add(i); //Pozisyonlar yukleniyor
                } 
                Collections.shuffle(positions); //Pozisyonlar karistiriliyor
                while (flag == false && positions.size() > 0) {
                    int position = positions.remove(0);

                    int x = position / 9;
                    int y = position % 9;
                    
                    //Ekranda verilmeyen bir pozisyon bulunup o deger ekleniyor    
                    if (sudokuBoard.getFields()[x][y].getText().equals("")) {
                        sudokuBoard.getFields()[x][y].setText(String.valueOf(sudokuBoard.getSolution()[x][y]));
                        flag = true;
                    }
                }
            }
        });
        add(btnHint, gbc); //Frame'e ekleniyor
        gbc.gridy++; //gbc konumu artırılıyor
    }

    private void newButtonAction() { //New button'ının eventlerinin olusturulduğu method
        btnNew.setFocusable(false);
        btnNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { //New'e basildiginda yapilan islemlerin oldugu method
                sudokuBoard.createGame(); //Yeni cozum ve game oluşturması için createGame() metodu çağrılıyor
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if(sudokuBoard.getGame()[i][j] == 0 )  //Oluşturulan yeni game'e göre TextField'lerin ayarlanması
                        {                                       //0'sa kullanıcı bu sahayı null görür, arkaplan beyaz olarak ayarlanır ve de yazılıbilirliği açık olur.
                            sudokuBoard.getFields()[i][j].setText("");
                            sudokuBoard.getFields()[i][j].setBackground(Color.WHITE);
                            sudokuBoard.getFields()[i][j].setEditable(true);     
                        }
                        else
                        {  //0 değilse kullanıcı gerçek değeri görür, ve onu değiştiremez.
                            sudokuBoard.getFields()[i][j].setText(String.valueOf(sudokuBoard.getGame()[i][j]));
                            sudokuBoard.getFields()[i][j].setBackground(new Color(-986896));
                            sudokuBoard.getFields()[i][j].setEditable(false);
                        }
                    }
                }
            }
        });
        add(btnNew, gbc); //Button frame'e eklenir 
        gbc.gridy++; //gbc artılır.
    }

    private void resetButtonAction() { //Reset button'ının eventlerinin olusturulduğu method
        btnReset.setFocusable(false);
        btnReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { //Reset button'ı kullanilmasi durumunda yapilacak olan islemler
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) { //Kullanıcının gireceği sahalara null atanır ve renkleri beyaz olarak ayarlanır.
                        sudokuBoard.getFields()[i][j].setText(sudokuBoard.getGame()[i][j] == 0 ? "" : String.valueOf(sudokuBoard.getGame()[i][j]));
                        sudokuBoard.getFields()[i][j].setBackground(sudokuBoard.getGame()[i][j] == 0 ? Color.WHITE : sudokuBoard.getFields()[i][j].getBackground());
                    }
                }
            }
        });
        add(btnReset, gbc);
        gbc.gridy++;
    }

    private void setRadioButtons() { //Radio button nesneleri olusturuluyor ve beraber kullanilmak uzere aynı gruba ekleyen method
        rBtnEasy = new JRadioButton("Easy"); //Button'ların yaratılması ilgili isimleriyle
        rBtnHard = new JRadioButton("Hard");
        btnGroup = new ButtonGroup();
        btnGroup.add(rBtnEasy); //Buttonlarin gruplanmasi
        btnGroup.add(rBtnHard);

        setSize(100,200);
        setLayout( new FlowLayout());
        
        rBtnEasy.setSelected(true); //Easy radio button'u default olarak seciliyor
        add(rBtnEasy, gbc);
        gbc.gridy++;
        add(rBtnHard, gbc);
        gbc.gridy++;
        
    }

    public ButtonController createController() { //Buttonlarin kullanilmak uzere sudokuboarda donduren method
        setMenu();
        checkButtonAction();
        hintButtonAction();
        newButtonAction();
        resetButtonAction();

        return this;
    }
}
