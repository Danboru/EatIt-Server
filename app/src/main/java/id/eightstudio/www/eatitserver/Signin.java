package id.eightstudio.www.eatitserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.eightstudio.www.eatitserver.Common.Common;
import id.eightstudio.www.eatitserver.Model.User;

public class Signin extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressBar = new ProgressDialog(Signin.this);
                progressBar.setMessage("Tunggu Sebentar");
                progressBar.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            progressBar.dismiss();

                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString()); //Set phone

                            if (Boolean.parseBoolean(user.getIsStaff())) { //If isStaff == True

                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent intent = new Intent(Signin.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Signin.this, "Gagal", Toast.LENGTH_SHORT).show();//Wrong password
                                }

                            } else {
                                Toast.makeText(Signin.this, "Login with staff account", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressBar.dismiss();
                            Toast.makeText(Signin.this, "Staff tidak Terdafatar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Signin.this, "Server Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}

