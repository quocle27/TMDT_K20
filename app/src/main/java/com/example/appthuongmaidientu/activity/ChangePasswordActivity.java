package com.example.appthuongmaidientu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.appthuongmaidientu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText edt_password, edt_cfpassword;
    Button btn_ChangePassword, btn_Exit;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    String password, cfpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        String phone = getIntent().getStringExtra("phone");

        edt_password = findViewById(R.id.edt_password);
        edt_cfpassword = findViewById(R.id.edt_cfpassword);
        btn_ChangePassword= findViewById(R.id.btn_ChangePassword);
        btn_Exit= findViewById(R.id.btn_Exit);

        //Quay lại trang quên mật khẩu
        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Tiến hành đổi mật khẩu
        btn_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = edt_password.getText().toString().trim();
                cfpassword = edt_cfpassword.getText().toString().trim();

                if (!password.equals(cfpassword)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng xác nhận lại mật khẩu!", Toast.LENGTH_SHORT).show();
                } else {
                    //Đổi pass
                    databaseReference.child("users").child(getIntent().getStringExtra("phone")).child("matKhau").setValue(password);
                    Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    startActivity(intent);
                }
            }
        });
    }
}