# Home-Assistant Add-on: TeddyCloud

> [!CAUTION]
> This is still work in progress and things might not work as expected.

![Logo](./assets/teddycloud.svg)

## How to use

Currently, this is published in two repositories. A stable repository [here](https://github.com/mrueg/hassos-addons) and an edge repository for unstable releases available [here](https://github.com/mrueg/hassos-addons-edge). You can add these to your home assistant installation under addons store.

## TODO

- Minimize Docker Image
- Fix Certificate Upload

## Tested

- Persistent storage of certificates, configs and data (stored under addons_config)
- Tested with an actual box
- Ingress in HA works

## About TeddyCloud

[TeddyCloud](https://github.com/toniebox-reverse-engineering/teddycloud) is an alternative server for your Toniebox, allowing you to host the
cloud services locally. This gives you the control about which data is sent to
the original manufacturer's cloud and allows you to host your own figurine
audio files.
