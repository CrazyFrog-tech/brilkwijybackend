package nl.spring.brilkwijt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PostMapping(value = "/description", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Bril postDescription(@RequestPart String bril,@RequestPart List<MultipartFile> images) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Bril realBril = mapper.readValue(bril, Bril.class);
        Bril savedBril = brilRepository.save(realBril);
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
