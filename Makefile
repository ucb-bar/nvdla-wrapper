#########################################################################################
# pre-process nvdla into a single blackbox file
#########################################################################################

# import srcs
include $(abspath .)/vsrc.mk

nvdla_dir=$(nvdla_blocks_dir)

# either "large" or "small"
NVDLA_TYPE ?= large
NVDLA_NAME = nvdla_$(NVDLA_TYPE)

# name of output pre-processed verilog file
PREPROC_VERILOG_DIR = $(nvdla_dir)/src/main/resources
PREPROC_VERILOG = $(PREPROC_VERILOG_DIR)/$(NVDLA_NAME).preprocessed.v

.PHONY: default $(PREPROC_VERILOG)
default: $(PREPROC_VERILOG)

#########################################################################################
# includes and vsrcs
#########################################################################################

ALL_VSRCS = $(filter-out %.vh,$($(NVDLA_NAME)_vsrcs))
INC_DIR = $(nvdla_blocks_dir)/vsrc/$(NVDLA_TYPE)/vmod/include

#########################################################################################
# pre-process using verilator
#########################################################################################

# default nvdla top module
TOP = $(NVDLA_NAME)

PREPROC_VERILATOR_OPTS = \
	-E \
	+incdir+$(INC_DIR)

# preprocess with Verilator
$(PREPROC_VERILOG): $(ALL_VSRCS)
	mkdir -p $(dir $(PREPROC_VERILOG))
	cat $(ALL_VSRCS) > combined.sv
	verilator --cc --exe $(PREPROC_VERILATOR_OPTS) combined.sv -Mdir $(nvdla_dir)/NVDLA.preprocess --top-module $(TOP) > $@
	sed -i '/^`line/d' $@
	rm -rf combined.sv $(nvdla_dir)/NVDLA.preprocess

clean:
	rm -rf $(PREPROC_VERILOG)
