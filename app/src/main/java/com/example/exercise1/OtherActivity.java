package com.example.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class OtherActivity extends AppCompatActivity {
    final String TITLE = "Intent Example";

    Button mButton;
    TextView mTextView;
    RadioGroup mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        setTitle(TITLE);
        mButton = findViewById(R.id.back);
        Intent intent  = getIntent();
        String myData = intent.getStringExtra("data");
        mTextView = findViewById(R.id.passedData);
        mTextView.setText(myData);
        mButtons = findViewById(R.id.buttons);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedButtonID = mButtons.getCheckedRadioButtonId();
                if(selectedButtonID == -1){
                    Toast.makeText(getApplicationContext(),"Nothing is selected", Toast.LENGTH_SHORT).show();
                } else{
                    RadioButton selectedButton = findViewById(selectedButtonID);
                    Intent data = new Intent();
                    data.putExtra("selected",selectedButton.getText());
                    setResult(RESULT_OK,data);
                    finish();
                }
            }
        });
    }
}