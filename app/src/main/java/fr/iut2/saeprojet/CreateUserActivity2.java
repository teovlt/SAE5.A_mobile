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

    public static final String
            PRENOM_KEY = "prenom_key";




    public static final String NOM_KEY = "nom_key" ;
    public static String USERNAME_KEY = "userName_key" ;
   public static String MDP_KEY = "mdp_key" ;


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
       // String nom = getIntent().getStringExtra(NOM_KEY);
       // String userName = getIntent().getStringExtra(USERNAME_KEY);
        //String mdp = getIntent().getStringExtra(MDP_KEY);
        String prenom = getIntent().getStringExtra(PRENOM_KEY);
        String nom = getIntent().getStringExtra(NOM_KEY);
        String userName = getIntent().getStringExtra(USERNAME_KEY);
        String mdp = getIntent().getStringExtra(MDP_KEY);







        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity2.this, android.R.layout.simple_spinner_item, parcoursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcours.setAdapter(adapter);



        createAcc.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || numINE.getText().toString().isEmpty() ){
                    Toast.makeText(CreateUserActivity2.this, "Vous devez complétez tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    //createAccount();
                    // Toast.makeText(CreateUserActivity2.this, "Nom complet :  "+ prenom + " "+nom , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Nom d'utilisateur : "+userName , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Mot de passe :  "+ mdp, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Adresse email :  "+ email.getText().toString() , Toast.LENGTH_SHORT).show();

                }
            }



        });



    }




}
