package com.mntn.services;

import com.mntn.pojo.Complaint;
import java.util.List;
import java.util.Map;

public interface ComplaintService {

    List<Complaint> getComplaints(Map<String, String> params);

    Complaint createComplaint(Map<String, String> params);

    Complaint updateComplaint(String complaintId, String status);
}
