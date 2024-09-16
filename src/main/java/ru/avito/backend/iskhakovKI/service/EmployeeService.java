package ru.avito.backend.iskhakovKI.service;

import ru.avito.backend.iskhakovKI.dto.EmployeeDto;
import ru.avito.backend.iskhakovKI.domain.Employee;
import ru.avito.backend.iskhakovKI.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Проверяем, существует ли пользователь с таким username
        if (employeeRepository.findByUsername(employeeDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Employee employee = new Employee();
        employee.setUsername(employeeDto.getUsername());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());

        employeeRepository.save(employee);

        EmployeeDto resultDto = new EmployeeDto();
        resultDto.setId(employee.getId());
        resultDto.setUsername(employee.getUsername());
        resultDto.setFirstName(employee.getFirstName());
        resultDto.setLastName(employee.getLastName());

        return resultDto;
    }
}
