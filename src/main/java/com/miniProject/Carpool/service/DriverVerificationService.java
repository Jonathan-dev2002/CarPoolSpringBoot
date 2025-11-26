package com.miniProject.Carpool.service;


import com.miniProject.Carpool.dto.DriverVerificationRequest;
import com.miniProject.Carpool.dto.DriverVerificationResponse;
import com.miniProject.Carpool.dto.DriverVerificationStatusUpdateRequest;
import com.miniProject.Carpool.dto.search.DriverVerificationSearchRequest;
import com.miniProject.Carpool.dto.search.UserSearchRequest;
import com.miniProject.Carpool.mapper.DriverVerificationMapper;
import com.miniProject.Carpool.model.*;
import com.miniProject.Carpool.repository.DriverVerificationRepository;
import com.miniProject.Carpool.repository.RouteRepository;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.spec.DriverVerificationSpecification;
import com.miniProject.Carpool.util.ApiError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverVerificationService {
    private final DriverVerificationRepository driverVerificationRepository;
    private final UserRepository userRepository;
    private final DriverVerificationMapper driverVerificationMapper;
    private final RouteRepository routeRepository;

    @Transactional
    public DriverVerificationResponse createDriverVerification(
            String userId,
            DriverVerificationRequest request,
            MultipartFile licensePhoto,
            MultipartFile selfiePhoto
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiError(404, "User not found"));

        DriverVerification existing = driverVerificationRepository.findByUser(user).orElse(null);

        //Mock
        String licensePhotoUrl = "mock_url_licensePhotoUrl";
        String selfiePhotoUrl = "mock_url_selfiePhotoUrl";

        if (existing != null) {
            return updateExistingVerification(existing, request, licensePhoto != null ? licensePhotoUrl : null, selfiePhoto != null ? selfiePhotoUrl : null);
        }

        DriverVerification dv = DriverVerification.builder()
                .user(user)
                .licenseNumber(request.getLicenseNumber())
                .firstNameOnLicense(request.getFirstNameOnLicense())
                .lastNameOnLicense(request.getLastNameOnLicense())
                .typeOnLicense(request.getTypeOnLicense())
                .verificationStatus(VerificationStatus.PENDING)
                .licenseIssueDate(request.getLicenseIssueDate())
                .licenseExpiryDate(request.getLicenseExpiryDate())
                .licensePhotoUrl(licensePhotoUrl)
                .selfiePhotoUrl(selfiePhotoUrl)
                .build();


        DriverVerification savedDv = driverVerificationRepository.save(dv);
        return driverVerificationMapper.toResponse(savedDv);
    }

    private DriverVerificationResponse updateExistingVerification(DriverVerification existing, DriverVerificationRequest request, String newLicenseUrl, String newSelfieUrl) {
        //update field
        existing.setLicenseNumber(request.getLicenseNumber());
        existing.setFirstNameOnLicense(request.getFirstNameOnLicense());
        existing.setLastNameOnLicense(request.getLastNameOnLicense());
        existing.setTypeOnLicense(request.getTypeOnLicense());
        existing.setVerificationStatus(VerificationStatus.PENDING);

        if (newLicenseUrl != null) {
            existing.setLicensePhotoUrl(newLicenseUrl);
        }
        if (newSelfieUrl != null) existing.setSelfiePhotoUrl(newSelfieUrl);

        return driverVerificationMapper.toResponse(driverVerificationRepository.save(existing));
    }

    public DriverVerificationResponse getDriverVerificationById(String id) {
        DriverVerification dv = driverVerificationRepository.findById(id).orElseThrow(() -> new ApiError(404, "not found"));
        return driverVerificationMapper.toResponse(dv);
    }

    public void deleteDriverVerificationById(String id) {
        if (!driverVerificationRepository.existsById(id)) {
            throw new ApiError(404, "not found");
        }
//        if(driverVerificationRepository.findById(id).isPresent()) {})
        driverVerificationRepository.deleteById(id);
    }

    public Page<DriverVerificationResponse> searchDriverVerifications(DriverVerificationSearchRequest request) {
        int pageNo = Math.max(0, request.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNo, request.getLimit(), Sort.by("createdDate").descending());

        Specification<DriverVerification> spec = Specification.where(DriverVerificationSpecification.hasKeyword(request.getQ()))
                .and(DriverVerificationSpecification.hasStatus(request.getVerificationStatus()))
                .and(DriverVerificationSpecification.hasTypeOnLicense(request.getTypeOnLicense()));

        Page<DriverVerification> driverVerificationsPage = driverVerificationRepository.findAll(spec, pageable);

        return driverVerificationsPage.map(driverVerificationMapper::toResponse);
    }

    @Transactional
    public DriverVerificationResponse updateDriverVerificationStatus(String id, DriverVerificationStatusUpdateRequest request) {
        DriverVerification dv = driverVerificationRepository.findById(id)
                .orElseThrow(() -> new ApiError(404, "not found"));

        if (request.getVerificationStatus() != null) {
            VerificationStatus newStatus = request.getVerificationStatus();
            dv.setVerificationStatus(newStatus);

            User user = dv.getUser();

            if (newStatus == VerificationStatus.APPROVED) {
                user.setRole(Role.DRIVER);
                user.setIsVerified(true);
            } else if (newStatus == VerificationStatus.REJECTED) {
                user.setRole(Role.PASSENGER);
                user.setIsVerified(false);
                List<Route> availableRoutes = routeRepository.findByDriverIdAndStatus(user.getId(), RouteStatus.AVAILABLE);

                if (!availableRoutes.isEmpty()) {
                    for (Route route : availableRoutes) {
                        route.setStatus(RouteStatus.CANCELLED);
                        // route.setCancelledBy("ADMIN");
                        // route.setCancelledAt(LocalDateTime.now());
                    }
                    routeRepository.saveAll(availableRoutes);
                }
            }
            userRepository.save(user);
        }
        return driverVerificationMapper.toResponse(driverVerificationRepository.save(dv));
    }
}



