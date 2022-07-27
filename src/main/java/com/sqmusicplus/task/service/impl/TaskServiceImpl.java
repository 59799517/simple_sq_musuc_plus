package com.sqmusicplus.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.task.entity.Task;
import com.sqmusicplus.task.service.TaskService;
import com.sqmusicplus.task.mapper.TaskMapper;
import org.springframework.stereotype.Service;

/**
* @author SQ
* @description 针对表【task】的数据库操作Service实现
* @createDate 2022-07-26 18:17:17
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
implements TaskService{

}
