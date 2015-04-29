import models.utility.AST;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class AstTest {

    @Test
    public void generateInverseGenderMap_GenderMap_CorrectInversionGenerated() {
        Map<String, String> actual = AST.genderMapInverse();
        Map<String, String> expected = new HashMap<>();
        expected.put("Keine Angabe","");
        expected.put("Männlich","male");
        expected.put("Weiblich","female");

        assertThat(actual).isEqualTo(expected);
    }

}