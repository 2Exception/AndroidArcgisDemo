package arc.zxz.com.arcgisruntimetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, BasicMapActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, MulchActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this, RendererActivity.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(this, BasicMap3DActivity.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(this, TaggingActivity.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(this, TaskActivity.class));
                break;
        }
    }
}
