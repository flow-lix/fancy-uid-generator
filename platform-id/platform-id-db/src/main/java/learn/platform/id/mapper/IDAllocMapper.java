package learn.platform.id.mapper;

import learn.platform.id.entity.IDAllocEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IDAllocMapper {

    @Select("SELECT biz_tag FROM leaf_alloc")
    List<String> getAllTags();

    @Update("UPDATE leaf_alloc SET max_id = max_id + step WHERE biz_tag = #{tag}")
    int updateMaxId(String tag);

    @Select("SELECT biz_tag tag, max_id maxId, step FROM leaf_alloc WHERE biz_tag = #{tag}")
    IDAllocEntity getIdAlloc(String tag);
}
