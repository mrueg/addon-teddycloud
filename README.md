# Home-Assistant Add-on: TeddyCloud

> [!CAUTION]
> This is still work in progress and things might not work as expected.

![Logo](./assets/teddycloud.svg)

## How to use

### Repositories

Currently, this is published in two repositories.
A stable repository [here](https://github.com/mrueg/hassos-addons) and an edge repository for unstable releases available [here](https://github.com/mrueg/hassos-addons-edge).
You can add these to your home assistant installation in the addons store.

### Ingress

We currently use the Home Assistant ingress to access this addon from within Home-Assistant.
If you need direct access to your Teddycloud, you can activate the ports (8443/https, 80/http) in the config of the addon.
Those are disabled by default to make sure that only users of Home Assistant can access the Teddycloud UI.

### Port 443

Teddycloud exposes the API that the Toniebox accesses on Port 443.
If your Home Assistant install listens already on this port, or you use a reverse proxy like NGINX, you will need to change those to another port as the Toniebox can only access the API on port 443.

### Storage / Backups

Certificates are stored under `/config/teddycloud/certs` and configuration is stored under `/config/teddycloud/config`.
Data is stored under `/data/`.

If you need to extract any of this (e.g. the certificates and keys to add it to your Toniebox), the easiest way is to download it from a backup that Home Assistant generates for you.

### Home Assistant Integration

#### MQTT / Dashboard

Teddycloud is able to send information about the current state of its connected boxes via MQTT.
Home Assistant can listen on the MQTT topic and generate resources that you can use to visualize the Toniebox in your Home Assistant Dashboard.

- Install a [MQTT broker addon](https://github.com/home-assistant/addons/tree/master/mosquitto) in your Home Assistant install.
- Configure the broker by adding a username and password to the local users.
- Enable MQTT in the Teddycloud settings and provide the URL and port to your MQTT broker, credentials and a topic prefix.
- If the connection is successful, a Teddycloud Server device and a Toniebox device should appear in your Home Assistant devices list.

You can now use this to visualize the state of your Toniebox in a dashboard ([Source](https://forum.revvox.de/t/example-ha-dashboard-page-shown-in-37c3-talk/191)).
Make sure to replace the ${TONIEBOX_ID} and the ${TONIEBOX_NAME} with what is in your devices list.

```
  - title: Toniebox
    path: toniebox
    icon: mdi:teddy-bear
    cards:
      - type: vertical-stack
        cards:
          - type: picture
            image_entity: image.teddycloud_box_${TONIEBOX_ID}_content_picture
          - type: entity
            entity: sensor.teddycloud_box_${TONIEBOX_ID}_content_title
            name: Titel
          - type: entities
            title: ${TONIEBOX_NAME}
            entities:
              - entity: sensor.teddycloud_box_${TONIEBOX_ID}_tag_valid
                name: Tag UID
              - entity: binary_sensor.teddycloud_box_${TONIEBOX_ID}_charger
                name: Ladestation
              - entity: sensor.teddycloud_box_${TONIEBOX_ID}_volume_db
                name: Volume dB
              - entity: sensor.teddycloud_box_${TONIEBOX_ID}_volume_level
                name: Volume Level
              - entity: event.teddycloud_box_${TONIEBOX_ID}_volume_down
                name: kleines Ohr (leiser)
              - entity: event.teddycloud_box_${TONIEBOX_ID}_volume_up
                name: großes Ohr (lauter)
              - entity: sensor.teddycloud_box_${TONIEBOX_ID}_content_audio_id
          - type: entities
            entities:
              - entity: >-
                  switch.teddycloud_server_cloud_cachecontent_cache_cloud_content_on_local_server
                name: Cache Cloud Content
              - entity: >-
                  switch.teddycloud_server_cloud_enabled_generally_enable_cloud_operation
                name: Enable Cloud Operation
```

#### Music-Assistant / Streaming (experimental)

If you use [Music Assistant](https://www.music-assistant.io/), you can stream audio to your Toniebox with the following approach:

- Install [Music Assistant Addon](https://www.music-assistant.io/).
- Install [MPD Addon](https://github.com/Poeschl-HomeAssistant-Addons/mpd).
- Configure MPD Addon and enable HTTPD output.
- Check if mpd shows up as a media_player device.
- Associate the MPD media player within Music Assistant using the HomeAssistant MediaPlayer provider.
- In the Teddycloud UI, select the Tonie that should connect the Toniebox to the Stream.
- Set the Source to the MPD stream and save the change.
- Play audio in Music Assistant and select MPD as the output.
- The Toniebox now should play the selected audio.

Caveat:

There might still be issues when skipping tracks and/or if the connection gets lost.

## About TeddyCloud

[TeddyCloud](https://github.com/toniebox-reverse-engineering/teddycloud) is an alternative server for your Toniebox, allowing you to host the
cloud services locally. This gives you the control about which data is sent to
the original manufacturer's cloud and allows you to host your own figurine
audio files.
