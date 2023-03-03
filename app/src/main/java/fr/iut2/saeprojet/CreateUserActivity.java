package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CreateUserActivity extends StageAppActivity{




    EditText nom;
    EditText job;
    Button creerUser;
    TextView signIn;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);


        nom = findViewById(R.id.editNom);
        job = findViewById(R.id.editJob);
        creerUser = findViewById(R.id.createUser);
        signIn = findViewById(R.id.signIn);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateUserActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
