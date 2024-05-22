ARG BUILD_FROM=ghcr.io/hassio-addons/base/amd64:15.0.9
# hadolint ignore=DL3006
FROM ${BUILD_FROM} as buildenv

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup base
ARG BUILD_ARCH=amd64
ARG TEDDYCLOUD_VERSION="v0.5.2"

# Install necessary dependencies
RUN apk --no-cache add \
    gcc=13.2.1_git20231014-r0 \
    protobuf-c-dev=1.4.1-r7 \
    build-base=0.5-r3 \
    git=2.43.4-r0 \
    zip=3.0-r12

WORKDIR /buildenv
RUN git clone https://github.com/toniebox-reverse-engineering/teddycloud /buildenv && git checkout tc_${TEDDYCLOUD_VERSION} \
    && curl -f https://raw.githubusercontent.com/toniebox-reverse-engineering/tonies-json/release/tonies.json -o /buildenv/contrib/config/tonies.json || true \
    && curl -f https://raw.githubusercontent.com/toniebox-reverse-engineering/tonies-json/release/tonieboxes.json -o /buildenv/contrib/config/tonieboxes.json || true \
    && git submodule init \
    && git submodule update \
    && make zip NO_SANITIZERS=1 # No libsanitize support in alpine

#RUN make preinstall

# hadolint ignore=DL3006
FROM ${BUILD_FROM} 

EXPOSE 80 443

# Install necessary runtime dependencies
RUN apk --no-cache add \
    ffmpeg=6.1.1-r0 \
    nginx=1.24.0-r16 \
    && update-ca-certificates \
    && mkdir -p /teddycloud/certs \
    && mkdir -p /teddycloud/config \
    && mkdir -p /teddycloud/data/content/default \
    && mkdir -p /teddycloud/data/library \
    && mkdir -p /teddycloud/data/firmware  \
    && mkdir -p /teddycloud/data/www   \
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
