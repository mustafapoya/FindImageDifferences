package net.golbarg.findimagedifferences.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class UtilController {

    public static Bitmap differVerticalImage(Bitmap inputImage) {
        Bitmap image = Bitmap.createBitmap(inputImage);

        Bitmap test = image.copy(image.getConfig(), true);
        test.setPremultiplied(true);

        int barrier = 0;
        barrier = image.getHeight()/2;
        barrier += 3; //This line needs to be fine tuned based on the image barrier barrier.

        int pixel1;
        int red1;
        int green1;
        int blue1;
        int pixel2;
        int red2;
        int green2;
        int blue2;

        for (int k = 0; k < image.getHeight() - (barrier); k++) {
            for (int i = 0; i < image.getWidth(); i++) {
                pixel1 = image.getPixel(i,k);
                red1 = (pixel1 >> 16) & 0xff;
                green1 = (pixel1 >> 8) & 0xff;
                blue1 = (pixel1) & 0xff;

                pixel2 = image.getPixel(i,k+(barrier));
                red2 = (pixel2 >> 16) & 0xff;
                green2 = (pixel2 >> 8) & 0xff;
                blue2 = (pixel2) & 0xff;

                //Because not all pixels are the same in the two images, you have to
                //set up a "range" of values that it will approve.
                if (red1 <= red2 && red1 + 35 >= red2){ //Playing with the range can produce interesting images
                    test.setPixel(i, k, Color.WHITE);
                } else if (red1 >= red2 && red2 + 35 >= red1){
                    test.setPixel(i, k, Color.WHITE);
                } if (green1 <= green2 && green1 + 35 >= green2){
                    test.setPixel(i, k, Color.WHITE);
                }
                else if (green1 >= green2 && green2 + 35 >= green1){
                    test.setPixel(i, k, Color.WHITE);
                }
                //
                if (blue1 <= blue2 && blue1 + 35 >= blue2){
                    test.setPixel(i, k, Color.WHITE);
                }
                else if (blue1 >= blue2 && blue2 + 35 >= blue1){
                    test.setPixel(i, k, Color.WHITE);
                }
            }
        }
        return test;
    }

    public static Bitmap differHorizontalImage(Bitmap inputImage) {
        Bitmap image = Bitmap.createBitmap(inputImage);

        Bitmap test = image.copy(image.getConfig(), true);
        test.setPremultiplied(true);

        int barrier = 0;
        barrier = image.getWidth()/2;
        barrier += 3; //This line needs to be fine tuned based on the image barrier barrier.

        int pixel1;
        int red1;
        int green1;
        int blue1;
        int pixel2;
        int red2;
        int green2;
        int blue2;

        for (int k = 0; k < image.getHeight(); k++) {
            for (int i = 0; i < image.getWidth() - (barrier); i++) {
                pixel1 = image.getPixel(i,k);
                red1 = (pixel1 >> 16) & 0xff;
                green1 = (pixel1 >> 8) & 0xff;
                blue1 = (pixel1) & 0xff;

                pixel2 = image.getPixel(i+(barrier),k);
                red2 = (pixel2 >> 16) & 0xff;
                green2 = (pixel2 >> 8) & 0xff;
                blue2 = (pixel2) & 0xff;

                //Because not all pixels are the same in the two images, you have to
                //set up a "range" of values that it will approve.
                if (red1 <= red2 && red1 + 35 >= red2){ //Playing with the range can produce interesting images
                    test.setPixel(i, k, Color.WHITE);
                } else if (red1 >= red2 && red2 + 35 >= red1){
                    test.setPixel(i, k, Color.WHITE);
                } if (green1 <= green2 && green1 + 35 >= green2){
                    test.setPixel(i, k, Color.WHITE);
                }
                else if (green1 >= green2 && green2 + 35 >= green1){
                    test.setPixel(i, k, Color.WHITE);
                }
                //
                if (blue1 <= blue2 && blue1 + 35 >= blue2){
                    test.setPixel(i, k, Color.WHITE);
                }
                else if (blue1 >= blue2 && blue2 + 35 >= blue1){
                    test.setPixel(i, k, Color.WHITE);
                }
            }
        }
        return test;
    }

    public static String appLink() {
        return "https://play.google.com/store/apps/details?id=net.golbarg.findimagedifferences";
    }

    public static void showSnackMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        snackbar.show();
    }

}
