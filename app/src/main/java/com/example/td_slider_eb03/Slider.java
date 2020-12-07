package com.example.td_slider_eb03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Slider extends View {

    //Valeur du customView
    private float mValue;

    //attributs devaleur
    private float mMin=0;
    private float mMax=100;

    //attributs d'activation
    private boolean mEnabled =true;

    //
    private final static float MIN_BAR_LENGTH = 160;
    private final static float MIN_CURSOR_DIAMETER = 30;

    //dimention par defaut
    private final static float DEFAULT_BAR_LENGTH = 160;
    private final static float DEFAULT_CURSOR_DIAMETER = 40;
    private final static float DEFAULT_BAR_WIDTH = 10;

    //attributs de dimentions en pixel
    private float mBarLength;
    private float mBarWidth;
    private float mCursorDiameter;

    //attributs de couleur
    private int mValueColor;
    private int mbarColor;
    private int mCursorColor;
    private int mDisableColor;

    //attributs de pinceaux
    private Paint mCursorPaint ;
    private Paint mValueBarPaint ;
    private Paint mBarPaint ;

    private float dpToPixel(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    public Slider(Context context) {
        super(context);
        init(context,null);
    }

    public Slider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }


    private void init(Context context,@Nullable AttributeSet attrs){
        mBarPaint= new Paint();
        mCursorPaint= new Paint();
        mValueBarPaint= new Paint();

        mCursorColor = ContextCompat.getColor(getContext(),R.color.design_default_color_primary);
        mbarColor = ContextCompat.getColor(getContext(),R.color.design_default_color_secondary);
        mValueColor =ContextCompat.getColor(getContext(),R.color.black);
        mDisableColor =ContextCompat.getColor(getContext(),R.color.grey);

        if (mEnabled){
            mBarPaint.setColor(mbarColor);
            mValueBarPaint.setColor(mValueColor);
            mCursorPaint.setColor(mCursorColor);
        }else{
            mBarPaint.setColor(mDisableColor);
            mValueBarPaint.setColor(mDisableColor);
            mCursorPaint.setColor(mDisableColor);
        }

        mBarPaint.setStyle(Paint.Style.STROKE);
        mCursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mValueBarPaint.setStyle(Paint.Style.STROKE);

        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mValueBarPaint.setStrokeCap(Paint.Cap.ROUND);

        mBarLength=dpToPixel(DEFAULT_BAR_LENGTH);
        mBarWidth=dpToPixel(DEFAULT_BAR_WIDTH);
        mCursorDiameter=dpToPixel(DEFAULT_CURSOR_DIAMETER);

        mBarPaint.setStrokeWidth(mBarWidth);
        mValueBarPaint.setStrokeWidth(mBarWidth);

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Point p1,p2,pc;
        p1=toPos(mMin);
        p2=toPos(mMax);
        pc=toPos((mValue));

        canvas.drawLine(p1.x,p1.y,p2.x,p2.y,mBarPaint);
        canvas.drawLine(p1.x,p1.y,pc.x,pc.y,mValueBarPaint);
        canvas.drawCircle(pc.x,pc.y,mCursorDiameter/2,mCursorPaint);
    }

    /**
     * transforme la valeur du slider en un ratio entre 0 et 1
     * @param value valeur du slider
     * @return ratio
     */

    private float valueToRatio(float value){
        return (value-mMin)/(mMax-mMin);
    }

    /**
     *transforme le ration en la valeur du slider
     * @param ratio ratio a l'ecran
     * @return valeur du slider
     */
    private float ratioToValue(float ratio){
        return ratio*(mMax-mMin)+mMin;
    }

    private Point toPos(float value){
        int x,y;
        x = (int)(Math.max(mCursorDiameter,mBarWidth)/2+getPaddingLeft());
        y=(int)((1-valueToRatio(value))*mBarLength+getPaddingTop()+mCursorDiameter/2);
        return new Point(x,y);
    }

    private float toValue(Point point){
        float ratio;
        ratio = (1-(point.y-getPaddingTop()-mCursorDiameter/2)/mBarLength);
        if(ratio >1 ) ratio =1;
        if(ratio <0 ) ratio =0;
        return ratioToValue(ratio);
    }

}
