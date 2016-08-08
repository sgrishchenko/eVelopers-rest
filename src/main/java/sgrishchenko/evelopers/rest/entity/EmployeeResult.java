package sgrishchenko.evelopers.rest.entity;

import java.time.LocalDate;

public class EmployeeResult {
    private String name;
    private int daysCount;

    public EmployeeResult(Employee employee) {
        this.name = employee.getName();
        this.daysCount = employee.getBirthday().getDayOfMonth() - LocalDate.now().getDayOfMonth();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }
}
