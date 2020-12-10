package logic.features;

import logic.core.Picture;
import logic.core.PictureProcessingException;
import logic.core.Quadrilateral ;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Task2Tests {

    @Test
    public void test_Denoise(){
        Picture origImg = new Picture("resources/OriginalDenoise.png");
        Picture expImg = new Picture("resources/ExpectedDenoise.png");
        PictureConvertor t = new PictureConvertor(origImg);
        Picture resultImg = t.denoise();

        assertEquals(expImg, resultImg);
    }

    @Test
    public void test_Denoise2(){
        Picture originalImg = new Picture("resources/Denoise.jpg");
        PictureConvertor t = new PictureConvertor(originalImg);
        originalImg.show();
        Picture outImg = t.denoise();
        outImg.show();
    }

    @Test
    public void test_clipException() throws  PictureProcessingException{
        Quadrilateral  rec = new Quadrilateral (0,0,2787,3288);
        Picture originalImg = new Picture("resources/95006.jpg");
        PictureConvertor t = new PictureConvertor(originalImg);
        t.clip(rec);
    }
    @Test
    public void test_boxPaint(){
        Picture origImg = new Picture("resources/95006.jpg");
        Picture expImg = new Picture("resources/tests/95006-seurat-4x4.png");
        PictureConvertor t = new PictureConvertor(origImg);
        Picture resultImg = t.boxPaint(4);

        assertEquals(expImg, resultImg);
    }

    @Test
    public void test_BoxPaint3x3(){
        Picture originalImg = new Picture("resources/216053.jpg");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture output = t.boxPaint(3);
        Picture expected = new Picture("resources/tests/216053-seurat-3x3.png");

        assertEquals(expected, output);
    }

    @Test
    public void test_BoxPaint4x4(){
        Picture originalImg = new Picture("resources/95006.jpg");
        PictureConvertor t = new PictureConvertor(originalImg);
        Picture output = t.boxPaint(4);
        Picture expected = new Picture("resources/tests/95006-seurat-4x4.png");
        output.show();

        assertEquals(expected, output);
    }
}
