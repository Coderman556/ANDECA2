package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<NotesTask> tasks;

    public TaskAdapter() {
        // Constructor
    }

    public void setTasks(List<NotesTask> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Bind data to each item
        NotesTask task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return tasks != null ? tasks.size() : 0;
    }

    // ViewHolder class
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the task_item_layout
            taskTextView = itemView.findViewById(R.id.taskTextView);
        }

        public void bind(NotesTask task) {
            // Bind data to the views
            taskTextView.setText(task.getCont());
        }
    }
}
