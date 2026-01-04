```markdown
# ✈️ Trip Planner – Android App

An Android application that allows users to **create, view, and manage trips**, built as part of an Android Developer interview assessment.  
The app closely follows the provided **Figma UI design** and integrates with a **CRUD API** for trip operations.

---

## 📱 Features

- View all planned trips
- Create a new trip
- View trip details
- Pixel-perfect UI based on Figma design
- API integration for CRUD operations
- Loading, success, and error state handling
- Clean architecture with state-driven UI

---

## 🎨 UI Design

The UI was implemented based on the provided Figma design, ensuring close adherence to:

- Spacing
- Typography
- Colors
- Layout structure
- Component behavior

🔗 **Figma Design:**  
[View Design](https://www.figma.com/design/QomXDEA1WE6pDJFhMcPqyt/Task-UI?node-id=1-5512)

---

## 🖼️ Screenshots

<div align="center">

### Plan Trip
<img src="https://i.imgur.com/6FRrZkf.jpeg" width="250" alt="Plan Trip"/>

### Create Trip
<img src="https://i.imgur.com/XXXXX.jpeg" width="250" alt="Create Trip"/>

### Trip List
<img src="https://i.imgur.com/XXXXX.jpeg" width="250" alt="Trip List"/>

### Trip Details
<img src="https://i.imgur.com/XXXXX.jpeg" width="250" alt="Trip Details"/>

### Date Screen
<img src="https://i.imgur.com/XXXXX.jpeg" width="250" alt="Date Screen"/>

### Where To
<img src="https://i.imgur.com/XXXXX.jpeg" width="250" alt="Where To"/>

</div>

> 📌 Screenshots are located in the `/screenshots` directory.

---

## 🎥 Screen Recording

A short screen recording demonstrating:

- Creating a trip
- Viewing trips
- Filtering by travel style
- Error and loading states

📽️ **Screen Recording Link:**  
👉 _Add your screen recording link here (Google Drive / YouTube / GitHub)_

---

## 🏗️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** MVVM
- **State Management:** StateFlow
- **Networking:** Ktor Client
- **Dependency Injection:** Hilt
- **Image Loading:** Coil
- **Asynchronous Programming:** Kotlin Coroutines

---

## 📦 Project Structure

```
com.example.tripplanner
│
├── data
│   ├── model          # DTOs and request models
│   ├── remote         # API services
│   └── repository     # Data repositories
│
├── ui
│   ├── screens        # Compose screens
│   ├── components     # Reusable UI components
│   └── theme          # Colors, typography, shapes
│   └── viewmodels     # Viewmodels
│
├── navigation         # NavGraph
│
└── utils              # Helpers & extensions
```

---

## 🌐 API Integration

🔗 **API Base URL:**  
`https://beeceptor.com/crud-api/`

### Endpoints Used

| Action | Method | Endpoint |
|--------|--------|----------|
| Create Trip | POST | `/trips` |
| Get All Trips | GET | `/trips` |
| Get Trip By ID | GET | `/trips/{id}` |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android SDK 24+
- Internet connection

### Setup Instructions

```bash
# Clone the repository
git clone https://github.com/your-username/trip-planner.git

# Open the project in Android Studio
# Sync Gradle
# Run on emulator or physical device
```

---

## 📦 APK

📥 **APK Download:**  
👉 _Add your APK link here (Google Drive / GitHub Releases)_

---

## 👨‍💻 Author

**Okolo Arthur**  
Android Developer

🔗 [GitHub Profile](https://github.com/your-username)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Figma design team for the UI specifications
- Interview assessment team for the opportunity
```
