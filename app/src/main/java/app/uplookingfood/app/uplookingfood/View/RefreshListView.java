package app.uplookingfood.app.uplookingfood.View;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import app.uplookingfood.R;

public class RefreshListView extends ListView {
    /**
     * 脚视图
     **/
    private View mFooterView;
    /**
     * 脚视图中显示加载动画的ImageView
     **/
    private ImageView mFooterLoadingImg;
    /**
     * 脚视图中显示加载失败的TextView
     **/
    private TextView mFooterFailedText;
    /**
     * 脚视图中显示已到数据结尾的ImageView
     **/
    private ImageView mFooterEndImg;

    private static final int STATE_LOADING = 1;
    private static final int STATE_LOAD_SUCCESS = 2;
    private static final int STATE_LOAD_FAILED = 3;
    private static final int STATE_END = 4;
    private int mCurState;

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.listview_refresh_footer, null);
        mFooterLoadingImg = (ImageView) mFooterView.findViewById(R.id.listview_refresh_footer_loadingImg);
        mFooterEndImg = (ImageView) mFooterView.findViewById(R.id.listview_refresh_footer_endImg);
        mFooterFailedText = (TextView) mFooterView.findViewById(R.id.listview_refresh_footer_failed_text);
        mFooterFailedText.setOnClickListener(onClickListener);
        addFooterView(mFooterView);
        setOnScrollListener(onScrollListener);
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mFooterFailedText) {
                if (onListener != null) {
                    onListener.onRetry();
                }
                loadMore();
            }
        }
    };
    OnScrollListener onScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (totalItemCount == 0) {
                return;
            }
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                if (onListener != null && mCurState != STATE_LOADING
                        && mCurState != STATE_END && mCurState != STATE_LOAD_FAILED) {
                    //TODO
                    onListener.onLoadNextPage();
                    loadMore();
                }
            } else if (mCurState == STATE_LOAD_FAILED) {
                mCurState = 0;
            }
        }
    };

    /**
     * 加载数据成功，需要从外部调用
     **/
    public void loadSuccess() {
        mAnimDrawable.stop();
        mCurState = STATE_LOAD_SUCCESS;
    }

    /**
     * 加载数据失败，需要从外部调用
     */
    public void loadFailed() {
        mFooterLoadingImg.setVisibility(View.GONE);
        mAnimDrawable.stop();
        mFooterFailedText.setVisibility(View.VISIBLE);
        mCurState = STATE_LOAD_FAILED;
    }

    /***
     * 加载数据已经到结尾了，需要从外部调用
     */
    public void loadEnd() {
        mAnimDrawable.stop();
        mFooterLoadingImg.setVisibility(View.GONE);
        mFooterEndImg.setVisibility(View.VISIBLE);
        mCurState = STATE_END;
    }

    /**
     * 帧动画
     **/
    private AnimationDrawable mAnimDrawable;

    /**
     * 加载更多
     */
    protected void loadMore() {
        if (mAnimDrawable == null) {
            mFooterLoadingImg.setBackgroundResource(R.drawable.loading);
            mAnimDrawable = (AnimationDrawable) mFooterLoadingImg.getBackground();
        }
        mAnimDrawable.start();//启动帧动画
        mFooterFailedText.setVisibility(View.GONE);
        mFooterLoadingImg.setVisibility(View.VISIBLE);
        mCurState = STATE_LOADING;
    }

    private OnListener onListener;

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    public interface OnListener {
        void onLoadNextPage();

        void onRetry();
    }
}
