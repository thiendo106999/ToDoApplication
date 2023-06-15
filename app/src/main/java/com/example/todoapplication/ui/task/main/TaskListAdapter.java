package com.example.todoapplication.ui.task.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplication.R;
import com.example.todoapplication.data.models.Task;

import java.util.List;

import lombok.NonNull;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ToDoViewHolder> {

    private Context context;
    private List<Task> tasks;
    private EventListener mListener;

    public void setEvent(EventListener listener) {
        mListener = listener;
    }

    public TaskListAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);

        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.tvName.setText(tasks.get(position).getName());
        holder.tvDescription.setText(tasks.get(position).getDescription());
        holder.tvDate.setText("Date: " + tasks.get(position).getDate());
        holder.btnDelete.setOnClickListener(v -> mListener.onDelete(position));
        holder.cardView.setOnClickListener(v -> mListener.onShowDetailScreen(position));
        holder.btnEdit.setOnClickListener(v -> mListener.onShowEditScreen(position));
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvDescription;
        ImageButton btnEdit, btnDelete;
        CardView cardView;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvDate = itemView.findViewById(R.id.date);
            tvDescription = itemView.findViewById(R.id.description);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            cardView = itemView.findViewById(R.id.item_todo);
        }
    }

    public interface EventListener {
        void onDelete(int position);
        void onShowDetailScreen(int position);
        void onShowEditScreen(int position);
    }
}
