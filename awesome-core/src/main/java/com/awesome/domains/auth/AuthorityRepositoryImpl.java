package com.awesome.domains.auth;

import com.wishwingz.platform.core.auth.simple.model.AuthenticationRepository;
import com.wishwingz.platform.core.auth.simple.model.ServiceUser;
import com.wishwingz.platform.service.auth.AuthDefaultClient;
import com.wishwingz.platform.service.auth.readmodels.models.QRoleMemberMapping;
import com.wishwingz.platform.service.auth.readmodels.querys.RetrieveRoleMemberMappingQuery;
import com.wishwingz.platform.service.core.WishwingzHasCollectionSpec;
import com.wishwingz.platform.service.member.MemberDefaultClient;
import com.wishwingz.platform.service.member.readmodels.models.QMember;
import com.wishwingz.platform.service.member.readmodels.querys.GetMemberQuery;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository(value = "authorityRepositoryImpl")
public class AuthorityRepositoryImpl implements AuthenticationRepository {
    private final AuthDefaultClient authDefaultClient;
    private final MemberDefaultClient memberDefaultClient;

    public AuthorityRepositoryImpl(@Lazy AuthDefaultClient authDefaultClient, @Lazy MemberDefaultClient memberDefaultClient) {
        this.authDefaultClient = authDefaultClient;
        this.memberDefaultClient = memberDefaultClient;
    }

    @Override
    public boolean hasRole(ServiceUser user, String[] roles) {
        // 권한 설정이 없음. free
        if (ArrayUtils.isEmpty(roles)) {
            return true;
        }

        // 권한이 설정이 있음
        GetMemberQuery memberQuery = GetMemberQuery.create(user.getUuid());
        memberDefaultClient.doInvokeQuery(memberQuery);
        QMember member = memberQuery.result();

        RetrieveRoleMemberMappingQuery authQuery = (RetrieveRoleMemberMappingQuery) authDefaultClient.doInvokeQuery(RetrieveRoleMemberMappingQuery.create().setMemberId(member.getId()));
        List<QRoleMemberMapping> qRoleMemberMappings = authQuery.result();
        if (!WishwingzHasCollectionSpec.get().isSatisfiedBy(qRoleMemberMappings)) {
            return false;
        }

        // 설정된 권한에 대한 하는 권한이 있음
        List<QRoleMemberMapping> filteredQRoleMemberMappings = qRoleMemberMappings.stream().filter(x -> ArrayUtils.contains(roles, x.getRoleName())).collect(Collectors.toList());
        if (WishwingzHasCollectionSpec.get().isSatisfiedBy(filteredQRoleMemberMappings)) {
            return true;
        }

        return false;
    }
}
