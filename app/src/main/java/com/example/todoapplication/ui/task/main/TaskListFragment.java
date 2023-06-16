package com.example.todoapplication.ui.task.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.todoapplication.R;
import com.example.todoapplication.data.database.TaskDatabase;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.data.remote.AppApi;
import com.example.todoapplication.ui.task.MainActivity;
import com.example.todoapplication.ui.task.create.TaskCreateFragment;
import com.example.todoapplication.ui.task.detail.TaskDetailFragment;
import com.example.todoapplication.ui.task.edit.TaskEditFragment;
import com.example.todoapplication.utils.APIUtils;
import com.example.todoapplication.utils.FragmentTag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
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
    private TaskListAdapter adapter;
    private AppApi appApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        taskList = TaskDatabase.getInstance(getContext()).taskDAO().getTasks();
        appApi = AppApi.getInstance();
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
    public void onViewCreated(@NotNull @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreate.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskCreateFragment());
        });

        appApi.getTaskList(getContext(), new AppApi.ApiResponseListener() {
            @Override
            public void onResponse(Object o) {
                taskList = (List<Task>) o;
                adapter = new TaskListAdapter(getContext(), taskList);
                setRecyclerView(taskList, adapter);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                Log.e(TAG, "onErrorResponse: " + errorMessage);
            }
        });
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

    private void setRecyclerView(List<Task> tasks, TaskListAdapter adapter) {
        adapter.setEvent(new TaskListAdapter.EventListener() {
            @Override
            public void onDelete(int position) {

//                TaskDatabase.getInstance(getContext()).taskDAO().deleteTaskById(tasks.get(position).getId());
                appApi.deleteTaskById(tasks.get(position).getId(), getContext(), new AppApi.ApiResponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(requireActivity(), getResources().getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                        tasks.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {

                    }
                });
            }

            @Override
            public void onShowDetailScreen(int position) {
                ((MainActivity) requireActivity()).fragmentJump(tasks.get(position), new TaskDetailFragment());
            }

            @Override
            public void onShowEditScreen(int position) {
                MainActivity.TASK_FRAGMENT_TAG = FragmentTag.LIST_FRAGMENT_TAG;
                ((MainActivity) requireActivity()).fragmentJump(tasks.get(position), new TaskEditFragment());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}