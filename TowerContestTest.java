import junit.framework.TestCase;

//pruebas unitarias para nuestro TowerContest
/**
 * Pruebas unitarias para TowerContest.
 */
public class TowerContestTest extends TestCase{
    public void testSolve_n1h1_zeroOperaciones()
    {
        assertEquals("1", TowerContest.solve(1, 1));
    }

    public void testSolve_n2h4_zeroOperaciones()
    {
        assertTrue(TowerContest.solve(2, 4).startsWith("1"));
    }

    public void testSolve_n2h3_unaOperacion()
    {
        String r = TowerContest.solve(2, 3);
        assertFalse("impossible".equals(r));
    }
    
    // pruebas propuestas para el grupo(Ospina - Rojas)
    /**
     * Valida que para n=4 y h=12 el resultado es 5 3 1 7
     */
    public void testAccordingORShouldSolveN_4H_12_Return5317()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(4, 12);
        assertEquals("5 3 1 7", result);
    }
    
    /**
     * Valida que para n=5 y h=16 el resultado es 7 5 3 1 9
     */
    public void testAccordingORShouldSolveN_5H_16_Return75319()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(5, 16);
        assertEquals("7 5 3 1 9", result);
    }
    
    /**
     * Valida que para n=4 y h=11 el resultado es 7 3 1 5
     */
    public void testAccordingCOShouldContestSolveReturn7315()
    {
        String result = TowerContest.solve(4, 11);
        assertEquals("7 5 3 1", result);
    }
    
    /**
     * Valida que para n=5 y h=11 el resultado es 9 7 5 3 1
     */
    public void testAccordingCOShouldTrivialSolutionReturn97531()
    {
        String result = TowerContest.solve(5, 11);
        assertEquals("9 7 5 3 1", result);
    }
}