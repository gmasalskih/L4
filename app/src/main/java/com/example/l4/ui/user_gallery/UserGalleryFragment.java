package com.example.l4.ui.user_gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.l4.databinding.FragmentUserGalleryBinding;
import com.example.l4.entity.User;
import com.jakewharton.rxbinding3.widget.RxTextView;

public class UserGalleryFragment extends Fragment {

    private UserGalleryViewModel viewModel;
    private FragmentUserGalleryBinding binding;
    private ListUsersAdapter adapter;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new UserViewModelFactory(getActivity().getApplication())).get(UserGalleryViewModel.class);
        adapter = new ListUsersAdapter(new ListUsersAdapter.UserClickListener(this::onUserSelected));
        binding = FragmentUserGalleryBinding.inflate(inflater, container, false);
        binding.listUsers.setAdapter(adapter);
        binding.setLifecycleOwner(UserGalleryFragment.this);
        binding.setViewModel(viewModel);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.saveBtn.setText("Save to DB");
        binding.loadBtn.setText("Load from DB");

        initObservers();
    }

    private void initObservers() {
        viewModel.getUsers()
                .observe(getViewLifecycleOwner(), userList -> adapter.submitList(userList));
        viewModel.getFilteredUsers()
                .observe(getViewLifecycleOwner(), userList -> adapter.submitList(userList));
        viewModel.saveTime()
                .observe(getViewLifecycleOwner(), msg -> binding.saveBtn.setText(msg));
        viewModel.loadTime()
                .observe(getViewLifecycleOwner(), msg -> binding.loadBtn.setText(msg));
        viewModel.userFilter(RxTextView.textChangeEvents(binding.enterLogin));
    }

    private void onUserSelected(User user) {
        UserGalleryFragmentDirections.ActionNavUserGalleryToNavRepoGallery action =
                UserGalleryFragmentDirections.actionNavUserGalleryToNavRepoGallery(user.login);
        navController.navigate(action);
    }
}
