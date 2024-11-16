package zjc.devicemanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import zjc.devicemanage.R;
import zjc.devicemanage.model.User;
import zjc.devicemanage.service.UserService;
import zjc.devicemanage.service.imp.UserServiceImp;
import zjc.devicemanage.util.MyApplication;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    //委托类的用户对象
    private User loginUser;
    //代理接口
    private UserService userService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 获得3个UI控件对象
        loginUsername=findViewById(R.id.login_username);
        loginPassword=findViewById(R.id.login_password);
        loginButton=findViewById(R.id.login_button);
        // 执行按钮btnLogin的onClick事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userService = new UserServiceImp(LoginActivity.this);
                userService.loginValidate(loginUsername.getText().toString(), loginPassword.getText().toString());
            }
        });
    }
    // 回调函数loginCallback
    public void loginCallback(User loginUserFromAgent) {
        this.loginUser = loginUserFromAgent;
        if(this.loginUser.getUserID() == null){
            MyApplication.subThreadToast("账号用户名或者密码输入错误");
        }else{
            //将已登录成功的用户编号写入偏好文件中
            MyApplication.setUser_id(this.loginUser.getUserID());
            //不能采用第一种方式在子线程中显示Toast
            //MyApplication.subThreadToast("用户编号（" + this.loginUser.getUserID()+ ")登录成功");
            //必须采用第二种方式在子线程中显示Toast
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(LoginActivity.this,"用户编号（" +
                                    LoginActivity.this.loginUser.getUserID()+ ")登录成功",
                            Toast.LENGTH_LONG).show();
                }
            });
            Log.i("zjc","用户编号("+ this.loginUser.getUserID() + ")登录成功");
            //跳转到MainActivity活动
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            //关闭当前LoginActivity活动
            finish();
        }
    }
}