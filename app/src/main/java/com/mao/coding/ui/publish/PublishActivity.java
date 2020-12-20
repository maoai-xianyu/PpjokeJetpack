package com.mao.coding.ui.publish;

import android.os.Bundle;
import android.widget.TextView;

import com.mao.coding.R;
import com.mao.libnavannotation.ActivityDestination;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = true)
public class PublishActivity extends AppCompatActivity implements LifecycleOwner {

    private PublishViewModel publishViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
        setContentView(R.layout.activity_publish);
        TextView textView = findViewById(R.id.text_publish);

        publishViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

    }

}
