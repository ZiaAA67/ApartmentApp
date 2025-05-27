package com.mntn.repositories;

import com.mntn.pojo.Complaint;
import java.util.List;
import java.util.Map;

public interface ComplaintRepository {

    Complaint getComplaintById(String complaintId);
            
    List<Complaint> getComplaints(Map<String, String> params);

    Complaint updateComplaint(Complaint c);

    Complaint createComplaint(Complaint c);
}
