package com.projet.stock.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.ServletContext;
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
import com.projet.stock.model.Expert;
import com.projet.stock.model.Generaliste;
import com.projet.stock.repository.GeneralisteRepository;
import com.projet.stock.repository.UserRepository;
import com.projet.stock.request.LoginRequest;
import com.projet.stock.request.RegisterRequestGeneraliste;
import com.projet.stock.services.GeneralisteService;
import com.projet.stock.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/medecins")
public class MedecinController {
	@Autowired 	AuthenticationManager authenticationManager;
	@Autowired	GeneralisteRepository genRepository;
	@Autowired	UserRepository userRepository;
	@Autowired
	private GeneralisteService generalisteService ;
	@Autowired	PasswordEncoder encoder;
	@Autowired	JwtTokenUtil jwtUtils;
	@Autowired  ServletContext context;
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
	  @PutMapping("/update/{id}")
	  public ResponseEntity<Generaliste> updateGeneraliste(@PathVariable("id") long id, @RequestBody Generaliste Utilisateur) {
	    System.out.println("Update Utilisateur with ID = " + id + "...");
	 
	    Optional<Generaliste> UtilisateurInfo = genRepository.findById(id);

	    	Generaliste utilisateur = UtilisateurInfo.get();
	    	utilisateur.setTelephone(Utilisateur.getTelephone());
	    	utilisateur.setGender(Utilisateur.getGender());
	    	//utilisateur.setImage(Utilisateur.getImage());       //  utilisateur.getEmail();
	        // utilisateur.getUsername();
	    	
	      return new ResponseEntity<>(genRepository.save(utilisateur), HttpStatus.OK);
	    } 	
	  @PutMapping("/updateImage/{id}")
	  public String updateExpert(@PathVariable("id") long id,
				 @RequestParam("image") MultipartFile image) throws IOException
	    		  {
		//  Expert expert = expertRepository.findById(id).get();

			
		  generalisteService.updateImage(id, image);
		    	return "Done  , image updated!!!";
		    }
	 
	  @GetMapping(path = { "/getImage/{id}" })
		public Generaliste getImage(@PathVariable("id") long id) throws IOException {

			Generaliste generaliste = genRepository.findById(id).get();
			Generaliste img = new Generaliste(generalisteService.decompressZLib(generaliste.getImage()));
			return img;
		}
  
	  @GetMapping(path = { "/getExpert/{id}" })
		public  Generaliste getGenetaliste (@PathVariable("id") long id) throws IOException {

		  Generaliste generaliste = genRepository.findById(id).get();
		  Generaliste img = new Generaliste(generaliste.getUsername(),
				  generaliste.getEmail(),
				  generaliste.getPassword(),
				  generaliste.getGender(),
				  generaliste.getTelephone(),
				 generalisteService.decompressZLib(generaliste.getImage()));
			return img;
		}
	    		

}

