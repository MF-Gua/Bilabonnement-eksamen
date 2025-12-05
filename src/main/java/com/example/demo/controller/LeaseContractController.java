package com.example.demo.controller;

import com.example.demo.model.CustomerModel;
import com.example.demo.model.LeaseModel;
import com.example.demo.model.Vehicle;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.LeaseService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LeaseContractController {

    private final LeaseService leaseService;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public LeaseContractController(LeaseService leaseService,
                                   CustomerRepository customerRepository,
                                   VehicleRepository vehicleRepository) {
        this.leaseService = leaseService;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Redirect from /lease to /leaseContract for navigation compatibility
    @GetMapping("/lease")
    public String redirectToLeaseContract() {
        return "redirect:/leaseContract";
    }

    // Method that shows the createContract page and handles GET requests
    @GetMapping("/leaseContract")
    public String showCreateLeaseForm(
            // Query parameters to pass data to thymeleaf template
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "vinId", required = false) String vinId,
            Model model) {

        // Loads all customers and vehicles sorted for dropdown menu
        var customers = customerRepository.findAll(Sort.by("firstName"));
        var vehicles = vehicleRepository.findAll(Sort.by("registrationNo"));

        // This makes the leaseform data as an object for the thymeleaf form
        LeaseModel leaseForm = new LeaseModel();
        if (customerId != null) leaseForm.setCustomerId(customerId);
        if (vinId != null) leaseForm.setVinId(vinId);

        // This finds the customer and vehicle data in the database
        CustomerModel selectedCustomer = (customerId != null)
                ? customerRepository.findById(customerId).orElse(null)
                : null;

        Vehicle selectedVehicle = (vinId != null)
                ? vehicleRepository.findById(vinId).orElse(null)
                : null;

        // This puts all data to the model and returns the view input data for thymeleaf
        model.addAttribute("customers", customers);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("leaseForm", leaseForm);
        model.addAttribute("selectedCustomer", selectedCustomer);
        model.addAttribute("selectedVehicle", selectedVehicle);

        return "pages/leaseContract";
    }

    // Method that handles "opret lejekontrakt" submit
    @PostMapping("/leaseContract")
    public String createLease(@ModelAttribute("leaseForm") LeaseModel leaseForm) {

        // Find the full customer and vehicle entity using the IDs from the form
        CustomerModel customer = customerRepository.findById(leaseForm.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(leaseForm.getVinId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        // Attaches customer and vehicle data to the lease entity
        leaseForm.setCustomer(customer);
        leaseForm.setVehicle(vehicle);

        // Saves the lease data to the database
        leaseService.save(leaseForm);

        // Redirects back to the same page
        return "pages/leaseContractSuccess";
    }
}

