package com.box.bookstore.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.box.bookstore.model.DoctorModel;
import com.box.bookstore.repo.Doctor_repo;
import com.box.bookstore.service.DoctorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private Doctor_repo doctor_repo;
	
	@GetMapping("/login")
	public String getlogin() {
		return "login";
	}
	
	@GetMapping("/forgot")
	public String getForget() {
		return "forget_doctor";
	}
	
	@PostMapping("/login")
	public String postlogin(DoctorModel doctortModel,Model model,HttpSession httpSession) {
		
		DoctorModel p=doctorService.findDoctor(doctortModel);
		if(p==null) {
			model.addAttribute("username_error","username or password not matched");
			return "login";
		}
		try {
		if(p.getRegistered().equals("yes") && p.getAuthorized().equals("yes")) {
			httpSession.setAttribute("validDoctor", p);
			httpSession.setMaxInactiveInterval(999999999);
			return "redirect:/appointmentRequestList";
		}else if(p.getAuthorized().equals("no")) {
			httpSession.setAttribute("validDoctor", p);
			httpSession.setMaxInactiveInterval(999999999);
			return "redirect:/unauthorized_doctor";
		}
		}catch(Exception e) {
		
		int id=p.getId();
		model.addAttribute("doctorObject", doctorService.getDoctorId(id));
		return "doctorregistration";
		}
		int id=p.getId();
		model.addAttribute("doctorObject", doctorService.getDoctorId(id));
		
		return "doctorregistration";
	}
	
	
	
	@GetMapping("/signup")
	public String getsignup() {
		return "signup";
	}
	
	@PostMapping("/signup")
	public String postsignup(DoctorModel doctorModel,Model model) {
		
		String pass=doctorModel.getPassword();
		String conpass=doctorModel.getConpassword();
		DoctorModel p=doctorService.findSameEmail(doctorModel);
		if(p==null) {
		if(pass.equals(conpass)){
			doctorModel.setAuthorized("no");
			doctorService.addDoctor(doctorModel);
			return "login";
		}
		model.addAttribute("same_username_found", "Password not matched");
		return "signup";
		}
		model.addAttribute("same_username_found", "This email is already registered");
		return "signup";	
	}
	
	
	
//	@PostMapping("/registration")
//	public String postregistration(DoctorModel doctorModel) {
//		
//		doctorModel.setRegistered("yes");
//		doctorService.addDoctor(doctorModel);
//		return "redirect:/doctor_interface";
//		
//	}
	
	
	@PostMapping("/registration")
	public String postregistration(DoctorModel doctorModel,Model model,RedirectAttributes redirectAttributes) {
		
		int id=doctorModel.getId();
		
		if(!getDateTrue(doctorModel.getDoctorPersonalDetailsModel().getDob())) {
			model.addAttribute("doctorObject", doctorService.getDoctorId(id));
			model.addAttribute("message", "Not Valid Date Of Birth");
			return "doctorregistration";
		}
		
		
		if(!isAtLeast16YearsOld(doctorModel.getDoctorPersonalDetailsModel().getDob())) {
			model.addAttribute("doctorObject", doctorService.getDoctorId(id));
			model.addAttribute("message", "Age Should Be 24 Years old");
			return "doctorregistration";
		}
		
		if(doctor_repo.existsByGmail(doctorModel.getDoctorPersonalDetailsModel().getGmail())) {
			model.addAttribute("doctorObject", doctorService.getDoctorId(id));
			model.addAttribute("message", "Gmail exist");
			return "doctorregistration";
		}
		
		if(!isCurrentYearGreaterThanGivenYear(Integer.parseInt(doctorModel.getDoctorStudyDetailsModel().getPassoutyear()))) {
			model.addAttribute("doctorObject", doctorService.getDoctorId(id));
			model.addAttribute("message", "Not Valid Pass Out year");
			return "doctorregistration";
		}
		
		int positiveDifference=getYearDifference(Integer.parseInt(doctorModel.getDoctorStudyDetailsModel().getPassoutyear()));
		int experience=Integer.parseInt(doctorModel.getDoctorStudyDetailsModel().getExperience());
		
		if(positiveDifference<experience) {
			model.addAttribute("doctorObject", doctorService.getDoctorId(id));
			model.addAttribute("message", "Not Valid Experience "+ getYearDifference(Integer.parseInt(doctorModel.getDoctorStudyDetailsModel().getPassoutyear()))+" "+Integer.parseInt(doctorModel.getDoctorStudyDetailsModel().getExperience()));
			return "doctorregistration";
		}
		
		
	
		doctorModel.setRegistered("yes");
		doctorService.addDoctor(doctorModel);
		return "redirect:/doctor_interface";
		
	}
	
	 public static int getYearDifference(int givenYear) {
	        int currentYear = LocalDate.now().getYear();  // Get the current year
	        return currentYear - givenYear;  // Calculate the difference in years
	    }
	
	 public static boolean isCurrentYearGreaterThanGivenYear(int givenYear) {
	        int currentYear = LocalDate.now().getYear();
	        return currentYear > givenYear;
	    }
	
	public static boolean getDateTrue(String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate chosenDate = LocalDate.parse(date, formatter);

	    // Get the current date
	    LocalDate currentDate = LocalDate.now();
	    return chosenDate.isBefore(currentDate);
	}
	
	public static boolean isAtLeast16YearsOld(String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate chosenDate = LocalDate.parse(date, formatter);

	    // Get the current date
	    LocalDate currentDate = LocalDate.now();

	    // Calculate the period between the chosen date and the current date
	    Period period = Period.between(chosenDate, currentDate);

	    // Check if the period is at least 16 years
	    return period.getYears() >= 24;
	}
	
	@GetMapping("/logout")
	private String logOut(HttpSession session) {
		session.invalidate();//session kill
		return "login";
	}

}
