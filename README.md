# 1. 아키텍처
```mermaid

---
config:
  layout: fixed
---
flowchart LR
 subgraph Frontend_Space["Frontend (GitHub Pages)"]
    direction TB
        Vite@{ label: "<img src=\"https://cdn.simpleicons.org/vite\" width=\"30\"><br>Vite" }
        React@{ label: "<img src=\"https://cdn.simpleicons.org/react\" width=\"30\"><br>React" }
  end
 subgraph CI_CD["CI/CD & Monitoring"]
        GitHub@{ label: "<img src=\"https://cdn.simpleicons.org/github\" width=\"30\"><br>GitHub" }
        SonarQube["SonarQube"]
  end
 subgraph App_Logic["Application"]
        Spring@{ label: "<img src=\"https://cdn.simpleicons.org/springboot\" width=\"40\"><br>Spring Boot" }
        Security@{ label: "<img src=\"https://cdn.simpleicons.org/springsecurity\" width=\"30\"><br>Security" }
  end
 subgraph Data_Storage["Data Storage"]
        MySQL@{ label: "<img src=\"https://cdn.simpleicons.org/mysql\" width=\"40\"><br>MySQL" }
        Redis@{ label: "<img src=\"https://cdn.simpleicons.org/redis\" width=\"40\"><br>Redis" }
  end
 subgraph Backend_Server["Backend (Amazon EC2)"]
    direction TB
        App_Logic
        Data_Storage
  end
 subgraph External["External Services"]
        Upbit["Upbit API"]
  end
    User(("User")) <--> Browser["Browser / Client"]
    Vite --- React
    Browser <-- Axios / REST API --> Spring
    GitHub -- Deploy --> Frontend_Space
    GitHub -- Workflow --> Spring
    Spring --> SonarQube
    Spring <--> MySQL & Redis & Upbit

    Vite@{ shape: rect}
    React@{ shape: rect}
    GitHub@{ shape: rect}
    Spring@{ shape: rect}
    Security@{ shape: rect}
    MySQL@{ shape: cylinder}
    Redis@{ shape: rect}
    style SonarQube fill:#fff,stroke:#4E9BCD,stroke-width:2px
    style Spring fill:#fff,stroke:#6DB33F,stroke-width:2px
    style MySQL fill:#fff,stroke:#4479A1,stroke-width:2px
    style Redis fill:#fff,stroke:#DC382D,stroke-width:2px
    style Upbit fill:#fff,stroke:#0066ff,stroke-width:2px
    style Frontend_Space fill:#fffbe6,stroke:#ffe58f,stroke-width:2px
    style Backend_Server fill:#e1f5fe,stroke:#01579b,stroke-width:2px
```

# 2. 시퀀스 다이어그램

## 1) 코인 매수
```mermaid
sequenceDiagram
    autonumber
    title: [01] 코인 매수 
    
    participant U as 사용자 (Frontend)
    participant C as OrderController
    participant S as OrderService
    participant W as WalletService
    participant O as OrderRepository

    U->>C: POST /api/v1/orders (매수 요청)
    C->>S: placeOrder(orderRequestDTO)
    
    S->>W: 잔액 조회 요청
    W-->>S: 잔액 충분함 확인
    
    rect rgb(240, 240, 240)
        Note over S, O: [Transaction Start]
        S->>O: 주문 생성 (Status: PENDING)
        
        Note over S: 체결 로직 수행 (시세 검증 등)
        
        S->>O: 주문 상태 업데이트 (Status: FILLED)
        
        S->>W: 잔액 차감 요청 (실제 지출)
        W-->>S: 차감 완료
        Note over S, O: [Transaction Commit]
    end
    
    S-->>C: 주문 처리 결과(OrderResponseDTO) 반환
    C-->>U: 201 Created (성공 메시지)
```

## 2) 시세 조회
```mermaid
sequenceDiagram
    autonumber
    title: [02] 시세 조회
    
    participant FE as 프론트엔드
    participant C as PriceController
    participant S as PriceService
    participant R as PriceRepository
    participant U as UpbitClient

    Note over FE: 사용자가 첫 접속 (DB가 비어있을 수 있음)
    FE->>C: GET /api/v1/prices/{symbol}
    C->>S: getLatestPrice(symbol)
    S->>R: DB에서 현재가 조회
    R-->>S: 결과 없음 (null 또는 Empty)

    alt DB에 데이터가 없는 경우 (초기 1회 발생)
        Note over S: "데이터가 없네? 즉시 가져오자!"
        S->>U: 외부 API 최신 시세 요청
        U-->>S: 최신가 응답
        S->>R: DB 저장 (스케줄러가 돌기 전까지의 공백 메우기)
        R-->>S: 완료
    end

    S-->>C: 가격 데이터 반환
    C-->>FE: 200 OK
```
## 3) 코인 목록 조회
```mermaid
sequenceDiagram
    autonumber
    title: [3] 코인 목록 제공 API 흐름 (DB + Upbit 연동)
    
    participant FE as 프론트엔드 (클라이언트)
    participant C as CoinController
    participant S as CoinService
    participant R as CoinRepository
    participant U as UpbitClient (Infra)

    Note over FE: 앱 접속 시 API 자동 호출
    FE->>C: GET /api/v1/coins
    
    C->>S: 활성 코인 목록 요청
    S->>R: findAllByStatus("ACTIVE") 조회
    R-->>S: 결과 반환 (List)

    alt DB에 지원 코인 목록이 존재하는 경우
        Note over S: 기존 DB 데이터 사용 (Overhead 최소화)
    else DB가 비어있거나 업데이트가 필요한 경우
        S->>U: 외부 API로부터 지원 마켓 목록 요청 (getMarkets)
        U-->>S: 최신 코인 목록 응답
        S->>R: 신규 코인 데이터 대량 저장 (Bulk Save)
        R-->>S: 저장 완료
    end
    
    Note right of S: Entity를 CoinResponseDTO로 변환
    
    S-->>C: DTO 리스트 반환
    C-->>FE: 200 OK (JSON 리스트 응답)
```
## 4) 체결 내역 조회
```mermaid
sequenceDiagram
    autonumber
    title: [04] 체결 내역 조회
    
    participant U as 사용자 (Frontend)
    participant C as OrderController
    participant S as OrderService
    participant R as OrderRepository

    U->>C: GET /api/v1/orders/trades (체결 내역 요청)
    C->>S: getTradeHistory(userId)
    
    S->>R: findByUserIdAndStatus(userId, "FILLED")
    R-->>S: 체결된 주문 리스트 반환
    
    Note right of S: 매수가, 수량, 체결 시간 계산/변환
    
    S-->>C: TradeResponseDTO 반환
    C-->>U: 200 OK (종목명, 체결가, 수량 정보)
```
## 5) 미체결 주문 취소
```mermaid
sequenceDiagram
    autonumber
    title: [05] 미체결 주문 취소 
    
    participant U as 사용자 (Frontend)
    participant C as OrderController
    participant S as OrderService
    participant W as WalletService
    participant O as OrderRepository

    U->>C: DELETE /api/v1/orders/{orderId} (취소 요청)
    C->>S: cancelOrder(orderId)
    
    S->>O: 주문 정보 조회
    O-->>S: 주문 엔티티 반환 (Status: PENDING)

    alt 주문이 이미 체결되었거나 취소된 상태인 경우
        S-->>C: 취소 불가 예외 발생 (400 Bad Request)
        C-->>U: "이미 처리된 주문은 취소할 수 없습니다."
    else 주문이 대기(PENDING) 상태인 경우
        rect rgb(255, 245, 230)
            Note over S, O: [Transaction Start]
            S->>O: 주문 상태 업데이트 (Status: CANCELLED)
            
            S->>W: 홀딩 금액(또는 차감액) 환불 요청
            W-->>S: 잔액 복구 완료
            Note over S, O: [Transaction Commit]
        end
        S-->>C: 취소 결과 반환
        C-->>U: 200 OK (취소 및 환불 완료 응답)
    end
```
## 6) 지갑 변동 내역 조회
```mermaid
sequenceDiagram
    autonumber
    title: [06] 지갑 변동 내역 조회
    
    participant U as 사용자 (Frontend)
    participant C as WalletController
    participant S as WalletService
    participant R as WalletRepository

    U->>C: GET /api/v1/wallets/history (내역 요청)
    C->>S: getWalletHistory(userId, pageable)
    
    S->>R: 사용자의 변동 이력 조회 (최신순)
    R-->>S: Wallet 엔티티 리스트 반환
    
    Note right of S: Entity -> WalletDTO 변환
    
    S-->>C: DTO 리스트 반환
    C-->>U: 200 OK (날짜, 금액, 유형, 잔액 정보)
```
## 7) 자산 현황 조회
```mermaid
sequenceDiagram
    autonumber
    title: [07] 자산 현황(Holding) 조회 
    
    participant U as 사용자 (Frontend)
    participant C as HoldingController
    participant S as HoldingService
    participant PR as PriceRepository
    participant HR as HoldingRepository

    U->>C: GET /api/v1/holding
    C->>S: getMyPortfolio(userId)
    
    S->>HR: findByUserId(userId)
    HR-->>S: List<Holding> 반환 (수량, 평단가 포함)
    
    S->>PR: findAll() 또는 특정 코인 시세 조회
    PR-->>S: 최신 시세 데이터 반환
    
    Note right of S: Java 로직으로 수익률/평가금액 계산
    
    S-->>C: PortfolioDTO 반환
    C-->>U: 200 OK
```

# 3. ERD
<img width="1620" height="912" alt="Image" src="https://github.com/user-attachments/assets/0a606897-6a2a-4dd2-8750-a80c6eea9b98" />
