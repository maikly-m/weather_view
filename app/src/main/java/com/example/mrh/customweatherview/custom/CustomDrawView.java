package com.example.mrh.customweatherview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.mrh.customweatherview.R;
import com.example.mrh.customweatherview.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2017/3/12 0012.
 */

public class CustomDrawView extends View{
    private static final String TAG = "CustomDrawView";

    private int mDimensionPixelSize;
    private List<Integer> temps;
    private Paint mLinePaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private int circle_radiu;
    private ArrayList<Integer> mY_List;
    private int mHeight;
    private int mLineHeight;
    private ArrayList<Integer> mX_List;
    private Path mPath;
    private float aBitmapWidth;
    private float aBitmapHeigth;
    private int mDistance;

    public CustomDrawView (Context context) {
        this(context, null);
    }

    public CustomDrawView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDrawView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .CustomDrawView,
                defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++){
            int attr = typedArray.getIndex(i);
            switch (attr)
            {
            case R.styleable.CustomDrawView_eachWidth:
                mDimensionPixelSize = typedArray.getDimensionPixelSize(attr, TypedValue.TYPE_NULL);
                break;

            }
        }
        typedArray.recycle();
        init();
    }

    private void init () {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(Utils.dip2px(getContext(), 5));
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setColor(Color.RED);


    }

    public void setData(List<Integer> temps){
        this.temps = temps;
        setData_();
        requestLayout();
    }

    private void setData_ () {
        if (mX_List != null){
            mX_List.clear();
        }
        if (mY_List != null){
            mY_List.clear();
        }

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        float scale = mBitmap.getWidth() / (mDimensionPixelSize * 0.6f);
        mMatrix = new Matrix();
        mMatrix.setScale(scale, scale);

        circle_radiu = Utils.dip2px(getContext(), 8);
        mLineHeight = Utils.dip2px(getContext(), 60);
        mHeight = Utils.dip2px(getContext(), 80);
        mY_List = new ArrayList<>();
        mX_List = new ArrayList<>();
        int maxT = temps.get(0);
        int minT = temps.get(0);
        for (int i = 1; i < temps.size(); i++){
            if (maxT < temps.get(i)){
                maxT = temps.get(i);
            }
            if (minT > temps.get(i)){
                minT = temps.get(i);
            }
        }
        float mid = maxT - minT;
        //获取Line y轴位置
        int h;
        for (int i = 0; i < temps.size(); i++){
            h = (int) (mHeight + circle_radiu * 2 + (temps.get(i) - minT) / mid * mLineHeight);
            mY_List.add(h);
        }
        //获取Line x轴位置
        int x;
        for (int i = 0; i < temps.size(); i++){
            x = (int) (mDimensionPixelSize * (i + 0.5f));
            mX_List.add(x);
        }

        //确定Line 路径
        mPath = new Path();
        mPath.moveTo(mX_List.get(0), mY_List.get(0));
        for (int i = 1; i < temps.size(); i++){
            mPath.lineTo(mX_List.get(i), mY_List.get(i));
        }
        //bitmap 变换后的width和height
        aBitmapWidth = mBitmap.getWidth() * scale;
        aBitmapHeigth = mBitmap.getHeight() * scale;

        //bitmap 和Line的圆点距离
        mDistance = Utils.dip2px(getContext(), 15);

    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else{
            if (temps != null){
                width = mDimensionPixelSize * temps.size();
            }else {
                width = 0;
            }
        }
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            throw new IllegalArgumentException("需要设置高度");
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if (temps != null){
            //先画曲线
            canvas.drawPath(mPath, mLinePaint);
            for (int i = 0; i < temps.size(); i++){
                canvas.drawCircle(mX_List.get(i), mY_List.get(i), circle_radiu, mBitmapPaint);
            }
            //画bitmap
            float preHeigth = -1f;
            canvas.save();
            for (int i = 0; i < temps.size(); i++){
                if (i == 0){
                    canvas.translate(mX_List.get(i)-aBitmapWidth/2, mY_List.get(i)
                            - mDistance - aBitmapHeigth);
                } else{
                    float dif = mY_List.get(i) - preHeigth;
                    canvas.translate(mDimensionPixelSize, dif);
                }
                preHeigth = mY_List.get(i);
                canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
            }
            canvas.restore();
//        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
        }

    }
}
