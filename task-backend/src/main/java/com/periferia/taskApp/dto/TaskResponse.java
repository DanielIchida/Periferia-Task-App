package com.periferia.taskApp.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String createdAt;
}
