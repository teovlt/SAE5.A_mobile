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




    EditText nom;
    EditText prenom;
    EditText userName;
    EditText mdp;
    EditText mdpConfirm;
    TextView signIn;
    Button suivant;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        suivant = findViewById(R.id.suivant1);
        nom = findViewById(R.id.createNom);
        prenom = findViewById(R.id.createPrenom);
        signIn = findViewById(R.id.signIn);
        userName = findViewById(R.id.createUserName);
        mdp = findViewById(R.id.createMdp);
        mdpConfirm = findViewById(R.id.createConfirmMdp);




        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prenom.getText().toString().isEmpty() || nom.getText().toString().isEmpty() || userName.getText().toString().isEmpty()
                        || mdp.getText().toString().isEmpty() || mdpConfirm.getText().toString().isEmpty() ){


                        Toast.makeText(CreateUserActivity.this, "Vous devez complétez tous les champs", Toast.LENGTH_SHORT).show();
                }else {


                    System.out.println(mdp.getText().toString());
                    System.out.println(mdpConfirm.getText().toString());
                    if (!mdp.getText().toString().equals(mdpConfirm.getText().toString())) {
                        Toast.makeText(CreateUserActivity.this, "Votre mot de passe n'est pas le même dans les deux champs", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent a = new Intent(CreateUserActivity.this, CreateUserActivity2.class);
                        a.putExtra("prenom",prenom.getText());
                        a.putExtra("nom",nom.getText());
                        a.putExtra("userName",userName.getText());
                        a.putExtra("mdp",mdp.getText());
                        a.putExtra("mdpConfirm",mdpConfirm.getText());
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
