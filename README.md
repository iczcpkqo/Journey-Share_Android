# Android Client Deployment Notes

> Gopher travel Android Client

## Introduction

An assignment for TCD CS7CS3 group project.

## Code Storage

- **Gopher_Travel_Android_Client_Code.zip** or TCD GitLab: `https://gitlab.scss.tcd.ie/group-2_ase/main-project_journey-sharing`
- TCD GitLab Information: 
  - Group-2_ASE
  - Group ID: 2938


## Technology Stack
- Android：10.0
- Java ：
- Map and Navigation ：Mapbox
- Offline Map：Mapbox offline-Plug
- Database：Firestore Database
- Authentication：Firebase Authentication

## Getting Started

### Android Studio Prerequisites
- Min SDK: 29
- Target SDK: 32
- Java Version: VERSION_1_8
- Simulator：Pixel 2  Android 10.0 Google Play API 29.
- Please open the project directly and wait for Gradle to synchronize the dependencies.
- Database and authentication already deployed in the Firebase Cloud.
- Run/Debug Configurations:
  -  Add New Configuration: Android App
  - Module: Journey_sharing.app
  - Deploy: Default APK
  - Launch: Default Activity


### Simulator configuration P2P network

Suppose you want to use an emulator to run the client. In that case, you need to configure interconnected emulator instances because each Android emulator's network is independent of the other. You need to tell the emulator to do port forwarding. Otherwise, you can't run the P2P part because the networks are separate, so they can't be linked.

- Assume that your environment is

  - A is your computer machine

  - B is your first emulator instance, running on A

  - C is your second emulator instance, also running on A
- And you want to run a server on B, to which C will connect, here is how you could set it up:

  - Set up the server on B, listening to `10.0.2.15:<serverPort>`
  - On the B console, set up a redirection from `A:localhost:<localPort> to B:10.0.2.15:<serverPort>`
  - On C, have the client connect to `10.0.2.2:<localPort>`
   For example, if you wanted to run an HTTP server, you can select `<serverPort>` as 80 and `<localPort>` as 8080:
  - B listens on `10.0.2.15:80`
  - On the B console, issue redir add tcp:`8080:80`
  - C connects to `10.0.2.2:8080`
- You need to use **Telnet** to control the emulator settings
  - https://developer.android.com/studio/run/emulator-console

