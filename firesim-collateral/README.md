# Using NVDLA with FireSim

To build an NVDLA FPGA image on FireSim, please use the `nvdla_config_build_recipes.ini` file when running the `firesim buildafi` command.

First, change your `config_build.ini` file to include the name(s) of the recipes you want to build an FPGA AFI for.

```
[builds]
# other build recipes
firesim-rocket-quadcore-small-nvdla-no-nic-l2-llc4mb-ddr3
# other build recipes
```

Then point to the build recipe file when running the `buildafi` command and all future FireSim commands.

```
firesim buildafi -r $NVDLA_DIR/firesim-collateral/nvdla_config_build_recipes.ini
```

## Disclaimer

This was last tested on Chipyard commit (0bc0e35) and FireSim commit (d5d4f4d).
However, these recipies "should" work on newer versions of FireSim with minimal modifications.
