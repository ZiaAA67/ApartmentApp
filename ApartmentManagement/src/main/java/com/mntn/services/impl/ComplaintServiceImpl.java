package com.mntn.services.impl;

import com.mntn.pojo.Complaint;
import com.mntn.pojo.User;
import com.mntn.repositories.ComplaintRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.ComplaintService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("complaintService")
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepo;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Complaint> getComplaints(Map<String, String> params) {
        return this.complaintRepo.getComplaints(params);
    }

    @Override
    public Complaint createComplaint(Map<String, String> params) {
        String title = params.get("title");
        String content = params.get("content");
        String userId = params.get("userId");

        if (title == null) {
            throw new IllegalArgumentException("Thiếu title");
        }
        if (content == null) {
            throw new IllegalArgumentException("Thiếu content");
        }
        if (userId == null) {
            throw new IllegalArgumentException("Thiếu userId");
        }

        User user = this.userRepository.getUserById(userId);
        String id = UUID.randomUUID().toString();

        Complaint c = new Complaint();
        c.setId(id);
        c.setTitle(title);
        c.setContent(content);
        c.setUserId(user);
        c.setDateSubmitted(new Date());
        c.setStatus("pending");

        return this.complaintRepo.createComplaint(c);
    }

    @Override
    public Complaint updateComplaint(String complaintId, String status) {
        if (complaintId == null || status == null) {
            throw new IllegalArgumentException("Complaint ID hoặc Status không được null!");
        }

        Complaint c = complaintRepo.getComplaintById(complaintId);
        if (c == null) {
            throw new IllegalArgumentException("Không tìm thấy Complaint với ID: " + complaintId);
        }

        c.setStatus(status);
        return complaintRepo.updateComplaint(c);
    }
}
