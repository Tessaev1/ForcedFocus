package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PreferencesActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    public static final String TAG = "PreferencesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        List<String> permissionsCopy = new ArrayList<String>();
        Set<String> permissions = this.userToken.getPermissions();
        for (String s : permissions) {
            permissionsCopy.add(s);
        }
        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, permissionsCopy));


//        List<String> permissions = this.getGrantedPermissions("edu.uw.tessa.forcedfocus");

        final LoginButton logout = (LoginButton) findViewById(R.id.login_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent i = new Intent(PreferencesActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

//    private List<String> getGrantedPermissions(String appPackage) {
//        List<String> granted = new ArrayList<String>();
//        try {
//            PackageInfo pi = getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
//            for (int i = 0; i < pi.requestedPermissions.length; i++) {
//                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
//                    granted.add(pi.requestedPermissions[i]);
//                }
//            }
//        } catch (Exception e) {}
//        return granted;
//    }
}
