package com.example.l4.ui.user_gallery;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.l4.api.API;
import com.example.l4.api.DAO;
import com.example.l4.api.DB;
import com.example.l4.entity.User;
import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserGalleryViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DAO db;

    private MutableLiveData<List<User>> _users = new MutableLiveData<>();

    LiveData<List<User>> getUsers() {
        return _users;
    }

    private MutableLiveData<String> _saveTime = new MutableLiveData<>();

    LiveData<String> saveTime() {
        return _saveTime;
    }

    private MutableLiveData<String> _loadTime = new MutableLiveData<>();

    LiveData<String> loadTime() {
        return _loadTime;
    }

    private MutableLiveData<List<User>> _filteredUsers = new MutableLiveData<>();

    LiveData<List<User>> getFilteredUsers() {
        return _filteredUsers;
    }

    public UserGalleryViewModel(Application application) {
        super(application);
        db = DB.getInstance(application.getApplicationContext()).getDao();
        API.GitHubService api = API.getInstance().getApi();
        compositeDisposable.add(api.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _users.setValue(userList)));
    }

    void userFilter(InitialValueObservable<TextViewTextChangeEvent> charSequenceObservable) {
        compositeDisposable.add(charSequenceObservable
                .skipInitialValue()
                .distinctUntilChanged()
                .observeOn(Schedulers.computation())
                .switchMap(this::getSortedListUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> _filteredUsers.setValue(userList)));
    }

    private Observable<List<User>> getSortedListUser(TextViewTextChangeEvent charSequence) {
        if (_users.getValue() == null) return Observable.just(new ArrayList<>());
        return Observable.fromIterable(_users.getValue())
                .filter(user -> user.login.toLowerCase().contains(charSequence.getText().toString().toLowerCase()))
                .toList()
                .toObservable();
    }

    public void saveUsersToDb() {
        List<User> userList = _users.getValue();
        if (userList != null && !userList.isEmpty()) {
            long first = new Date().getTime();
            compositeDisposable.add(Single.just(userList)
                    .observeOn(Schedulers.io())
                    .subscribe(users -> db.insertUser(users)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    compositeDisposable.add(d);
                                }

                                @Override
                                public void onComplete() {
                                    _saveTime.setValue("save time:"+(new Date().getTime() - first)+"ms");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("err", e.getMessage(), e);
                                }
                            })));
        }
    }

    public void loadUsersFromDb() {
        long first = new Date().getTime();
        compositeDisposable.add(db.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if(!users.isEmpty()){
                        _users.setValue(users);
                        _loadTime.setValue("load time:"+(new Date().getTime() - first)+"ms");
                    }
                }));
    }

    public void clearDb(){
        db.clearAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        _saveTime.setValue("Save to DB");
                        _loadTime.setValue("Load from DB");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("err", e.getMessage(), e);
                    }
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}