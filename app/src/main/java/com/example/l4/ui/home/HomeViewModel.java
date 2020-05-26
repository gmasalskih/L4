package com.example.l4.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l4.api.API;
import com.example.l4.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HomeViewModel extends ViewModel {

    private API.GitHubService api;
    private List<Disposable> disposables = new ArrayList<>();

    private MutableLiveData<List<User>> _users = new MutableLiveData<>();
    public LiveData<List<User>> getUsers(){
        return _users;
    }

    public HomeViewModel(){
        api = API.getInstance().getApi();
        disposables.add(api.getUsers().observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userList-> _users.setValue(userList)));
    }

    private void clearDisposable(){
        for (Disposable d : disposables) d.dispose();
        disposables.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearDisposable();
    }
}