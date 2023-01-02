FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI:append = " \
    file://AGL.conf \
"

VSS_SRV_IP ?= "192.168.56.4"

do_install:append() {
  sed -i "s@server =.*@server = \"${VSS_SRV_IP}\"@g"  ${D}/etc/xdg/AGL/cluster-dashboard.conf

  install -m 0644 ${WORKDIR}/AGL.conf  ${D}${sysconfdir}/xdg/AGL.conf 
}
