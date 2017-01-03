package com.niit.collaboration.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.model.UserDetails;

public class TUserController {

	@RestController
	public class TUserController {
		
		 private static final Logger log = LoggerFactory.getLogger(TUserController.class);
			

		@Autowired
		UserDetails user;

		@Autowired
		TUserDAO tUserDAO;

		// http://locahost:8080/collaboration/tusers
		@RequestMapping(value = "/tusers", method = RequestMethod.GET)
		public ResponseEntity<List<UserDetails>> getAllUsers() {
			log.debug("Starting of the method getAllUsers" );
			List<UserDetails> users = tUserDAO.list();

			if (users.isEmpty()) {
				user.setErrorCode("404");
				user.setErrorMessage("No Users are available");
			}
			log.debug("Ending of the method getAllUsers" );
			return new ResponseEntity<List<UserDetails>>(users, HttpStatus.OK);
		}

		// http://locahost:8080/collaboration/tuser/srinivas
		@RequestMapping(value = "/tuser/{id}", method = RequestMethod.GET)
		public ResponseEntity<UserDetails> getUserDetails(@PathVariable("id") String id) {
			log.debug("Starting of the method getUserDetails" );
			user = tUserDAO.get(id);
			
			if (user== null) {
	           user = new UserDetails();
				user.setErrorCode("404"); // NLP NullPointerException
				user.setErrorMessage("Iser does not exist with this id :" + id);
			}
			log.debug("Ending of the method getUserDetails" );
			return new ResponseEntity<UserDetails>(user, HttpStatus.OK);
		}

		@RequestMapping(value = "/user/authenticate/", method = RequestMethod.POST)
		public ResponseEntity<UserDetails> login(@RequestBody UserDetails user, HttpSession session) {
			log.debug("Starting of the method login" );
			
			String id = user.getId();
			 user = tUserDAO.validate(user.getId(), user.getPassword());
			if (user == null) {
				user = new UserDetails();
				user.setErrorCode("404"); // NLP NullPointerException
				user.setErrorMessage("User does not exist with this id:" + id);
			} else {
				user.setIsOnline('Y');
				user.setErrorCode("200"); // NLP NullPointerException
				user.setErrorMessage("You have successfully loggedIn");
				tUserDAO.update(user);

				session.setAttribute("loggedInUserID", user.getId());
			}
			log.debug("Ending of the method login" );
			return new ResponseEntity<UserDetails>(user, HttpStatus.OK);
		}

		@RequestMapping(value = "/tlogout/", method = RequestMethod.GET)
		public ResponseEntity<UserDetails> logout(HttpSession session) {
			log.debug("Starting of the method logout" );
			
			String loggedInUserID= (String) session.getAttribute("loggedInUserID");
			
			log.debug("loggedInUserID : " + loggedInUserID);
			
			user =tUserDAO.get(loggedInUserID);
			
			log.debug("user:"+ user);
			user.setIsOnline('N');

			session.invalidate();

			if (tUserDAO.update(user)) {
				user.setErrorCode("200");
				user.setErrorMessage("You have logged out successfully");
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("You could not logged. please contact admin");
			}
			log.debug("Ending of the method logout" );
			return new ResponseEntity<UserDetails>(user, HttpStatus.OK);

		}

		@RequestMapping(value = "/tregister/", method = RequestMethod.POST)
		public ResponseEntity<UserDetails> register(@RequestBody UserDetails user) 
		{
			log.debug("Starting of the method register" );
			if (tUserDAO.get(user.getId()) != null) {
				user.setErrorCode("404");
				user.setErrorMessage("With this id, the record is already exist.  Please choose another id");
			} else {
				user.setStatus('N');
				if (tUserDAO.save(user)) {
					user.setErrorCode("200");
					user.setErrorMessage("Your Registration is Successfull");

				} else {
					user.setErrorCode("405");
					user.setErrorMessage("Unable process your registration. Please contact Admin");
				}
			}
			log.debug("Ending of the method register" );
			return new ResponseEntity<UserDetails>(user, HttpStatus.OK);

		}
		
		
		
		@RequestMapping(value = "/taccept/{id}", method = RequestMethod.GET)
		public ResponseEntity<UserDetails> accept(@PathVariable("id") String id) {
			log.debug("Starting of the method register" );
			
			  
				user = updateStatus(id, 'A',"");
				
				return new  ResponseEntity<UserDetails>(user,HttpStatus.OK);
				
			
			
			
		}
		
		
		@RequestMapping(value = "/treject/{id}/{reason}", method = RequestMethod.GET)
		public ResponseEntity<UserDetails> reject(@PathVariable("id") String id ,@PathVariable("reason")  String reason) {
			log.debug("Starting of the method register" );
			
			 
			user = updateStatus(id, 'R', reason);
			
		
			
			return new  ResponseEntity<UserDetails>(user,HttpStatus.OK);
			
			
			
		}
		
		
		private UserDetails updateStatus(String id, char status, String reason)
		{
			log.debug("Starting of the method updateStatus" );
			
			log.debug("status: " + status);
		  user =tUserDAO.get(id);
		  
		  if(user==null)
		  {
			  user = new UserDetails();
			  user.setErrorCode("404");
			  user.setErrorMessage("Could not update the status");
		  }
		  else
		  {
				
			  user.setStatus(status);
			  user.setReason(reason);
			  tUserDAO.update(user);
		  }
		  log.debug("Ending of the method updateStatus" );
		return user;

	
	
}
