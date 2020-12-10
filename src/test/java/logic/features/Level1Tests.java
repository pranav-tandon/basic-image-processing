package logic.features;

import logic.core.Picture;
import logic.core.PictureProcessingException;
import logic.core.Quadrilateral ;
import org.junit.Test;

import static org.junit.Assert.*;

public class Level1Tests {

    @Test
    public void test_Mirroring() {
        Picture originalImg = new Picture("resources/15088.jpg");
        Picture expectedImg = new Picture("resources/tests/15088-mirror.png");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture outputPicture = t.mirror();
        assertEquals(expectedImg, outputPicture);
    }

    @Test
    public void test_Negative() {
        Picture originalImg = new Picture("resources/15088.jpg");
        Picture expectedImg = new Picture("resources/tests/15088-negative.png");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture outputPicture = t.negative();
        assertEquals(expectedImg, outputPicture);


    }

    @Test
    public void test_Posterize() {
        Picture originalImg = new Picture("resources/15088.jpg");
        Picture expectedImg = new Picture("resources/tests/15088-poster.png");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture outputPicture = t.posterize();
        assertEquals(expectedImg, outputPicture);
    }

    @Test
    public void test_Clip() throws PictureProcessingException {
        Picture originalImg = new Picture("resources/15088.jpg");
        Picture expectedImg = new Picture("resources/tests/15088-clip-60-100-250-350.png");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture outputPicture = t.clip(new Quadrilateral (60, 100, 250, 350));
        assertEquals(expectedImg, outputPicture);
    }

}
