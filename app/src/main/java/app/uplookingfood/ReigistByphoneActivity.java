package app.uplookingfood;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import app.uplookingfood.app.uplookingfood.Constants;
import app.uplookingfood.app.uplookingfood.tools.EditTextClearUtil;
import app.uplookingfood.app.uplookingfood.tools.MD5;
import app.uplookingfood.app.uplookingfood.tools.ToastUtil;
import app.uplookingfood.app.uplookingfood.tools.UserPrefrence;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;


public class ReigistByphoneActivity extends AppCompatActivity implements
        OnClickListener {
    private static final int RETRY_INTERVAL = 60;
    private String mPhone;
    private BroadcastReceiver smsReceiver;
    private EditText mVerifyCodeEt;
    private TextView mResendTv;
    /**
     * 完成 按钮
     **/
    private View mSubmitBtn;

    private EditText mPasswordEt, mNickEt;
    private EditTextClearUtil mEditTextClearUtil;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_by_phone_add_info);
        mPhone = getIntent().getStringExtra("phone");
        TextView tipTv = (TextView) findViewById(R.id.v_regist_by_phone_add_info_TextView_prompt);
        tipTv.setText(String.format(getText(R.string.regist_by_phone_tips).toString(), mPhone));
        mVerifyCodeEt = (EditText) findViewById(R.id.v_regist_by_phone_add_info_EditText_captcha);
        mResendTv = (TextView) findViewById(R.id.v_regist_by_phone_add_info_TextView_resend);
        mResendTv.setOnClickListener(this);
        mSubmitBtn = findViewById(R.id.v_regist_by_phone_add_info_Button_confirm);
        mSubmitBtn.setOnClickListener(this);
        mPasswordEt = (EditText) findViewById(R.id.v_regist_by_phone_add_info_EditText_pw);
        mNickEt = (EditText) findViewById(R.id.v_regist_by_phone_add_info_EditText_nick);
        mEditTextClearUtil = new EditTextClearUtil();
        mEditTextClearUtil.addEditText(mPasswordEt);
        mEditTextClearUtil.addEditText(mNickEt);
        mEditTextClearUtil.addClearBtn(findViewById(R.id.v_regist_by_phone_add_info_Button_pwClear));
        mEditTextClearUtil.addClearBtn(findViewById(R.id.v_regist_by_phone_add_info_Button_nickClear));
        initSmsReceiver();
        countDown();
        SMSSDK.registerEventHandler(mEventHandler);
        initProgressDialog();
    }

    /****
     * 初始化Dialog
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("请稍后...");

    }

    private EventHandler mEventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                // 提交验证码
                afterSubmit(result, data);
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                // 获取验证码成功后的执行动作(可以获取成功，可能获取失败)
                afterGet(result, data);
            }
        }
    };

    /**
     * 注册接受短信的广播
     */
    private void initSmsReceiver() {
        smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
            @Override
            public void onReadVerifyCode(final String verifyCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mVerifyCodeEt.getEditableText().clear();
                        mVerifyCodeEt.getEditableText().append(verifyCode);
                    }
                });
            }
        });
        registerReceiver(smsReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));
    }

    private Timer timer;
    private TimerTask task;
    private int time = RETRY_INTERVAL;

    /**
     * 倒数计时
     */
    private void countDown() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    time--;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (time == 0) {
                                mResendTv
                                        .setText(getString(R.string.regist_by_phone_resend_text_normal));
                                mResendTv.setEnabled(true);
                                task.cancel();
                                task = null;
                                time = RETRY_INTERVAL;
                            } else {
                                mResendTv
                                        .setText(String
                                                .format(getString(R.string.regist_by_phone_resend_text_time),
                                                        time));
                            }
                        }
                    });
                }
            };
            timer.schedule(task, 1000, 1000);
            mResendTv
                    .setText(String
                            .format(getString(R.string.regist_by_phone_resend_text_time),
                                    time));
            mResendTv.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mResendTv) {
            mProgressDialog.setTitle("正在请求验证码");
            mProgressDialog.show();
            SMSSDK.getVerificationCode("86", mPhone);
        } else if (v == mSubmitBtn) {
            submitAction();
        }
    }

    private void submitAction() {
        String password = mPasswordEt.getText().toString();
        //为空验证
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(this, getString(R.string.please_input_password));
            mPasswordEt.requestFocus();
            return;
        }
        //密码长度验证
        if (password.length() < 6) {
            ToastUtil.show(this, getString(R.string.password_num_count_less_than_sex));
            mPasswordEt.requestFocus();
            return;
        }
        String nickName = mNickEt.getText().toString();
        //为空验证
        if (TextUtils.isEmpty(nickName)) {
            ToastUtil.show(this, getString(R.string.please_input_nickname));
            mNickEt.requestFocus();
            return;
        }
        //名称不能包含空格
        if (nickName.contains(" ")) {
            ToastUtil.show(this, getString(R.string.nickname_isnot_right));
            mNickEt.requestFocus();
            return;
        }
        // 提交验证
        String verificationCode = mVerifyCodeEt.getText().toString().trim();
        //验证是在短信服务器上
        SMSSDK.submitVerificationCode("86", mPhone, verificationCode);
        mProgressDialog.setTitle("正在校对验证码");
        mProgressDialog.show();


    }

    /**
     * 提交验证码成功后的执行事件
     *
     * @param result
     * @param data
     */
    private void afterSubmit(final int result, final Object data) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 验证码验证成功，准备注册
                    mProgressDialog.setTitle("正在注册");
                    registAction();
                } else {
                    // 根据服务器返回的网络错误，给toast提示
                    mProgressDialog.dismiss();
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;

                        JSONObject object = new JSONObject(
                                throwable.getMessage());
                        String des = object.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
                            ToastUtil.show(getApplicationContext(), des);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 如果木有找到资源，默认提示
                    ToastUtil.show(getApplicationContext(),
                            getString(R.string.network_error_retry_later));
                }
            }
        });
    }

    /**
     * 向服务器发送注册请求
     */
    private void registAction() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", mPhone);
        params.addBodyParameter("password", MD5.encrypt(mPasswordEt.getText().toString()));
        params.addBodyParameter("nick_name", mNickEt.getText().toString());
        new HttpUtils().send(HttpMethod.POST, Constants.URL_REGISTER_BY_PHONE, params, httpCallBack);


    }

    /**
     * 注册请求的回调接口
     */
    RequestCallBack<Object> httpCallBack = new RequestCallBack<Object>() {

        @Override
        public void onSuccess(final ResponseInfo<Object> responseInfo) {
            String result = (String) responseInfo.result;
            if ("0".equals(result)) {
                UserPrefrence.putUserName(getApplicationContext(), mPhone);
                UserPrefrence.putPassword(getApplicationContext(), mPasswordEt.getText().toString());
                UserPrefrence.putNickName(getApplicationContext(), mNickEt.getText().toString());
                UserPrefrence.putHeadPhoto(getApplicationContext(), "");
                UserPrefrence.setLogin(getApplicationContext(), true);

                Intent intent = new Intent(ReigistByphoneActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if ("1".equals(result)) {
                ToastUtil.show(getApplicationContext(), "昵称已经存在");
            } else {
                ToastUtil.show(getApplicationContext(), "服务器异常");
            }
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            ToastUtil.show(getApplicationContext(),
                    getString(R.string.network_error_retry_later));
            mProgressDialog.dismiss();
        }
    };

    /**
     * 请求验证码成功后的执行动作(可能获取成功，可能获取失败)
     *
     * @param result
     * @param data
     */
    protected void afterGet(final int result, final Object data) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mProgressDialog.dismiss();
                if (result == SMSSDK.RESULT_COMPLETE) {
                    countDown();
                    ToastUtil.show(getApplicationContext(),
                            getString(R.string.verification_code_is_sended));
                } else {
                    // 根据服务器返回的网络错误，给toast提示
                    try {
                        System.out.println("------------------15");
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;

                        JSONObject object = new JSONObject(
                                throwable.getMessage());
                        String des = object.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
                            System.out.println("------------------16");
                            ToastUtil.show(getApplicationContext(), des);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 如果木有找到资源，默认提示
                    ToastUtil.show(getApplicationContext(),
                            getString(R.string.network_error_retry_later));
                }
            }
        });
    }

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
}
