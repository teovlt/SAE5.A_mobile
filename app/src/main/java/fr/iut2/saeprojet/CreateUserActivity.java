package fr.iut2.saeprojet;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends StageAppActivity{




    EditText nomView;
    EditText prenomView;
    EditText userNameView;
    EditText mdpView;
    EditText mdpConfirm;
    TextView signIn;
    Button suivant;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        suivant = findViewById(R.id.suivant1);
        nomView = findViewById(R.id.createNom);
        prenomView = findViewById(R.id.createPrenom);
        signIn = findViewById(R.id.signIn);
        userNameView = findViewById(R.id.createUserName);
        mdpView = findViewById(R.id.createMdp);
        mdpConfirm = findViewById(R.id.createConfirmMdp);




        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prenomView.getText().toString().isEmpty() || nomView.getText().toString().isEmpty() || userNameView.getText().toString().isEmpty()
                        || mdpView.getText().toString().isEmpty() || mdpConfirm.getText().toString().isEmpty() ){


                        Toast.makeText(CreateUserActivity.this, "Vous devez complétez tous les champs", Toast.LENGTH_SHORT).show();
                }else {


                    System.out.println(mdpView.getText().toString());
                    System.out.println(mdpConfirm.getText().toString());
                    if (!mdpView.getText().toString().equals(mdpConfirm.getText().toString())) {
                        Toast.makeText(CreateUserActivity.this, "Vos mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();

                    } else {
                        String prenom = prenomView.getText().toString();
                        String nom = nomView.getText().toString();
                        String userName = userNameView.getText().toString();
                        String mdp = mdpView.getText().toString();

                        Intent a = new Intent(CreateUserActivity.this, CreateUserActivity2.class);
                        a.putExtra(CreateUserActivity2.PRENOM_KEY,prenom);
                        a.putExtra(CreateUserActivity2.NOM_KEY,nom);
                        a.putExtra(CreateUserActivity2.USERNAME_KEY,userName);
                        a.putExtra(CreateUserActivity2.MDP_KEY,mdp);

                        startActivity(a);
                }

                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateUserActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });








    }


}
