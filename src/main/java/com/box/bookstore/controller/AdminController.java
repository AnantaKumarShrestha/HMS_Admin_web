package com.box.bookstore.controller;



import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.box.bookstore.model.AdminModel;
import com.box.bookstore.model.ChangePasswordRequest;
import com.box.bookstore.model.DoctorModel;
import com.box.bookstore.model.StaffModel;
import com.box.bookstore.repo.Admin_repo;
import com.box.bookstore.repo.Doctor_repo;
import com.box.bookstore.service.AdminService;
import com.box.bookstore.service.StaffService;
import com.box.bookstore.util.MailUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private Doctor_repo doctor_repo;
	
	@Autowired
	private Admin_repo admin_repo;
	
	@Autowired
	private MailUtil mailUtil;
	
	@PostMapping("/login")
	public String postAdmin(AdminModel adminModel,Model model,HttpSession httpSession) {
		AdminModel admin=adminService.findAdmin(adminModel);
		if(admin==null) {
			model.addAttribute("admin_error","Admin username and password not matched");
			return "adminlogin";
		}
		
//		if(changedpass.equals("yes")) {
//			model.addAttribute("adminObj",admin);
//			return "adminchangepassword";
//		}
		try {
		if(admin.getChangedpassword().equals("yes")) {
//			int n=p.getId();			
//			return "redirect:/patientinterface/"+n;
			httpSession.setAttribute("validAdmin",admin);
			httpSession.setMaxInactiveInterval(999999999);
			return "redirect:/doctorlist";
		}
		}catch(Exception e) {
		
		
			model.addAttribute("adminObj",admin);
			return "adminchangepassword";
		
		}
		model.addAttribute("adminObj",admin);
		return "adminchangepassword";
       }
		

		
	@GetMapping("login")
	public String getAdminLogin() {
		return "adminlogin";
	}
	
	@GetMapping("/forgot")
	public String getForget() {
		return "forget_admin";
	}
	
	
	@PostMapping("/change_email_password")
	public String postChangeEmailPassword(AdminModel adminModel,Model model,HttpSession httpSession) {
		
		
		String cpassword=adminModel.getCpassword();
		if(adminModel.getPassword().equals(cpassword)) {
			adminModel.setChangedpassword("yes");
			adminService.changeEmailPassword(adminModel);
			return "redirect:/adminuserinterface";
			
		}
		model.addAttribute("password_not_matched","Password not matched");
		model.addAttribute("adminObj", adminModel);
		return "adminchangepassword";
	}
	
	
	
	@PostMapping("/change_doctor_password")
	public String changeDoctorPassword(ChangePasswordRequest changePasswordRequest,Model model,HttpSession httpSession,RedirectAttributes redirectAttributes) {
		
		if(httpSession.getAttribute("validDoctor")==null) {
			return "login";
		}
		
		DoctorModel doctor=(DoctorModel) httpSession.getAttribute("validDoctor");
		
		String currentPassword=changePasswordRequest.getCurrentPassword();
		String newPassword = changePasswordRequest.getNewPassword();
		String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();
		
		
		if(doctor.getPassword().equals(currentPassword)) {
			if(newPassword.equals(confirmNewPassword)){
				doctor.setPassword(newPassword);
               doctor_repo.save(doctor);
               
               redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully");
				
               return "redirect:/appointmentRequestList";
			}
			model.addAttribute("password_not_matched","Password not matched");
			model.addAttribute("adminObj", changePasswordRequest);
			
			return "doctorchangepassword";
			
		}
		model.addAttribute("password_not_matched","Current Password not matched");
		model.addAttribute("adminObj", changePasswordRequest);
		return "doctorchangepassword";
	}
	
	
	@PostMapping("/change_admin_password")
	public String changeAdminPassword(ChangePasswordRequest changePasswordRequest,Model model,HttpSession httpSession,RedirectAttributes redirectAttributes) {
		
		if(httpSession.getAttribute("validAdmin")==null) {
			return "login";
		}
		
		AdminModel admin=(AdminModel) httpSession.getAttribute("validAdmin");
		
		String currentPassword=changePasswordRequest.getCurrentPassword();
		String newPassword = changePasswordRequest.getNewPassword();
		String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();
		
		
		if(admin.getPassword().equals(currentPassword)) {
			if(newPassword.equals(confirmNewPassword)){
				admin.setPassword(newPassword);
               adminService.save(admin);
               
               redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully");
               return "redirect:/doctorlist";
			}
			model.addAttribute("password_not_matched","Password not matched");
			return "adminchangepassword2";
			
		}
		model.addAttribute("password_not_matched","Current Password not matched");
		return "adminchangepassword2";
	}
	
	
	
	@PostMapping("/addStaff")
	public String postAddStaff(StaffModel staffModel,HttpSession httpSession) {
		if(httpSession.getAttribute("validAdmin")==null) {
			return "adminlogin";
		}
		staffService.addStaff(staffModel);
		return "redirect:/addStaff";
	}
	
	
	@GetMapping("/logout")
	private String logOut(HttpSession session) {
		session.invalidate();//session kill
		return "adminlogin";
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
	    public void changePasswordAndEmail(AdminModel admin,String randomPassword){
			admin.setPassword(randomPassword);
			admin_repo.save(admin);
			mailUtil.sendEmail(admin.getGmail(),"Password Changed By System", "New Password: "+randomPassword);
	    }
		
		@PostMapping("/forgot_admin_password")
		public String forgotDoctorPassword(@RequestParam("email")String email,Model model,RedirectAttributes redirectAttributes) {
			
			AdminModel admin=admin_repo.getByMail(email);

			if(admin==null) {
				redirectAttributes.addFlashAttribute("error","Email not found");
		        return "redirect:/admin/forgot";
			}
			
			try {
				System.out.println("as");
			changePasswordAndEmail(admin,generateRandomString(10));
	        redirectAttributes.addFlashAttribute("successMessage", "Password changed and send to your email successfully Change password immediately");
	        return "redirect:/admin/login";
			}catch(Exception e) {
			}
			redirectAttributes.addFlashAttribute("error","Email not found");
	        return "redirect:/admin/forgot";
		}
	
	
	

}
