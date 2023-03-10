package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateUserActivity2 extends StageAppActivity{



    EditText email;
    EditText numINE;



    Spinner parcours;
    Button createAcc;


    String[] parcoursList = {"Parcours A","Parcours B"};




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user2);
       // parcoursList.add("Déploiement d'applications and shit");
        //parcoursList.add("Réseau et administation ect ...");
        //parcoursList.add("Un troisième parcours random");
        parcours = findViewById(R.id.createParcours);
        createAcc = findViewById(R.id.createAccount);
        email =  findViewById(R.id.createEmail);
        numINE =  findViewById(R.id.createNumINE);




        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity2.this, android.R.layout.simple_spinner_item, parcoursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcours.setAdapter(adapter);



        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || numINE.getText().toString().isEmpty() ){
                    Toast.makeText(CreateUserActivity2.this, "Vous devez complétez tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    //creation de compte mon gars
                }
            }
        });


          /*

        private void createAccount() {

            String login = usernameView.getText().toString();
            String password = passwordView.getText().toString();

            Auth auth = new Auth(login, password);

            Call<LoginResponse> call = apiInterface.login(auth);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.isSuccessful()) {
                        setAuthData(login, response.body().token);
                        getInformationEtudiant(login);
                    } else {
                        Toast.makeText(LoginActivity.this, "Nom incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                    call.cancel();
                    Log.e("TAG",t.getMessage());
                }
            });
        }


        */




    }
}
