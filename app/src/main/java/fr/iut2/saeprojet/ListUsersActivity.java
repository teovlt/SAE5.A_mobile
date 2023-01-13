package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fr.iut2.saeprojet.exemple.api.APIUserClient;
import fr.iut2.saeprojet.exemple.api.APIUserService;
import fr.iut2.saeprojet.exemple.entity.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUsersActivity extends AppCompatActivity {

    // API
    private APIUserService apiInterface;

    // View
    private TextView listUserText;
    private Button page1View;
    private Button page2View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        // Chargement de l'API
        apiInterface = APIUserClient.getAPIService();

        // Init view
        listUserText = findViewById(R.id.listUserText);
        page1View = findViewById(R.id.page1);
        page2View = findViewById(R.id.page2);

        //
        page1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGetUserList(1);
            }
        });

        //
        page2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGetUserList(2);
            }
        });

        // Chargement par default
        doGetUserList(1);
    }

    private void doGetUserList(int pageNumber) {

        /**
         GET List Resources
         **/
        Call<UserList> call = apiInterface.doGetUserList(pageNumber);
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {


                Log.d("TAG",response.code()+"");

                listUserText.clearComposingText();

                String displayResponse = "";

                UserList resource = response.body();
                Integer text = resource.page;
                Integer total = resource.total;
                Integer totalPages = resource.totalPages;
                List<UserList.Datum> datumList = resource.data;

                displayResponse += text + " Page\n" + total + " Total\n" + totalPages + " Total Pages\n";

                for (UserList.Datum datum : datumList) {
                    displayResponse += datum.id + " " + datum.avatar + " " + datum.first_name + " " + datum.last_name + "\n";
                }

                listUserText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });
    }
}