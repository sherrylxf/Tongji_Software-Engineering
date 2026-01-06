// 2354093 李雪菲

package com.garfield.model;

import java.util.List;

/*
– Design a generic interface GarfieldTaskRepository. It should contain standard CRUD methods
like save, findById, findAll, and deleteById, as well as other methods necessary for supporting
the application’s features.
    – At a minimum, the interface should contain:
    ∗ void save(Task task);
    ∗ Task findById(String id) throws TaskException;
    ∗ List<Task> findAll();
    ∗ void deleteById(String id) throws TaskException;
    ∗ List<Task> findByStatus(Task.Status status);
    ∗ void update(Task task) throws TaskException;
 */
public interface GarfieldTaskRepository {
    void save(Task task);
    Task findById(String id) throws TaskException;
    List<Task> findAll();
    void deleteById(String id) throws TaskException;
    List<Task> findByStatus(Task.Status status);
    void update(Task task) throws TaskException;

}
