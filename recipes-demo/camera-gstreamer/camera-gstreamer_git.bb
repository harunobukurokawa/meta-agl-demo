SUMMARY     = "Camera gstreamer demo application"
DESCRIPTION = "AGL demonstration of displaying incoming camera feed"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/camera-gstreamer"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

DEPENDS = " \
    wayland wayland-native grpc grpc-native \
    gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad \
"

PV = "1.0+git${SRCPV}"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/camera-gstreamer;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "97faa830c65258ba8b60fa53cdbfb3f9d810abb3"

# WIP: adds an opt-in (ENABLE_LIBCAMERA_PATH=true) libcamerasrc pipeline
# branch alongside the existing v4l2/pipewire/still-image ones. Verified
# on Sparrow Hawk (V4H) + imx219, but kept as a local patch rather than
# moving SRCREV to the feature branch until it lands upstream.
SRC_URI += "file://0001-feat-Add-libcamerasrc-pipeline-branch-to-camera-gstr.patch"

S  = "${WORKDIR}/git"

inherit meson pkgconfig agl-app

AGL_APP_NAME = "Camera"

RDEPENDS:${PN} += " \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-good-gdkpixbuf \
"

# Boards that support the libcamerasrc GStreamer element set advertise
# MACHINE_FEATURES:append:<soc-family> = " libcamera" from AGL's own
# policy layer (see meta-agl-bsp/conf/include/agl_sparrow-hawk.inc).
# This stays a no-op (empty string) on every other machine, so it never
# affects builds that don't opt in.
RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'libcamera', 'libcamera-gst', '', d)}"

# On those same boards the default pipewiresrc path cannot work: the
# rcar-gen4 pipeline handler needs exclusive access to the ISP media
# devices, so the second libcamera::CameraManager that the PipeWire
# daemon spins up (separately from WirePlumber's) fails to enumerate the
# camera at all. Select the libcamerasrc branch out of the box instead.
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'libcamera', 'file://libcamera.conf', '', d)}"

# Artwork the libcamerasrc branch overlays onto the letterbox bars, from
# libcamera's own Documentation/theme/static/libcamera-logo-text.svg.
# Installed unconditionally so the pipeline string never references a
# missing file, even if the branch is enabled by hand on another board.
SRC_URI += "file://libcamera-logo.png"

do_install:append() {
    install -d ${D}${datadir}/applications/data
    install -m 0644 ${WORKDIR}/libcamera-logo.png ${D}${datadir}/applications/data/

    if ${@bb.utils.contains('MACHINE_FEATURES', 'libcamera', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}/${AGL_APP_TEMPLATE}@${AGL_APP_ID}.service.d
        install -m 0644 ${WORKDIR}/libcamera.conf \
            ${D}${systemd_system_unitdir}/${AGL_APP_TEMPLATE}@${AGL_APP_ID}.service.d/libcamera.conf
    fi
}
