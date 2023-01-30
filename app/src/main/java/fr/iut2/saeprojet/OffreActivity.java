package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.iut2.saeprojet.entity.Offre;

public class OffreActivity extends AppCompatActivity {
    private TextView retour;
    private Button candidater;
    private TextView intituleOffre;
    private TextView statutOffre;
    private TextView nomEntreprise;
    private TextView nomVille;
    private TextView url;

    private long id = -1;

    private String intitule = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        retour = findViewById(R.id.backOffres);
        candidater = findViewById(R.id.buttonCandidature);
        intituleOffre = findViewById(R.id.titreOffre);
        statutOffre = findViewById(R.id.statutOffre);
        nomEntreprise = findViewById(R.id.nomEntreprise);
        nomVille = findViewById(R.id.nomVille);
        url = findViewById(R.id.offreUrl);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("offre_id");
            intitule = extras.getString("offre_intitule");
            intituleOffre.setText(intitule);
            url.setText(extras.getString("offre_url"));
        }

        candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffreActivity.this, CandidatureActivity.class);
                intent.putExtra("offre_id", id);
                intent.putExtra("offre_intitule", intitule);
                startActivity(intent);
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffreActivity.this, ListOffresActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}