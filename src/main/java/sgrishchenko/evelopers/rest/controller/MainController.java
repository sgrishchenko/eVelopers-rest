package sgrishchenko.evelopers.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sgrishchenko.evelopers.rest.entity.EmployeeResult;
import sgrishchenko.evelopers.rest.service.TaskService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MainController {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<UUID, Future<List<EmployeeResult>>> processMap = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private TaskService taskService;

    @RequestMapping("/names")
    public UUID getNames(@RequestParam(required = false) Integer month) throws IOException, InterruptedException {
        logger.info("request task");
        if (month == null) month = LocalDate.now().getMonth().getValue();

        UUID id = UUID.randomUUID();
        processMap.put(id, taskService.getEmployeeResults(Month.of(month)));

        logger.info(String.format("processed task id=%s", id));
        return id;
    }

    @RequestMapping("/names/result")
    public List<EmployeeResult> getNamesResult(@RequestParam UUID id) throws ExecutionException, InterruptedException {
        logger.info("resolved result");

        Future<List<EmployeeResult>> process = processMap.get(id);
        if (!process.isDone()) throw new IllegalStateException("Задача не завершена");

        processMap.remove(id);

        logger.info(String.format("resolved task id=%s", id));
        return process.get();
    }

    @ExceptionHandler
    public String handleException(Throwable throwable) {
        logger.log(Level.SEVERE, throwable.getMessage(), throwable);
        return throwable.getMessage();
    }
}
