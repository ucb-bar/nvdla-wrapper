// See LICENSE for license details.
package nvidia.blocks.ip.dla

import sys.process._

import chisel3._
import chisel3.util._

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class nvdla(configName: String, blackboxName: String, hasSecondAXI: Boolean, dataWidthAXI: Int, synthRAMs: Boolean)
  extends BlackBox with HasBlackBoxPath
{
  override def desiredName = blackboxName

  val io = IO(new Bundle {

    val core_clk = Input(Clock())
    val rstn = Input(Bool())
    val csb_rstn = Input(Bool())

    val dla_intr = Output(Bool())
    // dbb AXI
    val nvdla_core2dbb_aw_awvalid = Output(Bool())
    val nvdla_core2dbb_aw_awready = Input(Bool())
    val nvdla_core2dbb_aw_awid = Output(UInt((8).W))
    val nvdla_core2dbb_aw_awlen = Output(UInt((4).W))
    val nvdla_core2dbb_aw_awsize = Output(UInt((3).W))
    val nvdla_core2dbb_aw_awaddr = Output(UInt((64).W))

    val nvdla_core2dbb_w_wvalid = Output(Bool())
    val nvdla_core2dbb_w_wready = Input(Bool())
    val nvdla_core2dbb_w_wdata = Output(UInt((dataWidthAXI).W))
    val nvdla_core2dbb_w_wstrb = Output(UInt((dataWidthAXI/8).W))
    val nvdla_core2dbb_w_wlast = Output(Bool())

    val nvdla_core2dbb_ar_arvalid = Output(Bool())
    val nvdla_core2dbb_ar_arready = Input(Bool())
    val nvdla_core2dbb_ar_arid = Output(UInt((8).W))
    val nvdla_core2dbb_ar_arlen = Output(UInt((4).W))
    val nvdla_core2dbb_ar_arsize = Output(UInt((3).W))
    val nvdla_core2dbb_ar_araddr = Output(UInt((64).W))

    val nvdla_core2dbb_b_bvalid = Input(Bool())
    val nvdla_core2dbb_b_bready = Output(Bool())
    val nvdla_core2dbb_b_bid = Input(UInt((8).W))

    val nvdla_core2dbb_r_rvalid = Input(Bool())
    val nvdla_core2dbb_r_rready = Output(Bool())
    val nvdla_core2dbb_r_rid = Input(UInt((8).W))
    val nvdla_core2dbb_r_rlast = Input(Bool())
    val nvdla_core2dbb_r_rdata = Input(UInt((dataWidthAXI).W))
    // cvsram AXI
    val nvdla_core2cvsram = if (hasSecondAXI) Some(new Bundle {

      val aw_awvalid = Output(Bool())
      val aw_awready = Input(Bool())
      val aw_awid = Output(UInt((8).W))
      val aw_awlen = Output(UInt((4).W))
      val aw_awsize = Output(UInt((3).W))
      val aw_awaddr = Output(UInt((64).W))

      val w_wvalid = Output(Bool())
      val w_wready = Input(Bool())
      val w_wdata = Output(UInt((dataWidthAXI).W))
      val w_wstrb = Output(UInt((dataWidthAXI/8).W))
      val w_wlast = Output(Bool())

      val ar_arvalid = Output(Bool())
      val ar_arready = Input(Bool())
      val ar_arid = Output(UInt((8).W))
      val ar_arlen = Output(UInt((4).W))
      val ar_arsize = Output(UInt((3).W))
      val ar_araddr = Output(UInt((64).W))

      val b_bvalid = Input(Bool())
      val b_bready = Output(Bool())
      val b_bid = Input(UInt((8).W))

      val r_rvalid = Input(Bool())
      val r_rready = Output(Bool())
      val r_rid = Input(UInt((8).W))
      val r_rlast = Input(Bool())
      val r_rdata = Input(UInt((dataWidthAXI).W))
    }) else None
    // cfg APB
    val psel = Input(Bool())
    val penable = Input(Bool())
    val pwrite = Input(Bool())
    val paddr = Input(UInt((32).W))
    val pwdata = Input(UInt((32).W))
    val prdata = Output(UInt((32).W))
    val pready = Output(Bool())
  })

  val chipyardDir = System.getProperty("user.dir")
  val nvdlaVsrcDir = s"$chipyardDir/generators/nvdla/src/main/resources"

  val makeStr = s"make -C $nvdlaVsrcDir default NVDLA_TYPE=${configName}"
  val preproc = if (synthRAMs) makeStr + " NVDLA_RAMS=synth" else makeStr
  require (preproc.! == 0, "Failed to run pre-processing step")

  addPath(s"$nvdlaVsrcDir/nvdla_${configName}.preprocessed.v")
}
