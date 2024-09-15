package com.box.bookstore.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.box.bookstore.api.AppointmentApi;
import com.box.bookstore.model.DoctorModel;
import com.box.bookstore.model.PatientModel;
import com.box.bookstore.repo.Doctor_repo;
import com.box.bookstore.util.MailUtil;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class DoctorPageController {

	@Autowired
	private AppointmentApi appointmentApi;
	
	@Autowired
	private Doctor_repo doctor_repo;
	
	@Autowired
	private MailUtil mailUtil;
	
	@GetMapping("/doctor_interface")
	public String getAdminUserInterface(Model model,HttpSession httpSession) {
		

		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
		try {
		model.addAttribute("appointmentRequestList", appointmentApi.getAppointmentRequestList(doctorModel.getId()));
		}catch (Exception e) {
			// TODO: handle exception
			return "doctorServerError";
		}
		return "appointmentRequestList";
	}
	

    public static String generateRandomString(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    
    @Transactional
    public void changePasswordAndEmail(DoctorModel doctor,String randomPassword){
		doctor.setPassword(randomPassword);
		doctor_repo.save(doctor); 
		mailUtil.sendEmail(doctor.getDoctorPersonalDetailsModel().getGmail(),"Password Changed By System", "New Password: "+randomPassword);
    }
	
	@PostMapping("/forgot_doctor_password")
	public String forgotDoctorPassword(@RequestParam("email")String email,Model model,RedirectAttributes redirectAttributes) {
		
		DoctorModel doctor=doctor_repo.findByEmail(email);

		if(doctor==null) {
			redirectAttributes.addFlashAttribute("error","Email not found");
	        return "redirect:/doctor/forgot";
		}
		
		try {
		changePasswordAndEmail(doctor,generateRandomString(10));
        redirectAttributes.addFlashAttribute("successMessage", "Password changed and send to your email successfully Change password immediately");
        return "redirect:/doctor/login";
		}catch(Exception e) {
		}
		redirectAttributes.addFlashAttribute("error","Email not found");
        return "redirect:/doctor/forgot";
	}
	
	
	@GetMapping("/appointmentRequestList")
	public String getAppointmentRequestList(Model model,HttpSession httpSession) {
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
		model.addAttribute("appointmentRequestListIndicator","active");         
		try {
		model.addAttribute("appointmentRequestList", appointmentApi.getAppointmentRequestList(doctorModel.getId()));
		}catch (Exception e) {
			// TODO: handle exception
		
			return "doctorServerError";
		}
		return "appointmentRequestList";
		
	}
	
	@GetMapping("/unauthorized_doctor")
	public String getUnauthorizedDoctor(Model model,HttpSession httpSession) {
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
		model.addAttribute("appointmentRequestListIndicator","active");         
		try {
		model.addAttribute("appointmentRequestList", appointmentApi.getAppointmentRequestList(doctorModel.getId()));
		}catch (Exception e) {
			// TODO: handle exception
		
			return "doctorServerError";
		}
		return "unauthorized_doctor_header";
		
	}
	
	
	@GetMapping("/doctor_change_password")
	public String getDoctorChangePassword(Model model,HttpSession httpSession) {
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		return "doctorchangepassword";
		
	}
	
	@GetMapping("/registration")
	public String getRegistration() {
		return "doctorregistration";
	}
	
	
//	@GetMapping("/appointmentList")
//	public String getAppointmentList(Model model,HttpSession httpSession) {
//		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
//		model.addAttribute("appointmentListIndicator", "active");
//		model.addAttribute("appointmentList", appointmentApi.getAppointmentList(doctorModel.getId()));
//		return "appointmentList";
//	}
	
	@GetMapping("/appointmentCanceledList")
	public String getAppointmentCanceledList(Model model,HttpSession httpSession) {
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
		model.addAttribute("appointmentCancelListIndicator", "active");
		try {
		model.addAttribute("appointmentList", appointmentApi.getAppointmentCanceledList(doctorModel.getId()));
		}catch (Exception e) {
			// TODO: handle exception
			
			return "doctorServerError";
		}
		return "appointmentCanceledList";
		
	}
	
	@GetMapping("/appointmentAcceptedList")
	public String getAppointmentAcceptedList(Model model,HttpSession httpSession){
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctorModel=(DoctorModel) httpSession.getAttribute("validDoctor");
		model.addAttribute("appointmentAcceptedListIndicator", "active");
		
		try {
		model.addAttribute("appointmentList", appointmentApi.getAppointmentAcceptedList(doctorModel.getId()));
		}catch (Exception e) {
			// TODO: handle exception
		
			return "doctorServerError";
		}
		
		return "appointmentAcceptedList";
	}
	
	
	

	
}
