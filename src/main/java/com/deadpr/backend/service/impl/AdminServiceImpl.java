package com.deadpr.backend.service.impl;

import com.deadpr.backend.dto.admin.CreatePackageRequestDto;
import com.deadpr.backend.dto.admin.CreateTrainerRequestDto;
import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.Role;
import com.deadpr.backend.model.Trainer;
import com.deadpr.backend.model.TrainingPackage;
import com.deadpr.backend.model.User;
import com.deadpr.backend.repository.BookingRepository;
import com.deadpr.backend.repository.TrainerRepository;
import com.deadpr.backend.repository.TrainingPackageRepository;
import com.deadpr.backend.repository.UserRepository;
import com.deadpr.backend.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.deadpr.backend.service.FileUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired private UserRepository userRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private TrainingPackageRepository trainingPackageRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private FileUploadService fileUploadService;


    @Override
    public User createTrainer(CreateTrainerRequestDto request, MultipartFile profileImage) throws IOException {
        log.info("Admin is attempting to create a new trainer with email: {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use.");
        }

        String imageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            imageUrl = fileUploadService.uploadFile(profileImage);
            log.info("Profile image uploaded to Cloudinary: {}", imageUrl);
        }
        User userAccount = new User();
        userAccount.setName(request.getName());
        userAccount.setEmail(request.getEmail());
        userAccount.setPhoneNumber(request.getPhoneNumber());
        userAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        userAccount.setRole(Role.ROLE_TRAINER);
        userAccount.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(userAccount);

        Trainer trainerProfile = new Trainer();
        trainerProfile.setDescription(request.getDescription());
        trainerProfile.setSpecializations(request.getSpecializations());
        trainerProfile.setUser(savedUser);
        trainerProfile.setProfileImageUrl(imageUrl);
        trainerRepository.save(trainerProfile);

        log.info("Trainer account and profile created successfully for user ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public TrainingPackage createPackage(CreatePackageRequestDto request) {

        log.info("Admin is attempting to create a new package named: {}", request.getName());
        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + request.getTrainerId()));
        TrainingPackage newPackage = new TrainingPackage();
        newPackage.setName(request.getName());
        newPackage.setDescription(request.getDescription());
        newPackage.setPrice(request.getPrice());
        newPackage.setDurationInDays(request.getDurationInDays());
        newPackage.setTrainer(trainer);
        TrainingPackage savedPackage = trainingPackageRepository.save(newPackage);
        log.info("Package created successfully with ID: {}", savedPackage.getId());
        return savedPackage;
    }

    @Override
    public Map<String, Long> getDashboardStats() {
        log.info("Admin fetching dashboard stats");
        long totalClients = userRepository.countByRole(Role.ROLE_CLIENT);
        long totalTrainers = userRepository.countByRole(Role.ROLE_TRAINER);
        long totalBookings = bookingRepository.count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalClients", totalClients);
        stats.put("totalTrainers", totalTrainers);
        stats.put("totalBookings", totalBookings);

        return stats;
    }

    @Override
    public List<User> getRecentClients() {
        log.info("Admin fetching recent clients");
        return userRepository.findByRole(Role.ROLE_CLIENT, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Override
    public List<Booking> getRecentBookings() {
        log.info("Admin fetching recent bookings");
        return bookingRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "bookingDate"))).getContent();
    }
}
