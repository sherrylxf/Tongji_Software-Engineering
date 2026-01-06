// 2354093 李雪菲

package com.garfield.model;

/*
Exception Handling
– Handle invalid situations (such as empty title, duplicate id, missing record) with a custom exception named TaskException.
 */
public class TaskException extends  Exception{
    public TaskException(String message) {
        super(message);
    }

    //empty title
    public static void checkTitle(String title) throws TaskException {
        if (title.isEmpty()) {
            throw new TaskException("Title cannot be empty");
        }
        System.out.println("checkTitle");
    }

    //duplicate id
    public static void checkId(int id) throws TaskException {
        if (id == 0) {
            throw new TaskException("Id cannot be 0");
        }
        System.out.println("checkId");
    }

    //missing record
    public static void checkRecord(Task task) throws TaskException {
        if (task == null) {
            throw new TaskException("Record not found");
        }
        System.out.println("checkRecord");
    }
}
