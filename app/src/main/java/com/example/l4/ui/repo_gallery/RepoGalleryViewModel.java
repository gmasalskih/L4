package com.example.l4.ui.repo_gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l4.api.API;
import com.example.l4.entity.Repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RepoGalleryViewModel extends ViewModel {

    private List<Disposable> disposables = new ArrayList<>();
    private PublishSubject<CharSequence> subject = PublishSubject.create();

    private MutableLiveData<List<Repo>> _repos = new MutableLiveData<>();

    LiveData<List<Repo>> getRepos() {
        return _repos;
    }

    private MutableLiveData<List<Repo>> _filteredRepos = new MutableLiveData<>();

    LiveData<List<Repo>> getFilteredRepos() {
        return _filteredRepos;
    }

    RepoGalleryViewModel(String login){
        API.GitHubService api = API.getInstance().getApi();
        disposables.add(api.getReposOfUser(login).observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos -> _repos.setValue(repos)));
        disposables.add(subject.subscribeOn(Schedulers.computation())
                .map(charSequence->{
                    List<Repo> list = new ArrayList<>();
                    for (Repo repo:_repos.getValue())
                        if (repo.name.toLowerCase().contains(charSequence.toString().toLowerCase())) list.add(repo);
                    return list;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(repos -> _filteredRepos.setValue(repos)));

    }

    private void clearDisposable() {
        for (Disposable d : disposables) d.dispose();
        disposables.clear();
    }


    void repoFilter(CharSequence s){
        subject.onNext(s);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearDisposable();
    }
}
