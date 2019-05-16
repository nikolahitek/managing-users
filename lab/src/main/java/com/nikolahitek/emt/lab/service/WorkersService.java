package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.consts.Role;
import com.nikolahitek.emt.lab.helpers.WorkerViewModel;
import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.model.Department;
import com.nikolahitek.emt.lab.model.Employee;
import com.nikolahitek.emt.lab.model.Manager;
import com.nikolahitek.emt.lab.repository.AccountsRepository;
import com.nikolahitek.emt.lab.repository.DepartmentsRepository;
import com.nikolahitek.emt.lab.repository.EmployeesRepository;
import com.nikolahitek.emt.lab.repository.ManagersRepository;
import com.nikolahitek.emt.lab.service.intefaces.IWorkersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkersService implements IWorkersService {

    private final static Logger logger = LoggerFactory.getLogger(ActivationAccountService.class);
    private final AccountsRepository accountsRepository;
    private final EmployeesRepository employeesRepository;
    private final DepartmentsRepository departmentsRep;
    private final ManagersRepository managersRepository;
    private final AccountsService accountsService;
    private final PasswordEncoder passwordEncoder;

    public WorkersService(AccountsRepository accountsRepository, EmployeesRepository employeesRepository, DepartmentsRepository departmentsRep, ManagersRepository managersRepository, AccountsService accountsService, PasswordEncoder passwordEncoder) {
        this.accountsRepository = accountsRepository;
        this.employeesRepository = employeesRepository;
        this.departmentsRep = departmentsRep;
        this.managersRepository = managersRepository;
        this.accountsService = accountsService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<WorkerViewModel> listAssignedWorkersFor(String username) {
        List<WorkerViewModel> list = new ArrayList<>();

        Account currentUser = accountsService.getAccountByUsername(username);

        if (currentUser.getRole().equals(Role.ADMIN)) {
            list.addAll(transformToUVM(employeesRepository.findAll()));
            list.addAll(transformToUVM(managersRepository.findAll()));
        }
        else if (currentUser.getRole().equals(Role.MANAGER)) {
            list.addAll(transformToUVM(employeesRepository.findAll()
                    .stream()
                    .filter(user -> user.getDepartment().equals(currentUser.getDepartment()))
                    .collect(Collectors.toList())));
        }

        return list;
    }

    public List<WorkerViewModel> listUnassignedWorkersFor(String username) {
        List<WorkerViewModel> list = new ArrayList<>();

        Account currentUser = accountsService.getAccountByUsername(username);

        if (currentUser.getRole().equals(Role.ADMIN)) {
            list.addAll(transformToUVM(accountsRepository.findAll()
                    .stream()
                    .filter(user -> user.getRole() == Role.USER)
                    .collect(Collectors.toList())));
        }
        else if (currentUser.getRole().equals(Role.MANAGER)) {
            list.addAll(transformToUVM(accountsRepository.findAll()
                    .stream()
                    .filter(user -> user.getRole() == Role.USER)
                    .collect(Collectors.toList())));
        }

        return list;
    }

    private <T extends Account> List<WorkerViewModel> transformToUVM(List<T> list) {
        return list
                .stream()
                .map(user -> {
                    WorkerViewModel uvm = new WorkerViewModel();
                    uvm.role = user.getRole().name();
                    uvm.firstname = user.getFirstName();
                    uvm.lastname = user.getLastName();
                    uvm.username = user.getUsername();
                    uvm.email = user.getEmail();
                    if (user.getDepartment() != null)
                        uvm.department = user.getDepartment().getName();
                    else uvm.department = " - ";
                    return uvm;
                })
                .collect(Collectors.toList());
    }

    public void changeWorkersRole(String username, String role) {
        Account user = accountsService.getAccountByUsername(username);
        if (user.getRole().name().equals(role)) return;

        if (role.equals(Role.EMPLOYEE.name())) {
            accountsRepository.deleteByUsername(username);
            logger.info("Changing role to EMPLOYEE of account: " + username);
            Employee employee = new Employee(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword(), true, user.getDepartment());
            employeesRepository.save(employee);
        } else if (role.equals(Role.MANAGER.name())) {
            accountsRepository.deleteByUsername(username);
            logger.info("Changing role to MANAGER of account: " + username);
            Manager manager = new Manager(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword(), true, user.getDepartment());
            managersRepository.save(manager);
        } else if (role.equals(Role.USER.name())) {
            accountsRepository.deleteByUsername(username);
            logger.info("Changing role to USER of account: " + username);
            Account newUser = new Account(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword(), true, Role.USER);
            accountsRepository.save(newUser);
        }
    }

    public void changeWorkersDepartment(String username, String department) {
        Account account = accountsService.getAccountByUsername(username);
        if (account.getRole().equals(Role.USER)) return;
        Department newDepartment = departmentsRep.findFirstByName(department)
                .orElse(null);
        account.setDepartment(newDepartment);
        accountsRepository.save(account);
    }

    @Override
    public void addEmployee(String firstname, String lastname, String email, String username, String password, String departmentName) {
        if (!accountsService.isUsernameValid(username)) return;
        password = passwordEncoder.encode(password);
        Department department = departmentsRep.findFirstByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Department not found."));
        employeesRepository.save(new Employee(username, firstname, lastname, email, password, true, department));
    }

    @Override
    public void addManager(String firstname, String lastname, String email, String username, String password, String departmentName) {
        if (!accountsService.isUsernameValid(username)) return;
        password = passwordEncoder.encode(password);
        Department department = departmentsRep.findFirstByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Department not found."));
        managersRepository.save(new Manager(username, firstname, lastname, email, password, true, department));
    }

    @Override
    public void addUnassignedWorker(String firstname, String lastname, String email, String username, String password) {
        if (!accountsService.isUsernameValid(username)) return;
        password = passwordEncoder.encode(password);
        accountsRepository.save(new Account(username, firstname, lastname, email, password, true, Role.USER));
    }
}