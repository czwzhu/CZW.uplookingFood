package app.uplookingfood.app.uplookingfood.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import app.uplookingfood.R;
import app.uplookingfood.SearchActivity;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.View.CategoryView;
import app.uplookingfood.app.uplookingfood.entity.ResponceCategory;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;

public class FragmentCategory extends Fragment implements OnClickListener {
    /**
     * 来自xUtils框架中的对象
     **/
    private HttpUtils mHttpUtils;
    private CategoryView mCategoryView;
    private ImageView mLoadingImg;
    private AnimationDrawable mAnimationDrawable;
    private TextView mFailedTv;
    private View mBack, mSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHttpUtils = new HttpUtils();
        initViews();
        loadDatas();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        mCategoryView = (CategoryView) getView().findViewById(R.id.categroryView);
        mLoadingImg = (ImageView) getView().findViewById(R.id.loadingView);
        mFailedTv = (TextView) getView().findViewById(R.id.failedText);
        mFailedTv.setOnClickListener(this);
        mBack = getView().findViewById(R.id.title_back);
        mSearch = getView().findViewById(R.id.title_serch);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
    }

    /**
     * 加载数据
     */
    private void loadDatas() {
        startLoadingAnim();
        new Thread() {
            public void run() {
                String json = getDatasJsonForLocal();
                if (!TextUtils.isEmpty(json)) {
                    //因为setDatas方法有点耗时，所以先休眠一会，避免卡界面
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setDatas(json);
                } else {
                    loadDatasFromNetwork();
                }
            }

            ;
        }.start();
    }

    /**
     * 从SharedPreferences中获取缓存json数据
     *
     * @return
     */
    private String getDatasJsonForLocal() {
        SharedPreferences preferences = getActivity().getSharedPreferences("cache", Activity.MODE_PRIVATE);
        return preferences.getString("recipe_category", null);
    }

    /**
     * 为控件设置数据
     *
     * @param json
     */
    private void setDatas(final String json) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                ResponceCategory responceCategory = gson.fromJson(json, ResponceCategory.class);
                //为CategoryView设置数据
                mCategoryView.initDatas(responceCategory);
                stopLoadingAnim();
            }
        });
    }

    /**
     * 开启加载动画
     */
    private void startLoadingAnim() {
        if (mAnimationDrawable == null) {
            mLoadingImg.setBackgroundResource(R.drawable.loading);
            mAnimationDrawable = (AnimationDrawable) mLoadingImg.getBackground();
        }
        mLoadingImg.setVisibility(View.VISIBLE);
        mAnimationDrawable.start();
        mFailedTv.setVisibility(View.GONE);
    }

    /**
     * 关闭加载动画
     */
    private void stopLoadingAnim() {
        mAnimationDrawable.stop();
        mLoadingImg.setVisibility(View.GONE);
    }

    /**
     * 从网络加载数据
     */
    private void loadDatasFromNetwork() {
        mHttpUtils.send(HttpMethod.GET, Constants.URL_CATEGORY, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                //网络获取数据失败
                stopLoadingAnim();
                mFailedTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(ResponseInfo<Object> arg0) {
                //网络获取数据成功
                String result = arg0.result.toString();
                SharedPreferences preferences = getActivity().getSharedPreferences("cache", Activity.MODE_PRIVATE);
                preferences.edit().putString("recipe_category", result).commit();
                setDatas(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mFailedTv) {
            startLoadingAnim();
            loadDatasFromNetwork();
        } else if (v == mBack) {
            Toast.makeText(getActivity(), "返回", Toast.LENGTH_SHORT).show();
            //TODO
        } else if (v == mSearch) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            IntentUtil.startActivity(getActivity(), intent);
        }
    }

    /**
     * 判断是否有分组展开
     *
     * @return
     */
    public boolean isExpand() {
        return mCategoryView.isExpand();
    }

    /**
     * 关闭当前展开的分组
     */
    public void collapse() {
        mCategoryView.collapse();
    }
}
