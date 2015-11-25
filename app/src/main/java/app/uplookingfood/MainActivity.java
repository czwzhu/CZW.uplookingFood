package app.uplookingfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {
    private boolean firstCome;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        editor = preferences.edit();
/**
 * 登录系统显示页面，第一次进入系统将进入 引导页面
 *                   第二次进入系统将进入 主界面
 */
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    firstCome = preferences.getBoolean("firstRun", firstCome);
                    if (!firstCome)

                    {

                        editor.putBoolean("firstRun",true);
                        editor.commit();
                            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                            startActivity(intent);
                            finish();
                    }
                    if(firstCome)
                    {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                }




        }.sendEmptyMessageDelayed(0,2500);

   }

}
