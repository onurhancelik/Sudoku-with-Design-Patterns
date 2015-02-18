/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing.Package;

import static org.junit.Assert.*;
import org.junit.Test;
import sudoku.SudokuCell;

/**
 *
 * @author USER
 */
public class SudokuCellTest {

    /**
     * Test of isTried method, of class SudokuCell.
     */
    @Test
    public void isTriedTest() {
        System.out.println("isTried");
        // Setup Fixture
        int number = 3;
        SudokuCell instance = new SudokuCell();
        boolean expResult = false;

        // Exercise SUT
        boolean result = instance.isTried(number);
        
        // Verify Outcome
        assertEquals(expResult, result);
    }

    /**
     * Test of tryNumber method, of class SudokuCell.
     */
    @Test
    public void tryNumberTest() {
        System.out.println("tryNumber");
        // Setup Fixture
        int number = 5;
        boolean expResult = true;
        SudokuCell instance = new SudokuCell();
        
        // Exercise SUT
        instance.tryNumber(number);
        boolean result = instance.isTried(number);
        
        // Verify Outcome
        assertEquals(expResult, result);
    }

    /**
     * Test of reset method, of class SudokuCell.
     */
    @Test
    public void resetTest() {
        System.out.println("reset");
        // Setup Fixture
        int expResult = 0;
        SudokuCell instance = new SudokuCell();
        instance.tryNumber(2);
        instance.tryNumber(4);
        instance.tryNumber(6);

        // Exercise SUT
        instance.reset();

        // Verify Outcome
        assertEquals(expResult, instance.numberOfTried());
    }

     /**
     * Test of numberOfTried method, of class SudokuCell.
     */
    @Test
    public void numberOfTriedTest() {
        System.out.println("numberOfTried");
        //Setup Fixture
        int expResult = 3;
        SudokuCell instance = new SudokuCell();
        instance.tryNumber(2);
        instance.tryNumber(4);
        instance.tryNumber(6);
        
        // Exercise SUT
        int result = instance.numberOfTried();
        
        // Verify Outcome
        assertEquals(expResult, result);
    }

}
