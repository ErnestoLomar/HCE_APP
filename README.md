# NFC Communication App - Ernesto Lomar

## Overview

This project consists of an Android application and a Python script that work together to enable NFC communication between a Raspberry Pi and an Android device. The Android app acts as a Host Card Emulation (HCE) service, while the Raspberry Pi, using a PN532 NFC module, interacts with the app to exchange data.

## Android Application

### Features

- **NFC Host Card Emulation**: The Android app uses the `HostApduService` to emulate a card and handle APDU commands sent from the Raspberry Pi.
- **User Interface**: The app provides a simple UI that displays the status of NFC communication.
- **Real-time Communication**: The app receives messages from the Raspberry Pi and displays them on the main screen.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ErnestoLomar/HCE_APP.git
   ```
2. **Open in Android Studio**:
    - Open Android Studio.
    - Click on "Open an existing Android Studio project".
    - Navigate to the cloned repository and select the project folder.
3. **Run the App**:
    - Connect your Android device with NFC enabled.
    - Run the application on your device from Android Studio.

### How It Works

- The Android app listens for NFC commands and processes APDU messages.
- When the Raspberry Pi sends a valid message, the app displays it on the screen.
- The communication is handled in the background using 'MyCardService', which extends HostApduService.

### Key Files

- MainActivity.java: The main activity that initializes the NFC adapter and handles UI updates.
- MyCardService.java: The service that processes APDU commands and interacts with the Raspberry Pi.

### Communication Flow

- Raspberry Pi sends a SELECT AID APDU command.
- Android App responds with a status code (SW1 and SW2).
- Raspberry Pi sends additional data APDUs.
- Android App receives and processes the data, sending it to MainActivity.

## Python Script (Raspberry Pi)

### Features

- Peer-to-Peer Communication: The script uses the PN532 NFC module to interact with the Android app.
- APDU Command Handling: It sends SELECT AID commands and exchanges data with the Android device.
- Real-time Data Exchange: The script continuously listens and sends messages to the Android app.

### Requirements

- Hardware: Raspberry Pi, PN532 NFC module.
- Python Packages:
  - pn532pi

### Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/gassajor000/pn532pi.git
    ```
2. **Install Dependencies**:
    ```bash
    pip install pn532pi
    ```
3. **Run the Script**:
    ```bash
    python3 hce_android.py
    ```

### How It Works

- The script sets up communication with the PN532 NFC module.
- It listens for NFC tags (in this case, the Android device) and sends APDU commands.
- When the Android app responds, the script prints the response in the console.

### Key Functions

- setup(): Initializes the PN532 module and configures it for communication.
- loop(): Continuously checks for an NFC tag and handles APDU exchanges.

### Communication Process

- Setup: Both the Android app and Raspberry Pi script are initialized.
- NFC Detection: The Raspberry Pi detects the Android device via NFC.
- Data Exchange: The Raspberry Pi sends commands, and the Android app responds, allowing for bi-directional communication.

### Conclusion

This project demonstrates how to establish an NFC communication link between a Raspberry Pi and an Android device, enabling real-time data exchange. It can be further expanded to include more complex communication protocols and data handling as required by specific use cases.