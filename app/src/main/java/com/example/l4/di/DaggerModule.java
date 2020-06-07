package com.example.l4.di;

import android.content.Context;

import com.example.l4.api.API;
import com.example.l4.api.DAO;
import com.example.l4.api.DB;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerModule {

    final Context context;

    public DaggerModule(Context context){
        this.context = context;
    }

    @Provides
    DAO getDao(){
        return DB.getInstance(context).getDao();
    }

    @Provides
    API.GitHubService getGitHubService(){
        return API.getInstance().getApi();
    }
}