package com.example.l4.di;

import com.example.l4.ui.repo_gallery.RepoGalleryViewModel;
import com.example.l4.ui.user_gallery.UserGalleryViewModel;

import dagger.Component;

@Component(modules = {DaggerModule.class})
public interface AppComponent {
    void injectToUserGallery(UserGalleryViewModel viewModel);
    void injectToRepoGallery(RepoGalleryViewModel viewModel);
}
