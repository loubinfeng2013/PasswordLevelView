package com.loubinfeng.www.passwordlevelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.loubinfeng.passwordlevel.PasswordLevelView;

public class MainActivity extends AppCompatActivity {

    private PasswordLevelView pwd_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pwd_level = (PasswordLevelView)findViewById(R.id.pwd_level);
    }

    public void handleClick(View view) {
        switch (view.getId()) {
            case R.id.n0:
                pwd_level.setLevel(0);
                break;
            case R.id.n1:
                pwd_level.setLevel(1);
                break;
            case R.id.n2:
                pwd_level.setLevel(2);
                break;
            case R.id.n3:
                pwd_level.setLevel(3);
                break;

        }
    }
}
