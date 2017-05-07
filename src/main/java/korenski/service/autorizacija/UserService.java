package korenski.service.autorizacija;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import korenski.model.autorizacija.User;

@Service
public class UserService {

	private SecureRandom random = new SecureRandom();
	
	private JavaMailSender mailSender=null;
	
	public boolean checkInput(User user){
		
		
		return true;
	}
	
	public String generatePassword() {
		
	    return new BigInteger(60, random).toString(32);
	  }
	
	public void sendPassToUser(String password, User user){
		
		if(mailSender == null){
			mailSender = new JavaMailSenderImpl();
		    ((JavaMailSenderImpl) mailSender).setHost("smtp.gmail.com");
		    ((JavaMailSenderImpl) mailSender).setPort(587);
		     
		    ((JavaMailSenderImpl) mailSender).setUsername("bsepprojectimpl@gmail.com");
		    ((JavaMailSenderImpl) mailSender).setPassword("LetsGetADigree");
		     
		    Properties props = ((JavaMailSenderImpl) mailSender).getJavaMailProperties();
		    props.put("mail.transport.protocol", "smtp");
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.debug", "true");
		    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		}
		
		SimpleMailMessage msg = new SimpleMailMessage();
		
		msg.setTo("teauvranju@gmail.com");
        msg.setSubject("Registration");
        msg.setText("Mr(s) " +user.getName() +" "+ user.getSurname()+ ",\n You have a registered account under the following username : "+user.getUsername()+".\n Your password is "+password);
        
		try{
			mailSender.send(msg);
			
			System.out.println("Sve je ok proslo!");
			
		}catch(Exception e){
			e.printStackTrace();
			//repository.delete(musterija.getId());
			System.out.println("Imamo gresku");
			
		}
		
	}
	
}
