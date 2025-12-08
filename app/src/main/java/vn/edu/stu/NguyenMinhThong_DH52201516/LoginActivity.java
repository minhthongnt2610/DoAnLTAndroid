package vn.edu.stu.NguyenMinhThong_DH52201516;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.NguyenMinhThong_DH52201516.sqlite.DatabaseCopyHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DatabaseCopyHelper dbCopy = new DatabaseCopyHelper(this);
        dbCopy.checkAndCopyDatabase();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyLogin();
            }
        });
    }

    private void xulyLogin() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if (username.equals("admin") && password.equals("123")) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, categoryListActivity.class);
            startActivity(intent);
            finish();
        } else {
            edtUsername.setError(getString(R.string.login_error));
            edtPassword.setError(getString(R.string.login_error));
        }
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        edtUsername.setText("admin");
        edtPassword.setText("123");
    }
}