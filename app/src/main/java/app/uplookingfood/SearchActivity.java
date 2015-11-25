package app.uplookingfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.app.uplookingfood.Adatper.SearchHistoryListAdapter;
import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.entity.SearchHistoryEntity;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
import app.uplookingfood.app.uplookingfood.tools.SoftKeyboardTool;
import app.uplookingfood.app.uplookingfood.tools.ToastUtil;

public class SearchActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {
    private PopupWindow mPop;
    private TextView mPopRecipe, mPopMaterial;
    private TextView mSearchKeyTv;
    private View mSearchImg;
    private EditText mSearchEt;
    private View mDeleteImg;
    private View mRootView;
    //历史搜索相关
    private ListView mHistoryListView;
    private SearchHistoryListAdapter mHistoryListAdapter;
    private View mClearHistorySearch;
    private List<SearchHistoryEntity> mHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRootView = findViewById(R.id.rootView);
        initViews();
        initPopupWindow();
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopupWindow() {
        //创建PopupWindow对象，需要设置宽高
        mPop = new PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        View popView = getLayoutInflater().inflate(R.layout.v_search_pop, null);
        mPop.setContentView(popView);//设置显示视图
        //下面几行代码实现 点击PopupWindow之外区域让其消失
        mPop.setFocusable(true);
        Bitmap bitmap = null;
        mPop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        mPop.setOnDismissListener(onPopDismissListener);//监听PopupWindow的消失

        mPopRecipe = (TextView) popView.findViewById(R.id.pop_search_recipe);
        mPopMaterial = (TextView) popView.findViewById(R.id.pop_search_material);
        mPopRecipe.setOnClickListener(this);
        mPopMaterial.setOnClickListener(this);
    }

    /**
     * 监听PopupWindow消失的监听器
     */
    OnDismissListener onPopDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss() {
            turnWindowLight();
        }
    };

    /**
     * 初始化控件
     */
    private void initViews() {
        mSearchKeyTv = (TextView) findViewById(R.id.search_key_text);
        mSearchKeyTv.setOnClickListener(this);
        mSearchImg = findViewById(R.id.search_btn);
        mSearchImg.setOnClickListener(this);
        mSearchEt = (EditText) findViewById(R.id.search_et);
        mSearchEt.addTextChangedListener(editTextWatcher);
        //
        mSearchEt.setOnEditorActionListener(editorActionListener);
        mDeleteImg = findViewById(R.id.search_delete);
        mDeleteImg.setOnClickListener(this);

        //历史搜索
        mHistoryListView = (ListView) findViewById(R.id.search_history_listview);
        mClearHistorySearch = findViewById(R.id.search_clearHistory_lay);
        mClearHistorySearch.setOnClickListener(this);
        mHistoryList = getHistory();//获取历史记录数据的集合
        if (mHistoryList.size() != 0) {
            mClearHistorySearch.setVisibility(View.VISIBLE);
        }
        mHistoryListAdapter = new SearchHistoryListAdapter(mHistoryList,this);
        //为ListView设置空视图（当ListView数据源条数为0时显示的视图，不需要手动设置显示隐藏）
        mHistoryListView.setEmptyView(findViewById(R.id.empty_view));
        mHistoryListView.setAdapter(mHistoryListAdapter);
        mHistoryListView.setOnItemClickListener(this);
    }

    /**
     * 获取历史查询记录
     *
     * @return
     */
    private List<SearchHistoryEntity> getHistory() {
        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        String historyJson = preferences.getString("history", null);
        if (historyJson != null) {
            return new Gson().fromJson(historyJson, new TypeToken<List<SearchHistoryEntity>>() {
            }.getType());
        }
        return new ArrayList<SearchHistoryEntity>();
    }

    /**
     * 保存历史查询记录
     */
    private void saveHistory() {
        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        preferences.edit().putString("history", new Gson().toJson(mHistoryList)).commit();
    }

    /**
     * 监听EditText内容的改变的监听器
     */
    TextWatcher editTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(mSearchEt.getText())) {
                mDeleteImg.setVisibility(View.GONE);
            } else {
                mDeleteImg.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    OnEditorActionListener editorActionListener = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                clickSearch();
                SoftKeyboardTool.closeKeyboard(mSearchEt);
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        if (v == mSearchKeyTv) {//弹PopupWindow的TextView
            mPop.showAsDropDown(v);
            turnWindowDark();
        } else if (v == mPopRecipe) {
            mSearchKeyTv.setText(mPopRecipe.getText());
            mPop.dismiss();
        } else if (v == mPopMaterial) {
            mSearchKeyTv.setText(mPopMaterial.getText());
            mPop.dismiss();
        } else if (v == mSearchImg) {
            clickSearch();
        } else if (v == mDeleteImg) {
            mSearchEt.getEditableText().clear();
            mDeleteImg.setVisibility(View.GONE);
            mSearchEt.requestFocus();
            SoftKeyboardTool.showSoftKeyboard(mSearchEt);
        } else if (v == mClearHistorySearch) {//清空历史搜索按钮
            mHistoryList.clear();
            mHistoryListAdapter.notifyDataSetChanged();
            saveHistory();
            mClearHistorySearch.setVisibility(View.GONE);
        }
    }

    /**
     * 点击了搜索按钮的处理
     */
    private void clickSearch() {
        if (TextUtils.isEmpty(mSearchEt.getText())) {
            ToastUtil.show(this, "请输入关键字！");
            return;
        }
        //将搜索关键字保存到历史搜索
        String type = mSearchKeyTv.getText().toString();
        String content = mSearchEt.getText().toString();
        mHistoryList.add(0, new SearchHistoryEntity(type, content));
        mHistoryListAdapter.notifyDataSetChanged();
        mClearHistorySearch.setVisibility(View.VISIBLE);
        saveHistory();
        //执行跳转
        goSearch(type, content);
    }

    /**
     * 跳转到菜谱列表界面
     *
     * @param type
     * @param key
     */
    private void goSearch(String type, String key) {
        intent = new Intent(this, RecipeListActivity.class);
        if ("菜谱".equals(type)) {
            intent.putExtra("tag", Constants.TAG_SEARCH_BY_RECIPE_NAME);
        } else if ("食材".equals(type)) {
            intent.putExtra("tag", Constants.TAG_SEARCH_BY_INGREDIENT);
        }
        intent.putExtra("title", key);
        SoftKeyboardTool.closeKeyboard(mSearchEt);
        handler.sendEmptyMessage(0);
    }

    int height;

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && height == 0) {
            height = mRootView.getHeight();
        }
    }

    ;
    Intent intent;
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (mRootView.getHeight() != height) {
                handler.sendEmptyMessageDelayed(0, 5);
            } else {
                IntentUtil.startActivity(SearchActivity.this, intent);
            }
            return false;
        }
    });

    /**
     * 使Activity的窗口变暗
     */
    private void turnWindowDark() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.8f;
        getWindow().setAttributes(params);
    }

    /**
     * 使Activity的窗口变亮
     */
    private void turnWindowLight() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1;
        getWindow().setAttributes(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        SearchHistoryEntity entity = mHistoryList.get(position);
        goSearch(entity.getType(), entity.getContent());
    }
}

