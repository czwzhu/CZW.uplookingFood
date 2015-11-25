package app.uplookingfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import app.uplookingfood.app.uplookingfood.Adatper.RecipeListAdapter;
import app.uplookingfood.app.uplookingfood.Adatper.ResponceRecipeList;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.View.RefreshListView;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;

public class RecipeListActivity extends AppCompatActivity implements RefreshListView.OnListener, OnItemClickListener{
    private RefreshListView mListView;
    private RecipeListAdapter mAdapter;
    private HttpUtils mHttpUtils;
    private TextView mTitleTv;
    private int mCurPage = 1;
    private int tag;
    private TextView mBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mHttpUtils = new HttpUtils();
        mBackBtn=(TextView)findViewById(R.id.title_back);
        initViews();
        tag = getIntent().getIntExtra("tag", -1);
        loadDatasFirst();
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private String getUrl(){
        switch (tag) {
            case Constants.TAG_CATEGORY:
                int id = getIntent().getIntExtra("id", 1);
                return Constants.URL_RECIPE_LIST.replace("_id",id+"").replace("_page", mCurPage+"");
            case Constants.TAG_SEARCH_BY_RECIPE_NAME:
                String key = getIntent().getStringExtra("title");
                return Constants.URL_SEARCH_BY_NAME.replace("_key",key).replace("_page", mCurPage+"");
            case Constants.TAG_SEARCH_BY_INGREDIENT:
                key = getIntent().getStringExtra("title");
                return Constants.URL_SEARCH_BY_MATERIAL.replace("_key",key).replace("_page", mCurPage+"");
        }
        return "";
    }
    private void loadDatasFirst() {
        loadDatas(getUrl());
    }

    private void initViews() {
        mListView = (RefreshListView) findViewById(R.id.listView);
        mListView.setOnListener(this);
        mListView.setOnItemClickListener(this);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setText(getIntent().getStringExtra("title"));
    }
    private void loadNextPage(){
        mCurPage++;
        loadDatas(getUrl());
    }
    private void loadDatas(String url){
        mHttpUtils.send(HttpMethod.GET, url, httpCallBack);
    }
    RequestCallBack<Object> httpCallBack = new RequestCallBack<Object>() {


        @Override
        public void onSuccess(ResponseInfo<Object> arg0) {
            String json = arg0.result.toString();
            Gson gson = new Gson();
            ResponceRecipeList responceRecipeList = gson.fromJson(json, ResponceRecipeList.class);
            if(mCurPage == 1){
                mAdapter = new RecipeListAdapter(RecipeListActivity.this, responceRecipeList.getList());
                mListView.setEmptyView(findViewById(R.id.empty_view));
                mListView.setAdapter(mAdapter);
            }else{
                mAdapter.addDatas(responceRecipeList.getList());
                if(responceRecipeList.getList().size() + 10*(mCurPage-1)
                        >= responceRecipeList.getTotal_count()){
                    mListView.loadEnd();
                }else{
                    mListView.loadSuccess();
                }
            }
        }

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            if(mCurPage == 1){

            }else{
                mListView.loadFailed();
                mCurPage--;
            }
        }
    };
    @Override
    public void onLoadNextPage() {
        loadNextPage();
    }
    @Override
    public void onRetry() {
        loadNextPage();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ResponceRecipeList.RecipeSimple recipeSimple = mAdapter.getItem(position);
        Intent intent = new Intent(this,RecipedetaActivity.class);
        intent.putExtra("recipe_id", recipeSimple.getCookbook_id());
        IntentUtil.startActivity(this, intent);
    }

}
