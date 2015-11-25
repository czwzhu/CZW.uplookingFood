package app.uplookingfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.uplookingfood.app.uplookingfood.Adatper.ParallaxAdatper;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.Handler.ContinarHanlder;
import app.uplookingfood.app.uplookingfood.Handler.mMaterialsHandler;
import app.uplookingfood.app.uplookingfood.View.ParallaxScrollListView;
import app.uplookingfood.app.uplookingfood.entity.RecicpeEntity;
import app.uplookingfood.app.uplookingfood.tools.BitmapHelper;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
import app.uplookingfood.app.uplookingfood.tools.SharedPrefrenceTool;
import app.uplookingfood.app.uplookingfood.tools.ToastUtil;
import app.uplookingfood.app.uplookingfood.tools.UserPrefrence;


public class RecipedetaActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private RecicpeEntity recicpeEntity;
    private HttpUtils mHttpUtils;
    private Intent intent;
    private ParallaxScrollListView parallaxListView;
    private ImageView mRecipeImage;
    private TextView mRecipeTv_name;
    private TextView mIntroduceTv_name;
    private LinearLayout mMaterialsContainer;
    private RelativeLayout mHeaderIconLayout;
    private BitmapUtils mBitmapUtils;
    private mMaterialsHandler materialsHandler;
    private LinearLayout mFooterTrick_layout;
    private TextView mFooterTrick_content;
    public LinearLayout container;
    private ContinarHanlder continarHanlder;
    private ParallaxAdatper parallaxAdatper;
    private Button mAllConment;
    private ImageView mBackBtn;
    private EditText mCommentEt;
    private View mCommentLayout, mCommentCommitBtn;
    private Animation mCommentInAnim, mCommentOutAnim;
    private ContinarHanlder commentHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        init();
        lodaNative();
    }

    /**
     * 初始化数据
     */

    private void init() {
        intent = getIntent();
        recicpeEntity = new RecicpeEntity();
        commentHandler=new ContinarHanlder(this);
        parallaxListView = (ParallaxScrollListView) findViewById(R.id.listView);
        mBitmapUtils = BitmapHelper.getBitmapUtils(getApplicationContext());
        mBackBtn = (ImageView)findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mCommentLayout = findViewById(R.id.comment_layout);
        mCommentEt = (EditText) findViewById(R.id.comment_input);
        mCommentCommitBtn = findViewById(R.id.comment_commit);
        mCommentCommitBtn.setOnClickListener(this);
        //mCommentLayout.setVisibility(View.GONE);

    }

    /**
     * 网络下载菜谱信息
     */
    private void lodaDatasNet() {
        intent = getIntent();
        final int data = intent.getIntExtra("recipe_id", 0);
        String URL = Constants.URL_RECIPE_DETAIL.replace("_id", data + "");
        mHttpUtils = new HttpUtils();
        mHttpUtils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String result = responseInfo.result.toString();
                Gson gson = new Gson();
                recicpeEntity = gson.fromJson(result, RecicpeEntity.class);
                /**
                 * 把jason数据缓存在本地文件中
                 */
                SharedPrefrenceTool.put(getApplicationContext(), "recipe_details", data + "", result);
                /**
                 * 将网络下载的jason数据加载到界面
                 */
                loadDataView(recicpeEntity);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 本地上获取菜谱信息和网络上获取评论，加载到缓冲中
     */
    private void lodaNative() {
        int data = intent.getIntExtra("recipe_id", 0);
        /**
         * 从本地上面获取Jason数据
         */
        String jsonData = SharedPrefrenceTool.getStr(getApplicationContext(), "recipe_details", data + "");
        /**
         * 判断本地Jason数据 是否为空，为空就从网络上下载数据
         */
        if (!TextUtils.isEmpty(jsonData)) {
            /**
             * object 是从本地缓存中获取的jason数据
             */
            final RecicpeEntity object = new Gson().fromJson(jsonData, RecicpeEntity.class);
            String URL = Constants.URL_GET_BRIEF_COOK_COMMENTS.replace("_cook_id", data + "")
                    .replace("_time_stamp", object.getComments().getTimeStamp());
            mHttpUtils = new HttpUtils();
            mHttpUtils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    String resultInfo = responseInfo.result.toString();
                    /**
                     * 将菜谱里面的评论实体类里面的数据解析出来
                     */
                    RecicpeEntity.Comments comments = new Gson().fromJson(resultInfo, RecicpeEntity.Comments.class);
                    /**
                     * 检测服务器数据，不为0则表示有更新
                     */
                    if (comments.totalCount != 0) {
                        object.setComments(comments);
                        object.getComments().setTimeStamp(comments.getTimeStamp());
                        /**
                         * 将数据加入SharedpefenceTool 进行缓冲
                         */
                        SharedPrefrenceTool.put(getApplicationContext(), "recipe_details", "recipe_id", new Gson().toJson(object));
                    }
                    /**
                     * 将本地获取的菜谱信息展示到界面上
                     */
                    loadDataView(object);
                    /**
                     * 从网络下载评论数据，写入一个实体类
                     */

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        } else {
            lodaDatasNet();
        }
    }

    /**
     * 把菜谱信息展示到控件上
     */
    private void loadDataView(RecicpeEntity recicpeEntity) {
        int data = intent.getIntExtra("recipe_id", 0);
        /**
         * 加载头部数据到listView
         */
        parallaxListView.addHeaderView(getHeaderView());
        parallaxListView.addFooterView(getFootView());
        parallaxAdatper = new ParallaxAdatper(RecipedetaActivity.this, recicpeEntity.steps);
        parallaxListView.setAdapter(parallaxAdatper);
        /**
         * 加载头部图片,和图片名字
         */
        mBitmapUtils.display(mRecipeImage, recicpeEntity.img);
        mRecipeTv_name.setText(recicpeEntity.name);
        /**
         * 判断菜谱有无介绍,如果没有菜谱的介绍 则设置介绍
         */
        if (!TextUtils.isEmpty(recicpeEntity.introduce)) {
            mIntroduceTv_name.setText(Html.fromHtml(recicpeEntity.introduce));
        }
        /**
         * 加载菜谱步骤
         */
        materialsHandler = new mMaterialsHandler(RecipedetaActivity.this);
        materialsHandler.setDatas(mMaterialsContainer, recicpeEntity.main_materials, recicpeEntity.assist_materials);
        /**
         * 加载菜谱的小提示
         */
        if (!TextUtils.isEmpty(recicpeEntity.tips)) {
            mFooterTrick_layout.setVisibility(View.VISIBLE);
            mFooterTrick_content.setText(Html.fromHtml(recicpeEntity.tips));
        }
        /**
         * 加载评论信息
         */
        continarHanlder = new ContinarHanlder(RecipedetaActivity.this);
        continarHanlder.setDatas(container, recicpeEntity.getComments(), data);


    }

    private View getHeaderView() {
        /**
         * 初始化头部数据，头部数据里面的控件
         */
        View headView = LayoutInflater.from(RecipedetaActivity.this).inflate(R.layout.v_recipe_detail_head, null);
        mRecipeImage = (ImageView) headView.findViewById(R.id.v_recipe_detail_head_imageview);
        mRecipeTv_name = (TextView) headView.findViewById(R.id.v_recipe_detail_head_recipeNameTv);
        mIntroduceTv_name = (TextView) headView.findViewById(R.id.v_recipe_detail_head_introduceTv);
        mMaterialsContainer = (LinearLayout) headView.findViewById(R.id.v_recipe_header_detail_material_container);
        mHeaderIconLayout = (RelativeLayout) headView.findViewById(R.id.v_header_hideableview);
        return headView;
    }

    private View getFootView() {
        /**
         * 初始化底部数据，底部数据里面的控件
         */
        View view = LayoutInflater.from(RecipedetaActivity.this).inflate(R.layout.v_recipe_detail_foot, null);
        mFooterTrick_layout = (LinearLayout) view.findViewById(R.id.recipe_detail_footer_trick_layout);
        mFooterTrick_content = (TextView) view.findViewById(R.id.recipe_detail_footer_trick_content);
        container = (LinearLayout) view.findViewById(R.id.v_recipe_detail_comment_layout);
        mAllConment = (Button) view.findViewById(R.id.v_recipe_detail_comment_layout_commentAllBtn);
        mAllConment.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mBackBtn) {
            onBackPressed();
        } else if (v == mCommentCommitBtn) {
            if (UserPrefrence.isLogin(getApplicationContext())) {
                commentAction();
                mCommentEt.getEditableText().clear();
            } else {
                ToastUtil.show(this, "此操作需要登入");
                Intent intent = new Intent(this, LoginActivity.class);
                IntentUtil.startActivity(this, intent);
                finish();
            }
        }
    }
    private void commentAction() {
        int data = intent.getIntExtra("recipe_id", 0);
        String commentContent = mCommentEt.getText().toString();
        if (TextUtils.isEmpty(commentContent)) {
            ToastUtil.show(getApplicationContext(),
                    getString(R.string.please_input_comment_content));
        } else {
            RequestParams params = new RequestParams();
            params.addBodyParameter("user_name", UserPrefrence.getUserName(getApplicationContext()));
            params.addBodyParameter("cook_id", data+"");
            params.addBodyParameter("content", mCommentEt.getText().toString());
            // 提交评论
            new HttpUtils().send(HttpRequest.HttpMethod.POST,
                    Constants.URL_ADD_COOK_COMMENT, params, commentCallBack);
        }
    }
    RequestCallBack<Object> commentCallBack = new RequestCallBack<Object>() {

        @Override
        public void onSuccess(ResponseInfo<Object> responseInfo) {
            String result = (String) responseInfo.result;
            if ("1".equals(result)) {
                RecicpeEntity.Comments.User comment=new RecicpeEntity.Comments.User();
                comment.setContent(mCommentEt.getText().toString());
                comment.setCommentDate(System.currentTimeMillis() + "");
                comment.setUserHeadPhoto(UserPrefrence.getHeadPhoto(getApplicationContext()));
                comment.setUserNickName(UserPrefrence.getNickName(getApplicationContext()));
                //commentHandler.addComment(comment);
                Log.e("y1y1y", "到这里了吗");
                parallaxListView.setSelection(parallaxListView.getAdapter().getCount() + 3);

                Log.e("y1y1y", "到这里了吗????");
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            ToastUtil.show(getApplicationContext(),
                    getString(R.string.network_error_retry_later));
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int datas = intent.getIntExtra("recipe_id", 0);
        Intent intent1 = new Intent(RecipedetaActivity.this, RecipeConmentActivity.class);
        intent1.putExtra("recipe_id", datas);
        IntentUtil.startActivity(this, intent1);
    }
}
