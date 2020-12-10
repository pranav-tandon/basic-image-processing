package logic.features;

import logic.core.Picture;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Task4Test {
    @Test
    public void test_DFTOutput() {
        double[][] exMag = {{565.00000000,
                28.00000000,
                28.00000000
        }, {28.00000000,
                14.00000000,
                14.00000000
        }, {28.00000000,
                14.00000000,
                14.00000000
        }};

        double[][] exPhase = {{0, 0, 0},{
                -1.04719755,
                        -1.04719755,
                        -1.04719755

        },{
                1.04719755,
                1.04719755,
                1.04719755

        }};
        DFTOutput expected = new DFTOutput(exMag,exPhase);

        Picture original = new Picture("resources/cosineSimilarityInput1.png");
        PictureConvertor t = new PictureConvertor(original);

        DFTOutput output = t.dft();

        assertEquals(expected.hashCode(), output.hashCode());

    }

}
