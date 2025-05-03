# 정산 요청 관리 시스템 - 설계 문서

---

## 시스템 개요

- **목표**: 판매자가 판매 내역을 기준으로 정산을 요청하고, 관리자가 승인/반려를 처리하는 시스템
- **특징**: 정산 요청 시점에 수수료율이 적용되고, 판매 상태가 변경됨
- **UI**: 관리자는 웹 UI를 통해 모든 요청을 확인하고 상태를 변경 가능

---

## 도메인 설계

### 주요 엔티티

| 엔티티 | 설명 |
|--------|------|
| `Seller` | 판매자 정보 (id, name, grade) |
| `GradePolicy` | 등급별 수수료율 정의 테이블 |
| `Sale` | 개별 판매 기록 (seller_id, amount, sale_date, 상태) |
| `SettlementRequest` | 정산 요청 (누적금액, 수수료, 지급금액, 요청 시점 수수료율 포함) |

### 상태 흐름 설계

```plaintext
Sale 상태: UNSETTLED → SETTLED (승인 시)
                    ↘ UNSETTLED (반려 시 재전환)

SettlementRequest 상태:
PENDING → APPROVED or REJECTED
```

---

## 수수료 정책 설계

- 수수료율은 `GradePolicy`에서 관리
- 판매자(`Seller`)는 `Grade` 정보를 갖고 있고, 정산 요청 시 이를 조회하여 **당시 수수료율을 SettlementRequest에 기록**
- **정산 요청 후 수수료율이 바뀌어도 기존 요청은 영향을 받지 않음**

## 관리자 UI

- 정산 요청 목록 출력
- 각 요청 옆에 [승인], [반려] 버튼 노출
- 승인된 건은 반려 불가 처리됨

---

## 폴더 구조

```
domain/
├── seller/
├── sale/
├── gradePolicy/
├── settlement/
│   ├── controller/
│   ├── dto/
│   ├── service/
│   └── entity/
global/
└── exception/
resources/
└── templates/
```

---