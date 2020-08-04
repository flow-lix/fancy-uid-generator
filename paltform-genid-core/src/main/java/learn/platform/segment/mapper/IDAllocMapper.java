package learn.platform.segment.mapper;

import learn.platform.segment.entity.IDAllocEntity;

import java.util.List;

public interface IDAllocMapper {
    List<String> getAllTags();

    IDAllocEntity updateMaxId(String tag);
}
