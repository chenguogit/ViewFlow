package dream.use_viewflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dream.use_viewflow.view.ViewFlow;

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);

        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.item, null);
            textView.setText("" + i);
            viewList.add(textView);
        }

        ViewFlow viewFlow = (ViewFlow) findViewById(R.id.view_flow);
        viewFlow.setViewList(viewList);
        viewFlow.setDrawable(R.drawable.icon_normal, R.drawable.icon_checked, 8);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
