package arc.zxz.com.arcgisruntimetest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.HashMap;
import java.util.Map;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * 标注地图
 */
public class TaggingActivity extends AppCompatActivity {

    private GraphicsLayer graphicsLayer;
    private MapView mMapView;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulch);
        mMapView = (MapView) findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        //加载动态图层
        mMapView.addLayer(new ArcGISDynamicMapServiceLayer(ServiceUrl.url));
        mMapView.addLayer(graphicsLayer);
        //地图初始化状态监听
        statusChangedListener();

        //长按地图监听
        longPressListener();
    }

    private void longPressListener() {
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float x, float y) {
                SelectOneGraphic(x, y);
                return true;
            }
        });
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                // 转换坐标
                Point pt = mMapView.toMapPoint(new Point(v, v1));
                // 附加特别的属性
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tag", i++ + "");
                // 创建 graphic对象
                Graphic gp1 = CreateGraphic(pt, map);
                // 添加 Graphics 到图层
                graphicsLayer.addGraphic(gp1);
            }
        });
    }

    /**
     * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
     * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
     *
     * @param xScreen
     * @param yScreen
     * @return
     */
    private Graphic GetGraphicsFromLayer(double xScreen, double yScreen, GraphicsLayer layer) {
        Graphic result = null;
        try {
            int[] idsArr = layer.getGraphicIDs();
            Log.d("TestNet", "一共：" + idsArr.length);
            double x = xScreen;
            double y = yScreen;
            for (int i = 0; i < idsArr.length; i++) {
                Graphic gpVar = layer.getGraphic(idsArr[i]);
                if (gpVar != null) {
                    Log.d("TestNet", "<<>>" + gpVar.getGeometry());
                    if (gpVar.getGeometry().toString().contains("Point")) {
                        Point pointVar = (Point) gpVar.getGeometry();
                        pointVar = mMapView.toScreenPoint(pointVar);
                        double x1 = pointVar.getX();
                        double y1 = pointVar.getY();
                        if (Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < 50) {
                            result = gpVar;
                            break;
                        } else {
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    private void SelectOneGraphic(float x, float y) {
        // 获得图层
        GraphicsLayer layer = GetGraphicLayer();
        if (layer != null && layer.isInitialized() && layer.isVisible()) {
            Graphic result = null;
            // 检索当前 光标点（手指按压位置）的附近的 graphic对象
            result = GetGraphicsFromLayer(x, y, layer);
            if (result != null) {
                // 获得附加特别的属性
                String msgTag = (String) result
                        .getAttributeValue("tag");
                // 显示提示
                Toast.makeText(this, msgTag, Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
         * 创建一个Graphic ， 参数geometry是屏幕坐标位置，map是附加的属性参数
         */
    private Graphic CreateGraphic(Point geometry, Map<String, Object> map) {
        Drawable image = ContextCompat.getDrawable(this, R.mipmap.jzw);
        image = zoomDrawable(image, 200, 200);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(image);
        // 构建graphic
        // Graphic g = new Graphic(geometry, symbol);
        Graphic g = new Graphic(geometry, symbol, map, R.mipmap.jzw);
        return g;
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
//                    mMapView.zoomin();
//                    mMapView.zoomin();
//                    mMapView.zoomin();
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

    /*
           * 获得 GetGraphicLayer
           */
    private GraphicsLayer GetGraphicLayer() {
        if (graphicsLayer == null) {
            graphicsLayer = new GraphicsLayer();
            mMapView.addLayer(graphicsLayer);
        }
        return graphicsLayer;
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

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
