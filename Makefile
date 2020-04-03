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
INC_DIRS = $(dir $(filter %.vh,$($(NVDLA_NAME)_vsrcs)))

#########################################################################################
# pre-process using custom py script
#########################################################################################

# preprocess to remove includes
$(PREPROC_VERILOG): $(ALL_VSRCS)
	mkdir -p $(dir $(PREPROC_VERILOG))
	cat $(ALL_VSRCS) > combined.v
	./insert-includes.py combined.v $@ $(INC_DIRS)
	rm -rf combined.v

clean:
	rm -rf $(PREPROC_VERILOG)
