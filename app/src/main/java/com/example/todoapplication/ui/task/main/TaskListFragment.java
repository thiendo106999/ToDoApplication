package com.example.todoapplication.ui.task.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplication.R;
import com.example.todoapplication.data.database.TaskDatabase;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.ui.task.MainActivity;
import com.example.todoapplication.ui.task.create.TaskCreateFragment;
import com.example.todoapplication.ui.task.detail.TaskDetailFragment;
import com.example.todoapplication.ui.task.edit.TaskEditFragment;
import com.example.todoapplication.utils.FragmentTag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import lombok.NonNull;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Task> taskList;
    private TextView tvToolbar;
    private Button btnBack;
    private FloatingActionButton btnCreate;
    private final String TAG = "ToDoListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        taskList = TaskDatabase.getInstance(getContext()).taskDAO().getTasks();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        mapping(view);
        tvToolbar.setText(R.string.task_list);
        btnBack.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreate.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskCreateFragment());
        });

        TaskListAdapter adapter = new TaskListAdapter(getContext(), taskList);
        adapter.setEvent(new TaskListAdapter.EventListener() {
            @Override
            public void onDelete(int position) {
                TaskDatabase.getInstance(getContext()).taskDAO().deleteTaskById(taskList.get(position).getId());
                adapter.notifyItemRemoved(position);
                taskList.remove(position);
            }

            @Override
            public void onShowDetailScreen(int position) {
                ((MainActivity) requireActivity()).fragmentJump(taskList.get(position), new TaskDetailFragment());
            }

            @Override
            public void onShowEditScreen(int position) {
                MainActivity.TASK_FRAGMENT_TAG = FragmentTag.LIST_FRAGMENT_TAG;
                ((MainActivity) requireActivity()).fragmentJump(taskList.get(position), new TaskEditFragment());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Mapping view to resource
     *
     * @param view
     */
    private void mapping(View view) {
        tvToolbar = view.findViewById(R.id.toolbar_text);
        btnBack = view.findViewById(R.id.btn_back);
        btnCreate = view.findViewById(R.id.btn_add);
        recyclerView = view.findViewById(R.id.rv_todo_list);
    }
}