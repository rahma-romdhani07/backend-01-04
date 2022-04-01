package com.projet.stock.Controller;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.projet.stock.config.JwtTokenUtil;
import com.projet.stock.domaine.JwtResponse;
import com.projet.stock.domaine.Message;
import com.projet.stock.exception.ResourceNotFoundException;
import com.projet.stock.model.Expert;
import com.projet.stock.model.Generaliste;
import com.projet.stock.repository.ExpertRepository;
import com.projet.stock.repository.UserRepository;
import com.projet.stock.request.LoginRequest;
import com.projet.stock.request.RegisterRequestExpert;
import com.projet.stock.request.RegisterRequestGeneraliste;
import com.projet.stock.services.ExpertService;
import com.projet.stock.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/expert")
public class ExpertController {
	
	@Autowired 	AuthenticationManager authenticationManager;
	@Autowired	ExpertRepository expertRepository;
	@Autowired	UserRepository userRepository;
	@Autowired
	private ExpertService expertService ;
	@Autowired	PasswordEncoder encoder;
	@Autowired	JwtTokenUtil jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest data) {
		System.out.println("aaaa");
		System.out.println(data.getPassword());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						data.getUsername(),
						data.getPassword()));	
		  System.out.println("bbbbb");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail()
											));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerExpert(@Valid @RequestBody RegisterRequestExpert signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Email is already in use!"));
		}

		// Create new user's account
		Expert expert = new Expert(signUpRequest.getUsername(), 
				 signUpRequest.getEmail(),
				 encoder.encode(signUpRequest.getPassword()),
						 signUpRequest.getGender(),
						 signUpRequest.getTelephone(),
						 signUpRequest.getImage());
		expertRepository.save(expert);

		return ResponseEntity.ok(new Message("User registered successfully!"));
	}	  
	  @PutMapping("/updateExpert/{id}")
	  public ResponseEntity<Expert> updateExpert(@PathVariable("id") long id, @RequestBody Expert Utilisateur) {
	    System.out.println("Update Utilisateur with ID = " + id + "...");
	 
	    Optional<Expert> UtilisateurInfo = expertRepository.findById(id);

	    Expert utilisateur = UtilisateurInfo.get();
	    	utilisateur.setTelephone(Utilisateur.getTelephone());
	    	utilisateur.setGender(Utilisateur.getGender());
	    	utilisateur.setImage(Utilisateur.getImage());       //  utilisateur.getEmail();
	        // utilisateur.getUsername();
	    	
	      return new ResponseEntity<>(expertRepository.save(utilisateur), HttpStatus.OK);
	    } 
	  @PutMapping("/updateImageExpert/{id}")
	  public String updateExpert(@PathVariable("id") long id,
				 @RequestParam("image") MultipartFile image) throws IOException
	    		  {
		//  Expert expert = expertRepository.findById(id).get();

			
	       expertService.updateImage(id , image);
		    	return "Done !!!";
		    }
	  @GetMapping(path = { "/getImageExpert/{id}" })
		public Expert getImage(@PathVariable("id") long id) throws IOException {

			Expert expert = expertRepository.findById(id).get();
			Expert img = new Expert(expertService.decompressZLib(expert.getImage()));
			return img;
		}
  
	  @GetMapping(path = { "/getExpert/{id}" })
		public Expert getExpert(@PathVariable("id") long id) throws IOException {

			Expert expert = expertRepository.findById(id).get();
			Expert img = new Expert(expert.getUsername(),
					                expert.getEmail(),
					                expert.getPassword(),
					                expert.getGender(),
					                expert.getTelephone(),
					                expertService.decompressZLib(expert.getImage()));
			return img;
		}

 }

