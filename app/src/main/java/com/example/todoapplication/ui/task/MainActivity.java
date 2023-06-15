package com.example.todoapplication.ui.task;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.todoapplication.R;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.ui.task.main.TaskListFragment;

public class MainActivity extends AppCompatActivity {
    public static int TASK_FRAGMENT_TAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.toolbar, this.getTheme()));

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, TaskListFragment.class, null).commit();
    }

    /**
     * Replace current fragment to specific fragment
     *
     * @param id
     * @param fragment
     */
    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Jump to another fragment with Task
     * @param task
     * @param fragment
     */
    public void fragmentJump(Task task, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        fragment.setArguments(bundle);
        switchContent(R.id.fragment, fragment);
    }
}