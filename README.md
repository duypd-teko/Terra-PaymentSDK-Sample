# Terra-PaymentSDK-Sample

## Setup for Android
1. Setup github token to able to sync project in `build.gradle`.
```
  maven() {
            name = "TekoAndroidPackages"
            url = uri("https://maven.pkg.github.com/teko-vn/android-packages")
            credentials {
                username "YourUserName"
                password "Token"
            }
        }
```

2. Update `TerraService-[DEFAULT].json` file based on `Terra Dashboard`
