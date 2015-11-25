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
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
import app.uplookingfood.app.uplookingfood.tools.ToastUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ReigistPhoneActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPhone_et;
    private Button mClear_btn;
    private Button mNext_btn;
    private HttpUtils mHttpUtils;
    private String APPKEY=Constants.MOB_APP_KEY;
    private String APP_SECRET=Constants.MOB_APP_SECRET;
    private ProgressDialog mProgressDialog;
    private String reusultCode;
    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                case EditorInfo.IME_ACTION_GO:
                case EditorInfo.IME_ACTION_DONE:
                    nextStep();
                    break;

            }
            return false;
        }
    };
    EventHandler mEventHandler = new EventHandler(){

        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            //这里可以操作主线程的控件
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(getApplicationContext(), event+"");
                    if(mProgressDialog != null){
                        mProgressDialog.dismiss();
                    }

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            // 请求支持国家列表
                            //							onCountryListGot((ArrayList<HashMap<String,Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //请求验证码后，跳转到验证码填写页面
                            afterVerificationCodeRequested();
                        }
                    }else{
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();

                      Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());

                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtil.show(getApplicationContext(), des);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 如果木有找到资源，默认提示
                        ToastUtil.show(getApplicationContext(), getString(R.string.network_error_retry_later));
                    }
                }
            });

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(mEventHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_by_phone);
        initSMSSDK();
        initViews();
        initPrgorss();
    }
    private void initViews(){
        mClear_btn=(Button)findViewById(R.id.v_regist_by_phone_Button_phoneClear);
        mNext_btn=(Button)findViewById(R.id.v_regist_by_phone_Button_next);
        mPhone_et=(EditText)findViewById(R.id.v_regist_by_phone_EditText_phone);
        mPhone_et.setOnEditorActionListener(editorActionListener);
        mNext_btn.setOnClickListener(this);
        mClear_btn.setOnClickListener(this);
        mHttpUtils=new HttpUtils();
    }
    private void initSMSSDK(){
        SMSSDK.initSDK(ReigistPhoneActivity.this, APPKEY, APP_SECRET);
    }
    private void initPrgorss(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("正在连接服务器");
        mProgressDialog.setMessage("请稍后...");
        mProgressDialog.setCancelable(false);
    }
    private void afterVerificationCodeRequested(){
        /**
         * 跳转页面的方法
         */
        Intent intent = new Intent(this, ReigistByphoneActivity.class);
        intent.putExtra("phone", mPhone_et.getText().toString());
        IntentUtil.startActivity(this, intent);

    }
    private void nextStep() {
        String phoneNumber = mPhone_et.getText().toString();
        if(TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11){
            ToastUtil.show(this, getString(R.string.phone_num_not_right));
            return;
        }
        mProgressDialog.setTitle("正在检查手机号是否已注册");
        mProgressDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v==mNext_btn){
            String URL= Constants.URL_CHECK_PHONE.replace("_phone",mPhone_et.getText());
            mHttpUtils.send(HttpRequest.HttpMethod.GET, URL, new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    String result=responseInfo.result.toString();
                    /**
                     * 根据服务器传来的数据 1就是传送过来的数据有，0则是服务器没有数据
                     */
                    if("0".equals(result)){
                        /**
                         * 如果没有该手机号码,注册手机号码
                         */
                        SMSSDK.getVerificationCode("86", mPhone_et.getText().toString());
                        nextStep();

                    }else{
                        Toast.makeText(ReigistPhoneActivity.this,"该手机号已经注册",Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ToastUtil.show(getApplicationContext(), getString(R.string.network_error_retry_later));
                            mProgressDialog.dismiss();
                        }
                    });

                }

            });
        }
    }
}
