package com.example.l4.ui.user_gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    UserViewModelFactory(Application application){
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(UserGalleryViewModel.class)){
            return (T) new UserGalleryViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}