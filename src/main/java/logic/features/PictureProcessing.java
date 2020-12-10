package logic.features;

import logic.core.Picture;

/**
 * This class provides some simple operations involving
 * more than one picture.
 */
public class PictureProcessing {

    /**
     * Compute the cosine similarity between two pictures.
     *
     * @param img1: the first picture, is not null.
     * @param img2: the second picture, in not null and matches img1 in dimensions.
     * @return the cosine similarity between the Pictures
     * referenced by img1 and img2.
     */
    public static double cosineSimilarity(Picture img1, Picture img2) {
        Picture gray1 = new PictureConvertor(img1).grayscale();
        Picture gray2 = new PictureConvertor(img2).grayscale();

        int breadth = gray1.breadth();
        int length = gray1.length();
        int dimension = gray1.breadth()* gray1.length();

        int[] vector1 = new int[dimension];
        int[] vector2 = new int[dimension];

        long dotProduct = 0;
        long sumOfSquares1 = 0;
        long sumOfSquares2 = 0;
        int count = 0;

        for (int i = 0 ; i < length; i++) {
            for (int j = 0; j < breadth; j++){
                vector1[count] = gray1.getRGB(j,i) >> 16 &0xFF;
                vector2[count] = gray2.getRGB(j,i) >> 16 &0xFF;
                dotProduct += Math.abs(vector1[count] * vector2[count]);
                sumOfSquares1 += Math.pow(vector1[count],2);
                sumOfSquares2 += Math.pow(vector2[count], 2);
                count++;
            }
        }

        if (dotProduct == 0 && sumOfSquares1 == 0 && sumOfSquares2 == 0) {
            return 1;
        }else if(sumOfSquares1 == 0 || sumOfSquares2 == 0){
            return 0;
        }

        double cosSimilarity = dotProduct/(Math.sqrt(sumOfSquares1) * Math.sqrt(sumOfSquares2));
        return cosSimilarity;
    }

}
