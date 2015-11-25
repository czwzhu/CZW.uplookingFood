package app.uplookingfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.uplookingfood.app.uplookingfood.Adatper.RerfresAdtaper;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.View.RefreshListView;
import app.uplookingfood.app.uplookingfood.entity.RecicpeEntity;

public class RecipeConmentActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mBack;
    private RefreshListView mlistView;
    private int mContmentPage;
    private HttpUtils mHttpUtils;
    private RerfresAdtaper rerfresAdtaper;
    private int data;
    private RequestCallBack<Object> objectRequestCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_comment);
        Intent intent = getIntent();
        Log.e("111", "跳转成功");
        data = intent.getIntExtra("recipe_id", 0);
        initViews();
        loaaDatas();
    }

    private void initViews() {
        Log.e("yyyy","进入初始化");
        mHttpUtils=new HttpUtils();
        mBack = (TextView) findViewById(R.id.back);
        mBack.setOnClickListener(this);
        mlistView = (RefreshListView) findViewById(R.id.listView);
        Log.e("yyyy","进入初始化2");
        mlistView.setOnListener(new RefreshListView.OnListener() {

            @Override
            public void onLoadNextPage() {
                loaaDatas();
            }

            @Override
            public void onRetry() {
                mContmentPage--;
                loaaDatas();
            }
        });
    }

    private void loaaDatas() {
        mContmentPage++;
        String URL = Constants.URL_GET_RECIPE_ALL_COMMENTS.replace("_cook_id", data + "")
                .replace("_page", mContmentPage + "");
        mHttpUtils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String result = responseInfo.result.toString();
                RecicpeEntity.Comments comments = new Gson().fromJson(result, RecicpeEntity.Comments.class);
                if (mContmentPage == 1) {
                    rerfresAdtaper = new RerfresAdtaper(getApplicationContext(), comments.getList());
                    mlistView.setAdapter(rerfresAdtaper);
                } else {
                    rerfresAdtaper.addDatas(comments.getList());
                    if (comments.totalCount == rerfresAdtaper.getCount()) {
                        mlistView.loadEnd();
                    } else {
                        mlistView.loadSuccess();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //mlistView.loadFailed();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mBack) {
            finish();
        }
    }
}
