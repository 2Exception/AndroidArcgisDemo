package arc.zxz.com.arcgisruntimetest.app;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true);
    }
}
