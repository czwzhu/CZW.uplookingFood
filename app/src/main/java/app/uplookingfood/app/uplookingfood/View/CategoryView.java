package app.uplookingfood.app.uplookingfood.View;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.RecipeListActivity;
import app.uplookingfood.app.uplookingfood.Adatper.RecipeCategoryGridViewAdapter;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.entity.ResponceCategory;
import app.uplookingfood.app.uplookingfood.tools.DensityUtil;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;

@SuppressLint("NewApi")
public class CategoryView extends ScrollView implements OnItemClickListener {

    private LinearLayout layout;
    private LayoutInflater mInflater;
    /**
     * 装分组视图的集合
     **/
    private List<View> mGroupViews;
    /**
     * 分组视图上箭头ImageView的集合
     **/
    private List<ImageView> mArrowImgs;

    /**
     * 数据源---对应从服务器获取到的 菜谱所有分类
     **/
    private ResponceCategory mResponceCategory;
    private GridView mGridView;
    private RecipeCategoryGridViewAdapter mAdapter;
    /**
     * GridView的列数
     **/
    private static final int NUM_COLUMNS = 3;
    private float paddingDip = 12;
    private float verticalSpaceDip = 12;
    private float horizontalSpacingDip = 12;
    /**
     * 用来控制ScrollView的平滑滚动
     **/
    private Scroller mScroller;
    /**
     * 分组视图上箭头的动画
     **/
    private Animation mArrowOpenAnim, mArrowCloseAnim;
    /**
     * xUtils框架中的类，用来处理Bitmap的异步加载
     **/
    private BitmapUtils mBitmapUtils;

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mInflater = LayoutInflater.from(context);
        mScroller = new Scroller(context);
        mArrowOpenAnim = AnimationUtils.loadAnimation(context, R.anim.arrow_open);
        mArrowCloseAnim = AnimationUtils.loadAnimation(context, R.anim.arrow_close);
        //创建BitmapUtils对象，并指定图片缓存地址和 图片内存缓存大小
        mBitmapUtils = new BitmapUtils(context, Constants.IMG_CACHE_PATH, 0.3f);
    }

    private void init() {
        //创建一个线性容器，并加入到ScrollView
        layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        addView(layout);

        mGridView = new GridView(getContext());
        //设置GridView的列数
        mGridView.setNumColumns(NUM_COLUMNS);
        //设置GridView的padding和里面item的间距
        int paddingPx = DensityUtil.dip2px(getContext(), paddingDip);
        mGridView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        mGridView.setHorizontalSpacing(DensityUtil.dip2px(getContext(), horizontalSpacingDip));
        mGridView.setVerticalSpacing(DensityUtil.dip2px(getContext(), verticalSpaceDip));
        mAdapter = new RecipeCategoryGridViewAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    /**
     * 初始化数据，并填充视图
     *
     * @param responceCategory
     */
    public void initDatas(ResponceCategory responceCategory) {
        mResponceCategory = responceCategory;
        List<ResponceCategory.CategoryParent> list = responceCategory.getResult();
        mGroupViews = new ArrayList<View>();
        mArrowImgs = new ArrayList<ImageView>();
        for (ResponceCategory.CategoryParent categoryParent : list) {
            View groupView = getGroupView(categoryParent);
            layout.addView(groupView);
            groupView.setOnClickListener(onClickListener);
            mGroupViews.add(groupView);
            ImageView imgArrow = (ImageView) groupView.findViewById(R.id.img_arrow);
            mArrowImgs.add(imgArrow);
        }
    }

    /**
     * 为分组视图设置的单击监听器
     **/
    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mGroupViews.size(); i++) {
                if (v == mGroupViews.get(i)) {
                    //点击的就是第i个
                    clickGroupItem(i);
                    break;
                }
            }
        }
    };
    /**
     * 上一次展开的分组位置
     **/
    private int mLastExpandPos = -1;

    /**
     * 点击第i个分组之后的处理
     *
     * @param i
     */
    private void clickGroupItem(int i) {
        if (mLastExpandPos == -1) {//代表此时没有展开的分组
            mAdapter.resetDatas(mResponceCategory.getResult().get(i).getList());
            layout.addView(mGridView, i + 1);
            mLastExpandPos = i;
            computeGridViewHeight();
            setGridViewHeight();
            //启动属性动画
            startAnimator(-mGridViewHeight, 0);

            //控制ScrollView的滚动
            if (i >= 3) {
                smoothScrollTo((i - 2) * mGroupViews.get(0).getHeight());
            }
            mArrowImgs.get(i).startAnimation(mArrowOpenAnim);

        } else if (mLastExpandPos == i) {//代表此时点击的就是展开的那个分组
            if (end == 0) {
                startAnimator((Integer) mAnimator.getAnimatedValue(), -mGridViewHeight);
                mArrowImgs.get(i).startAnimation(mArrowCloseAnim);
            } else {
                startAnimator((Integer) mAnimator.getAnimatedValue(), 0);
                mArrowImgs.get(i).startAnimation(mArrowOpenAnim);
            }
        } else {//代表此时有展开的分组，并且点击的是其它分组
            layout.removeView(mGridView);
            mArrowImgs.get(mLastExpandPos).startAnimation(mArrowCloseAnim);
            mLastExpandPos = -1;
            clickGroupItem(i);
        }
    }

    private int end;

    /**
     * 为GridView设置宽高
     */
    private void setGridViewHeight() {
        LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mGridView.getLayoutParams();
        params.height = mGridViewHeight;
    }

    /**
     * 保存GridView的高度--会变化
     **/
    private int mGridViewHeight;

    /**
     * 计算GridView的高度
     */
    private void computeGridViewHeight() {
        int totalHeight = 0;
        //先计算GridView有几行
        int lineNum = mAdapter.getCount() / NUM_COLUMNS;
        if (mAdapter.getCount() % NUM_COLUMNS != 0) {
            lineNum++;
        }
        //再计算GridView中item的高度
        View itemView = mAdapter.getView(0, null, mGridView);
        itemView.measure(0, 0); // 测量View自己的大小
        totalHeight += itemView.getMeasuredHeight() * lineNum;
        //再加上padding
        totalHeight += 2 * DensityUtil.dip2px(getContext(), paddingDip);
        //再加上item竖直方向的间距
        totalHeight += (lineNum - 1) * DensityUtil.dip2px(getContext(), verticalSpaceDip);

        mGridViewHeight = totalHeight;
    }

    /**
     * 获取一个分组视图
     *
     * @param categoryParent
     * @return
     */
    private View getGroupView(ResponceCategory.CategoryParent categoryParent) {
        View view = mInflater.inflate(R.layout.item_recipe_category_parent, null);
        TextView nameTv = (TextView) view.findViewById(R.id.item_recipe_category_group_nameTv);
        nameTv.setText(categoryParent.getName());
        ImageView imgView = (ImageView) view.findViewById(R.id.img);
        mBitmapUtils.display(imgView, categoryParent.getImg());
        return view;
    }

    private ValueAnimator mAnimator;

    /**
     * 启动属性动画
     *
     * @param start
     * @param end
     */
    private void startAnimator(int start, int end) {
        if (mAnimator == null) {
            //两种创建方式
            //			mAnimator = new ValueAnimator();
            //			mAnimator.setIntValues(start,end);
            mAnimator = ValueAnimator.ofInt(start, end);
            mAnimator.addUpdateListener(updateListener);
            mAnimator.addListener(animatorListener);
            mAnimator.setDuration(500);//设置持续时间
        } else {
            mAnimator.setIntValues(start, end);
        }
        //启动属性动画
        mAnimator.start();
        this.end = end;
    }

    AnimatorListener animatorListener = new AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            //判断 如果是收缩分组属性动画结束
            if ((Integer) mAnimator.getAnimatedValue() == -mGridViewHeight) {
                layout.removeView(mGridView);
                mLastExpandPos = -1;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };
    AnimatorUpdateListener updateListener = new AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里会实时获取到ValueAnimator中值的变化
            Integer value = (Integer) animation.getAnimatedValue();
            LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mGridView.getLayoutParams();
            params.bottomMargin = value;
            mGridView.setLayoutParams(params);
        }
    };

    /**
     * 平滑的滚动到哪里
     *
     * @param y
     */
    private void smoothScrollTo(int y) {
        int startY = getScrollY();
        int dy = y - startY;
        mScroller.startScroll(0, startY, 0, dy, 600);
        invalidate();//为了触发computeScroll()
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(0, mScroller.getCurrY());
                invalidate();
            }
        }
    }

    private boolean itemCanClick = true;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (!itemCanClick) {
            return;
        }
        ResponceCategory.Category category = mAdapter.getItem(position);
        Intent intent = new Intent(getContext(), RecipeListActivity.class);
        intent.putExtra("tag", Constants.TAG_CATEGORY);
        intent.putExtra("id", category.getId());
        intent.putExtra("title", category.getName());
        IntentUtil.startActivity(getContext(), intent);
        itemCanClick = false;

    }

    private int mLastScrollY;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            mLastScrollY = getScrollY();
        } else {
            scrollTo(0, mLastScrollY);
            itemCanClick = true;
        }
    }

    /**
     * 判断是否有分组展开
     *
     * @return
     */
    public boolean isExpand() {
        return mLastExpandPos != -1;
    }

    /**
     * 关闭当前展开的分组
     */
    public void collapse() {
        clickGroupItem(mLastExpandPos);
    }
}
