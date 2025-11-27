package com.miniProject.Carpool.service;

import com.miniProject.Carpool.dto.VehicleRequest;
import com.miniProject.Carpool.dto.VehicleResponse;
import com.miniProject.Carpool.dto.VehicleStatusUpdateRequest;
import com.miniProject.Carpool.dto.VehicleUpdateRequest;
import com.miniProject.Carpool.dto.search.VehicleSearchRequest;
import com.miniProject.Carpool.mapper.VehicleMapper;
import com.miniProject.Carpool.model.DriverVerification;
import com.miniProject.Carpool.model.User;
import com.miniProject.Carpool.model.Vehicle;
import com.miniProject.Carpool.repository.DriverVerificationRepository;
import com.miniProject.Carpool.repository.UserRepository;
import com.miniProject.Carpool.repository.VehicleRepository;
import com.miniProject.Carpool.spec.VehicleSpecification;
import com.miniProject.Carpool.util.ApiError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse createVehicle(
            String userId,
            VehicleRequest request,
            MultipartFile photoFile
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiError(404, "User not found"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            // หาคันที่เป็น Default อยู่ ถ้ามี
            vehicleRepository.findByUserAndIsDefaultTrue(user).ifPresent(existingDefault -> {
                existingDefault.setIsDefault(false); // ปลด Default ออก
                vehicleRepository.save(existingDefault);
            });
        }

        List<String> photoFileUrl = new ArrayList<>();
        photoFileUrl.add("mock_url_photoFileUrl");

//        Vehicle vehicle = new Vehicle();
//        vehicle.setLicensePlate(request.getLicensePlate());
//        vehicle.setVehicleModel(request.getVehicleModel());
//        vehicle.setVehicleType(request.getVehicleType());
//        vehicle.setColor(request.getColor());
//        vehicle.setSeatCapacity(request.getSeatCapacity());
//        vehicle.setAmenities(request.getAmenities());
        Vehicle vehicle = Vehicle.builder()
                .user(user)
                .licensePlate(request.getLicensePlate())
                .vehicleModel(request.getVehicleModel())
                .vehicleType(request.getVehicleType())
                .color(request.getColor())
                .seatCapacity(request.getSeatCapacity())
                .amenities(request.getAmenities())
                .photos(photoFileUrl)
                .build();

//        vehicle.setUser(user);
//        vehicle.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false); // กัน Null

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    public void deleteMyVehicle(String userId, String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ApiError(403, "You are not authorized to delete this vehicle");
        }
        vehicleRepository.delete(vehicle);
    }

    public void deleteVehicleByAdmin(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));
        vehicleRepository.delete(vehicle);
    }

//    @Transactional
//    public VehicleResponse updateVehicle(
//            String vehicleId,
//            String userId,
//            VehicleRequest request,
//            MultipartFile photoFile
//    ) {
//        Vehicle vehicle = vehicleRepository.findById(vehicleId)
//                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));
//
//        if (!vehicle.getUser().getId().equals(userId)) {
//            throw new ApiError(403, "You are not authorized to update this vehicle");
//        }
//
//        if (Boolean.TRUE.equals(request.getIsDefault())) {
//            // หารถที่เป็น Default อยู่ ถ้ามี
//            vehicleRepository.findByUserAndIsDefaultTrue(vehicle.getUser()).ifPresent(existingDefault -> {
//                if (!existingDefault.getId().equals(vehicle.getId())) {
//                    existingDefault.setIsDefault(false);
//                    vehicleRepository.save(existingDefault);
//                }
//            });
//        }
//
//        if (StringUtils.hasText(request.getLicensePlate())) vehicle.setLicensePlate(request.getLicensePlate());
//        if (StringUtils.hasText(request.getVehicleModel())) vehicle.setVehicleModel(request.getVehicleModel());
//        if (StringUtils.hasText(request.getVehicleType())) vehicle.setVehicleType(request.getVehicleType());
//        if (StringUtils.hasText(request.getColor())) vehicle.setColor(request.getColor());
//        if (request.getSeatCapacity() != null) vehicle.setSeatCapacity(request.getSeatCapacity());
//        if (request.getAmenities() != null) vehicle.setAmenities(request.getAmenities());
////        if (request.getPhotos() != null) vehicle.setPhotos(request.getPhotos());
//        if (request.getIsDefault() != null) vehicle.setIsDefault(request.getIsDefault());
//
//        if (photoFile != null && !photoFile.isEmpty()) {
//            String mockUrl = "mock_updated_url_" + photoFile.getOriginalFilename();
//            vehicle.setPhotos(List.of(mockUrl));
//        } else if (request.getPhotos() != null) {
//            vehicle.setPhotos(request.getPhotos());
//        }
//
//        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
//    }

//    @Transactional
//    public VehicleResponse updateVehicleByAdmin(
//            String vehicleId,
////            String userId,
//            VehicleRequest request,
//            MultipartFile photoFile
//    ) {
//        Vehicle vehicle = vehicleRepository.findById(vehicleId)
//                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));
//
////        if (!vehicle.getUser().getId().equals(userId)) {
////            throw new ApiError(403, "You are not authorized to delete this vehicle");
////        }
//
//        if (Boolean.TRUE.equals(request.getIsDefault())) {
//            // หารถที่เป็น Default อยู่ ถ้ามี
//            vehicleRepository.findByUserAndIsDefaultTrue(vehicle.getUser()).ifPresent(existingDefault -> {
//                if (!existingDefault.getId().equals(vehicle.getId())) {
//                    existingDefault.setIsDefault(false);
//                    vehicleRepository.save(existingDefault);
//                }
//            });
//        }
//
//        if (StringUtils.hasText(request.getLicensePlate())) vehicle.setLicensePlate(request.getLicensePlate());
//        if (StringUtils.hasText(request.getVehicleModel())) vehicle.setVehicleModel(request.getVehicleModel());
//        if (StringUtils.hasText(request.getVehicleType())) vehicle.setVehicleType(request.getVehicleType());
//        if (StringUtils.hasText(request.getColor())) vehicle.setColor(request.getColor());
//        if (request.getSeatCapacity() != null) vehicle.setSeatCapacity(request.getSeatCapacity());
//        if (request.getAmenities() != null) vehicle.setAmenities(request.getAmenities());

    /// /        if (request.getPhotos() != null) vehicle.setPhotos(request.getPhotos());
//        if (request.getIsDefault() != null) vehicle.setIsDefault(request.getIsDefault());
//
//        if (photoFile != null && !photoFile.isEmpty()) {
//            String mockUrl = "mock_updated_url_" + photoFile.getOriginalFilename();
//            vehicle.setPhotos(List.of(mockUrl));
//        } else if (request.getPhotos() != null) {
//            vehicle.setPhotos(request.getPhotos());
//        }
//
//        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
//    }
//
    @Transactional
    public VehicleResponse updateVehicleStatus(
            String vehicleId,
            String userId,
            VehicleStatusUpdateRequest request
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ApiError(403, "You are not authorized to update this vehicle status");
        }
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            // หารถที่เป็น Default อยู่ ถ้ามี
            vehicleRepository.findByUserAndIsDefaultTrue(vehicle.getUser()).ifPresent(existingDefault -> {
                if (!existingDefault.getId().equals(vehicle.getId())) {
                    existingDefault.setIsDefault(false);
                    vehicleRepository.save(existingDefault);
                }
            });
        }
        if (request.getIsDefault() != null) vehicle.setIsDefault(request.getIsDefault());
        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleResponse updateVehicle(
            String vehicleId,
            String userId,
            VehicleUpdateRequest request,
            MultipartFile photoFile
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ApiError(403, "You are not authorized to update this vehicle"); // แก้ข้อความแล้ว
        }

        return performVehicleUpdate(vehicle, request, photoFile);
    }

    @Transactional
    public VehicleResponse updateVehicleByAdmin(
            String vehicleId,
            VehicleUpdateRequest request,
            MultipartFile photoFile
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));

        return performVehicleUpdate(vehicle, request, photoFile);
    }

    private VehicleResponse performVehicleUpdate(Vehicle vehicle, VehicleUpdateRequest request, MultipartFile photoFile) {
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            vehicleRepository.findByUserAndIsDefaultTrue(vehicle.getUser()).ifPresent(existingDefault -> {
                if (!existingDefault.getId().equals(vehicle.getId())) {
                    existingDefault.setIsDefault(false);
                    vehicleRepository.save(existingDefault);
                }
            });
        }

        if (StringUtils.hasText(request.getLicensePlate())) vehicle.setLicensePlate(request.getLicensePlate());
        if (StringUtils.hasText(request.getVehicleModel())) vehicle.setVehicleModel(request.getVehicleModel());
        if (StringUtils.hasText(request.getVehicleType())) vehicle.setVehicleType(request.getVehicleType());
        if (StringUtils.hasText(request.getColor())) vehicle.setColor(request.getColor());
        if (request.getSeatCapacity() != null) vehicle.setSeatCapacity(request.getSeatCapacity());
        if (request.getAmenities() != null) vehicle.setAmenities(request.getAmenities());
        if (request.getIsDefault() != null) vehicle.setIsDefault(request.getIsDefault());

        if (photoFile != null && !photoFile.isEmpty()) {
            String mockUrl = "mock_updated_url_" + photoFile.getOriginalFilename();
            vehicle.setPhotos(List.of(mockUrl));
        } else if (request.getPhotos() != null) {
            vehicle.setPhotos(request.getPhotos());
        }

        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    public VehicleResponse getVehicleById(String vehicleId, String userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));
        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ApiError(403, "You are not authorized to view this vehicle");
        }
        return vehicleMapper.toResponse(vehicle);
    }

    public VehicleResponse getVehicleByAdmin(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiError(404, "Vehicle not found"));

        return vehicleMapper.toResponse(vehicle);
    }

    public Page<VehicleResponse> searchVehicle(VehicleSearchRequest request,String userId) {
        int pageNo = Math.max(0,request.getPage() -1);
        Pageable pageable = PageRequest.of(pageNo,request.getLimit(), Sort.by("createdAt").descending());

        Specification<Vehicle> spec = Specification.where(VehicleSpecification.hasKeyword(request.getQ()))
                .and(VehicleSpecification.hasLicensePlate(request.getLicensePlate()))
                .and(VehicleSpecification.hasVehicleType(request.getVehicleType()))
                .and(VehicleSpecification.hasSeatCapacity(request.getSeatCapacity()))
                .and(VehicleSpecification.hasUserId(userId));
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(spec, pageable);
        return vehiclesPage.map(vehicleMapper::toResponse);
    }
}
