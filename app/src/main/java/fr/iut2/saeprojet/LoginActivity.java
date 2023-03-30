package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.CompteEtudiantRequest;
import fr.iut2.saeprojet.entity.ComptesEtudiantsResponse;
import fr.iut2.saeprojet.entity.EtatCandidature;
import fr.iut2.saeprojet.entity.EtatCandidaturesResponse;
import fr.iut2.saeprojet.entity.EtatOffre;
import fr.iut2.saeprojet.entity.EtatOffresResponse;
import fr.iut2.saeprojet.entity.EtatRecherche;
import fr.iut2.saeprojet.entity.EtatRecherchesResponse;
import fr.iut2.saeprojet.entity.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends StageAppActivity {

    // View
    private EditText usernameView;
    private EditText passwordView;
    private Button loginView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init view
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginView = findViewById(R.id.login);

        //
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {

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
                    Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void refreshDateConnexion(CompteEtudiant compteEtudiant) {
        CompteEtudiantRequest req = new CompteEtudiantRequest();
        Date date = Calendar.getInstance().getTime();
        String dateFormatee = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
        req.derniereConnexion = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z", date);
        req.login = compteEtudiant.login;
        req.roles = compteEtudiant.roles;
        req.password = compteEtudiant.password;
        req.parcours = compteEtudiant.parcours;
        req.etatRecherche = compteEtudiant.etatRecherche;
        req.etudiant = compteEtudiant.etudiant;
        req.offreConsultees = compteEtudiant.offreConsultees;
        req.offreRetenues = compteEtudiant.offreRetenues;
        req.candidatures = compteEtudiant.candidatures;

        APIClient.updateCompteEtudiant(this, compteEtudiant.id, req, new ResultatAppel<CompteEtudiant>() {

            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant1) {
                setCompteDerniereConnexion(dateFormatee, 1, compteEtudiant._id);
            }
        });
    }

    private void getInformationEtudiant(String login) {
        String token = getToken();

        //
        Call<ComptesEtudiantsResponse> call = apiInterface.doGetCompteEtudiants("Bearer " + token);
        call.enqueue(new Callback<ComptesEtudiantsResponse>() {
            @Override
            public void onResponse(Call<ComptesEtudiantsResponse> call, Response<ComptesEtudiantsResponse> response) {

                if (response.isSuccessful()) {

                    for (CompteEtudiant compteEtudiant : response.body().compteEtudiants) {

                        if (compteEtudiant.login.equals(login)) {
                            setCompteId(compteEtudiant.id, compteEtudiant._id);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("derniere_connexion", getDerniereConnexion(1, compteEtudiant
                                    ._id));
                            refreshDateConnexion(compteEtudiant);
                            startActivity(intent);
                            break;
                        }
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Erreur connexion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComptesEtudiantsResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG", t.getMessage());

            }
        });

        APIClient.getEtatRecherches(this, new ResultatAppel<EtatRecherchesResponse>() {
            @Override
            public void traiterResultat(EtatRecherchesResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for (EtatRecherche etat : liste.etatRecherches) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }

        });

        APIClient.getEtatOffres(this, new ResultatAppel<EtatOffresResponse>() {
            @Override
            public void traiterResultat(EtatOffresResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for (EtatOffre etat : liste.etatOffres) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }
        });

        APIClient.getEtatCandidatures(this, new ResultatAppel<EtatCandidaturesResponse>() {
            @Override
            public void traiterResultat(EtatCandidaturesResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for (EtatCandidature etat : liste.etatCandidatures) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }
        });
    }
}