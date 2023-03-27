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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.CompteEtudiantRequest;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.EtudiantRequest;

public class CreateUserActivity2 extends StageAppActivity{

    public static final String PRENOM_KEY = "prenom_key";
    public static final String NOM_KEY = "nom_key" ;
    public static String USERNAME_KEY = "userName_key" ;
   public static String MDP_KEY = "mdp_key" ;


    EditText email;
    EditText numINE;
    Spinner parcours;
    Button createAcc;
    String prenom;
    String nom;
    String login;
    String mdp;
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
        prenom = getIntent().getStringExtra(PRENOM_KEY);
        nom = getIntent().getStringExtra(NOM_KEY);
        login = getIntent().getStringExtra(USERNAME_KEY);
        mdp = getIntent().getStringExtra(MDP_KEY);







        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity2.this, android.R.layout.simple_spinner_item, parcoursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcours.setAdapter(adapter);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || numINE.getText().toString().isEmpty() ){
                    Toast.makeText(CreateUserActivity2.this, "Vous devez complétez tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    //Création du compte et connexion->redirection sur la page main
                    createAccount();
                    // Toast.makeText(CreateUserActivity2.this, "Nom complet :  "+ prenom + " "+nom , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Nom d'utilisateur : "+userName , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Mot de passe :  "+ mdp, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CreateUserActivity2.this, "Adresse email :  "+ email.getText().toString() , Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void createAccount(){
        EtudiantRequest reqEtudiant = new EtudiantRequest();
        reqEtudiant.nom= nom;
        reqEtudiant.numeroINE = numINE.getText().toString();
        reqEtudiant.prenom = prenom;
        reqEtudiant.email = email.getText().toString();
        APIClient.createEtudiant(this, reqEtudiant, new ResultatAppel<Etudiant>(){
            @Override
            public void traiterResultat(Etudiant etudiant) {
                addCompteEtudiant(etudiant);

            }

            @Override
            public void traiterErreur() {

            }
        });

    }
    private void addCompteEtudiant(Etudiant etudiant){
        //L'étudiant a été créé, au tour du compte étudiant
        CompteEtudiantRequest req = new CompteEtudiantRequest();
        Date date = Calendar.getInstance().getTime();
        req.derniereConnexion =String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z", date);
        req.login = login;
        req.password = mdp;
        req.roles= new ArrayList<>();
        req.roles.add("ROLE_ETUDIANT");
        req.roles.add("ROLE_USER");
        req.candidatures = new ArrayList<>();
        req.offreRetenues= new ArrayList<>();
        req.offreConsultees = new ArrayList<>();
        String p = parcours.getSelectedItem().toString();
        req.parcours = p.substring(p.length() - 1);
        req.etatRecherche = "/api/etat_recherche/1";
        req.etudiant = etudiant._id;


        System.out.println(req.derniereConnexion);
        System.out.println(req.login);
        System.out.println(req.password);
        System.out.println(req.roles);
        System.out.println(req.candidatures);
        System.out.println(req.offreRetenues);
        System.out.println(req.offreConsultees);
        System.out.println(req.parcours);
        System.out.println(req.etatRecherche);
        System.out.println(req.etudiant);




        APIClient.createCompteEtudiant(this, req, new ResultatAppel<CompteEtudiant>(){

            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant1) {
                if(compteEtudiant1!=null){
                    Intent i = new Intent(CreateUserActivity2.this, LoginActivity.class);
                    startActivity(i);
                }else{
                    System.out.println("erreur compte etudiant null");
                }

            }

            @Override
            public void traiterErreur() {

            }
        });

    }
}
