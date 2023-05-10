package ba.sum.fsre.knjiznica.controller;

import ba.sum.fsre.knjiznica.model.Book;
import ba.sum.fsre.knjiznica.model.Reservation;
import ba.sum.fsre.knjiznica.repositories.FleetRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import ba.sum.fsre.knjiznica.model.Fleet;
import ba.sum.fsre.knjiznica.model.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class FleetController {

    @Autowired
    FleetRepository fleetRepo;
    @GetMapping("/upload")
    public String showUploadForm() {
        return "Slike/create";
    }
    @PostMapping("/uploadfile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Spremanje datoteke u "static" direktorij projekta
        Path staticDir = Paths.get("src/main/resources/static");
        Path filePath = staticDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // preusmjeravanje korisnika na stranicu za prikaz uspješnosti učitavanja
        return "redirect:/fleet";
    }


    @GetMapping("/fleet")
    public String listFleet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("userDetails", userDetails);
        List<Fleet> listFleet = fleetRepo.findAll();
        model.addAttribute("listFleet", listFleet);
        model.addAttribute("activeLink", "Fleet");
        return "Fleet/index";
    }

    @GetMapping("/fleet/add")
    public String showAddFleetForm (Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("activeLink", "Fleet");
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("fleet", new Fleet());
        return "Fleet/create";
    }

    @PostMapping("/fleet/add")
    public String addBook (@Valid Fleet fleet, Model model,@RequestParam("file") MultipartFile file) throws IOException {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("activeLink", "Fleet");
            model.addAttribute("userDetails", userDetails);
            model.addAttribute("fleet", fleet);
       /* // Generiranje slučajnog ID-a za naziv datoteke
        String fileId = UUID.randomUUID().toString();

        // Dobivanje originalnog naziva datoteke
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Dobivanje ekstenzije datoteke
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // Novi naziv datoteke s verzijom
        String newFileName = fileId + fileExtension;*/

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Spremanje datoteke u "static" direktorij projekta
        Path staticDir = Paths.get("src/main/resources/static");
        Path filePath = staticDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        fleet.setImage(fileName);
        // preusmjeravanje korisnika na stranicu za prikaz uspješnosti učitavanja
            fleetRepo.save(fleet);
             return "redirect:/fleet";
    }
    @GetMapping("/fleet/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Fleet fleet = fleetRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        fleetRepo.delete(fleet);
        return "redirect:/fleet";
    }

}
