package com.example.otpgenerate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificatioOTOPActivity extends AppCompatActivity {
    private TextView inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificatio_otopactivity);
        final ProgressBar progressBar=findViewById(R.id.prograssBar);
        final Button verifivationPButton=findViewById(R.id.buttonVerify);
        verificationId= getIntent().getStringExtra("verificationId");


        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+91-%s",getIntent().getStringExtra("mobile")

        ));
        inputCode1=findViewById(R.id.inputCode1);
        inputCode2=findViewById(R.id.inputCode2);
        inputCode3=findViewById(R.id.inputCode3);
        inputCode4=findViewById(R.id.inputCode4);
        inputCode5=findViewById(R.id.inputCode5);
        inputCode6=findViewById(R.id.inputCode6);

        setOTPInputs();

        verifivationPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        ||inputCode2.getText().toString().trim().isEmpty()
                   ||inputCode3.getText().toString().trim().isEmpty()
                   ||inputCode4.getText().toString().trim().isEmpty()
                   ||inputCode5.getText().toString().trim().isEmpty()
                   || inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerificatioOTOPActivity.this, "Please valid the code", Toast.LENGTH_SHORT).show();
                    return;

                }
                String code = inputCode1.getText().toString() +
                        inputCode2.getText().toString()+
                        inputCode3.getText().toString()+
                        inputCode4.getText().toString()+
                        inputCode5.getText().toString()+
                        inputCode6.getText().toString();

                if (verificationId !=null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifivationPButton.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    verifivationPButton.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(VerificatioOTOPActivity.this, "the verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    findViewById(R.id.textResendOTP).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" +getIntent().getStringExtra("mobile"),
                                    60,
                                    TimeUnit.SECONDS,
                                    VerificatioOTOPActivity.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                            Toast.makeText(VerificatioOTOPActivity.this, "", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                            Toast.makeText(VerificatioOTOPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String NewverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            //super.onCodeSent(s, forceResendingToken);
                                            verificationId =NewverificationId;
                                            Toast.makeText(VerificatioOTOPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    });


                }
            }
        });
    }

    private void setOTPInputs() {

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                     if (s.toString().trim().isEmpty()){
                         inputCode2.requestFocus();
                     }

                if (s.length() == 6) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
}