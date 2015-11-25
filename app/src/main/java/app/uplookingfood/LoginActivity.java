package app.uplookingfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.entity.ResponceLogin;
import app.uplookingfood.app.uplookingfood.entity.ResponceUserInfo;
import app.uplookingfood.app.uplookingfood.tools.EditTextClearUtil;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
import app.uplookingfood.app.uplookingfood.tools.MD5;
import app.uplookingfood.app.uplookingfood.tools.ToastUtil;
import app.uplookingfood.app.uplookingfood.tools.UserPrefrence;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mMob_et;
    private EditText mMobPassword_et;
    private TextView mLogin_btn;
    private TextView mMob_reigist;
    private TextView mBack_btn;
    private EditTextClearUtil editTextClearUtil;
    private Button mCler_btn;
    private Button mUserClear_btn;
    private HttpUtils mHttpUtlis;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mHttpUtlis=new HttpUtils();
        initViews();
        initDialog();
        mMob_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                    case EditorInfo.IME_ACTION_GO:
                    case EditorInfo.IME_ACTION_DONE:
                        login();
                }
                return false;
            }
        });
    }
    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在登入");
        mDialog.setMessage("请稍后...");
    }
    private void initViews() {
        editTextClearUtil = new EditTextClearUtil();
        mMob_et = (EditText) findViewById(R.id.v_login_username_et);
        mMobPassword_et = (EditText) findViewById(R.id.v_login_password_et);
        mLogin_btn = (TextView) findViewById(R.id.v_login_login_btn);
        mMob_reigist = (TextView) findViewById(R.id.v_login_regist_by_phone);
        mBack_btn = (TextView) findViewById(R.id.v_login_back);
        mCler_btn = (Button) findViewById(R.id.v_login_password_clearBtn);
        mUserClear_btn = (Button) findViewById(R.id.v_login_username_clearBtn);
        /**
         * 引用edit框架
         */
        editTextClearUtil.addEditText(mMob_et);
        editTextClearUtil.addEditText(mMobPassword_et);
        editTextClearUtil.addClearBtn(mUserClear_btn);
        editTextClearUtil.addClearBtn(mCler_btn);

        mBack_btn.setOnClickListener(this);
        mLogin_btn.setOnClickListener(this);
        mMob_reigist.setOnClickListener(this);
    }

    private void login() {
        String userName = mMob_et.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.show(this, "用户名不能为空！");
            mMob_et.requestFocus();
            return;
        }
        if (userName.contains(" ")) {
            ToastUtil.show(this, "用户名格式不正确~");
            mMob_et.requestFocus();
            return;
        }
        String password = mMobPassword_et.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(this, "密码不能为空");
            mMobPassword_et.requestFocus();
            return;
        }
        mDialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("user_name", userName);
        password = MD5.encrypt(password);
        params.addBodyParameter("password", password);
        mHttpUtlis.send(HttpRequest.HttpMethod.POST, Constants.URL_LOGIN, params, callBack);
    }

    RequestCallBack<Object> callBack = new RequestCallBack<Object>() {

        @Override
        public void onSuccess(ResponseInfo<Object> responseInfo) {
            String result = (String) responseInfo.result;
            Gson gson = new Gson();
            ResponceLogin responceObject = gson.fromJson(result, ResponceLogin.class);
            switch (responceObject.getLoginState()) {
                case 0:
                    UserPrefrence.putUserName(getApplicationContext(), mMob_et.getText().toString());
                    UserPrefrence.putPassword(getApplicationContext(), mMobPassword_et.getText().toString());
                    ResponceUserInfo userInfo = responceObject.getUserInfo();
                    UserPrefrence.putNickName(getApplicationContext(), userInfo.getNickName());
                    UserPrefrence.putHeadPhoto(getApplicationContext(), userInfo.getHeadPhoto());
                    UserPrefrence.setLogin(getApplicationContext(), true);
                    UserPrefrence.putUserInfoJson(getApplicationContext(), gson.toJson(userInfo));
                    Intent intent = new Intent(LoginActivity.this,ContentActivity.class);
                    startActivity(intent);
                    break;
                case -1:
                    ToastUtil.show(getApplicationContext(), "账号或密码错误");
                    break;
            }
            mDialog.dismiss();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            ToastUtil.show(getApplicationContext(), "连接服务器失败");
        }
    };


    @Override
    public void onClick(View v) {
        if (v == mMob_reigist) {
            Intent intent = new Intent(LoginActivity.this, ReigistPhoneActivity.class);
            IntentUtil.startActivity(LoginActivity.this, intent);
        } else if (v == mLogin_btn) {
            login();
        } else if (v == mBack_btn) {
            finish();
        }

    }
}
