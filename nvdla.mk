##############################################################
# extra variables/targets ingested by the chipyard make system
##############################################################

##################################################################
# THE FOLLOWING MUST BE += operators
##################################################################

# requirements needed to run the generator
EXTRA_GENERATOR_REQS += \
	$(call lookup_srcs,$(base_dir)/generators/nvdla,vlib) \
	$(call lookup_srcs,$(base_dir)/generators/nvdla,v) \
	$(call lookup_srcs,$(base_dir)/generators/nvdla,vh)
