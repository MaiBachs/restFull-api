package com.spm.viettel.msm.service;

import com.spm.viettel.msm.utils.StringHelper;
import com.spm.viettel.msm.dto.BranchDto;
import com.spm.viettel.msm.dto.request.StaffBaseRequest;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

@Service
public class BranchService {
    @Autowired
    private StaffRepository staffRepository;

    public Page<BranchDto> getBranches(StaffBaseRequest request, UserToken vsaUserToken, PageRequest pageRequest) {
        String branchCode = request.getShopCode();
        return staffRepository.getBranches(StringHelper.likeBuild(branchCode), StringHelper.likeBuild(request.getName()),pageRequest);
    }

}
