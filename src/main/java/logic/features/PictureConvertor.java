package logic.features;

import logic.core.Picture;
import logic.core.PictureProcessingException;
import logic.core.Quadrilateral ;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This datatype (or class) provides operations for transforming an picture.
 *
 * <p>The operations supported are:
 * <ul>
 *     <li>The {@code PictureConvertor} constructor generates an instance of an picture that
 *     we would like to transform;</li>
 *     <li></li>
 * </ul>
 * </p>
 */

public class PictureConvertor {

    private Picture picture;
    private int breadth;
    private int length;

    /**
     * Creates an PictureConvertor with an picture. The provided picture is
     * <strong>never</strong> changed by any of the operations.
     *
     * @param img is not null
     */
    public PictureConvertor(Picture img) {
        picture = img;
        this.breadth = picture.breadth();
        this.length = picture.length();
    }

    /**
     * Obtain the grayscale version of the picture.
     *
     * @return the grayscale version of the instance.
     */
    public Picture grayscale() {
        Picture gsPicture = new Picture(breadth, length);
        for (int col = 0; col < breadth; col++) {
            for (int row = 0; row < length; row++) {
                Color color = picture.get(col, row);
                Color gray = Picture.toGray(color);
                gsPicture.set(col, row, gray);
            }
        }
        return gsPicture;
    }

    /**
     * Obtain a version of the picture with only the red colours.
     *
     * @return a reds-only version of the instance.
     * @param color
     */
    public Picture red(Color color) {
        Picture redPicture = new Picture(breadth, length);
        for (int col = 0; col < breadth; col++) {
            for (int row = 0; row < length; row++) {
                int originalPixel = picture.getRGB(col, row);
                int alpha = (originalPixel >> 24) & 0xFF;
                int red = (originalPixel >> 16) & 0xFF;
                int desiredColor = (alpha << 24) | (red << 16) | (0 << 8) | (0);
                redPicture.setRGB(col, row, desiredColor);
            }
        }
        return redPicture;
    }

    /**
     * Returns the mirror picture of an instance.
     *
     * @return the mirror picture of the instance.
     */
    public Picture mirror() {
        Picture mirrored = new Picture(picture);
        Color color;


        for (int i = 0; i < length; i++){
            for (int j = 0; j < breadth/2; j++){
                color = mirrored.get(j,i);
                mirrored.set(j,i,mirrored.get(breadth -j-1 , i));
                mirrored.set(breadth -j-1 ,i, color);
            }
        }

        return mirrored;
    }

    /**
     * <p>Returns the negative version of an instance.<br />
     * If the colour of a pixel is (r, g, b) then the colours of the same pixel
     * in the negative of the picture are (255-r, 255-g, 255-b).</p>
     *
     * @return the negative of the instance.
     */
    public Picture negative() {
        Picture negative = new Picture(picture);

        for (int i = 0; i < length; i++) {

            for (int j = 0; j < breadth; j++)
            {
                int originalPixel = negative.getRGB(j, i);
                int a = (originalPixel >> 24) & 0xFF;
                int r = (originalPixel >> 16) & 0xFF;
                int g = (originalPixel>> 8) & 0xFF;
                int b = originalPixel & 0xFF;

                //converting to negative
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                //setting values
                originalPixel= (a << 24) | (r << 16) | (g << 8) | b;
                negative.setRGB(j, i, originalPixel);
            }
        }
        return negative;
    }

    /**
     * <p>Returns the posterized version of an instance.<br />
     * For each pixel, each colour is analyzed independently to produce a new picture as follows:
     * <ul>
     * <li>if the value of the colour is between 0 and 64 (limits inclusive), set it to 32;</li>
     * <li>if the value of the colour is between 65 and 128, set it to 96;</li>
     * <li>if the value of the colour is between 129 and 255, set it to 222.</li>
     * </ul>
     * </p>
     *
     * @return the posterized version of the instance.
     */
    public Picture posterize() {
        int col;
        int r, g, b;
        int desiredColor;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                col = picture.getRGB(j, i);
                r = (col >> 16) & 0xFF;
                g = (col >> 8) & 0xFF;
                b = (col) & 0xFF;

                r = posterizeColorSetter(r);
                g = posterizeColorSetter(g);
                b = posterizeColorSetter(b);

                desiredColor = (r <<16) | (g << 8) | b;
                picture.setRGB(j, i, desiredColor);

            }
        }

        return picture;
    }

    /**
     * Clip the picture given a rectangle that represents the region to be retained.
     *
     * @param clippingBox is not null.
     * @return a clipped version of the instance.
     * @throws PictureProcessingException if the clippingBox does not fit completely
     *                                  within the picture.
     */
    public Picture clip(Quadrilateral  clippingBox) throws PictureProcessingException{
        int breadth = clippingBox.xBottomRight - clippingBox.xTopLeft + 1;
        int length = clippingBox.yBottomRight - clippingBox.yTopLeft + 1;
        if (breadth > this.breadth || length > this.length){
            throw new PictureProcessingException();
        }
        Picture clippedPicture = new Picture(breadth, length);
        Color color;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                color = picture.get(j + clippingBox.xTopLeft, i + clippingBox.yTopLeft);
                clippedPicture.set(j, i, color);
            }
        }
        return clippedPicture;
    }

    /**
     * Denoise an picture by replacing each pixel by the median value of that pixel and
     * all its neighbouring pixels. During this process, each colour channel is handled
     * separately.
     *
     * @return a denoised version of the instance.
     */
    public Picture denoise() {
        Picture orgPicture = new Picture(picture);
        Picture denoisedPicture = new Picture(breadth,length);

        ArrayList<Integer> ArrayR = new ArrayList<>();
        ArrayList<Integer> ArrayG = new ArrayList<>();
        ArrayList<Integer> ArrayB = new ArrayList<>();

        for (int row = 0; row < length; row++){
            for (int col = 0; col < breadth; col++){
                for (int innerRow = row - 1; innerRow <= row + 1 ; innerRow++){
                    for (int innerCol = col - 1; innerCol <= col + 1; innerCol++){
                        if (innerRow < 0 || innerRow >= length){
                            break;
                        }
                        if (innerCol < 0 || innerCol >= breadth){
                            continue;
                        }
                        int color = orgPicture.getRGB(innerCol,innerRow);

                        ArrayR.add((color >> 16) & 0xFF);
                        ArrayG.add((color >> 8) & 0xFF);
                        ArrayB.add((color) & 0xFF);
                    }
                }

                sortAscending(ArrayR);
                sortAscending(ArrayG);
                sortAscending(ArrayB);

                double medianR = findMedian(ArrayR);
                double medianG = findMedian(ArrayG);
                double medianB = findMedian(ArrayB);

                int rgbColor = (int)(medianR) << 16 | (int)(medianG) << 8 | (int)(medianB);

                denoisedPicture.setRGB(col,row,rgbColor);

                ArrayR.clear();
                ArrayG.clear();
                ArrayB.clear();
            }
        }

        return denoisedPicture;
    }

    /**
     * Returns a weathered version of the picture by replacing each pixel by the minimum value
     * of that pixel and all its neighbouring pixels. During this process, each colour channel
     * is handled separately.
     *
     * @return a weathered version of the picture.
     */
    public Picture weather() {
        int desiredRGB;

        Picture weathered = new Picture(breadth, length);

        for(int row = 0; row < length; row++){
            for(int col = 0; col < breadth; col++){
                desiredRGB = smallestNeighbouringRGB(row,col);
                weathered.setRGB(col, row, desiredRGB);
            }
        }

        return weathered;

    }

    /**
     * Return a box paint version of the instance by treating the picture as a
     * sequence of squares of a given size and replacing all pixels in a square
     * by the average value of all pixels in that square.
     * During this process, each colour channel is handled separately.
     *
     * @param boxSize the dimension of the square box, > 1.
     * @return the box paint version of the instance.
     * When the original picture is not a perfect multiple of boxSize * boxSize,
     * the bottom rows and right columns are obtained by averaging the pixels that
     * fit the smaller rectangular regions. For example, if we have a 642 x 642 size
     * original picture and the box size is 4 x 4 then the bottom two rows will use
     * 2 x 4 boxs, the rightmost two columns will use 4 x 2 boxs, and the
     * bottom-right corner will use a 2 x 2 box.
     */
    public Picture boxPaint(int boxSize) {
        Picture boxPaint = new Picture(picture);
        int color;
        int r=0;
        int g=0;
        int b=0;
        int boxDesired;
        int avgR=0;
        int avgG=0;
        int avgB=0;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                for (int k = i; k <= boxSize; k++) {
                    for (int l = j; l <= boxSize; l++) {

                        color = picture.getRGB(l, k);
                        r += (color >> 16) & 0xFF;
                        g += (color >> 8) & 0xFF;
                        b += (color) & 0xFF;
                    }
                }
                avgR=r/(boxSize*boxSize);
                avgG=g/(boxSize*boxSize);
                avgB = b/(boxSize*boxSize);
                for (int k = i; k <= boxSize; k++) {
                    for (int l = j; l <= boxSize; l++) {

                        if (k < 0 || k >= length) {
                             //adding exceptions

                        }
                        if (l < 0 || l >= breadth) {
                            continue;
                        }
                        boxDesired = (avgR << 16) | (8 << avgG) | (avgB);
                        boxPaint.setRGB(l, k, boxDesired);
                    }

                }
            }
        }


        return boxPaint;
    }

    /**
     * Rotate an picture by the given angle (degrees) about the centre of the picture.
     * The centre of an picture is the pixel at (breadth/2, length/2). The new regions
     * that may be created are given the colour white (<code>#ffffff</code>) with
     * maximum transparency (alpha = 255).
     *
     * @param degrees the angle to rotate the picture by, 0 <= degrees <= 360.
     * @return a rotate version of the instance.
     */
    public Picture rotate(double degrees) {
        int original_breadth = breadth;
        int original_length = length;
        int new_breadth =  (int) (Math.abs(Math.cos(degrees * Math.PI/180)*breadth) +
                               Math.abs(Math.cos((90-degrees) * Math.PI/180)*length));

        int new_length = (int) (Math.abs(Math.sin(degrees * Math.PI/180)*breadth) +
                                Math.abs(Math.sin((90-degrees) * Math.PI/180)*length));

        int startPosCol = new_breadth/2-breadth/2;
        int startPosRow = new_length/2-length/2;

        Picture outPicture = new Picture(new_breadth,new_length);
        for(int i = 0; i < outPicture.length(); i++){
            for(int j = 0; j < outPicture.breadth(); j++){
                outPicture.set(j,i,Color.WHITE);
            }
        }
        for (int col = 0; col < new_breadth; col++) {
            for (int row = 0; row < new_length; row++) {
                int original_x = (int) ((col - breadth / 2 -startPosCol) * Math.cos(degrees * Math.PI / 180) +
                    (row - length / 2 -startPosRow) * Math.sin(degrees * Math.PI / 180) + original_breadth / 2);
                int original_y = (int) (-(col - breadth / 2 - startPosCol) * Math.sin(degrees * Math.PI / 180) +
                    (row - length / 2 - startPosRow) * Math.cos(degrees * Math.PI / 180) + original_length / 2);
                if (original_x >= 0 && original_y >= 0 &&
                    original_x < original_breadth &&
                    original_y < original_length) {
                    outPicture.set(col, row, picture.get(original_x, original_y));
                }
            }
        }
        return outPicture;
    }

    /**
     * Compute the discrete Fourier transform of the picture and return the
     * amplitude and phase matrices as a DFTOutput instance.
     *
     * @return the amplitude and phase of the DFT of the instance.
     */
    public DFTOutput dft() {
        Picture greyImg = grayscale();
        double sumReal = 0.0;
        double sumImaginary = 0.0;
        double[][] magnitude  = new double[breadth][length];
        double[][] phase = new double[breadth][length];

        for(int u = 0; u < breadth; u++){
            for(int v = 0; v < length; v++){
                for (int x = 0; x < breadth; x++){
                    for (int y = 0; y < length; y++){
                        sumReal += Math.cos(2*Math.PI*((u*x)/(double)breadth + (v*y)/(double)length)) * greyImg.get(y,x).getBlue();
                        sumImaginary += Math.sin(2*Math.PI * ((u*x)/(double)breadth + (v*y)/(double)length)) * greyImg.get(y,x).getBlue();
                    }
                }
                magnitude[u][v] = Math.sqrt(Math.pow(sumReal,2) + Math.pow(sumImaginary,2));
                phase[u][v] = Math.atan(sumImaginary/sumReal);
            }
        }


        return new DFTOutput(magnitude,phase);
    }

    /**
     * Replaces a background screen with a provided picture.
     * <p>
     * This operation identifies the largest connected region of the picture that matches
     * <code>screenColour</code> exactly. This operation determines a rectangle that bounds
     * the "green screen" region and overlays the <code>backgroundPicture</code> over that
     * rectangle by aligning the top-left corner of the picture with the top-left corner of the
     * rectangle. After determining the screen region, all pixels in that region matching
     * <code>screenColour</code> are replaced with corresponding pixels from
     * <code>backgroundPicture</code>.
     * <p>
     * If <code>backgroundPicture</code> is smaller
     * than the screen then the picture is tiled over the screen.
     *
     * @param screenColour    the colour of the background screen, is not null
     * @param backgroundPicture the picture to replace the screen with, is not null
     * @return an picture with provided picture replacing the background screen
     * of the specified colour, tiling the screen with the background picture if the
     * background picture is smaller than the screen size.
     */
    public Picture greenScreen(Color screenColour, Picture backgroundPicture) {
        Picture outGreenScreen = new Picture(picture);
        int sC = screenColour.getRGB();
        int breadthMin = breadth-1;
        int lengthMin = length-1;
        int breadthMax = 0;
        int lengthMax = 0;
        int count = 0;
        HashMap largestConnected = new HashMap();
        HashMap currentConnected = new HashMap();
        for(int row = 0; row < length; row++){
            for(int col = 0; col < breadth; col++){
                if(outGreenScreen.getRGB(col,row) == sC){
                    if(! largestConnected.containsKey(row + ""+col)){
                        currentConnected = connectedRegion(row,col,sC);
                        if(currentConnected.size() > largestConnected.size()){
                            largestConnected = new HashMap(currentConnected);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < length; i++){
            for(int j = 0; j < breadth; j++){
                if(largestConnected.containsKey(i+""+j)){
                    if(count == 0){
                        lengthMin = i;
                        breadthMin = j;
                    }
                    if(count == largestConnected.size()-1){
                        lengthMax = i;
                        breadthMax = j;
                    }
                    count++;
                }
            }
        }
        return outGreenScreen;
    }

    /**
     * Align (appropriately rotate) an picture of text that was improperly aligned.
     * This transformation can work properly only with text pictures.
     *
     * @return the aligned picture.
     */
    public Picture alignTextPicture() {
        // TODO: Implement this method
        return null;
    }

    private void sortAscending(ArrayList<Integer> arr){
        for (int i = 0; i < arr.size(); i++){
            for (int j = arr.size() -1; j > i; j--){
                if (arr.get(i) > arr.get(j)){
                    int temp = arr.get(j);
                    arr.set(j,arr.get(i));
                    arr.set(i,temp);
                }
            }
        }
    }

    private double findMedian(ArrayList<Integer> arr){
        double median;
        if (arr.size() % 2 == 0){
            median = (arr.get(arr.size()/2) + arr.get(arr.size()/2-1))/2;
        }else {
            median = arr.get(arr.size()/2);
        }
        return median;
    }

    /**
     * Finds the smallest value of R, G, and B in an picture
     * from a set of neighbouring pixels (including itself)
     * @param (row,col) index of pixel in the picture
     * @return RGB value representing the smallest value of R, G, and B in neighbours
     */
    private int smallestNeighbouringRGB(int row, int col){
        int minR = 255;
        int minG = 255;
        int minB = 255;
        int r,g,b;
        int color;
        int smallestRGB;

        for(int neighbourRow = row-1; neighbourRow <= row+1; neighbourRow++){
            for(int neighbourCol = col-1; neighbourCol <= col+1; neighbourCol++){
                if(neighbourRow < 0 || neighbourRow >= length){
                    break;
                }
                if(neighbourCol < 0 || neighbourCol >= breadth){
                    continue;
                }
                color = picture.getRGB(neighbourCol, neighbourRow);
                r = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                b = (color) & 0xFF;
                if(r < minR){
                    minR = r;
                }
                if(g < minG){
                    minG = g;
                }
                if(b < minB){
                    minB = b;
                }
            }
        }
        smallestRGB = (minR << 16) | (minG << 8) | minB;
        return smallestRGB;
    }

    /**
     * Takes a color channel (rgb) and gives the value as specified in posterize method
     *
     * @param colorChannel color channel value ranging from 0-255
     * @return posterized version of color value as specified in posterize method
     */
    private int posterizeColorSetter(int colorChannel) {
        if (colorChannel <= 64) {
            return 32;
        } else if (colorChannel <= 128) {
            return 96;
        } else {
            return 222;
        }

    }

    /**
     *Finds the largest connected (neighbouring pixels) region by color values
     *starting from given index
     * @param row index of the starting row pixel
     * @param col index of the starting column pixel
     * @param RGB the color value that creates the connecting region
     * @return a HashMap which contains the indexes of connecting region as the keys and
     * the keys point to the input color value
     * returns an empty Hashmap if RGB value doesn't exist at given starting index
     */
    private HashMap connectedRegion(int row, int col, int RGB) {
        HashMap connected = new HashMap();

        if (picture.getRGB(col, row) != RGB) {
            return connected;
        }else{
            connected.put(row+""+col, picture.getRGB(col,row));
        }

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i < 0 || i >= length) {
                    break;
                }
                if (j < 0 || j >= breadth ||
                    i == row && j == col ||
                    connected.containsKey(i + "" + j)) {
                    continue;
                }
                if (picture.getRGB(j, i) == RGB) {
                    connected.put(i + "" + j, picture.getRGB(j, i));
                    row = i;
                    col = j;
                    i = row-1;
                    break;
                }
            }
        }
        return connected;
    }
}
