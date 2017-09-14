package arc.zxz.com.arcgisruntimetest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.PopupContainer;
import com.esri.android.toolkit.geocode.GeocodeHelper;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.android.toolkit.map.PopupCreateListener;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;
import me.goldze.mvvmhabit.utils.ConvertUtils;

import static android.R.attr.x;
import static android.R.attr.y;


/**
 * 渲染器地图
 */
public class RendererActivity extends AppCompatActivity {

    private MapView mMapView;
    private GraphicsLayer graphicsLayer;
    private MapViewHelper mvHelper;
    private SimpleMarkerSymbol simpleMarkerSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renderer);
        mMapView = (MapView) findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url4));
        mMapView.addLayer(graphicsLayer);
        //地图初始化状态监听
        statusChangedListener();

        simpleMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
        SimpleRenderer simpleRenderer = new SimpleRenderer(simpleMarkerSymbol);
        graphicsLayer.setRenderer(simpleRenderer);

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                Point point = mMapView.toMapPoint(v, v1);//屏幕坐标转换成空间坐标
                Graphic graphic1 = new Graphic(point, simpleMarkerSymbol);
                graphicsLayer.addGraphic(graphic1);
            }
        });

        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float v, float v1) {
                return true;
            }
        });

    }

    private void statusChangedListener() {
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                //MapView或者Layer初始化成功
                if (status == STATUS.INITIALIZED) {
                    Log.d("TAG", "INITIALIZED");
                    mMapView.centerAt(new Point(671015.418140, 2933468.9968586233), true);
                    mMapView.setScale(3000);
                }
                //MapView或者Layer初始化失败
                else if (status == STATUS.INITIALIZATION_FAILED) {
                    Log.d("TAG", "INITIALIZATION_FAILED");
                }
                //Layer加载成功
                else if (status == STATUS.LAYER_LOADED) {
                    Log.d("TAG", "LAYER_LOADED");
                }
                //Layer加载失败
                else if (status == STATUS.LAYER_LOADING_FAILED) {
                    Log.d("TAG", "LAYER_LOADING_FAILED");
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_zoomin:
                mMapView.zoomin();
                break;
            case R.id.iv_zoomout:
                mMapView.zoomout();
                break;
        }
    }
}
