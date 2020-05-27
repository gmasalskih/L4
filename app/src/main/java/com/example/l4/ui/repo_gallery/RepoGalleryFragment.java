package com.example.l4.ui.repo_gallery;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.l4.databinding.FragmentRepoGalleryBinding;
import com.example.l4.entity.Repo;

public class RepoGalleryFragment extends Fragment {

    private RepoGalleryViewModel viewModel;
    private FragmentRepoGalleryBinding binding;
    private ListRepoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RepoGalleryFragmentArgs args = RepoGalleryFragmentArgs.fromBundle(requireArguments());
        viewModel = new ViewModelProvider(this, new RepoViewModelFactory(args.getLogin()))
                .get(RepoGalleryViewModel.class);
        adapter = new ListRepoAdapter(new ListRepoAdapter.RepoClickListener(this::onRepoSelected));
        binding = FragmentRepoGalleryBinding.inflate(inflater, container, false);
        binding.listRepos.setAdapter(adapter);
        binding.setLifecycleOwner(RepoGalleryFragment.this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObservers();
    }

    private void initObservers() {
        viewModel.getRepos()
                .observe(getViewLifecycleOwner(), repos -> adapter.submitList(repos));
        viewModel.getFilteredRepos()
                .observe(getViewLifecycleOwner(), repos -> adapter.submitList(repos));
        binding.enterRepo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.repoFilter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void onRepoSelected(Repo repo) {
        Toast.makeText(requireContext(), repo.htmlUrl, Toast.LENGTH_LONG).show();
    }
}