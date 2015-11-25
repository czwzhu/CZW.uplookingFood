package app.uplookingfood.app.uplookingfood.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.tools.DensityUtil;

public class IndicatorView extends View{
    /**普通圆的颜色**/
    private  int normalIndicatorColor = 0xffffffff;
    /**滑动的圆的颜色**/
    private  int slidingIndictorColor = 0xffff0000;
    private Integer circleWidth = 8;
    private Integer circleMargin = 8;
    private Integer topMargin = 6;
    private Integer bottomMargin = 6;
    private int indicatorsCount;
    private Paint paint;
    //如果getDimension()返回结果是20.5f，那么getDimensionPixelSize()
    //返回结果就是21，getDimensionPixelOffset()返回结果就是20。
    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        normalIndicatorColor = typedArray.getColor(R.styleable.IndicatorView_normalCircleColor,
                normalIndicatorColor);
        slidingIndictorColor = typedArray.getColor(R.styleable.IndicatorView_slideCircleColor,
                slidingIndictorColor);
        topMargin = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_topMargin,
                getPx(topMargin));
        bottomMargin =  typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_bottomMargin,
                getPx(bottomMargin));
        circleWidth = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_circleWidth,
                getPx(circleWidth));
        circleMargin = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_circleMargin,
                getPx(circleMargin));
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
    }
    private int getPx(int dimension){
        return DensityUtil.dip2px(getContext(), dimension);
    }
    /**
     * 设置指示器的个数
     * @param indicatorsCount
     */
    public void setIndicatorsCount(int indicatorsCount) {
        this.indicatorsCount = indicatorsCount;
        LayoutParams params = getLayoutParams();
        int height = topMargin+bottomMargin+circleWidth;
        int width = indicatorsCount*(circleWidth+circleMargin);
        if(params == null){
            params = new LayoutParams(width, height);
        }else{
            params.width = width;
            params.height = height;
        }
        setLayoutParams(params);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(normalIndicatorColor);
        for (int i = 0; i < indicatorsCount; i++) {
            canvas.drawCircle(circleWidth/2+i*(circleMargin+circleWidth), topMargin+circleWidth/2, circleWidth/2, paint);
        }
        paint.setColor(slidingIndictorColor);
        canvas.drawCircle(slidingCircleX+circleWidth/2, circleWidth/2+topMargin, circleWidth/2, paint);
        if(slidingCircleX > getWidth() - circleWidth){
            canvas.drawCircle(slidingCircleX+circleWidth/2-getWidth(), circleWidth/2+topMargin, circleWidth/2, paint);
        }
    }
    /**滑动的圆的x起点坐标**/
    private float slidingCircleX;
    public void move(int scrollX,int pagerWidth){
        slidingCircleX = (scrollX*1f*(circleWidth+circleMargin)/pagerWidth)%getWidth();
        if(slidingCircleX < 0 ){
            slidingCircleX += getWidth();
        }
        invalidate();
    }
}

