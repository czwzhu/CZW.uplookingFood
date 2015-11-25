package app.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;
/**
 * Created by czw on 2015/11/6.
 */

public class SlidingMenu extends FrameLayout
{
    private FrameLayout mMenuContainer;
    private FrameLayout mMainContainer;
    public static final int MENU_CONTAINER_ID = 1;
    public static final int MAIN_CONTAINER_ID = 2;
    private int mViewWidth;
    private int mMenuWidth;
    private float mMenuRatio = 0.75F;

    private float mMenuMinScale = 0.7F;

    private float mMainMinScale = 0.8F;

    private float mMenuPadingRatio = 0.2F;
    private Scroller mScroller;
    private int mTouchSlop;
    private static final int STATE_MENU_CLOSE = 1;
    private static final int STATE_SLIDING = 2;
    private static final int STATE_MENU_OPEN = 3;
    private int mCurState = 1;
    private float mFirstEventX;
    private float mLastEventX;
    private float mMaxMoveX;
    private boolean intercept = true;

    public SlidingMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public SlidingMenu(Context context) {
        super(context);
        init();
    }
    private void init() {
        this.mMenuContainer = new FrameLayout(getContext());
        @android.support.annotation.IdRes int id =1;
        @android.support.annotation.IdRes int id1 =2;
        this.mMenuContainer.setId(id);
        this.mMainContainer = new FrameLayout(getContext());
        this.mMainContainer.setId(id1);
        addView(this.mMenuContainer);
        addView(this.mMainContainer);

        this.mScroller = new Scroller(getContext());
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case 0:
                this.mFirstEventX = ev.getX();
                if ((this.mCurState == 2) &&
                        (!this.mScroller.isFinished())) {
                    this.mScroller.forceFinished(true);
                }

                if ((this.mCurState == 3) && (ev.getX() >= this.mMenuWidth)) {
                    return true;
                }

                if ((this.mCurState == 2) && (ev.getX() > -this.mMainContainer.getScrollX() * this.mMenuWidth / ((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth)))
                {
                    return true;
                }
                break;
            case 2:
                if (!this.intercept) {
                    return false;
                }
                if (Math.abs(ev.getX() - this.mFirstEventX) >= this.mTouchSlop) {
                    this.mLastEventX = ev.getX();
                    return true;
                }
                break;
            case 1:
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case 0:
                this.mLastEventX = event.getX();
                break;
            case 2:
                float offset = event.getX() - this.mLastEventX;
                int mainScrollX = (int)(this.mMainContainer.getScrollX() - offset);
                handleScroll(mainScrollX);
                this.mLastEventX = event.getX();

                float moveX = Math.abs(event.getX() - this.mFirstEventX);
                this.mMaxMoveX = Math.max(this.mMaxMoveX, moveX);
                break;
            case 1:
                if ((this.mMaxMoveX < this.mTouchSlop) && (event.getX() >= this.mMenuWidth)) {
                    closeMenu();
                    this.mMaxMoveX = 0.0F;
                }
                else {
                    this.mMaxMoveX = 0.0F;
                    if (ViewHelper.getScrollX(this.mMainContainer) != -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth))
                    {
                        if (ViewHelper.getScrollX(this.mMainContainer) >= -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth) / 2)
                            closeMenu();
                        else
                            openMenu();
                    }
                }
                break;
        }
        return true;
    }

    private void handleScroll(int mainScrollX)
    {
        if (mainScrollX >= 0) {
            mainScrollX = 0;
        }
        if (mainScrollX <= -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth)) {
            mainScrollX = -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth);
        }
        ViewHelper.setScrollX(this.mMainContainer, mainScrollX);

        ViewHelper.setPivotX(this.mMainContainer, this.mMainContainer.getWidth());
        ViewHelper.setPivotY(this.mMainContainer, this.mMainContainer.getHeight() / 2);
        ViewHelper.setScaleX(this.mMainContainer, 1.0F - (1.0F - this.mMainMinScale) * mainScrollX / -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth));
        ViewHelper.setScaleY(this.mMainContainer, 1.0F - (1.0F - this.mMainMinScale) * mainScrollX / -(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth));

        float menuScrollX = this.mMenuWidth * this.mMenuPadingRatio - this.mMenuWidth * mainScrollX / (1.0F / this.mMenuPadingRatio * -(1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth);
        ViewHelper.setScrollX(this.mMenuContainer, (int)menuScrollX);
        ViewHelper.setPivotX(this.mMenuContainer, 0.0F);
        ViewHelper.setPivotY(this.mMenuContainer, this.mMenuContainer.getHeight());
        ViewHelper.setScaleX(this.mMenuContainer, this.mMenuMinScale - mainScrollX * (1.0F - this.mMenuMinScale) / ((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth));
        ViewHelper.setScaleY(this.mMenuContainer, this.mMenuMinScale - mainScrollX * (1.0F - this.mMenuMinScale) / ((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mViewWidth == 0) {
            this.mViewWidth = getWidth();
            this.mMenuWidth = ((int)(this.mViewWidth * this.mMenuRatio));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.mMenuWidth, -1);
            this.mMenuContainer.setLayoutParams(params);
        }
    }

    public void computeScroll()
    {
        if (!this.mScroller.isFinished()) {
            if (this.mScroller.computeScrollOffset()) {
                int mainScrollX = this.mScroller.getCurrX();
                handleScroll(mainScrollX);
                invalidate();
            }
        }
        else if (this.mCurState == 2)
            if (this.mMainContainer.getScrollX() == 0)
                this.mCurState = 1;
            else
                this.mCurState = 3;
    }

    public void smoothScrollTo(int scrollX)
    {
        this.mCurState = 2;
        int startX = this.mMainContainer.getScrollX();
        int dx = scrollX - startX;
        int duration = 400;
        this.mScroller.startScroll(startX, 0, dx, 0, duration);
        invalidate();
    }

    public void openMenu()
    {
        smoothScrollTo(-(int)((1.0F - (1.0F - this.mMenuRatio) / this.mMainMinScale) * this.mViewWidth));
    }

    public void closeMenu()
    {
        smoothScrollTo(0);
    }
    public SlidingMenu setMenuRatio(float mMenuRatio) {
        this.mMenuRatio = mMenuRatio;
        return this;
    }
    public SlidingMenu setMenuMinScale(float mMenuMinScale) {
        this.mMenuMinScale = mMenuMinScale;
        return this;
    }
    public SlidingMenu setMainMinScale(float mMainMinScale) {
        this.mMainMinScale = mMainMinScale;
        return this;
    }
    public SlidingMenu setMenuPadingRatio(float mMenuPadingRatio) {
        this.mMenuPadingRatio = mMenuPadingRatio;
        return this;
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }

    public boolean menuIsOpen()
    {
        return this.mCurState == 3;
    }
}