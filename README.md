# WeighbridgeApp

**Android Weighbridge Collection & Vehicle Tracking System**

A mobile application designed to simplify and digitize weighbridge collection operations through structured data entry, vehicle tracking, session management, and automated reporting.

---

## Overview

WeighbridgeApp is an Android-based application developed to support tea collection operations by providing a centralized way to record weighbridge transactions, monitor vehicle collections, and generate collection summaries.

The application is designed to improve accuracy, accountability, and efficiency in daily weighbridge operations.

## Features

- **Collection Sessions** – Start, manage, and close collection sessions.
- **Weighment Recording** – Capture collection entries and recorded weights.
- **Route Management** – Organize collection activities by predefined routes.
- **Vehicle Tracking** – Monitor vehicle collections and trip records.
- **Collection Summaries** – View total weights, vehicle counts, and collection progress.
- **Vehicle Target Management** – Update vehicle collection targets during active sessions.
- **Session History** – Access previous collection sessions.
- **Data Corrections** – Manage recorded entries and correct mistakes.
- **Automated Reporting** – Generate formatted collection reports for sharing through WhatsApp.

## Technology Stack

| Component | Technology |
|---|---|
| Platform | Android |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| UI Design | Material 3 |
| Local Database | Room |
| Architecture | ViewModel / Repository |
| Build System | Gradle |
| IDE | Android Studio |

## Project Structure

```text
WeighbridgeApp/
│
├── app/
│   └── src/main/java/com/example/weighbridgeapp/
│       ├── data/
│       │   ├── model/
│       │   └── repository/
│       │
│       ├── navigation/
│       │
│       └── ui/
│           ├── components/
│           ├── dashboard/
│           ├── entry/
│           └── theme/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Getting Started

### Requirements

- Android Studio
- Kotlin
- Android SDK
- Gradle

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/mykolkiprono/WeighbridgeApp.git
   ```

2. Open the project in Android Studio.

3. Allow Gradle to synchronize the project.

4. Connect an Android device or start an emulator.

5. Build and run the application.

## Version Information

| Version | Status |
|---|---|
| Version 1.0 | Initial release |
| Version 2.0 | Planned development |

## Development Roadmap

Future development may focus on:

- Improved application architecture.
- Enhanced database management.
- Better reporting and analytics.
- Improved user experience.
- Data backup and synchronization.
- Expanded vehicle and trip management.
- Performance and reliability improvements.

## Developer

**Michael Kiprono**

Software Engineering | Android Development | Data Engineering

GitHub: [@mykolkiprono](https://github.com/mykolkiprono)

---

*Built to improve efficiency, accuracy, and accountability in weighbridge collection operations.*
