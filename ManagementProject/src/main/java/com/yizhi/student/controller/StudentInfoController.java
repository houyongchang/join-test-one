package com.yizhi.student.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yizhi.common.annotation.Log;
import com.yizhi.common.controller.BaseController;
import com.yizhi.common.utils.*;
import com.yizhi.student.dao.ClassDao;
import com.yizhi.student.domain.ClassDO;
import com.yizhi.student.service.ClassService;
import com.yizhi.student.service.CollegeService;
import com.yizhi.student.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.yizhi.student.domain.StudentInfoDO;
import com.yizhi.student.service.StudentInfoService;

import javax.annotation.Resource;

/**
 * 生基础信息表
 */
 
@Controller
@RequestMapping("/student/studentInfo")
public class StudentInfoController {

    @Autowired
    private StudentInfoService studentInfoService;
    @Resource
    private ClassDao classDao;

    //
    @Log("学生信息保存")
    @PostMapping("/save")
    @RequiresPermissions("student:studentInfo:add")
    public R save(StudentInfoDO studentInfoDO) {
        boolean success = studentInfoService.save(studentInfoDO);
        if (success == false) {
            return R.error("添加失败");
        }
        return R.ok("添加成功");

    }
    /**
     * 可分页 查询
     */
    @GetMapping("/list")
    @RequiresPermissions("student:studentInfo:studentInfo")
    public R list(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                  @RequestParam(value = "StudentName", required = false) String StudentName,
                  @RequestParam(value = "tocollege", required = false) String tocollege,
                  @RequestParam(value = "tomajor", required = false) String tomajor,
                  @RequestParam(value = "className", required = false) String className
    ) {
        Page<StudentInfoDO> pageInfo = new Page(page, pageSize);
        //封装条件
        LambdaQueryWrapper<StudentInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StudentName!=null,StudentInfoDO::getStudentName,StudentName);
        queryWrapper.eq(tocollege!=null, StudentInfoDO::getTocollege,tocollege);
        queryWrapper.eq(tomajor!=null,StudentInfoDO::getTomajor,tomajor);
        if (className!=null){
            ClassDO classDO = classDao.queryByClassName(className);
            if (classDO!=null){
                Integer classDOId = classDO.getId();
                queryWrapper.eq(StudentInfoDO::getClassId,classDOId);
            }
        }
        studentInfoService.page(pageInfo,queryWrapper);
        // 将对象转map
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> PageMap = BeanUtil.beanToMap(pageInfo, String.valueOf(map));
        return R.ok(PageMap);
    }

    /**
     * 修改
     */
    @Log("学生基础信息表修改")
    @PostMapping("/update")
    @RequiresPermissions("student:studentInfo:edit")
    public R update(@RequestBody StudentInfoDO studentInfo) {

        boolean b = studentInfoService.updateById(studentInfo);
        if (b==false){
            return R.error("修改失败");
        }
        return R.ok("修改成功");
    }

    /**
     * 删除
     */
    @Log("学生基础信息表删除")
    @PostMapping("/remove")
    @RequiresPermissions("student:studentInfo:remove")
    public R remove(@RequestParam("id") Integer id) {

        boolean b = studentInfoService.removeById(id);
        return R.ok("删除成功");
    }

    /**
     * 批量删除
     */
    @Log("学生基础信息表批量删除")
    @PostMapping("/batchRemove")
    @RequiresPermissions("student:studentInfo:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {

        List<Integer> idList=new ArrayList<>(ids.length);
        //将数组转List
        Collections.addAll(idList,ids);
        boolean b = studentInfoService.removeBatchByIds(idList);
        return R.ok("批量删除成功");
    }


	//前后端不分离 客户端 -> 控制器-> 定位视图
	/**
	 * 学生管理 点击Tab标签 forward页面
	 */
	@GetMapping()
	@RequiresPermissions("student:studentInfo:studentInfo")
	String StudentInfo(){
		return "student/studentInfo/studentInfo";
	}

	/**
	 * 更新功能 弹出View定位
	 */
	@GetMapping("/edit/{id}")
	@RequiresPermissions("student:studentInfo:edit")
	String edit(@PathVariable("id") Integer id,Model model){
//		StudentInfoDO studentInfo = studentInfoService.get(id);
//		model.addAttribute("studentInfo", studentInfo);
		return "student/studentInfo/edit";
	}

	/**
	 * 学生管理 添加学生弹出 View
	 */
	@GetMapping("/add")
	@RequiresPermissions("student:studentInfo:add")
	String add(){
	    return "student/studentInfo/add";
	}
	
}//end class
