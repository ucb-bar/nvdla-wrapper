##############################################################
# extra variables/targets ingested by the chipyard make system
##############################################################

##################################################################
# THE FOLLOWING MUST BE += operators
##################################################################

# requirements needed to run the generator
EXTRA_GENERATOR_REQS += \
	$(call lookup_srcs,$(base_dir)/generators/nvdla/src/main/resources/vsrc,vlib) \
	$(call lookup_srcs,$(base_dir)/generators/nvdla/src/main/resources/vsrc,v) \
	$(call lookup_srcs,$(base_dir)/generators/nvdla/src/main/resources/vsrc,vh)
