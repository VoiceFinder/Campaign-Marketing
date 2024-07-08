package com.example.CampaignMarketing.domain.campaign.controller;


import com.example.CampaignMarketing.domain.campaign.dto.CampaignRecommendResponseDto;
import com.example.CampaignMarketing.domain.campaign.dto.CampaignRequestDto;
import com.example.CampaignMarketing.domain.campaign.dto.CampaignResponseDto;
import com.example.CampaignMarketing.domain.campaign.service.CampaignService;
import com.example.CampaignMarketing.domain.user.entity.User;
import com.example.CampaignMarketing.global.s3.FileUploadService;
import com.example.CampaignMarketing.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    private final FileUploadService fileUploadService;



    @PostMapping(consumes = "multipart/form-data")
    public CampaignResponseDto createCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestPart("campaignData") CampaignRequestDto requestDto,
                                              @RequestPart(value = "images", required = false) List<MultipartFile> files) throws IOException {
        User user = userDetails.getUser();
        List<String> fileUrls = null;
        if (files != null && !files.isEmpty()) {
            fileUrls = fileUploadService.uploadFiles(files); // 파일 업로드 서비스 호출
            requestDto.setImageUrls(fileUrls);
        }
        return campaignService.createCampaign(user, requestDto);
    }

    @GetMapping()
    public Page<CampaignResponseDto> getCampaigns(  @RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                                    @RequestParam(name = "sortBy", defaultValue = "endDate") String sortBy,
                                                    @RequestParam(name = "isAsc", defaultValue = "false") boolean isAsc) {

        return campaignService.getCampaigns(page, size, sortBy, isAsc, keyword);
    }

//    @GetMapping("/recommend")
//    public Mono<Page<CampaignResponseDto>> getRecommendedCampaigns(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        return campaignService.getRecommendedCampaigns(user);
//    }
    @GetMapping("/recommend")  //for 게스트
    public Page<CampaignResponseDto> getRecentCampaigns(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return campaignService.getRecentCampaigns();
    }

    @GetMapping("/recent")  //for 게스트
    public Page<CampaignResponseDto> getRecommendCampaigns(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return campaignService.getRecentCampaigns();
    }

    @GetMapping("/{id}")
    public CampaignResponseDto getCampaignDto(@PathVariable Long id) {
        return campaignService.getCampaignDto(id);
    }

    @GetMapping("/totalPage")
    public long getTotalPages() {
        return campaignService.getTotalPages();
    }

    @DeleteMapping("/{id}")
    public void deleteCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        User user = userDetails.getUser();
        campaignService.deleteCampaign(user, id);
    }

}
