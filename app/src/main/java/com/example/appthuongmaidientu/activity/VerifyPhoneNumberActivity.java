package com.example.appthuongmaidientu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.control.MemoryData;
import com.example.appthuongmaidientu.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    private EditText edt_OTP;
    private Button btn_vertifyOTP, btn_backSignUp;
    private TextView tv_resendOTP;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String mphone, mVertificationId;
    private String vertificationCodeBySystem;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private User us;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    private static final String TAG = VerifyPhoneNumberActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        mAuth = FirebaseAuth.getInstance();

        edt_OTP = findViewById(R.id.edt_OTP);
        progressBar = findViewById(R.id.progress_bar);
        btn_vertifyOTP = findViewById(R.id.btn_vertifyOTP);
        btn_backSignUp = findViewById(R.id.btn_backSignUp);
        tv_resendOTP = findViewById(R.id.tv_resendOtp);

        mphone = getIntent().getStringExtra("phone");
        mVertificationId = getIntent().getStringExtra("mVerificationID");

        //sendVertificationCodeToUser(mphone);

        btn_vertifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edt_OTP.getText().toString().trim();
                confirmOTP(otp);
            }
        });

        btn_backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyPhoneNumberActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        //
        tv_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP();
            }
        });
    }
    //cũ
    private void resendOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Xác thực thất bại!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                                mVertificationId = verificationID;
                                mForceResendingToken = forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void confirmOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVertificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            Integer dh = getIntent().getIntExtra("dh", 0);

                            if (dh == 1){
                                goToSignInActivity(user.getPhoneNumber());
                            }else {
                                goToChangePasswodActivity(user.getPhoneNumber());
                            }


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Mã đăng nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToSignInActivity(String phoneNumber) {
        Toast.makeText(VerifyPhoneNumberActivity.this, "Đăng kí thành công !", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(VerifyPhoneNumberActivity.this, LoginActivity.class);
        us=new User();
        us.setTenUser(getIntent().getStringExtra("name"));
        us.setSDT(phoneNumber);
        us.setMatKhau(getIntent().getStringExtra("password"));
        us.setEmail(getIntent().getStringExtra("email"));
        us.setAnhnen("https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/anh_nen_default.png?alt=media&token=6dd72a7f-d897-40cd-b0b5-67b5a622dc19");
        us.setImgUS("https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/icon_user_default.jpg?alt=media&token=ce5c75d8-4d3b-4d20-8e34-de952becf786");
        us.setDiaChi("");
        databaseReference.child("users").child(phoneNumber).setValue(us);
        //intent.putExtra("phone", phoneNumber);
        startActivity(intent);
    }

    private void goToChangePasswodActivity(String phoneNumber) {
        Toast.makeText(VerifyPhoneNumberActivity.this, "Nhập mật khẩu mới để tiếp tục", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(VerifyPhoneNumberActivity.this, ChangePasswordActivity.class);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
    }

}