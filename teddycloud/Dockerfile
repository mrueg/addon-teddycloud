ARG BUILD_FROM=ghcr.io/hassio-addons/ubuntu-base/amd64:9.0.5
# hadolint ignore=DL3006
FROM ${BUILD_FROM} AS buildenv

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup base
ARG BUILD_ARCH=amd64
ARG TEDDYCLOUD_VERSION="v0.6.3"

# Install necessary dependencies
RUN apt-get update \
    && apt-get upgrade -qq \
    && apt-get install -y --no-install-recommends \
         gcc=4:11.2.0-1ubuntu1 \
         protobuf-c-compiler=1.3.3-1ubuntu2.1 \
         build-essential=12.9ubuntu3 \
         git=1:2.34.1-1ubuntu1.11 \
         zip=3.0-12build2 \
         curl=7.81.0-1ubuntu1.20

WORKDIR /buildenv
RUN git clone https://github.com/toniebox-reverse-engineering/teddycloud /buildenv && git checkout tc_${TEDDYCLOUD_VERSION} \
    && curl -f https://raw.githubusercontent.com/toniebox-reverse-engineering/tonies-json/release/tonies.json -o /buildenv/contrib/config/tonies.json || true \
    && curl -f https://raw.githubusercontent.com/toniebox-reverse-engineering/tonies-json/release/tonieboxes.json -o /buildenv/contrib/config/tonieboxes.json || true \
    && git submodule init \
    && git submodule update \
    && make zip

#RUN make preinstall

# hadolint ignore=DL3006
FROM ${BUILD_FROM} 

EXPOSE 80 443 8443

# Install necessary runtime dependencies
RUN apt-get update \ 
    && apt-get upgrade -qq \
    && apt-get install -y --no-install-recommends \
         libubsan1=12.3.0-1ubuntu1~22.04 \
         ffmpeg=7:4.4.2-0ubuntu0.22.04.1  \
         curl=7.81.0-1ubuntu1.20 \
         ca-certificates=20240203~22.04.1 \
         nginx=1.18.0-6ubuntu14.5 \
    && rm -rf /var/lib/apt/lists/* \
    && update-ca-certificates \
    && mkdir -p /teddycloud/certs \
    && mkdir -p /teddycloud/config \
    && mkdir -p /teddycloud/data/content/default \
    && mkdir -p /teddycloud/data/library \
    && mkdir -p /teddycloud/data/firmware \
    && mkdir -p /teddycloud/data/www \
    && mkdir -p /teddycloud/data/cache \
    && mkdir -p /tmp \
    && ln -s /config /etc/teddycloud

# Copy files from the build environment
COPY --from=buildenv /buildenv/install/pre/certs/ /teddycloud/certs/
COPY --from=buildenv /buildenv/install/pre/data/www/ /teddycloud/data/www/
COPY --from=buildenv /buildenv/install/pre/*.sh /usr/local/bin/
COPY --from=buildenv /buildenv/install/pre/teddycloud /usr/local/bin/teddycloud
COPY --from=buildenv /buildenv/install/zip/release.zip /tmp/teddycloud.zip

# Copy root filesystem
COPY rootfs /

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="Manuel Rueger <manuel@rueg.eu>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Home Assistant Community Add-ons" \
    org.opencontainers.image.authors="Manuel Rueger <manuel@rueg.eu>>" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.url="https://addons.community" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
