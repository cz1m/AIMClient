package com.like4u.aim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.like4u.aim.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 18:44
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
