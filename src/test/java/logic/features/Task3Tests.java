package logic.features;

import logic.core.Picture;
import logic.core.PictureProcessingException;
import logic.core.Quadrilateral ;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;

public class Task3Tests {

    @Test
    public void test_CosineSimilarity_AllBlack(){
        Picture blackOne = new Picture(200,500);
        Picture blackTwo = new Picture(200,500);

        double result = PictureProcessing.cosineSimilarity(blackOne,blackTwo);
        double expected = 1.0;

        assertEquals(expected,result, 1e-7);
    }

    @Test
    public void test_CosineSimilarity_Identical(){
        Picture test1 = new Picture("resources/8143.jpg");
        double result = PictureProcessing.cosineSimilarity(test1,test1);
        double expected = 1.0;

        assertEquals(expected, result, 1e-7);
    }

    @Test
    public void test_CosineSimilarity_Different(){
        Picture test1 = new Picture("resources/cosineSimilarityInput1.png");
        Picture test2 = new Picture("resources/cosineSimilarityInput2.png");

        double result = PictureProcessing.cosineSimilarity(test1,test2);
        //computed using calculator
        double expected = 0.876518469;

        assertEquals(expected,result, 1e-7);
    }

    @Test
    public void test_CosineSimilarity_BlackAndWhite(){
        Picture black = new Picture(200,500);
        Picture white = new Picture(200, 500);

        for(int i =0; i < white.length(); i++){
            for(int j = 0; j < white.breadth(); j++){
                white.set(j,i, Color.WHITE);
            }
        }
        double result = PictureProcessing.cosineSimilarity(black,white);
        double expected = 0.0;

        assertEquals(expected, result, 1e-7);
    }

    @Test
    public void test_CosineSimilarity_WhiteAndBlack(){
        Picture black = new Picture(200,500);
        Picture white = new Picture(200, 500);

        for(int i =0; i < white.length(); i++){
            for(int j = 0; j < white.breadth(); j++){
                white.set(j,i, Color.WHITE);
            }
        }
        double result = PictureProcessing.cosineSimilarity(white,black);
        double expected = 0.0;

        assertEquals(expected, result, 1e-7);
    }

    @Test
    public void test_Rotate_30() throws PictureProcessingException {
        Picture originalPicture = new Picture("resources/12003.jpg");
        Picture expected = new Picture("resources/tests/12003-r30.png");
        /*Quadrilateral  used to trim to get uniform pictures with same dimensions*/
        Quadrilateral  cropper = new Quadrilateral (0,0,expected.breadth()-1, expected.length()-2);
        PictureConvertor o = new PictureConvertor(originalPicture);
        Picture output = o.rotate(30);
        PictureConvertor t = new PictureConvertor(expected);
        Picture expectedCropped = t.clip(cropper);

        assertEquals(expectedCropped,output);

    }

    @Test
    public void test_Rotate_45(){
        Picture originalPicture = new Picture("resources/12003.jpg");
        Picture expected = new Picture("resources/tests/12003-r45.png");

        PictureConvertor o = new PictureConvertor(originalPicture);
        Picture output = o.rotate(45);

        assertEquals(expected,output);
    }

    @Test
    public void test_Rotate_75() throws PictureProcessingException {
        Picture originalPicture = new Picture("resources/12003.jpg");
        Picture expected = new Picture("resources/tests/12003-r75.png");
        /*Quadrilateral  used to trim to get uniform pictures with same dimensions*/
        Quadrilateral  cropper = new Quadrilateral (0,0,expected.breadth()-2, expected.length()-1);
        PictureConvertor o = new PictureConvertor(originalPicture);
        Picture output = o.rotate(75);

        PictureConvertor t = new PictureConvertor(expected);
        Picture expectedCropped = t.clip(cropper);

        assertEquals(expectedCropped,output);
    }

    @Test
    public void test_Rotate_180(){
        Picture originalPicture = new Picture("resources/12003.jpg");
        Picture expected = new Picture("resources/tests/12003-r180.png");

        PictureConvertor o = new PictureConvertor(originalPicture);
        Picture output = o.rotate(180);

        assertEquals(expected, output);
    }

    @Test
    public void test_Rotate_360(){
        Picture originalPicture = new Picture("resources/12003.jpg");
        Picture expected = new Picture("resources/12003.jpg");

        PictureConvertor o = new PictureConvertor(originalPicture);
        Picture output = o.rotate(360);

        /*We have to use cosine Similarity to compare since comparing jpg to png may not work*/
        double actual = PictureProcessing.cosineSimilarity(expected,output);
        double expectedSim = 1;

        assertEquals(expectedSim, actual, 1e-2);
    }
}