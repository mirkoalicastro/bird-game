package com.badlogic.androidgames.framework.impl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AndroidGraphics implements Graphics {
    private final AssetManager assets;
    private final Bitmap frameBuffer;
    private final Canvas canvas;
    private final Paint paint = new Paint();
    private final Rect srcRect = new Rect();
    private final Rect dstRect = new Rect();
    private final Options options = new Options();
    private final RectF rectF = new RectF();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        canvas = new Canvas(frameBuffer);
        paint.setAntiAlias(true);
    }

    @Override
    public Pixmap[] newPixmapsFromFolder(String path, PixmapFormat format) {
        Pixmap[] ret;
        try {
            String[] files = assets.list(path);
            if(files.length == 0)
                return null;
            Arrays.sort(files);
            ret = new Pixmap[files.length];
            for(int i=0; i<files.length; i++)
                ret[i] = newPixmap(path + "/" + files[i], format);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from path '"+ path + "'");
        }
        return ret;
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in,null,options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"+ fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"+ fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // nothing
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, int color) {
        paint.setColor(color);
        rectF.left = left;
        rectF.top = top;
        rectF.right  = right;
        rectF.bottom = bottom;

        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawRectBorder(int x, int y, int width, int height, int strokeWidth, int color) {
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }


    @Override
    public void drawPixmap(Pixmap pixmap, int dstX, int dstY, int dstWidth, int dstHeight, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = dstX;
        dstRect.top = dstY;
        dstRect.right = dstX + dstWidth - 1;
        dstRect.bottom = dstY + dstHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int dstX, int dstY, int dstWidth, int dstHeight) {
        AndroidPixmap tmp = (AndroidPixmap) pixmap;
        drawPixmap(pixmap, dstX, dstY, dstWidth, dstHeight, 0,0,tmp.getWidth(),tmp.getHeight());
    }

    @Override
    public void drawCircle(int x, int y, float radius, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(x,y,radius,paint);
    }

    @Override
    public void drawCircleBorder(int x, int y, float radius, int strokeWidth, int color) {
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(x,y,radius,paint);
    }

    @Override
    public void drawEffect(Effect effect, int x, int y, int width, int height) {
        ((AndroidEffect)effect).apply(canvas,paint,x,y,width,height);
    }

    @Override
    public void drawText(String text, int x, int y, int fontSize, int color) {
        paint.setTextSize(fontSize);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, float degrees) {
        canvas.save();
        canvas.rotate(degrees,x+(pixmap.getWidth()/2),y+(pixmap.getHeight()/2));
        drawPixmap(pixmap, x, y);
        canvas.restore();
    }

}
