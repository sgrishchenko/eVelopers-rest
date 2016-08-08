package sgrishchenko.evelopers.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import sgrishchenko.evelopers.rest.entity.Employee;
import sgrishchenko.evelopers.rest.entity.EmployeeResult;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskService {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ResourceLoader resourceLoader;

    @Async
    public Future<List<EmployeeResult>> getEmployeeResults(final Month month) throws IOException, InterruptedException {
        logger.info("start task");

        TimeUnit.MINUTES.sleep(1);

        Resource resource = resourceLoader.getResource("classpath:employees.json");
        Employee[] employees = objectMapper.readValue(resource.getInputStream(), Employee[].class);

        logger.info("end task");
        return new AsyncResult<>(Stream.of(employees)
                .filter(e -> e.getBirthday().getMonth() == month)
                .map(EmployeeResult::new)
                .collect(Collectors.toList()));
    }
}