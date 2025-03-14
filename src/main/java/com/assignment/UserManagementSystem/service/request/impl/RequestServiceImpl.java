package com.assignment.UserManagementSystem.service.request.impl;

import com.assignment.UserManagementSystem.dto.RequestDto;
import com.assignment.UserManagementSystem.exception.AlreadyExistedException;
import com.assignment.UserManagementSystem.exception.BadRequestException;
import com.assignment.UserManagementSystem.exception.RequestCreationException;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.BlackList;
import com.assignment.UserManagementSystem.model.Request;
import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.model.enums.RequestStatus;
import com.assignment.UserManagementSystem.model.enums.UserRole;
import com.assignment.UserManagementSystem.repository.BlackListRepository;
import com.assignment.UserManagementSystem.repository.RequestRepository;
import com.assignment.UserManagementSystem.repository.UserRepository;
import com.assignment.UserManagementSystem.request.AccountRegistrationRequest;
import com.assignment.UserManagementSystem.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final BlackListRepository blackListRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public RequestDto createRequest(AccountRegistrationRequest request) {

        Optional<Request> isBlocked = requestRepository.findByNrcNumberAndStatus(request.nrcNumber(),
                RequestStatus.BLOCKED);

        Optional<Request> isRejected = requestRepository.findByNrcNumberAndStatus(request.nrcNumber(),
                RequestStatus.REJECTED);

        Optional<BlackList> isBlackListed = blackListRepository.findByNrcNumber(request.nrcNumber());

        if (isBlocked.isPresent() || isRejected.isPresent())
            throw new RequestCreationException("This user can't register now.");

        if (isBlackListed.isPresent())
            throw new RequestCreationException("This user can never register.");

        Optional<User> isNrcNumberExisted = userRepository.findByNrcNumber(request.nrcNumber());
        Optional<User> isPhoneNumberExisted = userRepository.findByPhoneNumber(request.phoneNumber());

        if (isNrcNumberExisted.isPresent() || isPhoneNumberExisted.isPresent())
            throw new AlreadyExistedException("Nrc number or phone number already existed.");

        Request newRequest = Request.builder()
                .id(null)
                .name(request.name())
                .nrcNumber(request.nrcNumber())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .status(RequestStatus.PENDING)
                .build();

        Request saved = requestRepository.save(newRequest);
        return modelMapper.map(saved, RequestDto.class);
    }

    @Override
    public String checkRequestStatus(String phoneNumber) {
        Optional<BlackList> foundBlackList = blackListRepository.findByPhoneNumber(phoneNumber);

        if (foundBlackList.isPresent()) {
            return "BLACKLISTED";
        }

        Optional<Request> foundRequest = requestRepository.findByPhoneNumber(phoneNumber);

        if (foundRequest.isPresent()) {
            return foundRequest.get().getStatus().toString();
        }

        throw new ResourceNotFoundException("No request found with phone number: " + phoneNumber);

    }

    @Override
    public RequestDto approveRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request not found with id " + id));

        if (request.getStatus().equals(RequestStatus.APPROVED))
            throw new BadRequestException("This request is already approved.");

        request.setStatus(RequestStatus.APPROVED);
        Request updatedRequest = requestRepository.save(request);

        User user = User.builder()
                .id(null)
                .name(updatedRequest.getName())
                .nrcNumber(updatedRequest.getNrcNumber())
                .phoneNumber(updatedRequest.getPhoneNumber())
                .password(updatedRequest.getPassword())
                .role(UserRole.USER)
                .projects(new ArrayList<>())
                .build();

        userRepository.save(user);

        return modelMapper.map(updatedRequest, RequestDto.class);
    }

    @Override
    public RequestDto rejectRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request not found with id " + id));

        if (request.getStatus().equals(RequestStatus.APPROVED))
            throw new BadRequestException("This request is already approved.");

        if (request.getStatus().equals(RequestStatus.REJECTED))
            return modelMapper.map(request, RequestDto.class);

        request.setStatus(RequestStatus.REJECTED);
        Request rejectedRequest = requestRepository.save(request);
        return modelMapper.map(rejectedRequest, RequestDto.class);
    }

    @Override
    public RequestDto blockRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request not found with id " + id)
        );

        if (request.getStatus().equals(RequestStatus.APPROVED))
            throw new BadRequestException("This request is already approved.");

        if (request.getStatus().equals(RequestStatus.BLOCKED))
            return modelMapper.map(request, RequestDto.class);

        request.setStatus(RequestStatus.BLOCKED);

        Request blockedRequest = requestRepository.save(request);
        return modelMapper.map(blockedRequest, RequestDto.class);
    }

    @Override
    public RequestDto unblockRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Request not found with id " + id)
        );

        if (request.getStatus().equals(RequestStatus.BLOCKED)) {
            request.setStatus(RequestStatus.PENDING);
            Request unblockRequest = requestRepository.save(request);
            return modelMapper.map(unblockRequest, RequestDto.class);
        } else {
            throw new IllegalStateException("Request is not blocked.");
        }
    }

    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(request -> modelMapper.map(request, RequestDto.class))
                .toList();
    }

    @Override
    public RequestDto getARequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Request not found wit " +
                    "id " + id);
        });
        return modelMapper.map(request, RequestDto.class);
    }

    private Request buildRequest(AccountRegistrationRequest request) {
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Request newRequest = Request.builder()
                .id(null)
                .name(request.name())
                .nrcNumber(request.nrcNumber())
                .phoneNumber(request.phoneNumber())
                .status(RequestStatus.PENDING)
                .requestedAt(localDateTime)
                .build();
        return newRequest;
    }

}
