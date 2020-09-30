package com.dpk.hammoq.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dpk.hammoq.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AgentLoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText inputEditTextEmail, inputEditTextPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        textInputLayoutEmail = findViewById(R.id.Email);
        textInputLayoutPassword = findViewById(R.id.Password);

        inputEditTextEmail = findViewById(R.id.et_email);
        inputEditTextPassword = findViewById(R.id.et_password);

        inputEditTextEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayoutEmail.setBackgroundResource(R.drawable.agent_text_input_border_blue);
                textInputLayoutPassword.setBackgroundResource(R.drawable.agent_text_input_border_greylight);
                return false;
            }
        });

        inputEditTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textInputLayoutPassword.setBackgroundResource(R.drawable.agent_text_input_border_blue);
                textInputLayoutEmail.setBackgroundResource(R.drawable.agent_text_input_border_greylight);
                return false;
            }
        });

    }

    public void onClick1(View view1){
        Toast.makeText(this, "Login is Pressed", Toast.LENGTH_SHORT).show();
    }
    public void onClick2(View view2){
        Toast.makeText(this, "Create a new account is Pressed", Toast.LENGTH_SHORT).show();
    }
    public void onClick3(View view3){
        Toast.makeText(this, "Forget Password is Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
               super.onBackPressed();
    }
}
