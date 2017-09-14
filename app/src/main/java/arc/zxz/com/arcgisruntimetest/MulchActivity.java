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
import com.esri.core.symbol.TextSymbol;
import com.google.gson.Gson;

import java.util.List;

import arc.zxz.com.arcgisruntimetest.service.ServiceUrl;

/**
 * 覆盖物地图
 */
public class MulchActivity extends AppCompatActivity {

    private GraphicsLayer graphicsLayer;
    private MapView mMapView;

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
        //单击地图监听
        singleTapTest();
        //长按地图监听
        longPressListener();
        //绘制一个点
        pointTest();
        //绘制一条线
        linTest();
        //绘制一条折线
        linMultTest();
        //绘制一个面
        gonTest();
    }

    private void singleTapTest() {
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {

            }
        });
    }

    private void longPressListener() {
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float x, float y) {
                Point point = mMapView.toMapPoint(x, y);//屏幕坐标转换成空间坐标
                //绘制一张图片
//                pictureMarkerSymbolTest(point);
                //绘制一段文字
                textSymbolTest(point);
                //绘制一个气泡
//                testCallout(point);
                //绘制一个自定义view
//                tetsPopupContainer();

                Log.e("TAG", "空间坐标:" + point.getX() + "," + point.getY());
                return true;
            }
        });
    }

    private void tetsPopupContainer() {
    }

    private void testCallout(Point point) {
        //获取一个气泡
        Callout callout = mMapView.getCallout();
        View view = LayoutInflater.from(this).inflate(R.layout.view_test, null, true);
        TextView tv_jd = (TextView) view.findViewById(R.id.tv_jd);
        TextView tv_wd = (TextView) view.findViewById(R.id.tv_wd);
        Button btn_dsm = (Button) view.findViewById(R.id.btn_dsm);
        tv_jd.setText("经度:" + point.getX());
        tv_wd.setText("纬度:" + point.getY());
        btn_dsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.getCallout().animatedHide();
            }
        });
//        CalloutStyle calloutStyle = new CalloutStyle();
//        //设置尖尖角的位置，尖尖显示在气泡的左下角，
//        calloutStyle.setAnchor(Callout.ANCHOR_POSITION_LOWER_LEFT_CORNER);
//        callout.setStyle(calloutStyle);
        callout.animatedShow(point, view);
    }

    private void textSymbolTest(Point point) {
        //这个因为国外的问题, 所以显示不出中文
        TextSymbol textSymbol = new TextSymbol(40, "你好", Color.BLACK);
        textSymbol.setFontFamily("DroidSansFallback.ttf");
        Graphic graphic = new Graphic(point, textSymbol);
        graphicsLayer.addGraphic(graphic);

        //所以将文本转换成图片显示
//        Drawable drawable = createMapBitMap("我是一个文本");
//        Graphic graphic = new Graphic(point, new PictureMarkerSymbol(drawable));
//        graphicsLayer.addGraphic(graphic);
    }

    private void pictureMarkerSymbolTest(Point point) {
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.jzw);
        Drawable newDrawable = zoomDrawable(drawable, 200, 200);
        Graphic graphic = new Graphic(point, new PictureMarkerSymbol(newDrawable));
        graphicsLayer.addGraphic(graphic);
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

    private void gonTest() {
        Polygon poly = new Polygon();
        poly.startPath(670933.096239678, 2933685.1491215117); // 起点
        poly.lineTo(671141.6514514318, 2933662.588908538);
        poly.lineTo(671112.763747238, 2933544.1478657876);
        poly.lineTo(670997.2128873991, 2933548.377892263);
        poly.lineTo(670902.0947818623, 2933598.433341921);
        poly.lineTo(670933.096239678, 2933685.1491215117); // 最后一个点必须与起点相同
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.YELLOW);
        simpleFillSymbol.setStyle(SimpleFillSymbol.STYLE.CROSS);
        Graphic graphic = new Graphic(poly, simpleFillSymbol);
        graphicsLayer.addGraphic(graphic);
    }

    private void linMultTest() {
        Polyline poly = new Polyline();
        poly.startPath(671042.6324841654, 2933457.9347222517);
        poly.lineTo(671158.3594922755, 2933425.94507273);
        poly.lineTo(671157.5668387974, 2933336.321146984);
        poly.lineTo(671030.2142894063, 2933373.8627298274);
        Graphic graphic = new Graphic(poly, new SimpleLineSymbol(Color.BLUE, 10));
        graphicsLayer.addGraphic(graphic);
    }

    private void linTest() {
        Line line = new Line();
        line.setStart(new Point(670824.4941064344, 2933612.3936594673));//起始点
        line.setEnd(new Point(670859.5912121844, 2933134.460501028));//终止点
        Polyline poly = new Polyline();
        poly.addSegment(line, true);//添加线段到Polyline对象中
        Graphic graphic = new Graphic(poly, new SimpleLineSymbol(Color.GREEN, 10));
        graphicsLayer.addGraphic(graphic);
    }

    private void pointTest() {
        Point point = new Point(671015.418140, 2933468.9968586233);
        Graphic graphic1 = new Graphic(point, new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE));
        graphicsLayer.addGraphic(graphic1);
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

    public static Drawable createMapBitMap(String text) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textLength = paint.measureText(text);
        int width = (int) textLength + 10;
        int height = (int) textLength + 10;
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        cv.drawColor(Color.parseColor("#00000000"));
        cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        cv.drawText(text, width, height, paint);
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return new BitmapDrawable(newb);
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

//    private void testGraphicsLayers() {
//        GraphicsLayer tl = new GraphicsLayer();
//        binding.mapView.addLayer(tl);
//        TestT testT = new Gson().fromJson(DataDictionary.json, TestT.class);
//        Polygon poly = new Polygon();
//        for (int i = 0; i < testT.getGeometry().getRings().get(0).size(); i++) {
//            List<Double> doubles = testT.getGeometry().getRings().get(0).get(i);
//            if (i == 0) {
//                Point point = MapUtils.to4326(new Point(doubles.get(0), doubles.get(1)), binding.mapView);
//                poly.startPath(point); // 起点
//            }
//            poly.lineTo(MapUtils.to4326(new Point(doubles.get(0), doubles.get(1)), binding.mapView)); // 起点
//        }
//        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.YELLOW);
//        simpleFillSymbol.setStyle(SimpleFillSymbol.STYLE.CROSS);
//        Graphic graphic = new Graphic(poly, simpleFillSymbol);
//        tl.addGraphic(graphic);
//    }
}
