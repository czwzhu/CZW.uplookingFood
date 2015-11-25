package app.uplookingfood.app.uplookingfood.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.SearchActivity;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.Handler.HotFoodHanlder;
import app.uplookingfood.app.uplookingfood.Handler.HotIncridentHanlder;
import app.uplookingfood.app.uplookingfood.View.AutoScrollViewPager;
import app.uplookingfood.app.uplookingfood.View.IndicatorView;
import app.uplookingfood.app.uplookingfood.View.MyGridView;
import app.uplookingfood.app.uplookingfood.entity.ResponseHome;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;

/**
 * Created by czw on 2015/11/6.
 */
public class FragmentHome extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private AutoScrollViewPager.AutoSlidingPagerAdapter pagerAdapter;
    private ListView homelist;
    private AutoScrollViewPager mScorllview;
    private TextView mPagerTitle;
    private IndicatorView indicatorView;
    private MyGridView myGridView;
    private MyGridView mGridViewTwo;
    private ResponseHome responseHome;
    private HotFoodHanlder hotFoodHanlder;
    private HotIncridentHanlder hotIncridentHanlder;
    private TextView serchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.home, null);
        return view;
    }

    private HttpUtils mHttpUtil;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        loadData();
    }


    public void loadData() {
        /**
         * 用框架Xutils Http从网络下载数据
         */
        mHttpUtil = new HttpUtils();
        mHttpUtil.send(HttpRequest.HttpMethod.GET, Constants.URL_HOME,
                new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        String result = responseInfo.result.toString();
                        /**
                         * jason解析数据
                         */
                        Gson gson = new Gson();
                        responseHome = gson.fromJson(result, ResponseHome.class);
                        /**
                          * 调用内部类,让数据写进viewpager
                         */
                        pagerAdapter = mScorllview.new AutoSlidingPagerAdapter(responseHome.getHeadObject().list);
                        mScorllview.setAdapter(pagerAdapter);

                        /**
                         *
                         */

                        /**
                         * 模拟数据
                         */
                        homelist_adpter();
                        hotFoodHanlder.inited(getActivity(), responseHome.getRecipe_object().list, myGridView);
                        try {
                            hotIncridentHanlder.initIncrident(mGridViewTwo, getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }

    private void homelist_adpter() {
        /**
         * 模拟数据
         */
        final List<String> listTest = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            listTest.add("模拟数据" + i);
        }
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listTest.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(getActivity());
                textView.setText(listTest.get(position));
                return textView;
            }
        };
        homelist.setAdapter(baseAdapter);
    }

    private void init() {
        /**
         * 加载头部组件 并让home_list添加头部
         */
        /**  初始化数据
         * //在代码中使用 listView .addHeaderView(...)
         //方法可以在ListView组件上方添加上其他组件，并且连结在一起像是一个新组件。
         //如果多次使用 .addHeaderView(...) ,
         //则最先添加的组件在最上方，按添加的先后顺序由上到下罗列。
         */
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.autosocall, null);
        mScorllview = (AutoScrollViewPager) linearLayout.findViewById(R.id.home_head_viewpager);
        serchView=(TextView)getActivity().findViewById(R.id.home);
        serchView.setOnClickListener(this);
        indicatorView = (IndicatorView) linearLayout.findViewById(R.id.indicatorView);
        myGridView = (MyGridView) linearLayout.findViewById(R.id.home_head_recipe_gridview);
        mGridViewTwo = (MyGridView) linearLayout.findViewById(R.id.home_head_ingredient_gridview);
        mScorllview.setOnPageChangeListener(this);
        hotFoodHanlder = new HotFoodHanlder();
        hotIncridentHanlder = new HotIncridentHanlder();
        mPagerTitle = (TextView) linearLayout.findViewById(R.id.pager_title);
        homelist = (ListView) getActivity().findViewById(R.id.home_list);
        homelist.addHeaderView(linearLayout);
        //为计数器设置个数
        indicatorView.setIndicatorsCount(6);
        AutoScrollViewPager.OnScollChangeListener onScollChangeListener = new AutoScrollViewPager.OnScollChangeListener() {
            @Override
            public void scroll(int x) {
                indicatorView.move(x, mScorllview.getWidth());
            }
        };
        mScorllview.setOnScollChangeListener(onScollChangeListener);
    }

    @Override
    public void onClick(View v) {
        if (v == serchView) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            IntentUtil.startActivity(getActivity(), intent);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPagerTitle.setText(pagerAdapter.getItem(position).name);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
