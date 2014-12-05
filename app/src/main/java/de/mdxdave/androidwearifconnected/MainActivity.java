package de.mdxdave.androidwearifconnected;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends ActionBarActivity {

    GoogleApiClient googleClient;

    @Override
    public void onStart(){
        super.onStart();
        googleClient.connect();

        checkWearStatus();
    }

    private void checkWearStatus() {
        PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(googleClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                TextView status = (TextView) findViewById(R.id.status);
                if (result.getNodes().size() > 0) {
	   	            // Check issue for getDisplayName(): https://code.google.com/p/android/issues/detail?id=76108
                    status.setText(getString(R.string.wear_connected)+":\n"+result.getNodes().get(0).getDisplayName());
                } else {
                    status.setText(getString(R.string.wear_disconnected));
                }
            }
        });
    }

    @Override
    public void onStop(){
        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        Button refreshButton = (Button)findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWearStatus();
            }
        });

    }

}
