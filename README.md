# Auto Message - Android SMS Scheduler

Auto Message is a modern Android application that allows users to schedule SMS messages to be sent automatically at a specific date and time. It features a beautiful, animated UI and robust background handling to ensure messages are delivered even when the app is closed.

## Features

*   **Schedule SMS:** Pick a date and time to send a message automatically.
*   **Multiple Messages:** Schedule multiple messages to different contacts at different times.
*   **Contact Integration:** Select phone numbers directly from your device's contact list.
*   **Message Management:** View a list of all pending messages and cancel them if needed.
*   **Modern UI:**
    *   Vibrant gradient backgrounds.
    *   Glassmorphism card designs.
    *   Smooth entry and interaction animations.
    *   Dark mode support.
*   **Reliability:**
    *   Automatic phone number formatting (adds +90 prefix if missing).
    *   Handles battery optimization to ensure background execution.
    *   Provides feedback on SMS delivery status.

## Screenshots

*(Screenshots can be added here)*

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) 17 or higher.
*   Android SDK Command Line Tools.
*   Gradle (Wrapper included).

### Building the Project

This project is configured to be built via the command line using Gradle.

1.  **Clone the repository (or navigate to the project directory):**
    ```bash
    cd c:/Users/hamza/Documents/autoMessage
    ```

2.  **Clean the project:**
    ```bash
    ./gradlew clean
    ```

3.  **Build the Debug APK:**
    ```bash
    ./gradlew assembleDebug
    ```

    The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Installing

1.  Connect your Android device via USB and enable USB Debugging.
2.  Install the APK using ADB:
    ```bash
    adb install -r app/build/outputs/apk/debug/app-debug.apk
    ```

## Usage

1.  **Grant Permissions:** Upon first launch, grant the necessary permissions (SMS, Contacts, Alarms).
2.  **Schedule a Message:**
    *   Enter the phone number or pick from contacts.
    *   Type your message.
    *   Select the Date and Time.
    *   Tap "Zamanla" (Schedule).
3.  **Manage Messages:**
    *   Tap the list icon (top right) to view pending messages.
    *   Tap the delete icon next to a message to cancel it.

## Technologies Used

*   **Kotlin:** Primary programming language.
*   **Android SDK:** Core Android development tools.
*   **AlarmManager:** For precise scheduling.
*   **SmsManager:** For sending text messages.
*   **SharedPreferences & Gson:** For local data persistence.
*   **Material Design Components:** For modern UI elements.

## License

This project is open source.
