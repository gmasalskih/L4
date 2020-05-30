package com.example.l4.ui.user_gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l4.api.API;
import com.example.l4.entity.User;
import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class UserGalleryViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
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
        compositeDisposable.add(api.getUsers().observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _users.setValue(userList)));
    }

    void userFilter(InitialValueObservable<TextViewTextChangeEvent> charSequenceObservable) {
        compositeDisposable.add(charSequenceObservable
                .skipInitialValue()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.computation())
                .switchMap(this::getSortedListUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _filteredUsers.setValue(userList)));
    }

    private Observable<List<User>> getSortedListUser(TextViewTextChangeEvent charSequence) {
        List<User> list = new ArrayList<>();
        if (_users.getValue() == null) return Observable.just(new ArrayList<>());
        for (User user : _users.getValue())
            if (user.login.toLowerCase().contains(charSequence.getText().toString().toLowerCase()))
                list.add(user);
        return Observable.just(list);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}