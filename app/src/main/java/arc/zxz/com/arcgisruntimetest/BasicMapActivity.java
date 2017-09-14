package arc.zxz.com.arcgisruntimetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;

/**
 * 基础地图
 */
public class BasicMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_map);
        MapView mMapView1 = (MapView) findViewById(R.id.map1);
        mMapView1.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url3));
        MapView mMapView2 = (MapView) findViewById(R.id.map2);
        mMapView2.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url3));
    }
}
