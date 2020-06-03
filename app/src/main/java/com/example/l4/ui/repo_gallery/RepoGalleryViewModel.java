package com.example.l4.ui.repo_gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l4.api.API;
import com.example.l4.entity.Repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RepoGalleryViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishSubject<CharSequence> subject = PublishSubject.create();

    private MutableLiveData<List<Repo>> _repos = new MutableLiveData<>();

    LiveData<List<Repo>> getRepos() {
        return _repos;
    }

    private MutableLiveData<List<Repo>> _filteredRepos = new MutableLiveData<>();

    LiveData<List<Repo>> getFilteredRepos() {
        return _filteredRepos;
    }

    RepoGalleryViewModel(String login) {
        API.GitHubService api = API.getInstance().getApi();
        compositeDisposable.add(api.getReposOfUser(login).observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos -> _repos.setValue(repos)));
        compositeDisposable.add(subject
                .observeOn(Schedulers.computation())
                .map(this::getSortedListUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos -> _filteredRepos.setValue(repos)));
    }

    void repoFilter(CharSequence s) {
        subject.onNext(s);
    }

    private List<Repo> getSortedListUser(CharSequence charSequence){
        List<Repo> list = new ArrayList<>();
        if (_repos.getValue() == null) return list;
        for (Repo repo : _repos.getValue())
            if (repo.name.toLowerCase().contains(charSequence.toString().toLowerCase()))
                list.add(repo);
        return list;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}