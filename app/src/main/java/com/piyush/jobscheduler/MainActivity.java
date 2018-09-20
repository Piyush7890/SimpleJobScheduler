package com.piyush.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
RadioGroup radioGroup;
int id;
SeekBar deadline;
int selectedNetworkOption;
Switch idle,charging;
TextView progress;

private JobScheduler scheduler;
    private static final int JOB_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.group);
         selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
         scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            idle = findViewById(R.id.switch_idle);
            charging = findViewById(R.id.switch_charging);
            progress = findViewById(R.id.deadlint_Text);
            deadline = findViewById(R.id.deadline_seekbar);
            deadline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress>0)MainActivity.this.progress.setText("Override Deadline"+progress);
                    else MainActivity.this.progress.setText("Override Deadline: Not Set");
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

    }

    public void scheduleJob(View view)
    {
        id = radioGroup.getCheckedRadioButtonId();

        switch (id)
        {
            case R.id.none:
                selectedNetworkOption  = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.any:
                selectedNetworkOption=JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifi:
                selectedNetworkOption=JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }
        deadline.getProgress();
        boolean constraintSet=selectedNetworkOption!=JobInfo.NETWORK_TYPE_NONE;
        if(constraintSet) {
            scheduler
                    .schedule(new JobInfo.Builder(id,
                            new ComponentName(getPackageName(),
                                    jobService.class.getName()))
                            .setRequiredNetworkType(selectedNetworkOption)
                            .setRequiresCharging(charging
                                    .isSelected())
                            .setRequiresDeviceIdle(idle
                                    .isSelected())
                            .setOverrideDeadline(deadline.getProgress()*1000)
                            .build());

            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Set atleast one constraint.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAll(View v)
    {
        if(scheduler!=null)
        {
            scheduler.cancelAll();
            scheduler=null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();

        }
    }



}
