DESCRIPTION = "Example of how to build an external Linux kernel module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

PV = "0.1"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/can-lin/;protocol=https"
S = "${WORKDIR}/git/Usb-Driver"
SRCREV = "02ba272c0eb51b06160307b6cb71f91684772c8c"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.