package com.example.todoapplication.ui.task.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.todoapplication.R;
import com.example.todoapplication.data.database.TaskDatabase;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.ui.task.MainActivity;
import com.example.todoapplication.ui.task.edit.TaskEditFragment;
import com.example.todoapplication.ui.task.main.TaskListFragment;
import com.example.todoapplication.utils.FragmentTag;

import org.jetbrains.annotations.NotNull;

public class TaskDetailFragment extends Fragment {
    private TextView tvTaskName, tvDate, tvDescription, tvToolbar;
    private Button btnBack, btnEdit, btnDelete;
    private Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_task, container, false);

        mapping(view);
        tvToolbar.setText(R.string.task_detail);

        Bundle bundle = getArguments();
        if (bundle != null) {
            task = (Task) bundle.getSerializable("task");
            tvDescription.setText(task.getDescription());
            tvDate.setText(task.getDate());
            tvTaskName.setText(task.getName());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskListFragment());
        });

        btnDelete.setOnClickListener(v -> {
            TaskDatabase.getInstance(getContext()).taskDAO().deleteTaskById(task.getId());
            Toast.makeText(requireActivity(), getResources().getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskListFragment());
        });

        btnEdit.setOnClickListener(v -> {
            MainActivity.TASK_FRAGMENT_TAG = FragmentTag.DETAIL_FRAGMENT_TAG;
            ((MainActivity) requireActivity()).fragmentJump(task, new TaskEditFragment());
        });

    }

    private void mapping(View view) {
        btnBack = view.findViewById(R.id.btn_back);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnEdit = view.findViewById(R.id.btn_edit);
        tvDate = view.findViewById(R.id.tv_date);
        tvDescription = view.findViewById(R.id.tv_description);
        tvTaskName = view.findViewById(R.id.tv_task_name);
        tvToolbar = view.findViewById(R.id.toolbar_text);
    }
}