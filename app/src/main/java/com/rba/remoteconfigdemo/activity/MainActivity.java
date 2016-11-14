package com.rba.remoteconfigdemo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.rba.remoteconfigdemo.BuildConfig;
import com.rba.remoteconfigdemo.R;
import com.rba.remoteconfigdemo.model.entity.OptionEntity;
import com.rba.remoteconfigdemo.model.response.OptionResponse;
import com.rba.remoteconfigdemo.util.Constant;
import com.rba.remoteconfigdemo.view.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private OptionAdapter optionAdapter;
    private List<OptionEntity> optionEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        //firebaseRemoteConfig.setDefaults(R.xml.remote_config);

        optionEntityList = new ArrayList<>();

        RecyclerView rcvGeneral = (RecyclerView) findViewById(R.id.rcvGeneral);

        optionAdapter = new OptionAdapter(this, optionEntityList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvGeneral.setLayoutManager(layoutManager);
        rcvGeneral.setItemAnimator(new DefaultItemAnimator());
        rcvGeneral.setAdapter(optionAdapter);

        optionEntityList.addAll(loadData());
        optionAdapter.notifyDataSetChanged();

        getData();

    }

    private List<OptionEntity> loadData(){
        List<OptionEntity> optionEntityList = new ArrayList<>();
        optionEntityList.add(new OptionEntity("Filtro 1"));
        optionEntityList.add(new OptionEntity("Filtro 2"));
        optionEntityList.add(new OptionEntity("Filtro 3"));
        optionEntityList.add(new OptionEntity("Filtro 4"));
        optionEntityList.add(new OptionEntity("Filtro 5"));

        return  optionEntityList;

    }

    private void getData() {

        // in seconds
        long cacheExpiration = 3600;

        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // Default 12 hours
        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("x- remote", "fetch success");
                            firebaseRemoteConfig.activateFetched();
                            updateData();
                        } else {
                            Log.i("x- remote", "fetch error");
                        }
                    }
                });
    }

    private void updateData(){

        String filter = firebaseRemoteConfig.getString(Constant.FILTER);

        OptionResponse optionResponse = new Gson().fromJson(filter, OptionResponse.class);

        Log.i("x- filter", new Gson().toJson(optionResponse));

        this.optionEntityList.clear();

        List<OptionEntity> optionEntityList = new ArrayList<>();
        optionEntityList.add(new OptionEntity(optionResponse.getKey_1()));
        optionEntityList.add(new OptionEntity(optionResponse.getKey_2()));
        optionEntityList.add(new OptionEntity(optionResponse.getKey_3()));
        optionEntityList.add(new OptionEntity(optionResponse.getKey_4()));
        optionEntityList.add(new OptionEntity(optionResponse.getKey_5()));

        this.optionEntityList.addAll(optionEntityList);
        optionAdapter.notifyDataSetChanged();

    }


}
