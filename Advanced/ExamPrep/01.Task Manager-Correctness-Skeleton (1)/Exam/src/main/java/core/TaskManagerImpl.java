package core;

import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {
    LinkedHashMap<String, Task> taskById;
    LinkedHashMap<String, Task> pandingTasksById;
    HashMap<String, Task> executedTaskById;

    public TaskManagerImpl() {
        this.taskById = new LinkedHashMap<>();
        this.pandingTasksById = new LinkedHashMap<>();
        this.executedTaskById = new HashMap<>();
    }

    @Override
    public void addTask(Task task) {
        taskById.put(task.getId(), task);
        pandingTasksById.put(task.getId(), task);
    }

    @Override
    public boolean contains(Task task) {
        Task temp = tryGetTask(task.getId());
        if (temp == null) {
            return false;
        }
        return true;
    }

    private Task tryGetTask(String taskId) {
        return taskById.get(taskId);
    }


    @Override
    public int size() {
        return pandingTasksById.size();
    }

    @Override
    public Task getTask(String taskId) {

        Task temp = tryGetTask(taskId);

        if (temp == null) {
            throw new IllegalArgumentException("Task does not exist");
        }
        return temp;
    }

    @Override
    public void deleteTask(String taskId) {
        Task tempTaskById = getTask(taskId);

        if (tempTaskById == null) {
            throw new IllegalArgumentException("Task does not exist");
        }
        taskById.remove(taskId);


    }

    @Override
    public Task executeTask() {
        Iterator<Task> iterator = taskById.values().iterator();
        Task firstTask = iterator.next();

        if (firstTask == null) {
            throw new IllegalArgumentException("No task available to execute");
        }

        executedTaskById.put(firstTask.getId(), firstTask);
        taskById.remove(firstTask.getId());

        return firstTask;
    }

    @Override
    public void rescheduleTask(String taskId) {
        Task temp = executedTaskById.get(taskId);

        if (temp == null) {
            throw new IllegalArgumentException("No executed task available to reschedule");
        }
        executedTaskById.remove(taskId);
        taskById.put(taskId, temp);

    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        List<Task> taskByDomainList = pandingTasksById.values()
                .stream()
                .filter(task -> domain.equals(task.getDomain()))
                .collect(Collectors.toList());

        if (taskByDomainList.isEmpty()) {
            throw new IllegalArgumentException("No available unexecuted tasks for domain: " + domain);
        }

        return taskByDomainList;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
        List<Task>  tasksEETRangeList = pandingTasksById.values()
                .stream()
                .filter(task -> task.getEstimatedExecutionTime() >= lowerBound
                        && task.getEstimatedExecutionTime() <= upperBound)
                .collect(Collectors.toList());

        if (tasksEETRangeList.isEmpty()) {
            throw new IllegalArgumentException("No task in the given EET range available: ");
        }
        return tasksEETRangeList;
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        List<Task> sortedTasks = taskById.values()
                .stream()
                .sorted(Comparator
                        .comparingInt(Task::getEstimatedExecutionTime)
                        .reversed()  // Sort by EET in descending order
                        .thenComparing(task -> task.getName().length()) // Then by length of name in ascending order
                )
                .collect(Collectors.toList());

        return sortedTasks;
    }
}
