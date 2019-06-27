package com.yunkang.chat.home.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.ui.fragment.ServiceMessageFragment;
import com.yunkang.chat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends AppCompatActivity {
    @BindView(R.id.fr_content)
    FrameLayout fr_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        String title = "客服";
        ConsultSource source = new ConsultSource("msg", title, "custom information string");
        ServiceMessageFragment fragment = Unicorn.newServiceFragment(title, source, fr_content);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fr_content, fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }
}
