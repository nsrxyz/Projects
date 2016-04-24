package ru.nsrxyz.somegis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

    public interface OnColorChangedListener {
        void colorChanged(String key, int color);
    }

    private OnColorChangedListener mListener;
    private int mInitialColor, mDefaultColor;
    private String mKey;

    private class ColorPickerView extends View {
        private Paint mPaint;
        private float mCurrentHue = 0;
        private int mCurrentX = 0, mCurrentY = 0;
        private int mCurrentColor, mDefaultColor;
        private final int[] mHueBarColors = new int[258];
        private int[] mMainColors = new int[65536];
        private OnColorChangedListener mListener;
        private int width,height;

        ColorPickerView(Context c, OnColorChangedListener l, int color,
                        int defaultColor) {
            super(c);
            mListener = l;
            mDefaultColor = defaultColor;

            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            mCurrentHue = hsv[0];
            updateMainColors();

            mCurrentColor = color;

            int index = 0;
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb(255, 0, (int) i);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb(0, (int) i, 255);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb((int) i, 255, 0);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42)
            {
                mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
                index++;
            }

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(12);
        }

        private int getCurrentMainColor() {
            int translatedHue = 255 - (int) (mCurrentHue * 255 / 360);
            int index = 0;
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb(255, 0, (int) i);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb(255 - (int) i, 0, 255);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb(0, (int) i, 255);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb(0, 255, 255 - (int) i);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb((int) i, 255, 0);
                index++;
            }
            for (float i = 0; i < 256; i += 256 / 42) {
                if (index == translatedHue)
                    return Color.rgb(255, 255 - (int) i, 0);
                index++;
            }
            return Color.RED;
        }

        private void updateMainColors() {
            int mainColor = getCurrentMainColor();
            int index = 0;
            int[] topColors = new int[256];
            for (int y = 0; y < 256; y++) {
                for (int x = 0; x < 256; x++) {
                    if (y == 0) {
                        mMainColors[index] = Color.rgb(
                                255 - (255 - Color.red(mainColor)) * x / 255,
                                255 - (255 - Color.green(mainColor)) * x / 255,
                                255 - (255 - Color.blue(mainColor)) * x / 255);
                        topColors[x] = mMainColors[index];
                    } else
                        mMainColors[index] = Color.rgb(
                                (255 - y) * Color.red(topColors[x]) / 255,
                                (255 - y) * Color.green(topColors[x]) / 255,
                                (255 - y) * Color.blue(topColors[x]) / 255);
                    index++;
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int translatedHue = 255 - (int) (mCurrentHue * 255 / 360);
            for (int x = 0; x < 256; x++) {
                if (translatedHue != x) {
                    mPaint.setColor(mHueBarColors[x]);
                    mPaint.setStrokeWidth(1);
                } else
                {
                    mPaint.setColor(Color.BLACK);
                    mPaint.setStrokeWidth(3);
                }
                canvas.drawLine(x + 10, 0, x + 10, 40, mPaint);
            }
            for (int x = 0; x < 256; x++) {
                int[] colors = new int[2];
                colors[0] = mMainColors[x];
                colors[1] = Color.BLACK;
                Shader shader = new LinearGradient(0, 50, 0, 306, colors, null,
                        Shader.TileMode.REPEAT);
                mPaint.setShader(shader);
                canvas.drawLine(x + 10, 50, x + 10, 306, mPaint);
            }
            mPaint.setShader(null);

            if (mCurrentX != 0 && mCurrentY != 0) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.BLACK);
                canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
            }

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mCurrentColor);
            if (Color.red(mCurrentColor) + Color.green(mCurrentColor)
                    + Color.blue(mCurrentColor) < 384)
                mPaint.setColor(Color.WHITE);
            else
                mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mDefaultColor);
            if (Color.red(mDefaultColor) + Color.green(mDefaultColor)
                    + Color.blue(mDefaultColor) < 384)
                mPaint.setColor(Color.WHITE);
            else
                mPaint.setColor(Color.BLACK);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(276,366 );//276,366
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return true;
            float x = event.getX();
            float y = event.getY();

            if (x > 10 && x < 266 && y > 0 && y < 40) {
                mCurrentHue = (255 - x) * 360 / 255;
                updateMainColors();

                int transX = mCurrentX - 10;
                int transY = mCurrentY - 60;
                int index = 256 * (transY - 1) + transX;
                if (index > 0 && index < mMainColors.length)
                    mCurrentColor = mMainColors[256 * (transY - 1) + transX];

                invalidate();
            }

            if (x > 10 && x < 266 && y > 50 && y < 306) {
                mCurrentX = (int) x;
                mCurrentY = (int) y;
                int transX = mCurrentX - 10;
                int transY = mCurrentY - 60;
                int index = 256 * (transY - 1) + transX;
                if (index > 0 && index < mMainColors.length) {
                    mCurrentColor = mMainColors[index];
                    mListener.colorChanged("", mCurrentColor);
                    invalidate();
                }
            }

            return true;
        }
    }

    public ColorPickerDialog(Context context, OnColorChangedListener listener,
                             String key, int initialColor, int defaultColor) {
        super(context);

        mListener = listener;
        mKey = key;
        mInitialColor = initialColor;
        mDefaultColor = defaultColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(String key, int color) {
                mListener.colorChanged(mKey, color);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, mInitialColor,
                mDefaultColor));
        setTitle("Pick Text Color");

    }
}