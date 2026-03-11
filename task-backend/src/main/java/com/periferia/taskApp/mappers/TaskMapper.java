package com.periferia.taskApp.mappers;

import com.periferia.taskApp.dto.TaskRequest;
import com.periferia.taskApp.dto.TaskResponse;
import com.periferia.taskApp.entities.Task;
import com.periferia.taskApp.util.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToString")
    TaskResponse taskToTaskResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Task taskRequestToTask(TaskRequest taskRequest);

    @Named("statusToString")
    default String statusToString(Status status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Status stringToStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + status);
        }
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
