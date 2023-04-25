package com.devinsight.lab4.databaseelab4solution.controllers;

import com.devinsight.lab4.databaseelab4solution.models.Doctor;
import com.devinsight.lab4.databaseelab4solution.models.Hospital;
import com.devinsight.lab4.databaseelab4solution.services.DatabaseOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DatabaseOperations databaseOperations;

    @GetMapping("")
    public String getDoctors(Model model) {
        model.addAttribute("doctors", databaseOperations.getDoctors());

        String message = (String )model.getAttribute("message");
        model.addAttribute("message", message);

        return "doctors";
    }

    // delete doctor
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        databaseOperations.deleteDoctor(id);
        redirectAttributes.addFlashAttribute("message", "Doctors deleted successfully");
        return "redirect:/doctors";
    }

    @GetMapping("/new")
    public String addPatient(Model model) {
        List<Hospital> hospitals = databaseOperations.getHospitals();
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("hospitals", hospitals);
        return "new_doctor";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("doctor") Doctor doctor, RedirectAttributes redirectAttributes) {
        databaseOperations.saveDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "New doctors added successfully");
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Doctor doctor = databaseOperations.getDoctorById(id);
        List<Hospital> hospitals = databaseOperations.getHospitals();
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("doctor", doctor);
        return "edit_doctor";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(@ModelAttribute("doctor") Doctor doctor, @PathVariable("id") int id) {
        doctor.setId(id);
        databaseOperations.updateDoctor(doctor);
        return "redirect:/doctors";
    }
}
