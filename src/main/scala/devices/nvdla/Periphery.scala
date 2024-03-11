// See LICENSE for license details.
package nvidia.blocks.dla

import chisel3._
import org.chipsalliance.cde.config.Field
import freechips.rocketchip.subsystem.{BaseSubsystem, SBUS, PBUS}
import freechips.rocketchip.diplomacy.{LazyModule,BufferParams}
import freechips.rocketchip.tilelink.{TLBuffer, TLIdentityNode, TLWidthWidget, TLFragmenter}

case object NVDLAKey extends Field[Option[NVDLAParams]](None)
case object NVDLAFrontBusExtraBuffers extends Field[Int](0)

trait CanHavePeripheryNVDLA { this: BaseSubsystem =>
  p(NVDLAKey).map { params =>
    // assumes pbus/sbus/ibus are on the same clock
    val sbus = locateTLBusWrapper(SBUS)
    val pbus = locateTLBusWrapper(PBUS)
    val nvdlaDomain = sbus.generateSynchronousDomain
    nvdlaDomain {
      val nvdla = LazyModule(new NVDLA(params))
      sbus.coupleFrom("nvdla_dbb") { _ := TLBuffer.chainNode(p(NVDLAFrontBusExtraBuffers)) := nvdla.dbb_tl_node }
      pbus.coupleTo("nvdla_cfg") { nvdla.cfg_tl_node := TLFragmenter(4, pbus.blockBytes) := TLWidthWidget(pbus.beatBytes) := _ }
      ibus.fromSync := nvdla.int_node
    }
  }
}
