package app.uplookingfood.app.uplookingfood.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.uplookingfood.ContentActivity;
import app.uplookingfood.RecipedetaActivity;
import app.uplookingfood.app.uplookingfood.entity.ResponseHome;
import app.uplookingfood.app.uplookingfood.tools.BitmapHelper;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
public class AutoScrollViewPager extends ViewPager {
    private Timer timer;
    private TimerTask task;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            setCurrentItem(getCurrentItem() + 1);
            return false;
        }
    });

    public AutoScrollViewPager(Context context) {
        super(context, null);
        timer = new Timer();
    }
    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        timer = new Timer();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScollChangeListener != null) {
            onScollChangeListener.scroll(l);
        }
    }

    /**
     * 为计数器滑动重写的监听
     */
    private OnScollChangeListener onScollChangeListener;

    public void setOnScollChangeListener(OnScollChangeListener onScollChangeListener) {
        this.onScollChangeListener = onScollChangeListener;
    }

    /**
     * 为计数器里面的move方法提供X参数
     */
    public interface OnScollChangeListener {
        void scroll(int x);
    }

    // 20
    // 5页

    public void setAdapter(AutoSlidingPagerAdapter adapter) {
        super.setAdapter(adapter);
        int curPage = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % adapter.getViews().size();
        setCurrentItem(curPage, false);
        startSliding();
    }

    /**
     * 开始自动滑动
     */
    public void startSliding() {
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
            timer.schedule(task, 3000, 3000);
        }
    }

    /**
     * 停止自动滑动
     */
    public void stopSliding() {
        handler.removeMessages(0);
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopSliding();
                ContentActivity aty = (ContentActivity) getContext();
                aty.setIntercept(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startSliding();
                aty = (ContentActivity) getContext();
                aty.setIntercept(true);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startSliding();
                ContentActivity aty = (ContentActivity) getContext();
                aty.setIntercept(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public class AutoSlidingPagerAdapter extends PagerAdapter {
        private List<ImageView> views;

        private List<ResponseHome.HeadObject.Food> recipes;
        private BitmapUtils bitmapUtils;
        OnClickListener onClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer i = (Integer) v.getTag();
                ResponseHome.HeadObject.Food recipe = recipes.get(i);
                Intent intent = new Intent(getContext(), RecipedetaActivity.class);
                intent.putExtra("recipe_id", recipe.recipeId);
                IntentUtil.startActivity(getContext(), intent);
            }
        };

        public AutoSlidingPagerAdapter(List<ResponseHome.HeadObject.Food> recipes) {
            super();
            this.recipes = recipes;
            views = new ArrayList<ImageView>();
            for (int i = 0; i < recipes.size(); i++) {
                ImageView img = new ImageView(getContext());
                img.setScaleType(ScaleType.CENTER_CROP);
                img.setTag(i);
                img.setOnClickListener(onClickListener);
                views.add(img);
            }
            bitmapUtils = BitmapHelper.getBitmapUtils(getContext().getApplicationContext());
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = views.get(position % views.size());
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = views.get(position % views.size());
            container.addView(view);
            bitmapUtils.display(view, recipes.get(position % views.size()).img);
            return view;
        }

        public List<ImageView> getViews() {
            return views;
        }


        public ResponseHome.HeadObject.Food getItem(int position) {
            return recipes.get(position % recipes.size());
        }
    }
}
