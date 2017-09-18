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

public class TelaCadastro extends AppCompatActivity {

    private EditText edtNome,edtEmail,edtSenha;
    private Button btnEntrar,btnVoltar;



    private FirebaseAuth mAutch;
    private FirebaseAuth.AuthStateListener mAutchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacadastro);

        MainActivity mainActivity = new MainActivity();



        edtNome = (EditText)findViewById(R.id.edtNome);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtNome = (EditText) findViewById(R.id.edtNome);

        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnVoltar = (Button)findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaCadastro.this,MainActivity.class);
                startActivity(it);
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
                        }
                    }
                });


    }

    public void criarUsuario(View v){

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        mAutch.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Usuarios")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reference.child("Nome").setValue(edtNome.getText().toString());
                            reference.child("UID").setValue(FirebaseAuth.getInstance().getCurrentUser()
                                    .getUid());
                            Intent intent = new Intent(TelaCadastro.this,TelaApp.class);
                            startActivity(intent);

                        }else{




                        }

                    }
                });

    }

}
