package com.badlogic.androidgames.framework;

public interface Graphics {
    enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Pixmap[] newPixmapsFromFolder(String path, PixmapFormat format);

    Pixmap newPixmap(String fileName, PixmapFormat format);

    void clear(int color);

    void drawPixel(int x, int y, int color);

    void drawLine(int x, int y, int x2, int y2, int color);

    void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, int color);

    void drawRect(int x, int y, int width, int height, int color);

    void drawRectBorder(int x, int y, int width, int height, int strokeWidth, int color);

    void drawPixmap(Pixmap pixmap, int dstX, int dstY, int dstWidth, int dstHeight, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawPixmap(Pixmap pixmap, int x, int y);

    void drawPixmap(Pixmap pixmap, int dstX, int dstY, int dstWidth, int dstHeight);

    void drawCircle(int x, int y, float radius, int color);

    void drawCircleBorder(int x, int y, float radius, int strokeWidth, int color);

    void drawEffect(Effect effect, int x, int y, int width, int height);

    void drawText(String text, int x, int y, int fontSize, int color);

    int getWidth();

    int getHeight();

    void drawPixmap(Pixmap pixmap, int x, int y, float degrees);
}
