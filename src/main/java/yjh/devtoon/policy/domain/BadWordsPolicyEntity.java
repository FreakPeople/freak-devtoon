package yjh.devtoon.policy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjh.devtoon.common.entity.BaseEntity;
import yjh.devtoon.policy.common.Policy;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "bad_words_policy")
@Entity
public class BadWordsPolicyEntity extends BaseEntity implements Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bad_words_policy_no", nullable = false)
    private Long id;

    @Column(name = "warning_threshold", nullable = false)
    private int warningThreshold;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * 요청 DTO에서 받은 세부 정보를 기반으로 객체의 상태를 초기화합니다.
     */
    public BadWordsPolicyEntity(Map<String, Object> details) {
        this.warningThreshold = Integer.parseInt(details.get("warningThreshold").toString());
        this.startDate = LocalDateTime.parse(details.get("startDate").toString());
        this.endDate = details.containsKey("endDate") ? LocalDateTime.parse(details.get("endDate").toString()) : null;
    }

    /**
     * 정책 적용 메서드는 시스템에서 정책을 적용할 때 호출됩니다.
     */
    @Override
    public void applyPolicy() {
        System.out.println("비속어 정책이 적용되었습니다. : ID = " + id + ", 경고 임계값 = " + warningThreshold);
    }

    @Override
    public String toString() {
        return String.format("BadWordsPolicy {id=%d, warningThreshold=%s, start=%s, end=%s}",
                id, warningThreshold, startDate, endDate);
    }

}
