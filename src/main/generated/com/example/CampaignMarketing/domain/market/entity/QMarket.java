package com.example.CampaignMarketing.domain.market.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarket is a Querydsl query type for Market
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarket extends EntityPathBase<Market> {

    private static final long serialVersionUID = 654299190L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarket market = new QMarket("market");

    public final com.example.CampaignMarketing.global.entity.QTimestamped _super = new com.example.CampaignMarketing.global.entity.QTimestamped(this);

    public final StringPath address = createString("address");

    public final StringPath businessType = createString("businessType");

    public final ListPath<com.example.CampaignMarketing.domain.campaign.entity.Campaign, com.example.CampaignMarketing.domain.campaign.entity.QCampaign> campaigns = this.<com.example.CampaignMarketing.domain.campaign.entity.Campaign, com.example.CampaignMarketing.domain.campaign.entity.QCampaign>createList("campaigns", com.example.CampaignMarketing.domain.campaign.entity.Campaign.class, com.example.CampaignMarketing.domain.campaign.entity.QCampaign.class, PathInits.DIRECT2);

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrls = this.<String, StringPath>createList("imageUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> menus = this.<String, StringPath>createList("menus", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath phone = createString("phone");

    public final com.example.CampaignMarketing.domain.user.entity.QUser user;

    public QMarket(String variable) {
        this(Market.class, forVariable(variable), INITS);
    }

    public QMarket(Path<? extends Market> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarket(PathMetadata metadata, PathInits inits) {
        this(Market.class, metadata, inits);
    }

    public QMarket(Class<? extends Market> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.CampaignMarketing.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

