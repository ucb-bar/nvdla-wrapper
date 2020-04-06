// See LICENSE for license details.
package nvidia.blocks.dla

import chisel3._

import freechips.rocketchip.config.{Field, Parameters, Config}

/**
 * Config fragment to add a NVDLA to the SoC.
 * Supports "small" and "large" configs only.
 * Can enable synth. RAMs instead of default FPGA RAMs.
 */
class WithNVDLA(config: String, synthRAMs: Boolean = false) extends Config((site, here, up) => {
  case NVDLAKey => Some(NVDLAParams(config = config, raddress = 0x10040000L, synthRAMs = synthRAMs))
  case NVDLAFrontBusExtraBuffers => 0
})
