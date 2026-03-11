package com.periferia.taskApp.services;

import com.periferia.taskApp.dto.ResponseDTO;
import com.periferia.taskApp.dto.TaskRequest;
import com.periferia.taskApp.dto.TaskResponse;
import com.periferia.taskApp.entities.Task;
import com.periferia.taskApp.exceptions.NoExitsElementException;
import com.periferia.taskApp.exceptions.PaginateException;
import com.periferia.taskApp.mappers.TaskMapper;
import com.periferia.taskApp.repositories.TaskRepository;
import com.periferia.taskApp.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public ResponseDTO<Page<TaskResponse>> taskAll(int page, int size) {
        try {
            if (page < 0) {
                throw new PaginateException("El número de página no puede ser negativo");
            }
            if (size <= 0 || size > 100) {
                throw new PaginateException("El tamaño debe estar entre 1 y 100");
            }

            Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
            Page<Task> taskPage = taskRepository.findAll(pageable);
            Page<TaskResponse> taskResponsePage = taskPage.map(taskMapper::taskToTaskResponse);

            return ResponseDTO.<Page<TaskResponse>>builder()
                    .code(200)
                    .message("Tareas obtenidas exitosamente")
                    .data(taskResponsePage)
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ResponseDTO<TaskResponse> taskId(Long id) {
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isEmpty()) {
                throw  new NoExitsElementException("No se encuentra la tarea " + id);
            }

            return ResponseDTO.<TaskResponse>builder()
                    .code(200)
                    .message("Tarea encontrada")
                    .data(taskMapper.taskToTaskResponse(taskOptional.get()))
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ResponseDTO<TaskResponse> taskSave(TaskRequest request) {
        try {
            Task task = taskMapper.taskRequestToTask(request);
            task = taskRepository.save(task);
            return ResponseDTO.<TaskResponse>builder()
                    .code(200)
                    .message("Tarea registrada correctamente")
                    .data(taskMapper.taskToTaskResponse(task))
                    .build();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ResponseDTO<TaskResponse> taskUpdate(Long id, TaskRequest request) {
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isEmpty()) {
                throw  new NoExitsElementException("No se encuentra la tarea " + id);
            }

            Task taskCurrent = taskOptional.get();
            taskCurrent.setTitle(request.getTitle());
            taskCurrent.setDescription(request.getDescription());
            taskCurrent.setStatus(Status.valueOf(request.getStatus()));;
            TaskResponse taskResponse = taskMapper.taskToTaskResponse(
                    taskRepository.save(taskCurrent)
            );

            return ResponseDTO.<TaskResponse>builder()
                    .code(200)
                    .message("Tarea actualizada correctamente")
                    .data(taskResponse)
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ResponseDTO<Boolean> taskDelete(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty()) {
            throw new NoExitsElementException("No se encuentra la tarea " + id);
        }
        try {
            taskRepository.deleteById(taskOptional.get().getId());
            return ResponseDTO.<Boolean>builder()
                    .code(200)
                    .message("Tarea eliminada correctamente")
                    .data(true)
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
