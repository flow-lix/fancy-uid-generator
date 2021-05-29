package learn.platform.id.service;

import org.fancy.common.Result;

/**
 * ID生成服务接口
 */
public interface IDGenService {

    void init();

    Result get(String tag);
}
