package com.example.l4.ui.repo_gallery;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l4.databinding.ListItemRepoBinding;
import com.example.l4.entity.Repo;

public class ListRepoAdapter extends ListAdapter<Repo, ListRepoAdapter.ViewHolder> {

    private RepoClickListener clickListener;

    ListRepoAdapter(RepoClickListener clickListener) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemRepoBinding binding;

        public ViewHolder(@NonNull ListItemRepoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemRepoBinding binding = ListItemRepoBinding.inflate(layoutInflater, parent, false);
            return new ListRepoAdapter.ViewHolder(binding);
        }

        void bind(Repo repo, RepoClickListener clickListener) {
            binding.setRepo(repo);
            binding.setClickListener(clickListener);
        }
    }


    public static class DiffCallback extends DiffUtil.ItemCallback<Repo> {

        @Override
        public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.htmlUrl.equals(newItem.htmlUrl);
        }
    }

    public static class RepoClickListener {
        private Consumer<Repo> consumer;

        RepoClickListener(Consumer<Repo> consumer) {
            this.consumer = consumer;
        }

        public void onClick(Repo repo) {
            consumer.accept(repo);
        }
    }
}