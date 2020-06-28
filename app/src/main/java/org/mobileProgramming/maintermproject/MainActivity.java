package org.mobileProgramming.maintermproject;

import android.os.Bundle;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private int mSteps = 0;
    //리스너가 등록되고 난 후의 step count
    private int mCounterSteps = 0;

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView tvStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStepCount = (TextView)findViewById(R.id.tvStepCount);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null) {
            Toast.makeText(this, "산책 센서가 없는 모델이에요", Toast.LENGTH_SHORT).show();
        }
        Button stopBtn;

        stopBtn=(Button)findViewById(R.id.stopBtn);

        //초기화 버튼 : 다시 숫자를 0으로 만들어준다.
        stopBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mSteps=0;
                mCounterSteps=0;
                tvStepCount.setText(Integer.toString(mSteps));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // 이 값을 고객에게 보내서 고객이 확인할 수 있도록 해야함
            //tvStepCount.setText("Step Count : " + String.valueOf(event.values[0]));
            if(mCounterSteps<1){
                mCounterSteps=(int)event.values[0];
            }

            mSteps=(int)event.values[0]-mCounterSteps;
            // mSteps를 고객에게 보낼 수 있게 함
            tvStepCount.setText(Integer.toString(mSteps));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
