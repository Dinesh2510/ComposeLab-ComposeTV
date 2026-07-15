<div align="center">

# 📺 Compose Lab - Compose TV

### A modern Android TV application built entirely with Jetpack Compose for TV

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose TV](https://img.shields.io/badge/Compose%20TV-1.1.0-3DDC84?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/training/tv)
[![Android](https://img.shields.io/badge/Platform-Android%20TV-000000?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com/tv/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)
[![Stars](https://img.shields.io/github/stars/dinesh2510/compose-lab-tv?style=for-the-badge)](https://github.com/Dinesh2510/ComposeLab-ComposeTV/stargazers)

**A complete, production-style Android TV app demo — built with Jetpack Compose for TV, Hilt, Room, Paging 3, Ktor & Retrofit — showcasing real-world architecture patterns for TV app development.**

[📥 Download APK](#-download) • [🎥 Watch Demo](#-video-demo) • [✨ Features](#-features) • [🏗️ Tech Stack](#️-tech-stack) • [🚀 Setup](#-getting-started)

</div>

---

## 📖 About

**Compose Lab - Compose TV** is an open-source Android TV application built from scratch using **Jetpack Compose for TV (Compose TV)** — Google's modern declarative UI toolkit for building television experiences. This project demonstrates how to build a fully functional, streaming-style TV app with a clean **MVVM + Clean Architecture** approach, D-pad friendly navigation, lazy-loaded content rows, video playback, and offline-first data handling.

It's designed as both a **learning resource** for developers exploring Compose for TV and a **starter template / boilerplate** for building your own Android TV apps.

> ⭐ If you find this project useful, please consider giving it a star — it helps others discover it too!

---

## ✨ Features

- 🎬 Splash screen with onboarding flow for first-time users
- 🏠 TV-optimized Home dashboard with lazy content rows & carousels
- 🔍 Dedicated Search screen with D-pad friendly focus handling
- 📄 Home/Content details screen with rich metadata
- ▶️ Built-in Media Player screen for video playback
- ❤️ Wishlist / Favorites to save content for later
- ⚙️ Settings screen for app preferences
- 📜 Privacy Policy & Terms and Conditions screens
- 🎮 Full remote-control (D-pad) navigation support
- 🌙 Modern Material 3 for TV design system
- 🧩 Modular, scalable Clean Architecture (MVVM + Repository pattern)
- 💉 Dependency Injection with Hilt
- 💾 Offline caching using Room + DataStore
- 📡 Network layer built with Ktor & Retrofit
- 🖼️ Image loading & caching with Coil

---

## 📱 Screens

| Splash Screen | Onboarding | Dashboard / Home |
|:---:|:---:|:---:|
| <img src="screenshots/splash.png" width="270"/> | <img src="screenshots/onboarding.png" width="270"/> | <img src="screenshots/dashboard.png" width="270"/> |

| Home Details | Media Player | Search |
|:---:|:---:|:---:|
| <img src="screenshots/home_details.png" width="270"/> | <img src="screenshots/media_player.png" width="270"/> | <img src="screenshots/search.png" width="270"/> |

| Wishlist / Favorites | Settings | Privacy Policy / Terms |
|:---:|:---:|:---:|
| <img src="screenshots/wishlist.png" width="270"/> | <img src="screenshots/settings.png" width="270"/> | <img src="screenshots/privacy_terms.png" width="270"/> |

---

## 🎥 Video Demo

<div align="center">

[![Watch the demo on YouTube](https://img.youtube.com/vi/MmDndn9AkFs/hqdefault.jpg)](https://youtu.be/MmDndn9AkFs)

**▶️ [Watch full demo on YouTube](https://youtu.be/MmDndn9AkFs)**

</div>

---

## 🏗️ Tech Stack

### Language & UI
| Category | Library | Version |
|---|---|---|
| Language | [Kotlin](https://kotlinlang.org) | `2.3.20` |
| UI Toolkit | [Jetpack Compose BOM](https://developer.android.com/jetpack/compose/bom) | `2026.06.01` |
| TV UI | [Compose TV Foundation](https://developer.android.com/training/tv) | `1.0.0` |
| TV UI | [Compose TV Material](https://developer.android.com/training/tv) | `1.1.0` |
| Icons | Material Icons Extended | `1.7.8` |
| Core | AndroidX Core KTX | `1.18.0` |
| Compat | AndroidX AppCompat | `1.7.1` |
| Activity | Activity Compose | `1.13.0` |

### Architecture Components
| Category | Library | Version |
|---|---|---|
| Lifecycle | Lifecycle Runtime Compose | `2.11.0` |
| Lifecycle | Lifecycle Runtime KTX | `2.11.0` |
| Navigation | Navigation Compose | `2.9.8` |
| DI | Hilt | `2.59.2` |
| DI | Hilt Navigation Compose | `1.3.0` |

### Data & Storage
| Category | Library | Version |
|---|---|---|
| Database | Room | `2.8.4` |
| Paging | Paging 3 | `3.5.0` |
| Preferences | DataStore | `1.2.1` |

### Networking
| Category | Library | Version |
|---|---|---|
| HTTP Client | Ktor | `3.5.1` |
| HTTP Client | Retrofit | `3.0.0` |
| Logging | OkHttp Logging Interceptor | `5.4.0` |
| Serialization | Kotlinx Serialization | `1.11.0` |

### Image Loading & Build Tools
| Category | Library | Version |
|---|---|---|
| Image Loading | Coil | `2.7.0` |
| Build Plugin | Android Gradle Plugin (AGP) | `9.0.1` |
| Annotation Processing | KSP | `2.0.21-1.0.28` |

---

## 🌐 Data Source / API

This app fetches sample TV/video content from Google's public Android TV sample content feed:

```
https://storage.googleapis.com/androiddevelopers/samples_assets/android-tv/android_tv_videos_new.json
```

This is a publicly hosted sample JSON feed (used in Google's own Android TV sample apps) containing categorized video metadata — titles, descriptions, thumbnails, and playable media URLs — which this app parses and renders as content rows.

> ⚠️ This content is provided by Google for demo/sample purposes only. This project does not own, host, or claim rights to any of the media referenced by this feed — it is used strictly for educational and demonstration purposes.

---

## 🗂️ App Screens & Navigation

The app uses a single-activity architecture with `Navigation Compose`, structured as:

```kotlin
object Splash : Screen("splash")
object Onboarding : Screen("onboarding")

// Main Wrapper
object Dashboard : Screen("dashboard")

object HomeDetails : Screen("homedetails")
object MediaPlayerScreen : Screen("media_player")

// Drawer Items
object Search : Screen("search", "Search", Icons.Default.Search)
object Home : Screen("home", "Home", Icons.Default.Home)
object Favorite : Screen("wishlist", "Wishlist", Icons.Default.Bookmark)
object PrivacyPolicy : Screen("pvpc", "Privacy Policy", Icons.Default.Security)
object Terms : Screen("tnc", "Terms & Condition", Icons.Default.Description)
object Settings : Screen("settings", "Settings", Icons.Default.Settings)
```

---

## 🏛️ Architecture

This project follows **MVVM (Model-View-ViewModel)** combined with **Clean Architecture** principles:

```
app/
├── data/
│   ├── local/          → Room database, DataStore
│   ├── remote/         → Ktor / Retrofit API services, DTOs
│   └── repository/     → Repository implementations
├── domain/
│   ├── model/          → Domain models
│   ├── repository/     → Repository interfaces
│   └── usecase/        → Business logic / use cases
├── di/                 → Hilt modules
├── ui/
│   ├── screens/        → Splash, Onboarding, Dashboard, Home, Search, etc.
│   ├── components/     → Reusable Compose TV components
│   ├── navigation/      → NavHost & Screen routes
│   └── theme/           → TV Material theme
└── MainActivity.kt
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable version, Ladybug or newer)
- JDK 17+
- Android TV device or Android TV Emulator

### Installation

```bash
# Clone the repository
git clone https://github.com/Dinesh2510/compose-lab-tv.git

# Open in Android Studio
cd compose-lab-tv

# Sync Gradle and Run on an Android TV emulator/device
```

### 📥 Download

You can download the latest APK from the [Releases](https://github.com/Dinesh2510/compose-lab-tv/releases) section.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Feel free to check the [issues page](https://github.com/Dinesh2510/compose-lab-tv/issues).

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## ⚠️ Disclaimer

This project is created **strictly for educational and portfolio purposes** to demonstrate Jetpack Compose for TV development. It does not host, store, or own any media content. All content is fetched from third-party public sample APIs and belongs to their respective owners.

---

## 👤 Author

**Your Name**

- GitHub: [@Dinesh2510](https://github.com/Dinesh2510)
- LinkedIn: [in/dineshchavan2510](https://linkedin.com/in/dineshchavan2510)
- YouTube: [@pixeldesigndeveloper](https://youtube.com/@pixeldesigndeveloper)
	
---

<div align="center">

### 💖 Show your support

Give a ⭐️ if this project helped you or inspired your own Android TV app!

</div>
