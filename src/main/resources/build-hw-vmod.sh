#/bin/bash

# script to build both the small and large versions of nvdla

set -ex

cp tree.make hw

NVDLA_TYPE=small

echo "Build NVDLA \"$NVDLA_TYPE\""
cd hw
./tools/bin/tmake -build vmod
cd ..
rm -rf vsrc/$NVDLA_TYPE/vmod
mkdir -p vsrc/$NVDLA_TYPE
cp -r hw/outdir/nv_$NVDLA_TYPE/vmod vsrc/$NVDLA_TYPE

sed -i -e 's/nv_small/nv_large/' hw/tree.make
NVDLA_TYPE=large

echo "Build NVDLA \"$NVDLA_TYPE\""
cd hw
./tools/bin/tmake -build vmod
cd ..
rm -rf vsrc/$NVDLA_TYPE/vmod
mkdir -p vsrc/$NVDLA_TYPE
cp -r hw/outdir/nv_$NVDLA_TYPE/vmod vsrc/$NVDLA_TYPE
