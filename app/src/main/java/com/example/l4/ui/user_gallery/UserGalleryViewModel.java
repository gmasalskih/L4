package com.example.l4.ui.user_gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l4.api.API;
import com.example.l4.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class UserGalleryViewModel extends ViewModel {

    private List<Disposable> disposables = new ArrayList<>();
    private PublishSubject<CharSequence> subject = PublishSubject.create();

    private MutableLiveData<List<User>> _users = new MutableLiveData<>();

    LiveData<List<User>> getUsers() {
        return _users;
    }

    private MutableLiveData<List<User>> _filteredUsers = new MutableLiveData<>();

    LiveData<List<User>> getFilteredUsers() {
        return _filteredUsers;
    }

    public UserGalleryViewModel() {
        API.GitHubService api = API.getInstance().getApi();
        disposables.add(api.getUsers().observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _users.setValue(userList)));
        disposables.add(subject.subscribeOn(Schedulers.computation())
                .map(charSequence -> {
                    List<User> list = new ArrayList<>();
                    for (User user : _users.getValue())
                        if (user.login.toLowerCase().contains(charSequence.toString().toLowerCase())) list.add(user);
                    return list;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _filteredUsers.setValue(userList)));
    }

    private void clearDisposable() {
        for (Disposable d : disposables) d.dispose();
        disposables.clear();
    }

    void userFilter(CharSequence s) {
        subject.onNext(s);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearDisposable();
    }
}