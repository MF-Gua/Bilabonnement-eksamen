package com.example.demo.controller;

import com.example.demo.model.DamageReport;
import com.example.demo.repository.DamageReportRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DamageReportController {

    // Repositories (JDBC) der håndterer database-adgang
    private final DamageReportRepository damageReportRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(DamageReportController.class);

    // Dependency injection via constructor
    public DamageReportController(DamageReportRepository damageReportRepository,
                                  VehicleRepository vehicleRepository,
                                  EmployeeRepository employeeRepository) {
        this.damageReportRepository = damageReportRepository;
        this.vehicleRepository = vehicleRepository;
        this.employeeRepository = employeeRepository;
    }

    // Viser formularen til registrering af skader
    @GetMapping("/damagereport")
    public String showDamageReportForm(Model model) {
        // Objektet som Thymeleaf binder felterne til (th:object="${damageReport}")
        DamageReport damageReport = new DamageReport();
        // VIGTIGT (kontekst til eksamen):
        // lease_id i DamageReport-tabellen er NOT NULL og en foreign key til LeaseContract(lease_id).
        // Hvis leaseId er null, fejler INSERT i databasen.
        // Derfor sætter vi en default værdi her, så hidden field altid bliver sendt.
        damageReport.setLeaseId(1L);
        // Default værdier til hidden fields
        damageReport.setKmSlut(0);
        damageReport.setRepairCost(0.00);
        model.addAttribute("damageReport", damageReport);
        // Dropdown-data til formularen
        model.addAttribute("vehicles", vehicleRepository.findAllOrderedByVin());
        model.addAttribute("employees", employeeRepository.findAll());
        return "pages/damagereport";
    }

    // Modtager POST fra formularen og gemmer skadesrapporten i databasen
    @PostMapping("/damage/save")
    public String saveDamageReport(@ModelAttribute("damageReport") DamageReport damageReport) {
        try {
            damageReportRepository.save(damageReport);
            return "redirect:/damagereport?success=true";
        } catch (Exception e) {
            // Logger fejl så vi kan se den konkrete database-fejl (fx foreign key constraints)
            log.error("Failed to save damage report", e);
            return "redirect:/damagereport?error=true";
        }
    }
}

