package com.projet.stock.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.projet.stock.domaine.Message;
import com.projet.stock.config.JwtTokenUtil;
import com.projet.stock.domaine.JwtResponse;
import com.projet.stock.exception.ResourceNotFoundException;
import com.projet.stock.model.Admin;
import com.projet.stock.model.Expert;
import com.projet.stock.model.Generaliste;
import com.projet.stock.model.User;
import com.projet.stock.repository.AdminRepository;
import com.projet.stock.repository.ExpertRepository;
import com.projet.stock.repository.GeneralisteRepository;
import com.projet.stock.repository.UserRepository;
import com.projet.stock.request.LoginRequest;
import com.projet.stock.request.RegisterRequest;
import com.projet.stock.request.RegisterRequestAdmin;
import com.projet.stock.request.RegisterRequestExpert;
import com.projet.stock.request.RegisterRequestGeneraliste;
import com.projet.stock.services.ExpertService;
import com.projet.stock.services.GeneralisteService;

import com.projet.stock.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired 	UserRepository repository;
	@Autowired 	AuthenticationManager authenticationManager;
	@Autowired	UserRepository userRepository;
	@Autowired	ExpertRepository expertRepository;
	@Autowired	GeneralisteRepository genRepository;
	@Autowired	AdminRepository adminRepository;
	private ExpertService expertService ;
	@Autowired
	private GeneralisteService generalisteService ;
	@Autowired	PasswordEncoder encoder;
	@Autowired	JwtTokenUtil jwtUtils;
	
	
	
	@PostMapping("/users/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest data) {
		System.out.println("aaaa");
		System.out.println(data.getPassword());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						data.getUsername(),
						data.getPassword())
			
				);
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
	
	/*****  partie Expert ************/
	
		@PostMapping("/users/signupExpert")
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
		  @GetMapping( "/getImageExpert/{id}" )
			public Expert getImage(@PathVariable("id") long id) throws IOException {

				Expert expert = expertRepository.findById(id).get();
				Expert img = new Expert(expertService.decompressZLib(expert.getImage()));
				return img;
			}
	  
		  @GetMapping( "/getExpert/{id}" )
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
/*********** Partie Generalistr ************/
		  @PostMapping("/users/signupGeneraliste")
			public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestGeneraliste signUpRequest) {
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
				Generaliste user = new Generaliste(signUpRequest.getUsername(), 
									 signUpRequest.getEmail(),
									 encoder.encode(signUpRequest.getPassword()),
											 signUpRequest.getGender(),
											 signUpRequest.getTelephone(),
											 signUpRequest.getImage());
				genRepository.save(user);

				return ResponseEntity.ok(new Message("User registered successfully!"));
			}	  
		  
		  
		  
		  
		  
		  
		  /****** partie de premiere projet*************/ 
	 @GetMapping("/users")
	  public List<User> getAllUtilisateur() {
	    System.out.println("Get all Utilisateur...");
	 
	    List<User> Utilisateur = new ArrayList<>();
	    repository.findAll().forEach(Utilisateur::add);
	 
	    return Utilisateur;	
	  }

	@GetMapping("/generaliste/{id}")
	public ResponseEntity<Generaliste> getGenById(@PathVariable(value = "id") long UtilisateurId)
			throws ResourceNotFoundException {
		Generaliste Utilisateur = genRepository.findById(UtilisateurId)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found for this id : " + UtilisateurId));
		return ResponseEntity.ok().body(Utilisateur);
	}
	@GetMapping("/expert/{id}")
	public ResponseEntity<Expert> getExpertById(@PathVariable(value = "id") long UtilisateurId)
			throws ResourceNotFoundException {
Expert Utilisateur = expertRepository.findById(UtilisateurId)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found for this id : " + UtilisateurId));
		return ResponseEntity.ok().body(Utilisateur);
	}

	//crud
	 
	
	
	
		@DeleteMapping("/users/{id}")
	public Map<String, Boolean> deleteUtilisateur(@PathVariable(value = "id") Long UtilisateurId)
			throws ResourceNotFoundException {
		User Utilisateur = repository.findById(UtilisateurId)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found  id :: " + UtilisateurId));

		repository.delete(Utilisateur);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	  
	 
	  @DeleteMapping("/users/delete")
	  public ResponseEntity<String> deleteAllUtilisateur() {
	    System.out.println("Delete All Utilisateur...");
	 
	    repository.deleteAll();
	 
	    return new ResponseEntity<>("All Utilisateurs have been deleted!", HttpStatus.OK);
	  }
	 
	

	 /*@PutMapping("/Generaliste/{id}")
	  public String updateGeneraliste(@PathVariable("id") long id, @RequestParam("image") MultipartFile image,
	    	   	 //@RequestParam("username") String username,
	    		// @RequestParam("email") int email,
	    		 @RequestParam("gender") String gender,
	    		 @RequestParam("telephone") long telephone) {
	    System.out.println("Update Utilisateur with ID = " + id + "...");
	 
	    Optional<Generaliste> UtilisateurInfo = genRepository.findById(id);

	    	Generaliste utilisateur = UtilisateurInfo.get();
	    	utilisateur.setTelephone(utilisateur.getTelephone());
	    	utilisateur.setGender(utilisateur.getGender());
	    	      //  utilisateur.getEmail();
	        // utilisateur.getUsername();
	    	generalisteService.saveGeneraliste(id , image, utilisateur.getGender(), utilisateur.getTelephone());
	      return ("Done !!!");
	    } */
	 
		/*@PutMapping("/updateGeneraliste/{id}")
		 public String updateExpert(@PathVariable("id") long id,
													 @RequestParam("image") MultipartFile image,
										    	   	 //@RequestParam("username") String username,
										    		// @RequestParam("email") int email,
										    		 @RequestParam("gender") String gender,
										    		 @RequestParam("telephone") long telephone) {
			 Optional<Generaliste> expertA = genRepository.findById(id);

			 Generaliste expertP = expertA.get();
		        expertP.setTelephone(expertP.getTelephone());
		        expertP.setGender(expertP.getGender());
		        generalisteService.saveGeneraliste(id , image, expertP.getGender(), expertP.getTelephone());
			    	return "redirect:/listProducts.html";
			    }*/
		  
		    
		
		
		@PostMapping("/users/signupAdmin")
		public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestAdmin signUpRequest) {
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
			Admin user = new Admin(signUpRequest.getUsername(), 
								 signUpRequest.getEmail(),
								 encoder.encode(signUpRequest.getPassword()),
										 signUpRequest.getImage());
			adminRepository.save(user);

			return ResponseEntity.ok(new Message("User registered successfully!"));
		}	  
		
		// rahy bsh twali put ll lokhrina update prodil expert et medecin 
	    @PostMapping("/addP")
	    public String saveProduct(@RequestParam("file") MultipartFile file,
	    		@RequestParam("pname") String name,
	    		@RequestParam("price") int price,
	    		@RequestParam("desc") String desc)
	    {
	    	//productService.saveProductToDB(file, name, desc, price);
	    	return "redirect:/listProducts.html";
	    }
		 
}
