package com.example.l4.ui.repo_gallery;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RepoViewModelFactory implements ViewModelProvider.Factory {

    private String login;

    RepoViewModelFactory(String login){
        this.login = login;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(RepoGalleryViewModel.class)){
            return (T) new RepoGalleryViewModel(login);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
