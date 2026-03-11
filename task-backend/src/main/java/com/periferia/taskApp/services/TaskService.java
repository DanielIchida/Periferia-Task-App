package com.periferia.taskApp.services;

import com.periferia.taskApp.dto.ResponseDTO;
import com.periferia.taskApp.dto.TaskRequest;
import com.periferia.taskApp.dto.TaskResponse;
import org.springframework.data.domain.Page;

public interface TaskService {

    ResponseDTO<Page<TaskResponse>> taskAll(int page, int size);
    ResponseDTO<TaskResponse> taskId(Long id);
    ResponseDTO<TaskResponse> taskSave(TaskRequest request);
    ResponseDTO<TaskResponse> taskUpdate(Long id,TaskRequest request);
    ResponseDTO<Boolean> taskDelete(Long id);

}
