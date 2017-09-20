package com.tcc.android.tcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {



    private EditText edtEmail,edtSenha;
    private Button btnEntrar,btnCadastrar;

    private FirebaseAuth mAutch;
    private FirebaseAuth.AuthStateListener mAutchList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);


        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtSenha = (EditText)findViewById(R.id.edtSenha);

        btnEntrar = (Button)findViewById(R.id.btnEntrar);

        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TelaCadastro.class);
                startActivity(intent);
            }
        });



        mAutch = FirebaseAuth.getInstance();

        mAutchList = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null){
                    Log.d("log","usuario conectado"+user.getUid());
                    Intent it = new  Intent(getApplicationContext(),TelaApp.class);
                    startActivity(it);

                }else {
                    Log.d("log","usuario não conectado");
                }

            }
        };


    }

    public void clicaLogin(View V) {
        mAutch.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("log", "Falha na autenticação");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Flaha no Login",Toast.LENGTH_SHORT);
                            toast.show();

                        }else{
                            Intent intent = new Intent(MainActivity.this,TelaApp.class);
                            startActivity(intent);

                        }
                    }
                });


    }



    @Override
    protected void onStart() {
        super.onStart();

        mAutch.addAuthStateListener(mAutchList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAutchList != null)
        mAutch.removeAuthStateListener(mAutchList);
    }





}
