SUMMARY = "Cross SDK of Full AGL Distribution for IVI profile"

DESCRIPTION = "SDK image for full AGL Distribution for IVI profile. \
It includes the full meta-toolchain, plus developement headers and libraries \
to form a standalone cross SDK."

require agl-demo-platform.bb

LICENSE = "MIT"

IMAGE_FEATURES_append = " dev-pkgs"
IMAGE_INSTALL_append = " kernel-dev kernel-devsrc"

inherit populate_sdk populate_sdk_qt5

# Task do_populate_sdk and do_rootfs can't be exec simultaneously.
# Both exec "createrepo" on the same directory, and so one of them
# can failed (randomly).
addtask do_populate_sdk after do_rootfs

# Add wayland-scanner to SDK (SPEC-945)
# Use TOOLCHAIN_HOST_TASK instead of adding to the packagegroup
# wayland-scanner is in nativesdk-wayland-dev !
# option: add also nativesdk-qtwayland-tools
TOOLCHAIN_HOST_TASK_append = " nativesdk-wayland nativesdk-wayland-dev"

TOOLCHAIN_HOST_TASK_append = " nativesdk-perl-modules "

# Add mosquitto to support building the telematics demo application.
# This is currently required for CI, as it uses agl-demo-platform-crosssdk
# to build everything. An agenda item has been tabled for the May 2019 F2F
# meeting to discuss the path forward (separate versus superset SDKs), this
# should be reviewed after that.
TOOLCHAIN_TARGET_TASK += "mosquitto-dev"

# Add nlohmann-json to support building the speech services.
# Required until either the agl-speech-framework feature is added as a
# dependency of agl-demo, or the speech services are migrated into the
# core profile.
TOOLCHAIN_TARGET_TASK += "nlohmann-json-dev"

# Add libstdc++-staticdev to support building agl-service-voice-high or
# other users of the C++17 filesystem standard library feature.
# Can be removed upon upgrade to gcc 9.x, as it will no longer be necessary.
TOOLCHAIN_TARGET_TASK += "libstdc++-staticdev"

# Add gcc-sanitizers to support building applications using the SDK with
# AddressSanitizer support to detect use-after-frees along with other
# memory issue.
TOOLCHAIN_TARGET_TASK += "gcc-sanitizers"
