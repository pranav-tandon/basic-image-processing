package logic.features;

import logic.core.Picture;
import org.junit.Test;

import static org.junit.Assert.*;

public class Level2Tests {

    @Test
    public void test_Weathering() {
        Picture originalImg = new Picture("resources/95006.jpg");
        Picture expectedImg = new Picture("resources/tests/95006-weathered.png");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture outputPicture = t.weather();
        assertEquals(expectedImg, outputPicture);
    }
}
