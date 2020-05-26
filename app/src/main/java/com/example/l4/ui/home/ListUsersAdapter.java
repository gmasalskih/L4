package com.example.l4.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l4.databinding.ListItemUserBinding;
import com.example.l4.entity.User;

public class ListUsersAdapter extends ListAdapter<User, ListUsersAdapter.ViewHolder> {

    private UserClickListener clickListener;

    public ListUsersAdapter(UserClickListener clickListener) {
        super(new DiffCallback());
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemUserBinding binding;

        private ViewHolder(@NonNull ListItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemUserBinding binding = ListItemUserBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }

        public void bind(User user, UserClickListener clickListener) {
            binding.setUser(user);
            binding.setClickListener(clickListener);
        }
    }

    public static class DiffCallback extends DiffUtil.ItemCallback<User> {

        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.login.equals(newItem.login);
        }
    }

    public static class UserClickListener {

        private Consumer<User> consumer;

        public UserClickListener(Consumer<User> consumer) {
            this.consumer = consumer;
        }

        public void onClick(User user) {
            consumer.accept(user);
        }
    }
}