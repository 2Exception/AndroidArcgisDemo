package arc.zxz.com.arcgisruntimetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;

/**
 * 3D基础地图
 */
public class BasicMap3DActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_map_3d);
        MapView mMapView = (MapView) findViewById(R.id.map);
        mMapView.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url4));
    }
}
