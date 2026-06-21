package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.*;
import com.taskmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Task create(Task task, Long projectId, Long assigneeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé : " + projectId));
        task.setProject(project);

        if (assigneeId != null) {
            User user = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + assigneeId));
            task.setAssignee(user);
        }

        return taskRepository.save(task);
    }

    public List<Task> findByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task updateStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée : " + id));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée : " + id));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tâche non trouvée : " + id);
        }
        taskRepository.deleteById(id);
    }
}