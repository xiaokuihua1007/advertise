package org.classmatechen.core.application.local.promotion;

import org.classmatechen.common.Platform;
import org.classmatechen.core.application.local.promotion.command.GeneratePromotionConfigCommand;
import org.classmatechen.core.application.local.promotion.command.PublishPlatformPromotionCommand;
import org.classmatechen.core.application.local.promotion.dto.PromotionConfigDTO;
import org.classmatechen.core.domain.local.promotion.entity.LocalPromotion;
import org.classmatechen.core.domain.local.promotion.error.LocalPromotionNoExistException;
import org.classmatechen.core.domain.local.promotion.repository.LocalPromotionRepository;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.domain.platform.account.error.AccountNoExistException;
import org.classmatechen.core.domain.platform.account.repository.AccountRepository;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.shared.service.LocalPromotionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalPromotionAppService {

    @Autowired
    private LocalPromotionRepository localPromotionRepository;

    @Autowired
    private AccountRepository accountRespository;

    @Autowired
    private LocalPromotionDomainService localPromotionDomainService;
    
    public void publishPlatformPromotion(PublishPlatformPromotionCommand command) {

        Account account = noNullAccount(command.getAccountId(), Platform.platform(command.getPlatformId()));
        LocalPromotion localPromotion = noNullLocalPromotion(command.getLocalPromotionId());

        localPromotionDomainService.publishPlatformPromotion(account, localPromotion, command.getConfig());
    }

    public PromotionConfigDTO generatePlatformConfig(GeneratePromotionConfigCommand command) {

        LocalPromotion localPromotion = noNullLocalPromotion(command.getLocalPromotionId());
        Platform platform = Platform.platform(command.getPlatform());

        PromotionConfig config = localPromotion.generatePlatformConfig(platform);

        return new PromotionConfigDTO(command.getLocalPromotionId(), config);
    }

    private LocalPromotion noNullLocalPromotion(Long localPromotionId) {

        LocalPromotion localPromotion = this.localPromotionRepository.findById(new LocalPromotionId(localPromotionId));
        if (null == localPromotion) {
            throw new LocalPromotionNoExistException(new LocalPromotionId(localPromotionId));
        }
        return localPromotion;
    }

    private Account noNullAccount(Long accountId, Platform platform) {

        Account account = this.accountRespository.findById(new AccountId(accountId, platform));
        if (null == account) {
            throw new AccountNoExistException(new AccountId(accountId, platform));
        }
        return account;
    }
}
