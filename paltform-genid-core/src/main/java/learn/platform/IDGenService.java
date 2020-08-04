package learn.platform;

import learn.platform.common.Result;

/**
 * ID生成服务接口
 */
public interface IDGenService {

    void init();

    Result get(String tag);
}
