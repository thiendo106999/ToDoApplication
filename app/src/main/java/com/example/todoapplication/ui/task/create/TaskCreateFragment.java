package com.example.todoapplication.ui.task.create;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.todoapplication.R;
import com.example.todoapplication.data.database.TaskDatabase;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.ui.task.main.TaskListFragment;

import org.jetbrains.annotations.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lombok.NonNull;

public class TaskCreateFragment extends Fragment {
    private TextView tvToolbar;
    private Button btnBack, btnCreate;
    private ImageButton btnCalendar;
    private EditText edtTaskName, edtDate, edtDescription;
    private final String TAG = "CreateTaskFragment";
    private final String TASK_LIST_STORAGE = "task_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTaskName = view.findViewById(R.id.et_task_name);
        edtDate = view.findViewById(R.id.et_date);
        edtDescription = view.findViewById(R.id.et_description);
        btnCreate = view.findViewById(R.id.btn_create);

        tvToolbar = view.findViewById(R.id.toolbar_text);
        tvToolbar.setText(R.string.create_new_task);

        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, TaskListFragment.class, null).commit();
        });

        btnCalendar = view.findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i, i1, i2);
                    edtDate.setText(simpleDateFormat.format(calendar.getTime()));
                }
            }, 2023, 6, 12);
            datePickerDialog.show();
        });
        btnCreate.setOnClickListener(v -> {
            Task task = new Task(
                    edtTaskName.getText().toString(),
                    edtDate.getText().toString(),
                    edtDescription.getText().toString());
            TaskDatabase.getInstance(getContext()).taskDAO().insert(task);

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, TaskListFragment.class, null).commit();
        });
    }
}