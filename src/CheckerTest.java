import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckerTest {

    private Checker checkerTest;

    @BeforeEach
    public void setUp() {
        checkerTest = new Checker("words");
    }

    @Test
    public void testDuplicationCorrected() {
        String expected = "hello";
        assertEquals(expected, checkerTest.checkWord("helllooo"));
    }

    @Test
    public void testCapitalizationCorrected() {
        String expected = "English";
        assertEquals(expected, checkerTest.checkWord("english"));
    }
}

