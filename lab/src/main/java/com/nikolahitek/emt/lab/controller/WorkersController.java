package com.nikolahitek.emt.lab.controller;

import com.nikolahitek.emt.lab.consts.Role;
import com.nikolahitek.emt.lab.helpers.WorkerViewModel;
import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.service.AccountsService;
import com.nikolahitek.emt.lab.service.WorkersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@PreAuthorize("hasAnyRole('ADMIN, MANAGER')")
@Controller
public class WorkersController {

    private final WorkersService workersService;
    private final AccountsService accountsService;

    public WorkersController(WorkersService workersService, AccountsService accountsService) {
        this.workersService = workersService;
        this.accountsService = accountsService;
    }

    @GetMapping("/workers")
    public String listWorkers(Model model, Principal principal) {
        if (accountsService.getAccountByUsername(principal.getName()).getRole().equals(Role.ADMIN)) {
            model.addAttribute("canChangeDpt", true);
        } else {
            model.addAttribute("canChangeDpt", false);
        }
        model.addAttribute("assigned", workersService.listAssignedWorkersFor(principal.getName()));
        model.addAttribute("unassigned", workersService.listUnassignedWorkersFor(principal.getName()));
        model.addAttribute("selected", new WorkerViewModel());

        return "workers";
    }

    @PostMapping("/workers")
    @Transactional
    public String editUser(Principal principal,
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String department,
                           @RequestParam String username) {

        Account current = accountsService.getAccountByUsername(principal.getName());
        if (current.getRole().equals(Role.MANAGER)) {
            department = current.getDepartment().getName();
        }

        workersService.changeWorkersRole(username, role);
        workersService.changeWorkersDepartment(username, department);

        return "redirect:/workers";
    }

    @PostMapping("/workers/select")
    public String selectUser(Model model, Principal principal,
                             @RequestParam String username) {

        if (accountsService.getAccountByUsername(principal.getName()).getRole().equals(Role.ADMIN)) {
            model.addAttribute("canChangeDpt", true);
        } else {
            model.addAttribute("canChangeDpt", false);
        }
        model.addAttribute("assigned", workersService.listAssignedWorkersFor(principal.getName()));
        model.addAttribute("unassigned", workersService.listUnassignedWorkersFor(principal.getName()));

        WorkerViewModel uvm = new WorkerViewModel();
        Account account = accountsService.getAccountByUsername(username);
        uvm.username = username;
        if (account.getDepartment() != null) uvm.department = account.getDepartment().getName();
        uvm.role = account.getRole().name();
        model.addAttribute("selected", uvm);

        return "workers";
    }

    @PostMapping("/workers/delete")
    @Transactional
    public String deleteUser(@RequestParam String username) {
        accountsService.deleteAccount(username);
        return "redirect:/workers";
    }

    @PostMapping("/workers/add")
    public String addWorker(Principal principal,
                            @RequestParam String firstname,
                            @RequestParam String lastname,
                            @RequestParam String email,
                            @RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String role,
                            @RequestParam(required = false) String department) {

        Account current = accountsService.getAccountByUsername(principal.getName());

        if (current.getRole().equals(Role.MANAGER)) {
            if (role.equals(Role.EMPLOYEE.name())) {
                department = current.getDepartment().getName();
                workersService.addEmployee(firstname, lastname, email, username, password, department);
            } else if (role.equals(Role.USER.name())) {
                workersService.addUnassignedWorker(firstname, lastname, email, username, password);
            }
        } else {
            if (role.equals(Role.EMPLOYEE.name())) {
                workersService.addEmployee(firstname, lastname, email, username, password, department);
            } else if (role.equals(Role.USER.name())) {
                workersService.addUnassignedWorker(firstname, lastname, email, username, password);
            } else if (role.equals(Role.MANAGER.name())) {
                workersService.addManager(firstname, lastname, email, username, password, department);
            }
        }

        return "redirect:/workers";
    }
}
