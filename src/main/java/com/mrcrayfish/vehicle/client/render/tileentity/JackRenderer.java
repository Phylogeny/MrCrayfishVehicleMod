package com.mrcrayfish.vehicle.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.vehicle.client.SpecialModel;
import com.mrcrayfish.vehicle.client.render.Axis;
import com.mrcrayfish.vehicle.client.render.RenderVehicleWrapper;
import com.mrcrayfish.vehicle.client.render.VehicleRenderRegistry;
import com.mrcrayfish.vehicle.entity.VehicleEntity;
import com.mrcrayfish.vehicle.tileentity.JackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class JackRenderer extends TileEntityRenderer<JackTileEntity>
{
    public JackRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void func_225616_a_(JackTileEntity jack, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int i1)
    {
        if(!jack.hasWorld())
            return;

        matrixStack.func_227860_a_();

        BlockPos pos = jack.getPos();
        BlockState state = jack.getWorld().getBlockState(pos);

        matrixStack.func_227860_a_();
        {
            matrixStack.func_227861_a_(0.5, 0.0, 0.5);
            matrixStack.func_227863_a_(Axis.POSITIVE_Y.func_229187_a_(180F));
            matrixStack.func_227861_a_(-0.5, 0.0, -0.5);
            BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            IBakedModel model = dispatcher.getModelForState(state);
            IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.func_228643_e_());
            dispatcher.getBlockModelRenderer().func_228802_a_(jack.getWorld(), model, state, pos, matrixStack, builder, true, new Random(), state.getPositionRandom(pos), OverlayTexture.field_229196_a_);
        }
        matrixStack.func_227865_b_();

        matrixStack.func_227860_a_();
        {
            matrixStack.func_227861_a_(0, -2 * 0.0625, 0);
            float progress = (jack.prevLiftProgress + (jack.liftProgress - jack.prevLiftProgress) * partialTicks) / (float) JackTileEntity.MAX_LIFT_PROGRESS;
            matrixStack.func_227861_a_(0, 0.5 * progress, 0);

            //Render the head
            BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            IBakedModel model = SpecialModel.JACK_PISTON_HEAD.getModel();
            IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.func_228643_e_());
            dispatcher.getBlockModelRenderer().func_228805_b_(jack.getWorld(), model, state, pos, matrixStack, builder, false, new Random(), state.getPositionRandom(pos), OverlayTexture.field_229196_a_);
        }
        matrixStack.func_227865_b_();

        matrixStack.func_227860_a_();
        {
            Entity jackEntity = jack.getJack();
            if(jackEntity != null && jackEntity.getPassengers().size() > 0)
            {
                Entity passenger = jackEntity.getPassengers().get(0);
                if(passenger instanceof VehicleEntity && passenger.isAlive())
                {
                    matrixStack.func_227861_a_(0.5, 0.5, 0.5);
                    matrixStack.func_227861_a_(0, -1 * 0.0625, 0);
                    float progress = (jack.prevLiftProgress + (jack.liftProgress - jack.prevLiftProgress) * partialTicks) / (float) JackTileEntity.MAX_LIFT_PROGRESS;
                    matrixStack.func_227861_a_(0, 0.5 * progress, 0);

                    VehicleEntity vehicle = (VehicleEntity) passenger;
                    Vec3d heldOffset = vehicle.getProperties().getHeldOffset().rotateYaw(passenger.rotationYaw * 0.017453292F);
                    matrixStack.func_227861_a_(-heldOffset.z * 0.0625, -heldOffset.y * 0.0625, -heldOffset.x * 0.0625);
                    matrixStack.func_227863_a_(Axis.POSITIVE_Y.func_229187_a_(-passenger.rotationYaw));

                    RenderVehicleWrapper wrapper = VehicleRenderRegistry.getRenderWrapper((EntityType<? extends VehicleEntity>) vehicle.getType());
                    if(wrapper != null)
                    {
                        wrapper.render(vehicle, matrixStack, renderTypeBuffer, partialTicks, light);
                    }
                }
            }
        }
        matrixStack.func_227865_b_();

        matrixStack.func_227865_b_();
    }
}
