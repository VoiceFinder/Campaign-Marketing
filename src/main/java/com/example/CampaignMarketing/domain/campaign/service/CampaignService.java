package com.example.CampaignMarketing.domain.campaign.service;


import com.example.CampaignMarketing.domain.campaign.dto.CampaignRecommendResponseDto;
import com.example.CampaignMarketing.domain.campaign.dto.CampaignRequestDto;
import com.example.CampaignMarketing.domain.campaign.dto.CampaignResponseDto;
import com.example.CampaignMarketing.domain.campaign.entity.Campaign;
import com.example.CampaignMarketing.domain.campaign.entity.QCampaign;
import com.example.CampaignMarketing.domain.campaign.repository.CampaignRepository;
import com.example.CampaignMarketing.domain.market.entity.Market;
import com.example.CampaignMarketing.domain.market.entity.QMarket;
import com.example.CampaignMarketing.domain.market.repository.MarketRepository;
import com.example.CampaignMarketing.domain.user.entity.User;
import com.example.CampaignMarketing.global.exception.CustomException;
import com.example.CampaignMarketing.global.exception.ErrorCode;
import com.example.CampaignMarketing.global.s3.FileUploadService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CampaignService {

//    @Autowired
//    private WebClient webClient;

    private final CampaignRepository campaignRepository;
    private final MarketRepository marketRepository;
    private final FileUploadService fileUploadService;


    public CampaignResponseDto createCampaign(User user, CampaignRequestDto requestDto) {
        Market market = findMarket(requestDto.getMarketId());

        Campaign campaign = new Campaign(market, requestDto);
        // DB 저장
        Campaign saveCampaign = campaignRepository.save(campaign);

        return new CampaignResponseDto(saveCampaign);

    }

    public Page<CampaignResponseDto> getCampaigns(int page, int size, String sortBy, boolean isAsc, String keyword) {
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        QCampaign qCampaign = QCampaign.campaign;
        BooleanBuilder builder = new BooleanBuilder();
        System.out.println("keyword = " + keyword);
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(qCampaign.title.containsIgnoreCase(keyword)
                    .or(qCampaign.description.containsIgnoreCase(keyword))
                    .or(qCampaign.market.companyName.containsIgnoreCase(keyword)));
        }

        return campaignRepository.findAll(builder, pageable).map(CampaignResponseDto::new);
    }

    public CampaignResponseDto getCampaignDto(Long id) {
        Campaign campaign = findCampaign(id);
        CampaignResponseDto campaignResponseDto = new CampaignResponseDto(campaign);
        return campaignResponseDto;
    }

    private Campaign findCampaign(Long id) {
        return campaignRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_CAMPAIGN)
        );
    }

    private Market findMarket(Long id) {
        return marketRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MARKET)
        );
    }

//    public Mono<Page<CampaignResponseDto>> getRecommendedCampaigns(User user) {
//        Pageable pageable = PageRequest.of(0, 3);
//        Map<String, Object> requestBody = new HashMap<>();
//
//        requestBody.put("user_id", user.getId());
//        requestBody.put("cant_foods", user.getCant_foods());
//        requestBody.put("fav_foods", user.getFav_foods());
//
//        //requestBody.put("gender", user.getGender());
////        LocalDate currentData = LocalDate.now();
////        int age = currentData.getYear() - user.getBirthDate().getYear() + 1;
//        //requestBody.put("age", age);
//        //requestBody.put("considerations", user.getConsiderations());
//        //return campaignRepository.findAll(pageable).map(CampaignResponseDto::new);
//        return webClient.post()
//                .uri("/recommend")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(requestBody))
//                .retrieve()
//                .bodyToMono(CampaignRecommendResponseDto.class)
//                .flatMapMany(response -> Flux.fromIterable(response.getIds()))
//                .flatMap(this::findCampaignByMarketId)
//                .map(CampaignResponseDto::new)
//                .collectList()
//                .map(list -> new PageImpl<>(list, pageable, list.size()));
//    }


    private Mono<Campaign> findCampaignByMarketId(Long marketId) {
        return Mono.fromSupplier(() -> findMarket(marketId))
                .map(market -> market.getCampaigns().get(0))
                .onErrorResume(e -> Mono.empty());
    }

    public Page<CampaignResponseDto> getRecentCampaigns() {
        Pageable pageable = PageRequest.of(0, 3);
        return campaignRepository.findAll(pageable).map(CampaignResponseDto::new);
    }

    public long getTotalPages() {
        return campaignRepository.count();
    }

    public void deleteCampaign(User user, Long id) {
        Campaign campaign = findCampaign(id);

        if(campaign.getMarket().getUser().getId().equals(user.getId())) {
            if (campaign.getImageUrls() != null) {
                campaign.getImageUrls().forEach(fileUploadService::deleteFile);
            }

            campaignRepository.delete(campaign);
        } else {
            throw new CustomException(ErrorCode.REQUIRED_ADMIN_USER_AUTHORITY);
        }
    }
}
