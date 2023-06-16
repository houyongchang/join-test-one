package com.yizhi.student.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yizhi.student.domain.StudentInfoDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 生基础信息表
 * @author dunhf
 * @email 499345515@qq.com
 * @date 2019-08-01 09:45:46
 */
@Mapper
public interface StudentInfoDao extends BaseMapper<StudentInfoDO> {

	StudentInfoDO get(Integer id);
//
//	List<StudentInfoDO> list(Map<String,Object> map);
//
//	int count(Map<String,Object> map);
//
//	int save(StudentInfoDO studentInfo);
//
//	int update(StudentInfoDO studentInfo);
//
//	int remove(Integer id);
//
//	int batchRemove(Integer[] ids);
}
