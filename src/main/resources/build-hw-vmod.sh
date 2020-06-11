#!/bin/bash

# Build RTL for multiple NVDLA configurations

set -ex

cd "$(dirname "$0")"

function build_nvdla_rtl() {
    local NVDLA_TYPE="$1"
    echo "Building NVDLA configuration: \"$NVDLA_TYPE\""
    cp tree.make hw
    sed -i -e "s/nv_small/nv_$NVDLA_TYPE/" hw/tree.make
    cd hw
    ./tools/bin/tmake -build vmod
    cd ..
}

function install_nvdla_rtl() {
    local NVDLA_TYPE="$1"
    local SRC_DIR=hw/outdir/nv_$NVDLA_TYPE/vmod
    local DST_DIR=vsrc/$NVDLA_TYPE
    rm -rf "$DST_DIR/vmod"
    mkdir -p "$DST_DIR"
    cp -r "$SRC_DIR" "$DST_DIR"
}

NVDLA_TYPE=small
build_nvdla_rtl $NVDLA_TYPE
install_nvdla_rtl $NVDLA_TYPE

NVDLA_TYPE=large
build_nvdla_rtl $NVDLA_TYPE
install_nvdla_rtl $NVDLA_TYPE
