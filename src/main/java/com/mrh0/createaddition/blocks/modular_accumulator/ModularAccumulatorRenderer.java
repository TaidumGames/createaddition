package com.mrh0.createaddition.blocks.modular_accumulator;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrh0.createaddition.index.CAPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class ModularAccumulatorRenderer extends SafeTileEntityRenderer<ModularAccumulatorTileEntity> {

	public ModularAccumulatorRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(ModularAccumulatorTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
		int light, int overlay) {
		if (!te.isController()) return;
		if(te.width < 2) return;
		renderDial(te, partialTicks, ms, buffer, light, overlay);
	}

	protected void renderDial(ModularAccumulatorTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
		int light, int overlay) {
		BlockState blockState = te.getBlockState();
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());
		ms.pushPose();
		TransformStack msr = TransformStack.cast(ms);
		msr.translate(te.width / 2f, te.height - 0.5f, te.width / 2f);

		float dialPivot = 0f;//5.75f / 16;
		float progress = te.gauge.getValue(partialTicks);

		for (Direction d : Iterate.horizontalDirections) {
			ms.pushPose();
			CachedBufferer.partial(CAPartials.ACCUMULATOR_GUAGE, blockState)
				.rotateY(d.toYRot())
				.unCentre()
				.translate(te.width / 2f - 6 / 16f, 0, 0)
				.light(light)
				.renderInto(ms, vb);
			CachedBufferer.partial(AllBlockPartials.BOILER_GAUGE_DIAL, blockState)
				.rotateY(d.toYRot())
				.unCentre()
				.translate(te.width / 2f - 6 / 16f, 0, 0)
				.translate(0, dialPivot, dialPivot)
				.rotateX(-90 * progress)
				.translate(0, -dialPivot, -dialPivot)
				.light(light)
				.renderInto(ms, vb);
			ms.popPose();
		}

		ms.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(ModularAccumulatorTileEntity te) {
		return te.isController();
	}
}