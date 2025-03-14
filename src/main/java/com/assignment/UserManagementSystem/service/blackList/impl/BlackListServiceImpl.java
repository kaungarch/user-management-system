package com.assignment.UserManagementSystem.service.blackList.impl;

import com.assignment.UserManagementSystem.dto.BlackListDto;
import com.assignment.UserManagementSystem.exception.AlreadyExistedException;
import com.assignment.UserManagementSystem.exception.BadRequestException;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.BlackList;
import com.assignment.UserManagementSystem.model.Request;
import com.assignment.UserManagementSystem.model.enums.RequestStatus;
import com.assignment.UserManagementSystem.repository.BlackListRepository;
import com.assignment.UserManagementSystem.repository.RequestRepository;
import com.assignment.UserManagementSystem.service.blackList.BlackListService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;
    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;

    @Override
    public BlackListDto blackListARequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found with id " + requestId)
        );

        if (request.getStatus().equals(RequestStatus.APPROVED))
            throw new BadRequestException("This request is already approved.");

        Optional<BlackList> isExisted = blackListRepository.findByNrcNumber(request.getNrcNumber());

        if (isExisted.isEmpty()) {
            BlackList blackList = BlackList.builder()
                    .id(null)
                    .nrcNumber(request.getNrcNumber())
                    .phoneNumber(request.getPhoneNumber())
                    .build();
            requestRepository.delete(request);
            BlackList saved = blackListRepository.save(blackList);
            return modelMapper.map(saved, BlackListDto.class);
        } else {
            throw new AlreadyExistedException("Request already black listed.");
        }
    }

    @Override
    public void deleteBlackList(Long id) {
        BlackList blackList = blackListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blacklist not found."));

        blackListRepository.delete(blackList);
    }

}
