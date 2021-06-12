package com.meembusoft.safewaypharmaonline.util;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ColorPickerManager {

    public static int getPixelColor(ImageView imageView, float x, float y) {
        Point snapPoint = getColorPoint(imageView, new Point((int) x, (int) y));
        return getColorFromBitmap(imageView, snapPoint.x, snapPoint.y);
    }

    /**
     * gets a pixel color on the specific coordinate from the bitmap.
     *
     * @param x coordinate x.
     * @param y coordinate y.
     * @return selected color.
     */
    private static int getColorFromBitmap(ImageView imageView, float x, float y) {
        Matrix invertMatrix = new Matrix();
        imageView.getImageMatrix().invert(invertMatrix);

        float[] mappedPoints = new float[]{x, y};
        invertMatrix.mapPoints(mappedPoints);

        if (imageView.getDrawable() != null
                && imageView.getDrawable() instanceof BitmapDrawable
                && mappedPoints[0] >= 0
                && mappedPoints[1] >= 0
                && mappedPoints[0] < imageView.getDrawable().getIntrinsicWidth()
                && mappedPoints[1] < imageView.getDrawable().getIntrinsicHeight()) {

            Rect rect = imageView.getDrawable().getBounds();
            float scaleX = mappedPoints[0] / rect.width();
            int x1 = (int) (scaleX * ((BitmapDrawable) imageView.getDrawable()).getBitmap().getWidth());
            float scaleY = mappedPoints[1] / rect.height();
            int y1 = (int) (scaleY * ((BitmapDrawable) imageView.getDrawable()).getBitmap().getHeight());
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap().getPixel(x1, y1);
        }
        return 0;
    }

    private static Point getColorPoint(ImageView imageView, Point point) {
        if (getColorFromBitmap(imageView, point.x, point.y) != Color.TRANSPARENT) return point;
        Point center = new Point(imageView.getMeasuredWidth() / 2, imageView.getMeasuredHeight() / 2);
        return approximatedPoint(imageView, point, center);
    }

    private static Point approximatedPoint(ImageView imageView, Point start, Point end) {
        if (getDistance(start, end) <= 3) return end;
        Point center = getCenterPoint(start, end);
        int color = getColorFromBitmap(imageView, center.x, center.y);
        if (color == Color.TRANSPARENT) {
            return approximatedPoint(imageView, center, end);
        } else {
            return approximatedPoint(imageView, start, center);
        }
    }

    private static Point getCenterPoint(Point start, Point end) {
        return new Point((end.x + start.x) / 2, (end.y + start.y) / 2);
    }

    private static int getDistance(Point start, Point end) {
        return (int)
                Math.sqrt(
                        Math.abs(end.x - start.x) * Math.abs(end.x - start.x)
                                + Math.abs(end.y - start.y) * Math.abs(end.y - start.y));
    }
}
