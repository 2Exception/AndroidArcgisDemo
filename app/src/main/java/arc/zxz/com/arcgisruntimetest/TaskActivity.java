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
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.HashMap;
import java.util.Map;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;
import me.goldze.mvvmhabit.utils.ConvertUtils;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static android.R.attr.x;
import static android.R.attr.y;
import static arc.zxz.com.arcgisruntimetest.R.id.map;

/**
 * 检索地图要素信息
 */
public class TaskActivity extends AppCompatActivity {

    private GraphicsLayer graphicsLayer;
    private MapView mMapView;
    private IdentifyParameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulch);
        mMapView = (MapView) findViewById(map);
        graphicsLayer = new GraphicsLayer();
        //加载动态图层
        mMapView.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url));
        mMapView.addLayer(graphicsLayer);
        //地图初始化状态监听
        statusChangedListener();
        //长按地图监听
        longPressListener();
        //单击地图监听
        singleTapTest();
    }

    private void singleTapTest() {
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                Point point = mMapView.toMapPoint(v, v1);
                QueryParameters qParameters = new QueryParameters();
                SpatialReference sr = SpatialReference.create(102100);
                qParameters.setGeometry(point);
                qParameters.setOutSpatialReference(sr);
                qParameters.setReturnGeometry(true);
                QueryTask qTask = new QueryTask(ServiceUrl.url);
                try {
                    qTask.execute(qParameters, new CallbackListener<FeatureResult>() {
                        @Override
                        public void onCallback(FeatureResult objects) {
                            FeatureResult results = objects;
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void longPressListener() {
        //识别任务所需参数对象
        params = new IdentifyParameters();
        params.setTolerance(20);//设置容差
        params.setDPI(98);//设置地图的DPI
//        params.setLayers(new int[]{0});//设置要识别的图层数组
        params.setLayerMode(IdentifyParameters.ALL_LAYERS);//设置识别
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float x, float y) {
                final Point identifyPoint = mMapView.toMapPoint(x, y);
                params.setGeometry(identifyPoint);//设置识别位置
                params.setSpatialReference(mMapView.getSpatialReference());//设置坐标系
                params.setMapHeight(mMapView.getHeight());//设置地图像素高
                params.setMapWidth(mMapView.getWidth());//设置地图像素宽
                Envelope env = new Envelope();
                mMapView.getExtent().queryEnvelope(env);
                params.setMapExtent(env);//设置当前地图范围
                IdentifyTask itask = new IdentifyTask(ServiceUrl.url);
                itask.execute(params, new CallbackListener<IdentifyResult[]>() {

                    @Override
                    public void onError(Throwable e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onCallback(IdentifyResult[] objs) {
                        // TODO Auto-generated method stub
                        //在此添加代码处理查询的结果
                        if (objs != null && objs.length > 0) {
                            //查询到数据
                            IdentifyResult result = objs[0];
                            Callout callout = mMapView.getCallout();
                            TextView tv = new TextView(TaskActivity.this);
                            tv.setTextSize(ConvertUtils.px2sp(26));
                            tv.setTextColor(Color.BLUE);
                            tv.setText(result.toString());
                            callout.animatedShow(identifyPoint, tv);
                        } else {
//                            ToastUtils.showShort("没有查询到数据");
                            Toast.makeText(TaskActivity.this, "没有查询到数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
