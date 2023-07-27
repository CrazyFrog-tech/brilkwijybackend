package nl.spring.brilkwijt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import nl.spring.brilkwijt.repos.dto.Bril;
import nl.spring.brilkwijt.repos.BrilRepository;
@CrossOrigin
@RestController
@RequestMapping("")
public class BrilController {
	
	@Autowired
	BrilRepository brilRepository;


    @Value("${image.upload.path}")
    private String imageUploadPath;
	
	@PostMapping
	public String create(@RequestBody Bril bril) {
		brilRepository.save(bril);
		return "Bril is created";
	}
    @GetMapping("/brillen")
    public List<Bril> getAllBrillen() {
        List<Bril> brillen = brilRepository.findAll();
        return brillen;
    }

    @PostMapping("/description")
    public Bril postDescription(@RequestBody Bril bril, List<MultipartFile> images) throws IOException {
        Bril savedBril = brilRepository.save(bril);
        List<String> imageFileNames = new ArrayList<>();
        for (MultipartFile image : images){
            String imageFileName = "user_" + savedBril.getId() + "_" + image.getOriginalFilename();
            Path imagePath = Path.of(imageUploadPath + imageFileName);
            if (!Files.exists(imagePath)) {
                Files.createDirectories(imagePath);
            }
            //write data to file
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            imageFileNames.add(imageFileName);
        }
        savedBril.setImageFilenames(imageFileNames);

        return savedBril;

    }

}
