package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.iut2.saeprojet.exemple.api.APIUserClient;
import fr.iut2.saeprojet.exemple.api.APIUserService;
import fr.iut2.saeprojet.exemple.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {

    private APIUserService apiInterface;

    // View
    private Button createUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Chargement de l'API
        apiInterface = APIUserClient.getAPIService();

        // init View
        createUserView = findViewById(R.id.createUser);

        //
        createUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                EditText editNomView = findViewById(R.id.editNom);
                EditText editJobView = findViewById(R.id.editJob);

                // Récupérer les informations contenues dans les vues
                final String nom = editNomView.getText().toString().trim();
                final String job = editJobView.getText().toString().trim();

                // Vérifier les informations fournies par l'utilisateur
                if (nom.isEmpty()) {
                    editNomView.setError("Nom required");
                    editNomView.requestFocus();
                    return;
                }

                if (job.isEmpty()) {
                    editJobView.setError("Job required");
                    editJobView.requestFocus();
                    return;
                }

                //
                createUser(nom, job);
            }
        });
    }

    private void createUser(String nom, String job) {

        User user = new User(nom, job);
        Call<User> call1 = apiInterface.createUser(user);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user1 = response.body();

                Toast.makeText(getApplicationContext(), user1.name + " " + user1.job + " " + user1.id + " " + user1.createdAt, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });
    }
}