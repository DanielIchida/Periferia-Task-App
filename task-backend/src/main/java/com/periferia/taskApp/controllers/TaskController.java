package com.periferia.taskApp.controllers;

import com.periferia.taskApp.dto.ResponseDTO;
import com.periferia.taskApp.dto.TaskRequest;
import com.periferia.taskApp.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                taskService.taskAll(page,size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> getIdTask(@PathVariable Long id) {
        return ResponseEntity.ok(
                taskService.taskId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> saveTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)  // Forzar JSON explícitamente
                .body( taskService.taskSave(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(
                taskService.taskUpdate(id,request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(
                taskService.taskDelete(id)
        );
    }
}
