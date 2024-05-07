package yjh.devtoon.policy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveActivePoliciesResponse {

    private List<PolicyDetailsInfo> policyDetailsInfo;

    @Override
    public String toString() {
        String details = policyDetailsInfo.stream()
                .map(PolicyDetailsInfo::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return "PolicyCreateResponse {policyDetailsInfo= " + details + "}";
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyDetailsInfo {

        private Long id;
        private String type;
        private String details;

        @Override
        public String toString() {
            return "PolicyDetailsInfo{" +
                    "id=" + id +
                    ", type='" + type + '\'' +
                    ", details='" + details + '\'' +
                    '}';
        }

    }

}
