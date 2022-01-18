SUMMARY = "GLib BlueZ interface library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

DEPENDS = "systemd glib-2.0"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/bluez-glib;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "0a7ac44764e96898370bdd14badfb484ad9d3fc7"
S = "${WORKDIR}/git"

# PV needs to be modified with SRCPV to work AUTOREV correctly
PV = "1.0+git${SRCPV}"

inherit meson pkgconfig
