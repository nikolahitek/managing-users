package com.nikolahitek.emt.lab.service.intefaces;

import com.nikolahitek.emt.lab.helpers.WorkerViewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWorkersService {

    List<WorkerViewModel> listAssignedWorkersFor(String username);

    List<WorkerViewModel> listUnassignedWorkersFor(String username);

    void changeWorkersRole(String username, String role);

    void changeWorkersDepartment(String username, String department);

    void addEmployee(String firstname, String lastname, String email, String username, String password, String departmentName);

    void addManager(String firstname, String lastname, String email, String username, String password, String departmentName);

    void addUnassignedWorker(String firstname, String lastname, String email, String username, String password);

}