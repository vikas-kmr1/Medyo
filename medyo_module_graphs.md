<body style="background: #ffffff;">
# 🏗️ Medyo — Complete Architecture & Module Flow Graphs

> **Medyo** is a modular Android medicine-scanning app powered by **Gemini AI** (Firebase AI Logic). Users scan medicine images via CameraX, the AI analyzes them, and results are persisted locally via Room.

---

## 📋 Table of Contents

1. [High-Level Module Dependency Graph](#1-high-level-module-dependency-graph)
2. [Feature Module API/Impl Pattern](#2-feature-module-apiimpl-separation)
3. [Core Module Internal Dependencies](#3-core-module-internal-dependencies)
4. [Medicine Scan — End-to-End Data Flow](#4-medicine-scan--end-to-end-data-flow)
5. [Navigation System Architecture](#5-navigation-system--multi-stack-tab-architecture)
6. [UI Layer — Unidirectional Data Flow](#6-ui-layer--unidirectional-data-flow-udf)
7. [Data Layer — Repository Pattern](#7-data-layer--repository-pattern)
8. [AI Logic Pipeline — Gemini Integration](#8-ai-logic-pipeline--gemini-integration)
9. [Logger Module — API/Impl Pattern](#9-logger-module--apiimpl-pattern)
10. [User Journey Flowchart](#10-user-journey-flowchart)

---

## 1. High-Level Module Dependency Graph

This is the **master graph** showing every module in the project and how they connect. Derived from all `build.gradle.kts` files.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%

graph TD
    classDef app fill:#ff6b6b,stroke:#c92a2a,stroke-width:3px,color:#fff;
    classDef featureApi fill:#e8d5f5,stroke:#7c3aed,stroke-width:2px;
    classDef featureImpl fill:#c4b5fd,stroke:#7c3aed,stroke-width:2px;
    classDef core fill:#bbf7d0,stroke:#16a34a,stroke-width:2px;
    classDef coreAI fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef logger fill:#fecaca,stroke:#dc2626,stroke-width:2px;

    App[":app"]:::app

    %% Feature API Modules
    HomeAPI[":feature:home:api"]:::featureApi
    HomeImpl[":feature:home:impl"]:::featureImpl
    BioScanAPI[":feature:bio-scan:api"]:::featureApi
    BioScanImpl[":feature:bio-scan:impl"]:::featureImpl
    ScannerAPI[":feature:scanner:api"]:::featureApi
    ScannerImpl[":feature:scanner:impl"]:::featureImpl
    SettingsAPI[":feature:settings:api"]:::featureApi
    SettingsImpl[":feature:settings:impl"]:::featureImpl

    %% Core Modules
    Domain[":core:domain"]:::core
    Data[":core:data"]:::core
    Database[":core:database"]:::core
    AiLogic[":core:ai-logic"]:::coreAI
    Navigation[":core:navigation"]:::core
    DesignSystem[":core:design-system"]:::core
    UI[":core:ui"]:::core
    Utils[":core:utils"]:::core
    Network[":core:network"]:::core
    Notification[":core:notification"]:::core
    Datastore[":core:datastore"]:::core
    DatastoreProto[":core:datastore-proto"]:::core
    WorkManager[":core:work-manager"]:::core

    %% Logger Modules
    LoggerAPI[":logger:api"]:::logger
    LoggerImpl[":logger:impl"]:::logger

    %% ── App Dependencies ──
    App --> HomeAPI & HomeImpl
    App --> BioScanAPI & BioScanImpl
    App --> ScannerAPI & ScannerImpl
    App --> SettingsAPI & SettingsImpl
    App --> DesignSystem & UI & AiLogic
    App --> Data & Database & Domain & Navigation
    App --> LoggerAPI & LoggerImpl

    %% ── Feature Impl → API ──
    HomeImpl --> HomeAPI
    BioScanImpl --> BioScanAPI
    ScannerImpl --> ScannerAPI
    SettingsImpl --> SettingsAPI

    %% ── Feature Impl → Core ──
    HomeImpl --> DesignSystem & UI & Navigation
    HomeImpl --> BioScanAPI
    BioScanImpl --> DesignSystem & UI & Domain & Navigation & Utils
    BioScanImpl --> ScannerAPI
    ScannerImpl --> DesignSystem & Domain & Navigation
    SettingsImpl --> DesignSystem & UI & Navigation

    %% ── Core Internal ──
    Data --> AiLogic & Database & Domain
    Domain --> AiLogic
    AiLogic --> Utils
    UI --> DesignSystem & Utils
    DesignSystem --> Utils
    LoggerImpl --> LoggerAPI
```

---

## 2. Feature Module API/Impl Separation

Each feature is split into two sub-modules. The `api` module exposes **only the navigation key** (route), while `impl` contains the full UI, ViewModel, and logic. This prevents feature-to-feature coupling.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph LR
    classDef api fill:#e8d5f5,stroke:#7c3aed,stroke-width:2px;
    classDef impl fill:#c4b5fd,stroke:#7c3aed,stroke-width:2px;
    classDef app fill:#ff6b6b,stroke:#c92a2a,stroke-width:2px,color:#fff;
    classDef core fill:#bbf7d0,stroke:#16a34a,stroke-width:2px;

    subgraph BioScan ["feature:bio-scan"]
        BS_API["api\n─────────\nBioScanNavKey"]:::api
        BS_IMPL["impl\n─────────\nBioScanScreen\nBioScanViewmodel\nBioScanEntryProvider"]:::impl
        BS_IMPL --> BS_API
    end

    subgraph Scanner ["feature:scanner"]
        SC_API["api\n─────────\nScannerNavKey"]:::api
        SC_IMPL["impl\n─────────\nScannerScreen\nScannerViewModel\nScannerEntryProvider"]:::impl
        SC_IMPL --> SC_API
    end

    subgraph Home ["feature:home"]
        H_API["api\n─────────\nHomeNavKey"]:::api
        H_IMPL["impl\n─────────\nHomeScreen\nHomeRoute\nHomeViewModel\nHomeEntryProvider"]:::impl
        H_IMPL --> H_API
    end

    subgraph Settings ["feature:settings"]
        S_API["api\n─────────\nSettingsNavKey"]:::api
        S_IMPL["impl\n─────────\nSettingsScreen\nSettingsEntryProvider"]:::impl
        S_IMPL --> S_API
    end

    %% Cross-feature: only through API
    BS_IMPL -.->|"navigates to"| SC_API
    H_IMPL -.->|"navigates to"| BS_API

    App[":app"]:::app
    App --> BS_API & BS_IMPL & SC_API & SC_IMPL & H_API & H_IMPL & S_API & S_IMPL
```

> [!IMPORTANT]
> Feature `impl` modules **never** depend on another feature's `impl`. They only import `api` modules for navigation keys. This is the key to build-time isolation.

---

## 3. Core Module Internal Dependencies

Detailed view of how the 13 core modules relate to each other.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph TD
    classDef data fill:#bfdbfe,stroke:#2563eb,stroke-width:2px;
    classDef ai fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef storage fill:#d9f99d,stroke:#65a30d,stroke-width:2px;
    classDef ui fill:#fecdd3,stroke:#e11d48,stroke-width:2px;
    classDef infra fill:#e2e8f0,stroke:#64748b,stroke-width:2px;

    Data[":core:data\n─────────\nBioScanRepositoryImpl\nAiMedicineMapper\nDataModule"]:::data

    Domain[":core:domain\n─────────\nBioScanRepository ⟨interface⟩\nScanAndSaveMedicineUseCase\nGetScannedMedicineUseCase\nGetAllScannedMedicinesUseCase\nMedicineInfo ⟨model⟩"]:::data

    AiLogic[":core:ai-logic\n─────────\nGeminiAiDataSource ⟨interface⟩\nGeminiAiDataSourceImpl\nAiMedicineResponseDto\nAiLogicModule ⟨Hilt⟩"]:::ai

    Database[":core:database\n─────────\nMedicationDao\nMedicationEntity\nMedicineInfoEntity\nRoom DB"]:::storage

    Datastore[":core:datastore"]:::storage
    DatastoreProto[":core:datastore-proto"]:::storage
    Network[":core:network\n─────────\nRetrofit + OkHttp"]:::infra

    Navigation[":core:navigation\n─────────\nNavigator\nNavigationState"]:::infra
    DesignSystem[":core:design-system\n─────────\nTheme, Colors, Typography\nMedicineCardItem\nCustomBottomNavigation\nShimmerBrush\nScrollbar"]:::ui
    UI[":core:ui\n─────────\nShared Composables"]:::ui
    Utils[":core:utils\n─────────\nURLs, Enums, MedicineType\nDateAndTime, PrettyPrint\nKotlinExtension"]:::infra
    Notification[":core:notification"]:::infra
    WorkManager[":core:work-manager"]:::infra

    Data --> AiLogic
    Data --> Database
    Data --> Domain
    Domain --> AiLogic
    AiLogic --> Utils
    UI --> DesignSystem
    UI --> Utils
    DesignSystem --> Utils
```

---

## 4. Medicine Scan — End-to-End Data Flow

The **core user journey**: capturing medicine images, sending them to Gemini AI, saving the result, and displaying it. This sequence diagram traces data through every architectural layer.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
sequenceDiagram
    autonumber
    participant User as 👤 User
    participant ScanUI as ScannerScreen<br/>⟨Compose⟩
    participant ScanVM as ScannerViewModel<br/>⟨Hilt ViewModel⟩
    participant UseCase as ScanAndSaveMedicine<br/>UseCase
    participant Repo as BioScanRepository<br/>Impl
    participant AI as GeminiAiDataSource<br/>Impl
    participant Gemini as ☁️ Firebase Gemini AI
    participant Mapper as AiMedicineMapper
    participant DAO as MedicationDao<br/>⟨Room⟩
    participant DB as 💾 SQLite

    User->>ScanUI: Tap capture button
    ScanUI->>ScanVM: captureImage(context)
    ScanVM->>ScanVM: CameraX ImageCapture → Bitmap
    ScanVM-->>ScanUI: Update capturedImages list

    Note over User,ScanUI: User can capture up to 5 images

    User->>ScanUI: Tap "Proceed"
    ScanUI->>ScanVM: onProceed()
    ScanVM->>ScanVM: _uiState = Loading

    ScanVM->>UseCase: invoke(images)
    UseCase->>Repo: scanAndSaveMedicine(images)
    
    Repo->>AI: generateContext(images)
    AI->>Gemini: genAI.generateContent(prompt + images)
    Gemini-->>AI: JSON response text
    AI->>AI: json.decodeFromString → AiMedicineResponseDto
    AI-->>Repo: AiMedicineResponseDto

    Repo->>Mapper: toMedicationEntity()
    Repo->>DAO: insertMedication(entity)
    DAO->>DB: INSERT INTO medications
    DB-->>DAO: generatedId (Long)
    
    Repo->>Mapper: toMedicineInfoEntity(generatedId)
    Repo->>DAO: insertMedicineInfo(infoEntity)
    DAO->>DB: INSERT INTO medicine_info

    DAO-->>Repo: Success
    Repo-->>UseCase: Result.success(generatedId)
    UseCase-->>ScanVM: Result<Long>

    ScanVM->>ScanVM: _uiState = Success(medicineId)
    ScanVM-->>ScanUI: Re-render with success state
    ScanUI-->>User: Navigate to BioScan details
```

---

## 5. Navigation System — Multi-Stack Tab Architecture

Medyo uses a custom **multi-backstack navigation system** built on Jetpack Navigation 3. Each tab maintains its own sub-stack of screens.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph TD
    classDef tab fill:#dbeafe,stroke:#2563eb,stroke-width:2px;
    classDef screen fill:#f0fdf4,stroke:#16a34a,stroke-width:2px;
    classDef active fill:#fef08a,stroke:#ca8a04,stroke-width:3px;
    classDef nav fill:#fce7f3,stroke:#db2777,stroke-width:2px;

    subgraph AppShell ["MedyoApp (Scaffold)"]
        NavDisplay["NavDisplay\n⟨renders active backstack⟩"]:::nav
        BottomBar["CustomBottomNavigation\n🏠 Home | 💊 BioScan | ⚙️ Settings"]:::nav
    end

    subgraph NavigationState ["NavigationState"]
        TopStack["topLevelStack\n⟨ordered tab history⟩"]:::tab
        
        subgraph SubStacks ["Per-Tab Sub-Stacks"]
            HomeStack["Home Stack\n[HomeNavKey]"]:::tab
            BioStack["BioScan Stack\n[BioScanNavKey, ScannerNavKey?]"]:::active
            SettingsStack["Settings Stack\n[SettingsNavKey]"]:::tab
        end
    end

    subgraph Navigator ["Navigator (Engine)"]
        NavFn["navigate(key)"]:::screen
        BackFn["goBack()"]:::screen
        GoToKey["goToKey() → push to sub-stack"]:::screen
        GoToTop["goToTopLevel() → switch tabs"]:::screen
        ClearSub["clearSubStack() → pop to tab root"]:::screen
    end

    BottomBar -->|"tab tap"| NavFn
    NavFn -->|"same tab"| ClearSub
    NavFn -->|"diff tab"| GoToTop
    NavFn -->|"detail screen"| GoToKey

    GoToTop --> TopStack
    GoToKey --> SubStacks
    ClearSub --> SubStacks
    BackFn -->|"at tab root"| TopStack
    BackFn -->|"at detail"| SubStacks
```

### Navigation Decision Tree

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
flowchart TD
    A["navigate(key) called"] --> B{Is key the\ncurrently active tab?}
    B -->|Yes| C["clearSubStack()\nPop all detail screens\nReturn to tab root"]
    B -->|No| D{Is key a\ntop-level tab?}
    D -->|Yes| E["goToTopLevel(key)\nSwitch bottom tab\nBring target to front"]
    D -->|No| F["goToKey(key)\nPush onto current\ntab's sub-stack"]

    G["goBack() called"] --> H{Is current key\nthe startKey?}
    H -->|"Yes (BioScan)"| I["No-op\nLet system handle exit"]
    H -->|No| J{Is current key\na tab root?}
    J -->|Yes| K["Pop topLevelStack\nReturn to previous tab"]
    J -->|No| L["Pop currentSubStack\nReturn to previous screen"]

    style A fill:#7c3aed,color:#fff
    style G fill:#7c3aed,color:#fff
    style C fill:#bbf7d0
    style E fill:#bfdbfe
    style F fill:#fef08a
    style I fill:#fecaca
    style K fill:#bfdbfe
    style L fill:#bbf7d0
```

---

## 6. UI Layer — Unidirectional Data Flow (UDF)

This pattern is consistent across all feature `impl` modules. Shown here with the **Scanner** feature as a concrete example.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph TD
    classDef ui fill:#dbeafe,stroke:#2563eb,stroke-width:2px;
    classDef vm fill:#bbf7d0,stroke:#16a34a,stroke-width:2px;
    classDef state fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef domain fill:#fde68a,stroke:#d97706,stroke-width:2px;

    Screen["ScannerScreen\n⟨Jetpack Compose⟩"]:::ui
    VM["ScannerViewModel\n⟨@HiltViewModel⟩"]:::vm

    subgraph UIState ["ScannerUiState (Sealed Interface)"]
        Idle["Idle"]:::state
        Loading["Loading"]:::state
        Success["Success(medicineId)"]:::state
        Error["Error(message)"]:::state
    end

    UseCase["ScanAndSaveMedicineUseCase"]:::domain
    CameraX["CameraX\nImageCapture + Preview"]:::ui

    Screen -- "1️⃣ captureImage()\nonProceed()\ntoggleTorch()\ntapToFocus()" --> VM
    VM -- "2️⃣ invoke(images)" --> UseCase
    UseCase -. "3️⃣ Result<Long>" .-> VM
    VM -- "4️⃣ Emits new state" --> UIState
    UIState -. "5️⃣ Collected as StateFlow\nRecomposes UI" .-> Screen
    CameraX -. "surfaceRequest\nFlow" .-> VM
    VM -. "surfaceRequest\nStateFlow" .-> Screen
```

---

## 7. Data Layer — Repository Pattern

The `core:data` module implements the repository interface defined in `core:domain`, orchestrating between the AI data source and the local database.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph TD
    classDef domain fill:#e8d5f5,stroke:#7c3aed,stroke-width:2px;
    classDef data fill:#bfdbfe,stroke:#2563eb,stroke-width:2px;
    classDef ai fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef db fill:#d9f99d,stroke:#65a30d,stroke-width:2px;

    subgraph DomainLayer [":core:domain"]
        RepoInterface["BioScanRepository\n⟨interface⟩"]:::domain
        UseCases["UseCases\n──────\nScanAndSaveMedicine\nGetScannedMedicine\nGetAllScannedMedicines"]:::domain
        MedicineInfo["MedicineInfo\n⟨domain model⟩"]:::domain
        UseCases --> RepoInterface
    end

    subgraph DataLayer [":core:data"]
        RepoImpl["BioScanRepositoryImpl\n⟨@Inject⟩"]:::data
        Mapper["AiMedicineMapper\n──────\ntoMedicationEntity()\ntoMedicineInfoEntity()\ntoDomainModel()"]:::data
        DataModule["DataModule\n⟨@Provides Hilt⟩"]:::data
        RepoImpl --> Mapper
        DataModule -.->|"binds"| RepoImpl
    end

    subgraph AILayer [":core:ai-logic"]
        AiInterface["GeminiAiDataSource\n⟨interface⟩"]:::ai
        AiImpl["GeminiAiDataSourceImpl\n⟨Firebase GenerativeModel⟩"]:::ai
        DTO["AiMedicineResponseDto\n⟨@Serializable⟩"]:::ai
        AiImpl --> AiInterface
        AiImpl --> DTO
    end

    subgraph DBLayer [":core:database"]
        DAO["MedicationDao\n⟨@Dao⟩"]:::db
        Entities["MedicationEntity\nMedicineInfoEntity\nMedicationCategory"]:::db
        DAO --> Entities
    end

    RepoImpl -.->|"implements"| RepoInterface
    RepoImpl --> AiInterface
    RepoImpl --> DAO

    %% Data flow
    AiInterface -- "AI Response DTO" --> RepoImpl
    RepoImpl -- "Mapper → Entity" --> DAO
    DAO -- "Flow<Entity>" --> RepoImpl
    RepoImpl -- "Mapper → Domain Model" --> MedicineInfo
```

---

## 8. AI Logic Pipeline — Gemini Integration

The `core:ai-logic` module encapsulates all interaction with the Firebase Gemini generative AI model.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
sequenceDiagram
    participant Repo as BioScanRepositoryImpl
    participant AI as GeminiAiDataSourceImpl
    participant Model as Firebase GenerativeModel<br/>⟨Gemini AI⟩
    participant Json as Kotlinx Serialization

    Repo->>AI: generateContext(List<Bitmap>)
    
    Note over AI: Build prompt with strict<br/>pharmaceutical AI rules
    
    AI->>AI: Build Content block<br/>images.forEach { image(bitmap) }<br/>text(pharmacyPrompt)

    AI->>Model: generateContent(inputContent)
    
    Note over Model: AI analyzes medicine images<br/>Returns structured JSON

    Model-->>AI: GenerateContentResponse

    AI->>AI: Extract response.text

    alt Response is not blank
        AI->>Json: decodeFromString<AiMedicineResponseDto>(text)
        Json-->>AI: AiMedicineResponseDto
        AI-->>Repo: AiMedicineResponseDto
    else Response is blank
        AI-->>Repo: null
    end

    Note over Repo: Maps DTO → Entity<br/>Saves to Room DB
```

### AI Response Structure

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
classDiagram
    class AiMedicineResponseDto {
        +String? brand
        +String? salts
        +Long? mfgDate
        +Long? expDate
        +List~String~ sideEffects
        +List~String~ cures
        +List~String~ precautions
        +List~String~ instructions
        +String? category
        +String? errorMessage
        +String? statusCode
    }

    class MedicineInfo {
        +Long medicationId
        +String name
        +String brand
        +String salts
        +Long? mfgDate
        +Long? expDate
        +List~String~ sideEffects
        +List~String~ cures
        +List~String~ precautions
        +List~String~ instructions
        +String category
        +String? errorMessage
        +String? statusCode
    }

    AiMedicineResponseDto ..> MedicineInfo : "mapped via\nAiMedicineMapper"
```

---

## 9. Logger Module — API/Impl Pattern

The logger follows the same API/Impl split as features, allowing it to be swapped for different platforms or implementations.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
graph LR
    classDef api fill:#fecaca,stroke:#dc2626,stroke-width:2px;
    classDef impl fill:#fca5a5,stroke:#dc2626,stroke-width:2px;
    classDef ext fill:#e2e8f0,stroke:#64748b,stroke-width:2px;

    subgraph LoggerModule ["logger"]
        API["logger:api\n──────────\nLoggerApi ⟨interface⟩\n• logD(message)\n• logDWithTag(tag, message)\n• logE(message, e)\n• logEWithTag(tag, message, e)"]:::api

        IMPL["logger:impl\n──────────\nLoggerApiImpl\n• Uses Timber internally\n• Guarded by BuildConfig.DEBUG\n• Provided via Hilt (LoggerModule)"]:::impl

        IMPL --> API
    end

    Timber["🌲 Timber\n⟨Jake Wharton⟩"]:::ext
    Hilt["⚡ Hilt DI\n⟨LoogerModule⟩"]:::ext

    IMPL --> Timber
    Hilt -.->|"@Provides"| IMPL

    App[":app"]
    App --> API
    App --> IMPL
```

---

## 10. User Journey Flowchart

The complete user flow through the application, mapping which modules are involved at each step.

```mermaid
%%{init: {'theme': 'neutral', 'themeVariables': { 'darkMode': false }}}%%
flowchart TD
    classDef start fill:#7c3aed,color:#fff,stroke-width:0;
    classDef screen fill:#dbeafe,stroke:#2563eb,stroke-width:2px;
    classDef action fill:#bbf7d0,stroke:#16a34a,stroke-width:2px;
    classDef ai fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef decision fill:#fecdd3,stroke:#e11d48,stroke-width:2px;

    Launch["🚀 App Launch\nMainActivity + SplashScreen"]:::start
    --> AppShell["MedyoApp Scaffold\nNavigationState initialized\nstartKey = BioScanNavKey"]:::screen

    AppShell --> BioScan["💊 BioScan Screen\n⟨feature:bio-scan:impl⟩\nShows list of scanned medicines\nvia GetAllScannedMedicinesUseCase"]:::screen

    BioScan -->|"Tap scan button"| Scanner["📷 Scanner Screen\n⟨feature:scanner:impl⟩\nCameraX preview\nCapture up to 5 images"]:::screen

    Scanner -->|"Tap capture"| Capture["📸 Capture Image\nCameraX ImageCapture\nimageProxyToBitmap()"]:::action

    Capture --> MorePhotos{More photos\nneeded?}:::decision
    MorePhotos -->|"Yes (max 5)"| Scanner
    MorePhotos -->|"No, tap Proceed"| Analyze

    Analyze["🤖 AI Analysis\nScanAndSaveMedicineUseCase\n→ BioScanRepositoryImpl\n→ GeminiAiDataSourceImpl"]:::ai

    Analyze --> AIResult{AI Result}:::decision
    AIResult -->|"Success"| SaveDB["💾 Save to Room DB\nMedicationEntity +\nMedicineInfoEntity"]:::action
    AIResult -->|"Error"| ErrorState["❌ Error State\nShow error message"]:::screen

    SaveDB --> Navigate["Navigate to\nBioScan Details\nmedicineId passed"]:::action
    Navigate --> BioScan

    ErrorState -->|"Retry"| Scanner

    AppShell -->|"Tab: Home"| Home["🏠 Home Screen\n⟨feature:home:impl⟩"]:::screen
    AppShell -->|"Tab: Settings"| Settings["⚙️ Settings Screen\n⟨feature:settings:impl⟩"]:::screen

    Home -->|"Tab: BioScan"| BioScan
    Settings -->|"Tab: BioScan"| BioScan
```

---

## Module Count Summary

| Category | Modules | Count |
|----------|---------|-------|
| **App** | `:app` | 1 |
| **Feature API** | `:feature:home:api`, `:feature:bio-scan:api`, `:feature:scanner:api`, `:feature:settings:api` | 4 |
| **Feature Impl** | `:feature:home:impl`, `:feature:bio-scan:impl`, `:feature:scanner:impl`, `:feature:settings:impl` | 4 |
| **Core** | `:core:data`, `:core:domain`, `:core:database`, `:core:ai-logic`, `:core:navigation`, `:core:design-system`, `:core:ui`, `:core:utils`, `:core:network`, `:core:notification`, `:core:datastore`, `:core:datastore-proto`, `:core:work-manager` | 13 |
| **Logger** | `:logger:api`, `:logger:impl` | 2 |
| **Total** | | **24** |

---

> [!TIP]
> The most critical data path in the app is: **ScannerScreen → ScannerViewModel → ScanAndSaveMedicineUseCase → BioScanRepositoryImpl → GeminiAiDataSourceImpl → Firebase Gemini → Room DB**. This is the heart of Medyo.

</body>