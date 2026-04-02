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
}