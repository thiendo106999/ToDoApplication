package com.example.todoapplication.ui.task.edit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.todoapplication.R;
import com.example.todoapplication.data.database.TaskDatabase;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.data.remote.AppApi;
import com.example.todoapplication.ui.task.MainActivity;
import com.example.todoapplication.ui.task.detail.TaskDetailFragment;
import com.example.todoapplication.ui.task.main.TaskListFragment;
import com.example.todoapplication.utils.FragmentTag;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskEditFragment extends Fragment {
    private TextView tvToolbar;
    private Button btnBack, btnEdit;
    private ImageButton btnCalendar;
    private EditText edtTaskName, edtDate, edtDescription;
    private Task task;
    private AppApi appApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        appApi = AppApi.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        mapping(view);

        tvToolbar.setText(R.string.task_edit);
        Bundle bundle = getArguments();
        if (bundle != null) {
            task = (Task) bundle.getSerializable("task");
            if (task != null) {
                edtDescription.setText(task.getDescription());
                edtDate.setText(task.getDate());
                edtTaskName.setText(task.getName());
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBack.setOnClickListener(v -> {
            if (MainActivity.TASK_FRAGMENT_TAG == FragmentTag.LIST_FRAGMENT_TAG) {
                ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskListFragment());
            } else {
                ((MainActivity) requireActivity()).fragmentJump(task, new TaskDetailFragment());
            }
        });

        btnEdit.setOnClickListener(v -> {
            String taskName = edtTaskName.getText().toString();
            String date = edtDate.getText().toString();
            String description = edtDescription.getText().toString();
            Task newTask = new Task(taskName, date, description);
            newTask.setId(task.getId());

//            TaskDatabase.getInstance(getContext())
//                    .taskDAO()
//                    .update(newTask);

            appApi.createTask(requireActivity(), new AppApi.ApiResponseListener() {
                @Override
                public void onResponse(Object o) {
                    Toast.makeText(requireActivity(), getResources().getText(R.string.update_success), Toast.LENGTH_SHORT).show();

                    if (MainActivity.TASK_FRAGMENT_TAG == FragmentTag.LIST_FRAGMENT_TAG) {
                        ((MainActivity) requireActivity()).switchContent(R.id.fragment, new TaskListFragment());
                    } else {
                        ((MainActivity) requireActivity()).fragmentJump(newTask, new TaskDetailFragment());
                    }
                }

                @Override
                public void onErrorResponse(String errorMessage) {

                }
            }, newTask);
        });

        btnCalendar.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                edtDate.setText(simpleDateFormat.format(calendar.getTime()));
            }, 2023, 6, 12);
            datePickerDialog.show();
        });
    }

    private void mapping(View view) {
        edtTaskName = view.findViewById(R.id.et_task_name);
        edtDate = view.findViewById(R.id.et_date);
        edtDescription = view.findViewById(R.id.et_description);
        tvToolbar = view.findViewById(R.id.toolbar_text);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnBack = view.findViewById(R.id.btn_back);
        btnCalendar = view.findViewById(R.id.btn_calendar);
    }
}