package com.myhexaville.login.Activities.LogandReg;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.myhexaville.login.APIanURLs.Api;
import com.myhexaville.login.APIanURLs.REtroURls;
import com.myhexaville.login.Adapters.Service_Adapter;
import com.myhexaville.login.AllParsings.GET_Services;
import com.myhexaville.login.AllParsings.GET_Services_Data;
import com.myhexaville.login.AllParsings.Type_Sub_Users;
import com.myhexaville.login.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration_Step_1 extends AppCompatActivity {
    RecyclerView serv_id;
    Service_Adapter service_adapter;
    private ProgressDialog progressDialog;
    String name, email, com_name, password, address, mobile;
    ArrayList<GET_Services_Data> get_services_data = new ArrayList<>();
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        serv_id = findViewById(R.id.serv_id);
//        name = getIntent().getStringExtra("name");
//        email = getIntent().getStringExtra("email");
//        com_name = getIntent().getStringExtra("com_name");
//        password = getIntent().getStringExtra("password");
//        address = getIntent().getStringExtra("address");
//        mobile = getIntent().getStringExtra("mobile");
//        Log.e("name", "" + name);
//        Log.e("email", "" + email);
//        Log.e("com_name", "" + com_name);
//        Log.e("password", "" + password);
//        Log.e("address", "" + address);
//        Log.e("mobile", "" + mobile);
         gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
       // set LayoutManager to RecyclerView
        GETAllServiceS();

    }

    private void GETAllServiceS() {
                progressDialog = new ProgressDialog(this);
        progressDialog.setMax(1000);
        progressDialog.setTitle("Getting Your Data");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit RetroLogin = new Retrofit.Builder()
                .baseUrl(REtroURls.The_Base).client(client).addConverterFactory(GsonConverterFactory.create())
                .build();
        Api AbloutApi = RetroLogin.create(Api.class);
        Call<GET_Services> get_aboutCall = AbloutApi.Get_ServicesUsersCall("3");
        get_aboutCall.enqueue(new Callback<GET_Services>() {
            @Override
            public void onResponse(Call<GET_Services> call, Response<GET_Services> response) {
               // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
             //   SubTypestrings = new String[response.body().getData().size()];
                Log.e("getact" , ""+response.body().getData().size());
                for(int k=0;k<response.body().getData().size();k++)
                {
                    Log.e("getact msain" , ""+response.body().getData().get(k).getTypeId());
                    get_services_data.add(new GET_Services_Data(response.body().getData().get(k).getId() ,response.body().getData().get(k).getTypeId() ,response.body().getData().get(k).getService() ,response.body().getData().get(k).getImage(),response.body().getData().get(k).getStatus()));
                   // type_sub_user_data.add(new Type_Sub_User_Data(response.body().getData().get(k).getId() ,response.body().getData().get(k).getTypeId(),response.body().getData().get(k).getSubtypename() ));
                    //SubTypestrings[k] = response.body().getData().get(k).getSubtypename();
                }
               // sub_main_type.setAdapter(new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_expandable_list_item_1 , SubTypestrings));
             //   sub_main_type.setVisibility(View.VISIBLE);
                service_adapter = new Service_Adapter(Registration_Step_1.this , get_services_data);
             //   gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                serv_id.addItemDecoration(new DividerItemDecoration(Registration_Step_1.this, LinearLayoutManager.VERTICAL));
                serv_id.setLayoutManager(gridLayoutManager);
                serv_id.setAdapter(service_adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GET_Services> call, Throwable t) {
                Toast.makeText(Registration_Step_1.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Registration_Step_1.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
