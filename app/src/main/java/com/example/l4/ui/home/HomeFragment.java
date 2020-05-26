package com.example.l4.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.l4.databinding.FragmentHomeBinding;
import com.example.l4.entity.User;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;
    private ListUsersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        adapter = new ListUsersAdapter(new ListUsersAdapter.UserClickListener(this::onUserSelected));
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.listUsers.setAdapter(adapter);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObservers();
    }

    private void initObservers() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), userList -> {
            adapter.submitList(userList);
        });
    }

    private void onUserSelected(User user) {

    }
}